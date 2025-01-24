<?php

namespace App\Controller;

use App\Entity\Event;
use App\Entity\Partner;
use App\Form\EventType;
use App\Repository\EventRepository;
use App\Repository\PartnerRepository;
use Doctrine\ORM\EntityManagerInterface;
use Endroid\QrCode\Builder\Builder;
use Endroid\QrCode\Writer\PngWriter;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\SerializerInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\ResponseHeaderBag;
use Dompdf\Dompdf;
use Symfony\Component\HttpFoundation\Session\SessionInterface;

#[Route('/event')]
class EventController extends AbstractController
{
    #[Route('/set-locale', name: 'set_locale')]
    public function setLocale(Request $request, SessionInterface $session): Response
    {
        $locale = $request->request->get('locale');
        $session->set('_locale', $locale);

        // Redirect back to the previous page
        return $this->redirect($request->headers->get('referer'));
    }

    #[Route('/list', name: 'app_event')]
    public function showEvent(Request $request, PaginatorInterface $paginator)
    {
        // Fetch reservations from the database
        $events = $this->getDoctrine()->getRepository(Event::class)->findAll();
        
    
        // Paginate the reservations
        $pagination = $paginator->paginate(
            $events, // Query results
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
        $route = 'app_event';
    
        // Get the query parameters
        $query = $request->query->all();
    
        // Define the name of the query parameter for the page number
        $pageParameterName = 'page';
    
        // Render the template to display the list of reservations
        return $this->render('event/index.html.twig', [
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


    #[Route('/admin', name: 'app_event_back')]
    public function EventBack(Request $request, PaginatorInterface $paginator,EventRepository $eR,PartnerRepository $pR)
    {
        // Fetch reservations from the database
        $partiesBD=$pR->findAll();
        $eventsBD=$eR->findAll();
    
        // Paginate the reservations
        $pagination = $paginator->paginate(
            $eventsBD, // Query results
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
        $route = 'app_event_back';
    
        // Get the query parameters
        $query = $request->query->all();
    
        // Define the name of the query parameter for the page number
        $pageParameterName = 'page';
    
        $rdvs = [];
        foreach ($eventsBD as $event) {
            $rdvs[] = [
                'id' => $event->getIdevent(),
                'start' => $event->getDateevent()->format('Y-m-d H:i:s'),
                'title' => $event->getNameevent(),
                'description' => $event->getDescription(),
                // Add other fields as needed
            ];
        }

        $data = json_encode($rdvs);

        // Render the template to display the list of reservations
        return $this->render('event/listEventBack.html.twig', [
            'pagination' => $pagination,
            'pageCount' => $pageCount,
            'startPage' => $startPage,
            'endPage' => $endPage,
            'pagesInRange' => $pagesInRange,
            'current' => $currentPage, // Pass current page number to the template
            'route' => $route, // Pass route name to the template
            'query' => $query, // Pass query parameters to the template
            'pageParameterName' => $pageParameterName, // Pass the name of the page parameter
            'events'=>$eventsBD,
            'partners'=>$partiesBD,
            'data' => $data, 
        ]);
    }

    #[Route('/add', name: 'app_event_add')]
    public function newEvent(EntityManagerInterface $em, Request $req): Response
    {
        $event = new Event();

        $form = $this->createForm(EventType::class, $event);
        $form->handleRequest($req);

        if ($form->isSubmitted() && $form->isValid()) { 
            // Vérifie si le formulaire a été soumis et est valide

            // Récupérer le fichier téléchargé à partir du formulaire
            $imageFile = $form->get('image')->getData();

            // Vérifier si un fichier a été téléchargé
            if ($imageFile) {
                // Générer un nom de fichier unique
                $fileName = md5(uniqid()).'.'.$imageFile->guessExtension();

                // Déplacer le fichier téléchargé vers le répertoire de destination
                $imageFile->move(
                    $this->getParameter('upload_directory'), // Répertoire de destination configuré dans services.yaml ou parameters.yaml
                    $fileName
                );

                // Définir le nom du fichier dans l'entité Event
                $event->setImage($fileName);
            }
            else
            {
                $event->setImage('');
            }

            // Enregistrer l'entité Event en base de données
            $em->persist($event);
            $em->flush();

            // Rediriger vers une autre page après l'ajout d'un événement
            return $this->redirectToRoute("app_event_back");
        }

        // Rendre le formulaire dans le template twig approprié
        return $this->render('event/add.html.twig', [
            'title' => 'Add Event',
            'formEvent' => $form->createView()
        ]);
    }


    #[Route('/partner/{id}', name: 'show_events_by_partner')]
    public function EventPartieFront(EventRepository $eR,PartnerRepository $pR,$id): Response
    {
        $eventsBD=$eR->findBy([
            'idpartnerce' => $id
        ]);
        return $this->render('event/eventsParnter.html.twig', [
            'events'=>$eventsBD,
        ]);
    }

    #[Route('/delete/{id}', name: 'app_event_delete')]
    public function delete($id, EntityManagerInterface $entityManagerInterface, EventRepository $eventRepository)
    {
        $event= $eventRepository->find($id);
        $entityManagerInterface->remove($event);
        $entityManagerInterface->flush();
        return $this->redirectToRoute('app_event_back');
    }

    #[Route('/update/{id}', name: 'app_event_update')]
    public function fedit(Request $request,$id, EntityManagerInterface $entityManagerInterface, EventRepository $eventRepository)
    {
        $event= $eventRepository->find($id);
        $form=$this->createForm(EventType::class,$event);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $file = $form->get('image')->getData();
            if($file)
            {
                $fileName = md5(uniqid()).'.'.$file->guessExtension();
                try {
                    $file->move(
                        $this->getParameter('upload_directory'),
                        $fileName
                    );
                } catch (FileException $e){

                }
                $event->setImage($fileName);
            }
            $entityManagerInterface->persist($event);
            $entityManagerInterface->flush();
            return $this->redirectToRoute('app_event_back');
        }
         return $this->renderForm('event/add.html.twig',[
            'formEvent'=>$form,
            'title'=>'Edit Event']);   
    }

    #[Route('/details/{id}', name: 'show_details')]
    public function detailsEvent($id): Response
    {
        // Attempt to find the event by ID
        $event = $this->getDoctrine()->getRepository(Event::class)->find($id);
    
        // Check if event is not found
        if (!$event) {
            // Throw a more descriptive exception with the actual ID value
            throw $this->createNotFoundException('Event not found for ID: ' . $id);
        }
    
       // Generate the QR code image URL
        $qrCodeUrl = $this->generateUrl('generate_qr_code', ['id' => $event->getIdevent()]);
            
        // Render the template to display the details of the reservation
        return $this->render('event/oneEvent.html.twig', [
            'event' => $event,
            'qrCodeUrl' => $qrCodeUrl, // Pass the QR code image URL to the template
        ]);
    }


#[Route('/generate-qrcode/{id}', name: 'generate_qr_code')]
public function generateQRCode($id): Response
{
    // Fetch the event by ID
    $event = $this->getDoctrine()->getRepository(Event::class)->find($id);

    // Check if event is not found
    if (!$event) {
        // Throw a more descriptive exception with the actual ID value
        throw $this->createNotFoundException('Event not found for ID: ' . $id);
    }

    // Generate QR code content (You can customize this according to your requirements)
    $qrCodeContent = 'http://192.168.1.18:8000/download-pdf/' . $event->getIdevent();

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

#[Route('/download-pdf/{id}', name: 'download_pdf')]
public function downloadPDF($id): Response
{
    $event = $this->getDoctrine()->getRepository(Event::class)->find($id);

    if (!$event) {
        throw $this->createNotFoundException('Event not found for ID: ' . $id);
    }

    // Render the PDF content using the template and event data
    $html = $this->renderView('event/pdf_template.html.twig', [
        'event' => $event,
    ]);

    // Create a new instance of Dompdf
    $dompdf = new Dompdf();

    // Load HTML content into Dompdf
    $dompdf->loadHtml($html);

    // Set paper size and orientation (optional)
    $dompdf->setPaper('A4', 'portrait');

    // Render PDF (optional: you can directly output the PDF to browser instead of saving to a file)
    $dompdf->render();

    // Output PDF as a response
    $response = new Response($dompdf->output(), Response::HTTP_OK);
    $response->headers->set('Content-Type', 'application/pdf');
    $response->headers->set('Content-Disposition', 'attachment; filename="event_' . $event->getIdevent() . '.pdf"');

    return $response;
}


#[Route('/r/search_event', name: 'search_event', methods: ['GET'])]
public function search_event(
    Request $request,
    SerializerInterface $serializer,
): Response {
    $searchValue = $request->query->get('searchValue');
    $orderId = $request->query->get('orderid');

    $em = $this->getDoctrine()->getManager();
    $qb = $em->createQueryBuilder();

    $qb->select('e')
        ->from(Event::class, 'e')
        ->where($qb->expr()->like('e.nameevent', ':value'))
       // ->orWhere($qb->expr()->like('e.dateevent', ':value'))
        ->setParameter('value', '%' . $searchValue . '%');

    if ($orderId === 'DESC') {
        $qb->orderBy('e.nameevent', 'DESC');
    } else {
        $qb->orderBy('e.nameevent', 'ASC');
    }

    $query = $qb->getQuery();
    $event = $query->getResult();

    $jsonData = $serializer->serialize($event, 'json', [
        'groups' => ['event:read']
    ]);
    return new JsonResponse($jsonData);


}
}
