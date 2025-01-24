<?php

namespace App\Controller;

use App\Entity\Certificat;
use App\Entity\Niveau;
use App\Form\NiveauCertType;
use App\Form\NiveauType;
use App\Repository\NiveauRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpKernel\Exception\NotFoundHttpException;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\Mailer\MailerInterface;

use CMEN\GoogleChartsBundle\GoogleCharts\Charts\BarChart;
use App\Notification\NouveauCompteNotification;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Dompdf\Dompdf;
use Dompdf\Options;
use App\Notification\MessageService;


#[Route('/niveau')]
class NiveauController extends AbstractController

{

    private $notify_creation;
    public function __construct()
    {
        // $this->notify_creation = $notify_creation;
    }

    #[Route('/', name: 'app_niveau_index', methods: ['GET'])]
    public function index(NiveauRepository $niveauRepository, Request $request, PaginatorInterface $paginator,MailerInterface $mailer): Response
    {
        $Niv = $niveauRepository->notifyNewNiveau();
        // $this->notify_creation->notify($Niv, $mailer);

        // Récupérer les données à paginer depuis le repository
        $donnees = $niveauRepository->findAll();

        // Paginer les données
        $niveaux = $paginator->paginate(
            $donnees,
            $request->query->getInt('page', 1),
            4
        );

        // Passer les données paginées à la vue Twig
        return $this->render('back/niveau/index.html.twig', [
            'niveaux' => $niveaux,
        ]);
    }


    #[Route("/back", name: "niveau_back", methods: ['GET'])]
    public function niveauback(NiveauRepository $niveauRepository): Response
    {
        $niveau = $this->getDoctrine()->getRepository(Niveau::class)->findAll();

        // Passez les apprenants à la vue Twig
        return $this->render('back/niveau/listniveauback.html.twig', [
            'niveau' => $niveau,
        ]);
    }
    #[Route('/showOne/{id}', name: 'app_niveau_show', methods: ['GET'])]
    public function show(string $id, NiveauRepository $niveauRepository): Response
    {
        // Convertir l'ID en entier
        $id = (int) $id;

        $niveau = $niveauRepository->find($id);

        if (!$niveau) {
            throw new NotFoundHttpException('Niveau not found');
        }

        return $this->render('back/niveau/show.html.twig', [
            'niveau' => $niveau,
        ]);
    }

    #[Route('/add', name: 'app_niveau_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager, MessageService $messageService): Response
    {
        $niveau = new Niveau();
        $form = $this->createForm(NiveauCertType::class, $niveau);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {

            $file = $form->get('image')->getData();
            if($file)
            {
                $fileName = md5(uniqid()).'.'.$file->guessExtension();
                try {
                    $file->move(
                        $this->getParameter('images_directory'),
                        $fileName
                    );
                } catch (FileException $e){

                }
                $niveau->setImage($fileName);
            }
            else
            {
                $niveau->setImage("course-2.jpg");
            }

            $entityManager->persist($niveau);
            $entityManager->flush();

            // Utiliser le service MessageService pour ajouter le message flash
            $messageService->addSuccess('Level a été ajouté avec succès.');

            return $this->redirectToRoute('app_niveau_index');
        }

        return $this->render('back/niveau/add.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    #[Route('/statistiques', name: 'app_niveau_statistique')]
    public function stat(NiveauRepository $repository): Response
    {
        // Utilisez une méthode du repository pour récupérer les statistiques souhaitées
        $niveauxStats = $repository->statistiquesNiveau();

        // Préparez les données pour le graphique
        $data = [['Prérequis', 'Number of Levels']];
        foreach ($niveauxStats as $stat) {
            $data[] = [$stat['prerequis'], $stat['countPrerequis']];
        }

        // Créez l'objet Barchart et configurez les options
        $barChart = new barchart();
        $barChart->getData()->setArrayToDataTable($data);
        $barChart->getOptions()->getTitleTextStyle()->setColor('#07600');
        $barChart->getOptions()->getTitleTextStyle()->setFontSize(50);

        // Renvoyer la vue Twig avec les données du graphique et les statistiques des niveaux
        return $this->render('back/niveau/stat.html.twig', [
            'barchart' => $barChart,
            'nbs' => $niveauxStats,
        ]);
    }

    #[Route('/niveaux', name: 'app_niveau_front', methods: ['GET'])]
    public function front(NiveauRepository $niveauRepository, Request $request, PaginatorInterface $paginator): Response
    {
        // Récupérer les données à paginer depuis le repository
        $donnees = $niveauRepository->findAll();

        // Paginer les données
        $niveaux = $paginator->paginate(
            $donnees,
            $request->query->getInt('page', 1),
            4 // Nombre d'éléments par page
        );

        // Passer les données paginées à la vue Twig
        return $this->render('front/niveau/showfront.html.twig', [
            'niveaux' => $niveaux,
        ]);
    }





    #[Route('/edit/{id}', name: 'app_niveau_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, $id, EntityManagerInterface $entityManager): Response
    {
        $niveau = $this->getDoctrine()->getRepository(Niveau::class)->find($id);

        $form = $this->createForm(NiveauCertType::class, $niveau);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('image')->getData();
            if($file)
            {
                $fileName = md5(uniqid()).'.'.$file->guessExtension();
                try {
                    $file->move(
                        $this->getParameter('images_directory'),
                        $fileName
                    );
                } catch (FileException $e){

                }
                $niveau->setImage($fileName);
            }

            $entityManager->flush();

            return $this->redirectToRoute('app_niveau_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('back/niveau/edit.html.twig', [
            'niveau' => $niveau,
            'form' => $form,
        ]);
    }

    #[Route('/delete/{id}', name: 'app_niveau_delete')]
    public function delete(Request $request, $id, EntityManagerInterface $entityManager): Response
    {
        $niveau = $this->getDoctrine()->getRepository(Niveau::class)->find($id);

            $entityManager->remove($niveau);
            $entityManager->flush();


        return $this->redirectToRoute('app_niveau_index', [], Response::HTTP_SEE_OTHER);
    }
    #[Route('/tri', name: 'app_niveau_tri')]
    function Order(NiveauRepository  $repository,Request $request,PaginatorInterface $paginator){
        $donnees=$repository->Order();
        $niveaux = $paginator->paginate(
            $donnees, // Requête contenant les données à paginer (ici nos articles)
            $request->query->getInt('page', 1), // Numéro de la page en cours, passé dans l'URL, 1 si aucune page
            4 // Nombre de résultats par page
        );
        return $this->render("niveau/index.html.twig",
            ['niveaux'=>$niveaux]);
    }

    #[Route('/search', name:'search_niveau', methods:['GET'])]
    public function search(Request $request): Response
    {
        $query = $request->query->get('query');

        // Perform the search query using your repository or ORM
        $entityManager = $this->getDoctrine()->getManager();
        $niveaux = $entityManager->getRepository(Niveau::class)->findBySearchQuery($query);

        // Render the template with the search results
        return $this->render('apprenants/index.html.twig', [
            'niveaux' => $niveaux,
        ]);
    }

    #[Route('/imprimerNi', name: 'app_niveau_imprimerNi')]
    public function imprimer(NiveauRepository  $repository )

    {
        // Configure Dompdf according to your needs
        $pdfOptions = new Options();
        $pdfOptions->set('isRemoteEnabled', true);
        $pdfOptions->set('isHtml5ParserEnabled', true);
        $pdfOptions->set('defaultFont', 'Arial');

        // Instantiate Dompdf with our options
        $dompdf = new Dompdf($pdfOptions);

        $niveaux = $repository->findAll();

        // Retrieve the HTML generated in our twig file
        $html = $this->renderView('back/niveau/pdf.html.twig', [
            'niveaux' => $niveaux,
        ]);

        // Load HTML to Dompdf
        $dompdf->loadHtml($html);

        // (Optional) Setup the paper size and orientation 'portrait' or 'portrait'
        $dompdf->setPaper('A4', 'portrait');

        // Render the HTML as PDF
        $dompdf->render();

        // Output the generated PDF to Browser (inline view)
        $dompdf->stream("Liste  Niveau.pdf", [
            "Attachment" => true

        ]);
    }
}