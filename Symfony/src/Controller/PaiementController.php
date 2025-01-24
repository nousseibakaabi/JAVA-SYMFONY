<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

use App\Entity\Reservation; // Add this line
use App\Form\ReservationType; // Add this line
use Symfony\Component\HttpFoundation\Request;
use App\Entity\Paiement; // Add this line
use App\Form\PaiementType;
use App\Entity\Remiseentry; // Add this line
use App\Repository\RemiseRepository;
use App\Repository\ReservationRepository;
use Stripe\Stripe;
use Stripe\PaymentIntent;
use Stripe\Exception\ApiErrorException;

use Twilio\Rest\Client;

class PaiementController extends AbstractController
{
    #[Route('/paiement', name: 'app_paiement')]
    public function index(): Response
    {
        return $this->render('paiement/index.html.twig', [
            'controller_name' => 'PaiementController',
        ]);
    }



    #[Route('/addP', name: 'app_paiement_add')]
    public function addPaiement(Request $request, RemiseRepository $remiseRepository): Response
    {
        $clientSecret = '';

        \Stripe\Stripe::setApiKey($_ENV['STRIPE_SECRET_KEY']);

        $paiement = new Paiement();
        $remise = 0;


        $reservationId = $request->query->get('reservationId');
    
        $reservationValue = $request->request->get('reservation_value');
    
        $remiseError = null;
    
        $randomRemise = $remiseRepository->getRandomRemise();
    
        if ($reservationValue != "" && $reservationValue != null ) {
            $remiseEntry = $remiseRepository->findOneBy(['code' => $reservationValue]);
            if($remiseEntry) {
                $remiseError =  "Remise de ".$remiseEntry->getPourcentage()."%";
                $remise = $remiseEntry->getPourcentage();
            } else {
                // Remiseentry with the provided code does not exist
                $remiseError = "Code Incorrect";
            }
        }
        // If reservation ID is provided, set it to the paiement
        if ($reservationId) {
            $idRes = $this->getDoctrine()->getRepository(Reservation::class)->find($reservationId);
            if (!$idRes) {
                throw $this->createNotFoundException('Reservation not found');
            }
            $paiement->setIdRes($idRes);
            $eventPrice = $idRes->getEventPrice();
            $nbPlaces = $idRes->getNbPlaces();
            if($remise != 0)
            {
                $montant = $eventPrice * $nbPlaces;
                $montant = $montant - ($montant/100) * $remise;
            }else{
                $montant = $eventPrice * $nbPlaces;
            }
            $paiement->setMontant($montant);
        }
    
        $form = $this->createForm(PaiementType::class, $paiement);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $remiseEntry = $remiseRepository->findOneBy(['code' => $request->get('reservationValue')]);
            if($remiseEntry) {
                $remise = $remiseEntry->getPourcentage();
            }
            if($remise != 0){
                $montant = $paiement->getMontant();
                $montant = $montant - ($montant/100) * $remise;
                $paiement->setMontant($montant);
            }
            // Persist the paiement
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($paiement);
            $entityManager->flush();
    
            // Delete the remise code associated with the entered reservationValue
            $remiseEntry = $remiseRepository->findOneBy(['code' => $request->get('reservationValue')]);
            if ($remiseEntry) {
                $entityManager->remove($remiseEntry);
                $entityManager->flush();
            }


            $amountInCents = (int)($paiement->getMontant() * 1000);

                $paymentIntentParams = [
                    'amount' => $amountInCents, // Amount in cents
                    'currency' => 'usd', // Change to your currency
                    'payment_method_types' => ['card'], // Set payment method as card
                ];
                $intent = PaymentIntent::create($paymentIntentParams);

                // Retrieve client secret from payment intent
                $clientSecret = $intent->client_secret;

                
          
    
    // Send SMS
    $phoneNumber = $form->get('phoneNumber')->getData();
    $nbPlaces = $idRes->getNbPlaces(); // Retrieve the number of places from the reservation
    $name = $idRes->getName(); // Retrieve the number of places from the reservation
    $nameEvent = $idRes->getNamee(); // Retrieve the number of places from the reservation
    $amountPaid = $paiement->getMontant(); // Retrieve the amount paid from the paiement entity
    $heure = $paiement->getHeureP(); // Retrieve the payment time from the paiement entity
    $dateEvent = $idRes->getIdEvent()->getDateevent(); // Retrieve the number of places from the reservation

    // Format the payment time into a string
    $formattedHeure = $heure->format('Y-m-d H:i:s');
    $formattedDate = $dateEvent->format('Y-m-d ');

    $messageBody = "Dear " . $name . "\n";
    $messageBody .= "Your payment has been successfully processed for " . $nbPlaces . " places. \n";
    $messageBody .= "You have paid " . $amountPaid . " TND AT : " . $formattedHeure . "\n";
    $messageBody .= "Thank you for visiting our event " . $nameEvent . " made by TANIT ONLINE on ". $formattedDate. " . \n";
    
    // Your Twilio Account SID, Auth Token, and Twilio phone number
    $accountSid = $_ENV['TWILIO_ACCOUNT_SID'];
    $authToken = $_ENV['TWILIO_AUTH_TOKEN'];
    $twilioPhoneNumber = $_ENV['TWILIO_NUMBER'];

    // Initialize Twilio client
    $twilio = new Client($accountSid, $authToken);

    // Send SMS
    $message = $twilio->messages
        ->create(
            $phoneNumber, // Destination phone number from the form
            [
                'from' => $twilioPhoneNumber, // Your Twilio phone number
                'body' => $messageBody // Modified message body
            ]
        );

            $this->addFlash('success', 'Paiement successfully.');
    
            // Redirect to the list of reservations
            return $this->redirectToRoute('app_reservations');
        }
    
        return $this->render('/front/paiement/add.html.twig', [
            'f' => $form->createView(),
            'reservation' => $idRes,
            'errorCode' => $remiseError,
            'reservationValue' => $reservationValue,
            'remise' => $remise,
            'randomRemise' => $randomRemise,
            'client_secret' => $clientSecret,
        ]);
    }



   

}
