<?php

namespace App\Controller;

use App\Entity\Event;
use App\Entity\Partner;
use App\Form\PartnerType;
use App\Repository\PartnerRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Knp\Component\Pager\PaginatorInterface;


#[Route('/partner')]
class PartnerController extends AbstractController
{
    #[Route('/add', name: 'app_partner_add')]//ajout avec un formulaire
    public function newPartner(EntityManagerInterface $em, Request $req): Response
    {
        $partner = new Partner();
        $form=$this->createForm(PartnerType::class,$partner);
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

                // Définir le nom du fichier dans l'entité Partner
                $partner->setImage($fileName);
            }

            // Enregistrer l'entité Partner en base de données
            $em->persist($partner);
            $em->flush();

            // Rediriger vers une autre page après l'ajout d'un événement
            return $this->redirectToRoute("app_partner");
        }
        return $this->render('partner/add.html.twig', [
            'title' => 'Add Partner',
            'formPartner' => $form->createView()
        ]);
    }

    #[Route('/admin', name: 'app_partner')]
    public function Partner(PaginatorInterface $paginator,Request $request): Response
    {
        // Fetch reservations from the database
        $partners = $this->getDoctrine()->getRepository(Partner::class)->findAll();
        
    
        // Paginate the reservations
        $pagination = $paginator->paginate(
            $partners, // Query results
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
        $route = 'app_partner';
    
        // Get the query parameters
        $query = $request->query->all();
    
        // Define the name of the query parameter for the page number
        $pageParameterName = 'page';
    
        // Render the template to display the list of reservations
        return $this->render('partner/listPartnerBack.html.twig', [
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


    #[Route('/', name: 'app_partner_front')]
    public function PartnerFront(PaginatorInterface $paginator,Request $request): Response
    {
        // Fetch reservations from the database
        $partners = $this->getDoctrine()->getRepository(Partner::class)->findAll();
        
    
        // Paginate the reservations
        $pagination = $paginator->paginate(
            $partners, // Query results
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
        $route = 'app_partner_front';
    
        // Get the query parameters
        $query = $request->query->all();
    
        // Define the name of the query parameter for the page number
        $pageParameterName = 'page';
    
        // Render the template to display the list of reservations
        return $this->render('partner/listPartnerFront.html.twig', [
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

    #[Route('/delete/{id}', name: 'app_partner_delete')]
    public function delete($id, EntityManagerInterface $entityManagerInterface, PartnerRepository $partnerRepository)
    {
        $partner= $partnerRepository->find($id);
        if (!$partner) {
            throw $this->createNotFoundException('Partner not found');
        }
        $entityManagerInterface->remove($partner);
        $entityManagerInterface->flush();
        return $this->redirectToRoute('app_partner');
    }

    #[Route('/update/{id}', name: 'app_partner_update')]
    public function fedit(Request $request,$id, EntityManagerInterface $entityManagerInterface, PartnerRepository $partnerRepository)
    {
        $partner= $partnerRepository->find($id);
        $form=$this->createForm(PartnerType::class,$partner);
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
                $partner->setImage($fileName);
            }
            $entityManagerInterface->persist($partner);
            $entityManagerInterface->flush();
            return $this->redirectToRoute('app_partner');
        }
         return $this->renderForm('partner/add.html.twig',[
            'formPartner'=>$form,
            'title'=>'Edit Partner']);   
    }
    #[Route('/event/{id}', name: 'show_partners_by_event')]
    public function showPartnersByEvent(int $id): Response
    {
        $entityManager = $this->getDoctrine()->getManager();

        // Récupérer l'événement par son identifiant
        $event = $entityManager->getRepository(Event::class)->find($id);

        if (!$event) {
            throw $this->createNotFoundException('Event not found');
        }

        // Récupérer les partenaires associés à l'événement en utilisant son identifiant
        $partners = $entityManager->getRepository(Partner::class)->findBy(['idEvent' => $id]);

        // Rendre le template Twig et passer l'événement et ses partenaires associés
        return $this->render('partner/show_partners_by_event.html.twig', [
            'event' => $event,
            'partners' => $partners,
        ]);
    }

}
