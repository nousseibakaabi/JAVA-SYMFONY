<?php

namespace App\Controller;
use Endroid\QrCode\Builder\Builder;
use Endroid\QrCode\ErrorCorrectionLevel;
use Endroid\QrCode\LabelAlignment;
use Symfony\Component\HttpFoundation\Response;
use BaconQrCode\Renderer\RendererStyle;
use Endroid\QrCode\QrCode;
use Endroid\QrCode\Response\QrCodeResponse;
use Endroid\QrCode\Writer\PngWriter;
use App\Entity\Reservation; 
use App\Entity\Event; 
use App\Form\ReservationType; 
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Annotation\Route;
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use BaconQrCode\Writer;
use BaconQrCode\Renderer\ImageRenderer;
use BaconQrCode\Renderer\Color\Rgb;
use App\Entity\Paiement; 
use App\Form\PaiementType;
use Symfony\Component\HttpFoundation\JsonResponse;
use Knp\Component\Pager\PaginatorInterface;
use App\Repository\ReservationRepository;
use App\Repository\PaiementRepository;

use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;

use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Writer\Xlsx;
class ReservationController extends AbstractController
{
    #[Route('/reservation', name: 'app_reservation')]
    public function index(): Response
    {
        return $this->render('/reservation/index.html.twig', [
            'controller_name' => 'ReservationController',
        ]);
    }


    #[Route('/add/{eventId}', name: 'app_reservation_add')]
    public function addReservation(Request $request, int $eventId ): Response
    {
        $event = $this->getDoctrine()->getRepository(Event::class)->find($eventId);
        if (!$event) {
            throw $this->createNotFoundException('Event not found');
        }
        
        $reservation = new Reservation();
        $form = $this->createForm(ReservationType::class, $reservation);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $user = $this->getUser();
            $nameU= $user->getName();
            $email=$user->getEmail();
            $nbPlaces = $reservation->getNbPlaces();
            $eventPrice = $event->getPrix();
            $eventName = $event->getNameevent();
            $image = $event->getImage();
    
            // Calculate the total reserved places for the event
            $totalReservedPlaces = $event->getTotalReservedPlaces();
    
            if (($totalReservedPlaces + $nbPlaces) > $event->getNbrmax()) {
                $this->addFlash('error', 'We dont have this available places. Please choose a lower number of places.');
                return $this->redirectToRoute('app_reservation_add', ['eventId' => $eventId]);
            }
    
            $reservation->setIdUser($user);
            $reservation->setName($nameU);
            $reservation->setEmail($email);
            $reservation->setEventPrice($eventPrice);
            $reservation->setIdEvent($event);
            $reservation->setNamee($eventName);
            $reservation->setImagesrc($image);
    
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($reservation);
            $entityManager->flush();
    
            $this->addFlash('success', 'Reservation added successfully.');
    
            return $this->redirectToRoute('app_paiement_add', ['reservationId' => $reservation->getId()]);
        }
    
        return $this->render('/front/reservation/add.html.twig', [
            'f' => $form->createView(),
            'event' => $event,
        ]);
    }

   


    #[Route('/list', name: 'app_reservations')]
    public function listReservations(Request $request, PaginatorInterface $paginator)
    {
        $user = $this->getUser();
        // Fetch reservations from the database
        $reservations = $this->getDoctrine()->getRepository(Reservation::class)->findBy([
            'idUser' => $user
        ]);
    
        $paiments = $this->getDoctrine()->getRepository(Paiement::class)->findAll();

        // Paginate the reservations
        $pagination = $paginator->paginate(
            $reservations, // Query results
            $request->query->getInt('page', 1), // Current page number
            4 // Number of items per page
        );
    
        // Get the total number of pages
        $pageCount = $pagination->getPageCount();
    
        // Get the current page number
        $currentPage = $pagination->getCurrentPageNumber();
    
        // Calculate startPage and endPage based on the pagination
        $startPage = max(1, $currentPage - 2);
        $endPage = min($pageCount, $currentPage + 2);
    
        // Calculate pagesInRange array
        $pagesInRange = range($startPage, $endPage);
    
        // Get the route name for pagination
        $route = 'app_reservations';
    
        // Get the query parameters
        $query = $request->query->all();
    
        // Define the name of the query parameter for the page number
        $pageParameterName = 'page';
    
        // Render the template to display the list of reservations
        return $this->render('/front/reservation/list.html.twig', [
            'pagination' => $pagination,
            'pageCount' => $pageCount,
            'startPage' => $startPage,
            'endPage' => $endPage,
            'pagesInRange' => $pagesInRange,
            'current' => $currentPage, // Pass current page number to the template
            'route' => $route, // Pass route name to the template
            'query' => $query, // Pass query parameters to the template
            'pageParameterName' => $pageParameterName, // Pass the name of the page parameter,
            'paiments' => $paiments
        ]);
    }
    
    

    #[Route('/listb', name: 'back_reservation')]
    public function listReservation(ReservationRepository $res, PaginatorInterface $paginator,Request $request): Response
    {
        // Fetch reservations from the database
        $reservations = $res->findAllSortedByNbPl();
     // Paginate the reservations
     $pagination = $paginator->paginate(
        $reservations, // Query results
        $request->query->getInt('page', 1), // Current page number
        3 // Number of items per page
    );

    // Get the total number of pages
    $pageCount = $pagination->getPageCount();

    // Get the current page number
    $currentPage = $pagination->getCurrentPageNumber();

    // Calculate startPage and endPage based on the pagination
    $startPage = max(1, $currentPage - 2);
    $endPage = min($pageCount, $currentPage + 2);

    // Calculate pagesInRange array
    $pagesInRange = range($startPage, $endPage);

    // Get the route name for pagination
    $route = 'back';

    // Get the query parameters
    $query = $request->query->all();

    // Define the name of the query parameter for the page number
    $pageParameterName = 'page';

    // Render the template to display the list of reservations
    return $this->render('/back/reservation/listback.html.twig', [
        'pagination' => $pagination,
        'pageCount' => $pageCount,
        'startPage' => $startPage,
        'endPage' => $endPage,
        'pagesInRange' => $pagesInRange,
        'current' => $currentPage, // Pass current page number to the template
        'route' => $route, // Pass route name to the template
        'query' => $query, // Pass query parameters to the template
        'pageParameterName' => $pageParameterName, // Pass the name of the page parameter
    ]);
    }
    


#[Route('/reservation/{id}', name: 'show_resB')]
public function detailResBack($id): Response
{
    // Attempt to find the reservation by ID
    $reservation = $this->getDoctrine()->getRepository(Reservation::class)->find($id);

    // Check if reservation is not found
    if (!$reservation) {
        // Throw a more descriptive exception with the actual ID value
        throw $this->createNotFoundException('Reservation not found for ID: ' . $id);
    }

    // Fetch the associated paiement
    $paiement = $this->getDoctrine()->getRepository(Paiement::class)->findOneBy(['idRes' => $reservation]);

    // Generate the QR code image URL
    $qrCodeUrl = $this->generateUrl('qr_code', ['id' => $reservation->getId()]);

    // Render the template to display the details of the reservation
    return $this->render('/back/reservation/detailB.html.twig', [
        'reservation' => $reservation,
        'paiement' => $paiement,
        'qrCodeUrl' => $qrCodeUrl, // Pass the QR code image URL to the template
    ]);
}


#[Route('/reservation/front/{id}', name: 'show_ticket')]
public function detailRes($id): Response
{
    // Attempt to find the reservation by ID
    $reservation = $this->getDoctrine()->getRepository(Reservation::class)->find($id);

    // Check if reservation is not found
    if (!$reservation) {
        // Throw a more descriptive exception with the actual ID value
        throw $this->createNotFoundException('Reservation not found for ID: ' . $id);
    }

    // Fetch the associated paiement
    $paiement = $this->getDoctrine()->getRepository(Paiement::class)->findOneBy(['idRes' => $reservation]);

    // Generate the QR code image URL
    $qrCodeUrl = $this->generateUrl('qr_code', ['id' => $reservation->getId()]);

    // Render the template to display the details of the reservation
    return $this->render('/front/reservation/ticketFront.html.twig', [
        'reservation' => $reservation,
        'paiement' => $paiement,
        'qrCodeUrl' => $qrCodeUrl, // Pass the QR code image URL to the template
    ]);
}

#[Route('/print/{id}', name: 'print')]
public function print(int $id): Response // Add the $id parameter to the method signature
{
    // Attempt to find the reservation by ID
    $reservation = $this->getDoctrine()->getRepository(Reservation::class)->find($id);

    // Check if reservation is not found
    if (!$reservation) {
        // Throw a more descriptive exception with the actual ID value
        throw $this->createNotFoundException('Reservation not found for ID: ' . $id);
    }

    // Fetch any other necessary data (e.g., paiement)
    // You may need to adjust this based on your application's logic
    $paiement = $this->getDoctrine()->getRepository(Paiement::class)->findOneBy(['idRes' => $reservation]);

    // Generate the QR code image URL
    $qrCodeUrl = $this->generateUrl('qr_code', ['id' => $reservation->getId()]);

    // Render the print.html.twig template with the fetched data
    $htmlContent = $this->renderView('/front/reservation/print.html.twig', [
        'reservation' => $reservation,
        'paiement' => $paiement,
        'qrCodeUrl' => $qrCodeUrl,
    ]);

    // Create a response with the HTML content
    $response = new Response($htmlContent);

    // Set headers to make sure the browser renders it as HTML
    $response->headers->set('Content-Type', 'text/html');

    // Return the response
    return $response;
}



#[Route('/reservation/delete/{id}', name: 'app_reservation_delete', methods: ['POST'])]
public function deleteReservation(Request $request, int $id): Response
{
    $entityManager = $this->getDoctrine()->getManager();
    $reservation = $entityManager->getRepository(Reservation::class)->find($id);

    if (!$reservation) {
        throw $this->createNotFoundException('Reservation not found');
    }

    try {
        // Delete the reservation
        $entityManager->remove($reservation);
        $entityManager->flush();

        // Return a success response
        return new JsonResponse(['message' => 'Reservation deleted successfully'], 200);
    } catch (\Exception $e) {
        // Return an error response
        return new JsonResponse(['error' => 'Failed to delete reservation: ' . $e->getMessage()], 500);
    }
}

#[Route('/res/delete/{id}', name: 'app_delete', methods: ['POST'])]
public function deleteRes(Request $request, int $id): Response
{
    $entityManager = $this->getDoctrine()->getManager();
    $reservation = $entityManager->getRepository(Reservation::class)->find($id);

    if (!$reservation) {
        throw $this->createNotFoundException('Reservation not found');
    }

    // Delete the reservation
    $entityManager->remove($reservation);
    $entityManager->flush();

    // Add flash message
    $this->addFlash('success', 'Reservation deleted successfully.');

    // Redirect to the list of reservations
    return $this->redirectToRoute('app_reservations');
}


#[Route('/reservation/update/{id}', name: 'app_reservation_update')]
public function updateReservation(Request $request, int $id): Response
{
    // Fetch the reservation to be updated
    $entityManager = $this->getDoctrine()->getManager();
    $reservation = $entityManager->getRepository(Reservation::class)->find($id);

    if (!$reservation) {
        throw $this->createNotFoundException('Reservation not found');
    }

    // Create the form for updating the reservation
    $form = $this->createForm(ReservationType::class, $reservation);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {

        // Save the updated reservation to the database
        $entityManager->flush();

        // Fetch the associated paiement
        $paiement = $entityManager->getRepository(Paiement::class)->findOneBy(['idRes' =>$reservation->getId()]);

        if ($paiement) {
            // Calculate montantT based on eventPrice and updated nbPlaces
            $eventPrice = $reservation->getEventprice();
            $updatedNbPlaces = $reservation->getNbPlaces();
            $montantT = $eventPrice * $updatedNbPlaces;
            $paiement->setMontant($montantT);

            // Update the paiement's montantT in the database
            $entityManager->flush();
        }else{
            $paiement = new Paiement();
            $paiement->setIdRes($reservation);

            $eventPrice = $reservation->getEventPrice();
            $montant = $eventPrice * $reservation->getNbPlaces();
            $paiement->setMontant($montant);
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($paiement);
            $entityManager->flush();


        }

        $this->addFlash('success', 'Reservation updated successfully.');

        // Redirect to the list of reservations
        return $this->redirectToRoute('app_paiement_update', ['id' => $paiement->getId()]);
    }

    // Render the form for updating the reservation
    return $this->render('reservation/update.html.twig', [
        'f' => $form->createView(),
        'reservation' => $reservation,

    ]);
}



#[Route('/update-nb-places', name: 'update_nb_places', methods: ['POST'])]
public function updateNbPlaces(Request $request): JsonResponse
{
    $data = json_decode($request->getContent(), true);

    $reservationId = $data['reservationId'];
    $newNbPlaces = $data['newNbPlaces'];

    // Retrieve the reservation from the database
    $entityManager = $this->getDoctrine()->getManager();
    $reservation = $entityManager->getRepository(Reservation::class)->find($reservationId);

    if (!$reservation) {
        return new JsonResponse(['error' => 'Reservation not found'], 404);
    }

    // Update the number of places in the reservation
    $reservation->setNbPlaces($newNbPlaces);
    $entityManager->flush();

    // Calculate the updated total amount (if needed)
    $updatedTotalAmount = $reservation->getEventPrice() * $newNbPlaces;

    // Return the updated total amount in the response
    return new JsonResponse(['updatedTotalAmount' => $updatedTotalAmount]);
}




#[Route('/reservation/{id}/pdf', name: 'generate_pdf')]
public function generatePdf($id): Response
{
    // Attempt to find the reservation by ID
    $reservation = $this->getDoctrine()->getRepository(Reservation::class)->find($id);

    // Check if reservation is not found
    if (!$reservation) {
        // Throw a more descriptive exception with the actual ID value
        throw $this->createNotFoundException('Reservation not found for ID: ' . $id);
    }

    // Fetch the associated paiement
    $paiement = $this->getDoctrine()->getRepository(Paiement::class)->findOneBy(['idRes' => $reservation]);
    $qrCodeUrl = $this->generateUrl('qr_code', ['id' => $reservation->getId()]);

    // Render the PDF with all necessary data
    $html = $this->renderView('/back/reservation/generate_pdf.html.twig', [
        'reservation' => $reservation,
        'paiement' => $paiement,
        'qrCodeUrl' => $qrCodeUrl,
    ]);

    $options = new Options();
    $options->set('isHtml5ParserEnabled', true);
    $options->set('defaultFont', 'Arial');

    // Instantiate Dompdf with the configured options
    $dompdf = new Dompdf($options);
    $dompdf->loadHtml($html);

    // Set paper size and orientation
    $dompdf->setPaper('A4', 'portrait');

    // Render the PDF
    $dompdf->render();

    // Generate PDF file name
    $pdfFileName = 'reservation_' . $id . '.pdf';

    // Save the generated PDF to the project directory
    $pdfPath = $this->getParameter('kernel.project_dir') . '/public/pdf/' . $pdfFileName;
    file_put_contents($pdfPath, $dompdf->output());

    // Output the generated PDF to the browser (inline download)
    return new BinaryFileResponse($pdfPath);
}




#[Route('/qrcode/{id}', name: 'qr_code')]
public function generateQRCode($id): Response
{
    // Fetch the reservation by ID
    $reservation = $this->getDoctrine()->getRepository(Reservation::class)->find($id);

    // Check if reservation is not found
    if (!$reservation) {
        // Throw a more descriptive exception with the actual ID value
        throw $this->createNotFoundException('Reservation not found for ID: ' . $id);
    }

    // Fetch the associated paiement
    $paiement = $this->getDoctrine()->getRepository(Paiement::class)->findOneBy(['idRes' => $reservation]);

    // Generate QR code content (You can customize this according to your requirements)
    $qrCodeContent = 'Number of Places: ' . $reservation->getNbPlaces() . "\n";
    $qrCodeContent .= 'Event: ' . $reservation->getNamee()  . "\n";
    $qrCodeContent .= ' AT: ' . $reservation->getIdEvent()->getDateevent()->format('Y-m-d') . "\n";
    $qrCodeContent .= 'Reserved by: ' . ($reservation->getIdUser() ? $reservation->getIdUser()->getName() : 'Unknown User') . "\n";

    // Generate QR code
    $qrCode = Builder::create()
        ->writerOptions(['exclude_xml_declaration' => true])
        ->writer(new PngWriter())
        ->data($qrCodeContent)
        ->size(300)
        ->margin(10)
        ->build();

    // Return QR code as response
    return new Response($qrCode->getString(), Response::HTTP_OK, [
        'Content-Type' => $qrCode->getMimeType(),
    ]);
}


#[Route('/reservations/export/excel', name: 'export_reservations_excel', methods: ['GET'])]
public function exportToExcel(ReservationRepository $reservationRepository, PaiementRepository $paiementRepository): Response
{
    // Fetch all reservations and payments from the database
    $reservations = $reservationRepository->findAll();
    $paiements = $paiementRepository->findAll();

    // Create a new Spreadsheet instance
    $spreadsheet = new Spreadsheet();

    // Create a new worksheet
    $sheet = $spreadsheet->getActiveSheet();

    // Add headers to the first row
    $sheet->setCellValue('A1', 'Event Name');
    $sheet->setCellValue('B1', 'User Name');
    $sheet->setCellValue('C1', 'Email');
    $sheet->setCellValue('D1', 'Nb Places');
    $sheet->setCellValue('E1', 'Event Price');
    $sheet->setCellValue('F1', 'Total Amount');
    $sheet->setCellValue('G1', 'Reserved at');

    // Fill in reservation data
    $row = 2;
    foreach ($reservations as $reservation) {
        // Find the corresponding payment for this reservation
        $paiement = $this->findPaiementForReservation($reservation, $paiements);

        // If a corresponding payment is found, fill in the data
        if ($paiement) {
            $sheet->setCellValue('A' . $row, $reservation->getNamee());
            $sheet->setCellValue('B' . $row, $reservation->getName());
            $sheet->setCellValue('C' . $row, $reservation->getEmail());
            $sheet->setCellValue('D' . $row, $reservation->getNbPlaces());
            $sheet->setCellValue('E' . $row, $reservation->getEventPrice());
            $sheet->setCellValue('F' . $row, $paiement->getMontant());
            $sheet->setCellValue('G' . $row, $paiement->getHeureP());
        }

        $row++;
    }

    // Create a temporary file to save the Excel data
    $tempFilePath = tempnam(sys_get_temp_dir(), 'reservations');
    $writer = new Xlsx($spreadsheet);
    $writer->save($tempFilePath);

    // Return the Excel file as a response
    $response = new BinaryFileResponse($tempFilePath);
    $response->headers->set('Content-Type', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet');
    $response->headers->set('Content-Disposition', 'attachment;filename="reservations.xlsx"');

    return $response;
}


private function findPaiementForReservation($reservation, $paiements)
{
    foreach ($paiements as $paiement) {
        if ($paiement->getIdRes() === $reservation) {
            return $paiement;
        }
    }

    return null;
}


}