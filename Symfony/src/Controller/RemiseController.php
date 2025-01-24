<?php

namespace App\Controller;
use App\Entity\Remiseentry; // Add this line
use App\Repository\RemiseRepository; // Add this line
use App\Form\RemiseType; 
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\JsonResponse;
use Doctrine\ORM\EntityManagerInterface;

class RemiseController extends AbstractController
{
    #[Route('/remise', name: 'app_remise')]
    public function index(): Response
    {
        return $this->render('/back/remise/listR.html.twig', [
            'controller_name' => 'RemiseController',
        ]);
    }

    #[Route('/remise/reem', name: 'app_remise_rem')]
    public function indeeeeex(RemiseRepository $remiseRepository): Response
    {
        return $remiseRepository->getRandomRemise();
    }


    #[Route('/addR', name: 'app_remise_add')]
    public function addRemise(Request $request): Response
    {
        $remise = new Remiseentry();
        $form = $this->createForm(RemiseType::class, $remise);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            // Calculate the eventPrice based on the form data
          
            // Persist the reservation to the database
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($remise);
            $entityManager->flush();
    
            // Add flash message
            $this->addFlash('success', 'Remise added successfully.');
            // Redirect to the list of reservations
            return $this->redirectToRoute('app_remises');
        }
    
        return $this->render('/back/remise/add.html.twig', [
            'f' => $form->createView(),
        ]);
    }

   


    #[Route('/listR', name: 'app_remises')]
public function listRemise(): Response
{
    // Fetch reservations from the database
    $remises = $this->getDoctrine()->getRepository(Remiseentry::class)->findAll();

    // Render the template to display the list of reservations
    return $this->render('/back/remise/listR.html.twig', [
        'remises' => $remises,
    ]);
}


#[Route('/remise/delete/{id}', name: 'delete_remise', methods: ['POST'])]

public function deleteRemise(Request $request, int $id): Response
{
    $entityManager = $this->getDoctrine()->getManager();
    $remise = $entityManager->getRepository(Remiseentry::class)->find($id);

    if (!$remise) {
        throw $this->createNotFoundException('remise not found');
    }

    // Delete the reservation
    $entityManager->remove($remise);
    $entityManager->flush();

    // Add flash message
    $this->addFlash('success', 'remise deleted successfully.');

    // Redirect to the list of reservations
    return $this->redirectToRoute('app_remises');
}


#[Route('/remise/update/{id}', name: 'edit_remise')]
public function updateRemise(Request $request, int $id): Response
{
    // Fetch the reservation to be updated
    $entityManager = $this->getDoctrine()->getManager();
    $remise = $entityManager->getRepository(Remiseentry::class)->find($id);

    if (!$remise) {
        throw $this->createNotFoundException('remise not found');
    }

    // Create the form for updating the reservation
    $form = $this->createForm(RemiseType::class, $remise);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        // Save the updated reservation to the database
        $entityManager->flush();

        $this->addFlash('success', 'remise updated successfully.');

        // Redirect to the list of reservations
        return $this->redirectToRoute('app_remises');
    }

    // Render the form for updating the reservation
    return $this->render('/back/remise/update.html.twig', [
        'f' => $form->createView(),
    ]);
}

#[Route('/apply-remise', name: 'apply_remise', methods: ['POST'])]
public function applyRemise(Request $request): JsonResponse
{
    $code = $request->getContent(); // Get the code entered by the user from the request body
    // Search for the remise entry in the database based on the code
    $remise = $this->getDoctrine()->getRepository(Remiseentry::class)->findOneBy(['code' => $code]);

    if (!$remise) {
        return new JsonResponse(['error' => 'Remise not found'], JsonResponse::HTTP_NOT_FOUND);
    }

    // Calculate the discounted amount based on the remise percentage
    $discountPercentage = $remise->getPourcentage();
    $totalAmount = $reservation->getNbPlaces() * $reservation->getEventPrice();
    $discountedAmount = $totalAmount * (1 - ($discountPercentage / 100));

    // Return the discounted amount as a response
    return new JsonResponse(['discountedAmount' => $discountedAmount]);
}
}

