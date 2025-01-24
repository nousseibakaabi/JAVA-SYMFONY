<?php

namespace App\Controller;

use App\Entity\Formationn;
use App\Form\FormationnType;
use App\Repository\FormationnRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\File\UploadedFile;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Session\SessionInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Process\Exception\ProcessFailedException;
use Symfony\Component\Process\Process;
use Symfony\Component\Serializer\SerializerInterface;

#[Route('/formation')]
class FormationController extends AbstractController
{
    #[Route('/', name: 'app_formation_index', methods: ['GET'])]
    public function index(FormationnRepository $formationnRepository): Response
    {
        return $this->render('back/formation/index.html.twig', [
            'formationns' => $formationnRepository->findAll(),
        ]);
    }

    #[Route('/front', name: 'app_formation_index_front', methods: ['GET'])]
    public function indexFront(FormationnRepository $formationnRepository): Response
    {
        return $this->render('front/formation/index.html.twig', [
            'formationns' => $formationnRepository->findAll(),
        ]);
    }

    #[Route('/new', name: 'app_formation_new', methods: ['GET', 'POST'])]
    public function new(Request $request): Response
    {
        $formation = new Formationn();
        $form = $this->createForm(FormationnType::class, $formation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $pdfFile = $form->get('pdfFile')->getData();
            if (!$form->get('pdfFile')->isEmpty()) {
                if ($pdfFile instanceof UploadedFile) {
                    $originalFilename = pathinfo($pdfFile->getClientOriginalName(), PATHINFO_FILENAME);
                    $newFilename = $originalFilename.'-'.uniqid().'.'.$pdfFile->guessExtension();

                    try {
                        $pdfFile->move(
                            $this->getParameter('pdf_directory'),
                            $newFilename
                        );
                    } catch (FileException $e) {
                        // Handle the file exception if needed
                    }

                    $formation->setPdfFilename($newFilename);
                }
            }

            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($formation);
            $entityManager->flush();
            return $this->redirectToRoute('app_formation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('back/formation/new.html.twig', [
            'formation' => $formation,
            'form' => $form->createView(),
        ]);
    }

    #[Route('front/new', name: 'app_formation_new_front', methods: ['GET', 'POST'])]
    public function newfront(Request $request): Response
    {
        $formation = new Formationn();
        $form = $this->createForm(FormationnType::class, $formation);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $pdfFile = $form->get('pdfFile')->getData();
            if (!$form->get('pdfFile')->isEmpty()) {
                if ($pdfFile instanceof UploadedFile) {
                    $originalFilename = pathinfo($pdfFile->getClientOriginalName(), PATHINFO_FILENAME);
                    $newFilename = $originalFilename.'-'.uniqid().'.'.$pdfFile->guessExtension();

                    try {
                        $pdfFile->move(
                            $this->getParameter('pdf_directory'),
                            $newFilename
                        );
                    } catch (FileException $e) {
                        // Handle the file exception if needed
                    }

                    $formation->setPdfFilename($newFilename);
                }
            }

            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($formation);
            $entityManager->flush();

            return $this->redirectToRoute('app_formation_index_front', [], Response::HTTP_SEE_OTHER);
        }

        return $this->render('front/formation/new.html.twig', [
            'formation' => $formation,
            'form' => $form->createView(),
        ]);
    }

    

    #[Route('/show/{Idformation}', name: 'app_formation_show', methods: ['GET'])]
    public function show($Idformation,FormationnRepository $formationnRepository): Response
    {
        $formationn = $formationnRepository->find($Idformation);

        return $this->render('back/formation/show.html.twig', [
            'formationn' => $formationn,
        ]);
    }

    #[Route('/edit/{Idformation}', name: 'app_formation_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, $Idformation, FormationnRepository $formationnRepository,EntityManagerInterface $entityManager): Response
    {

        $formationn = $formationnRepository->find($Idformation);


        $form = $this->createForm(FormationnType::class, $formationn);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {

            $pdfFile = $form->get('pdfFile')->getData();
            if (!$form->get('pdfFile')->isEmpty()) {
                if ($pdfFile instanceof UploadedFile) {
                    $originalFilename = pathinfo($pdfFile->getClientOriginalName(), PATHINFO_FILENAME);
                    $newFilename = $originalFilename.'-'.uniqid().'.'.$pdfFile->guessExtension();

                    try {
                        $pdfFile->move(
                            $this->getParameter('pdf_directory'),
                            $newFilename
                        );
                    } catch (FileException $e) {
                        // Handle the file exception if needed
                    }

                    $formationn->setPdfFilename($newFilename);
                }
            }

            $entityManager->flush();

            return $this->redirectToRoute('app_formation_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('back/formation/edit.html.twig', [
            'formationn' => $formationn,
            'form' => $form,
        ]);
    }

    #[Route('/edit_front/{Idformation}', name: 'app_formation_edit_front', methods: ['GET', 'POST'])]
    public function editfront(Request $request, $Idformation,FormationnRepository $formationnRepository, EntityManagerInterface $entityManager): Response
    {
        $formationn = $formationnRepository->find($Idformation);

        $form = $this->createForm(FormationnType::class, $formationn);
        $form->handleRequest($request);
    
        if ($form->isSubmitted() && $form->isValid()) {
            $pdfFile = $form->get('pdfFile')->getData();
            if (!$form->get('pdfFile')->isEmpty()) {
                if ($pdfFile instanceof UploadedFile) {
                    $originalFilename = pathinfo($pdfFile->getClientOriginalName(), PATHINFO_FILENAME);
                    $newFilename = $originalFilename.'-'.uniqid().'.'.$pdfFile->guessExtension();

                    try {
                        $pdfFile->move(
                            $this->getParameter('pdf_directory'),
                            $newFilename
                        );
                    } catch (FileException $e) {
                        // Handle the file exception if needed
                    }

                    $formationn->setPdfFilename($newFilename);
                }
            }

            $entityManager->flush();
    
            return $this->redirectToRoute('app_formation_index_front', [], Response::HTTP_SEE_OTHER);
        }
    
        return $this->renderForm('front/formation/edit.html.twig', [
            'formationn' => $formationn,
            'form' => $form,
        ]);
    }



    #[Route('/delete/{Idformation}', name: 'app_formation_delete', methods: ['POST'])]
    public function delete(Request $request, $Idformation, EntityManagerInterface $entityManager, FormationnRepository $formationnRepository): Response
    {
        $formationn = $formationnRepository->find($Idformation);

        if (!$formationn) {
            throw $this->createNotFoundException('Formation not found');
        }

        foreach ($formationn->getQuizzes() as $quiz) {
            $entityManager->remove($quiz);
        }

        $entityManager->remove($formationn);
        $entityManager->flush();

        // Redirect to the index page or any other appropriate page after deletion
        return $this->redirectToRoute('app_formation_index', [], Response::HTTP_SEE_OTHER);
    }


    #[Route('/front/{Idformation}', name: 'app_formation_delete_front', methods: ['POST'])]
    public function deleteFront(Request $request, $Idformation, EntityManagerInterface $entityManager,FormationnRepository $formationnRepository): Response
    {
        $formationn = $formationnRepository->find($Idformation);

            $quizzes = $formationn->getQuizzes();

            foreach ($quizzes as $quiz) {
                $entityManager->remove($quiz);
            }
            $entityManager->flush();
            $entityManager->remove($formationn);
            $entityManager->flush();


        return $this->redirectToRoute('app_formation_index_front', [], Response::HTTP_SEE_OTHER);
    }

    //*****************google meet****************//

    #[Route('/open-link', name: 'open_link', methods: ['POST'])]
    public function openLink(Request $request): Response
    {
        $formationId = $request->request->get('Idformation');
        
        // Récupérer la formation depuis la base de données
        $formationn = $this->getDoctrine()->getRepository(Formationn::class)->find($formationId);
    
        if (!$formationn) {
            // Gérer le cas où la formation n'est pas trouvée
            $this->addFlash('error', 'Formation not found.');
            return $this->redirectToRoute('app_formation_index');
        }
    
        $link = $formationn->getLien();
    
        if (empty($link)) {
            $this->addFlash('error', "Le lien est vide.");
            return $this->redirectToRoute('app_formation_index');
        }
    
        try {
            // Ouvrir le lien dans le navigateur
            $process = new Process(['xdg-open', $link]);
            $process->run();
    
            if (!$process->isSuccessful()) {
                throw new ProcessFailedException($process);
            }
        } catch (ProcessFailedException $e) {
            // Gérer les erreurs ici
            $this->addFlash('error', "Erreur lors de l'ouverture du lien : " . $e->getMessage());
        }
    
        return $this->redirectToRoute('app_formation_index');
    }



    #[Route('/open-pdf/{filename}', name: 'open_pdf')]
    public function openPdfAction(string $filename): Response
    {
        $pdfDirectory = $this->getParameter('pdf_directory');
        $filePath = $pdfDirectory . '/' . $filename;

        // Check if the file exists
        if (!file_exists($filePath)) {
            throw $this->createNotFoundException('The file does not exist');
        }

        // Serve the file
        return new BinaryFileResponse($filePath);

    }

    #[Route('/r/search_formation', name: 'search_formation', methods: ['GET'])]
    public function searchFormation(
        Request $request,
        SerializerInterface $serializer,
        FormationnRepository $formationnRepository
    ): JsonResponse {
        $searchValue = $request->query->get('searchValue');
        $orderBy = $request->query->get('orderid');

        $results = $formationnRepository->searchAndOrder($searchValue, $orderBy);

        // Serialize the data into JSON format
        $jsonData = $serializer->serialize($results, 'json', [
            'groups' => ['post:read']
        ]);

        return new JsonResponse($jsonData);
    }

    #[Route('/set-locale', name: 'set_locale_formation')]
    public function setLocale(Request $request, SessionInterface $session): Response
    {
        $locale = $request->request->get('locale');
        $session->set('_locale', $locale);

        // Redirect back to the previous page
        return $this->redirect($request->headers->get('referer'));
    }


}
