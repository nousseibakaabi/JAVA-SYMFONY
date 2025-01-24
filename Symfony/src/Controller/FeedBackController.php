<?php

namespace App\Controller;

use App\Entity\Feedback;
use App\Form\FeedbackType;
use App\Repository\FeedbackRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/feed/back')]
class FeedBackController extends AbstractController
{
    #[Route('/', name: 'app_feed_back_index', methods: ['GET'])]
    public function index(FeedbackRepository $feedbackRepository): Response
    {
        return $this->render('front/feed_back/index.html.twig', [
            'feedback' => $feedbackRepository->findAll(),
        ]);
    }

    #[Route('/show/{id}', name: 'app_feed_back_show', methods: ['GET'])]
    public function show(Feedback $feedback): Response
    {
        return $this->render('front/feed_back/show.html.twig', [
            'feedback' => $feedback,
        ]);
    }



    #[Route('/new', name: 'app_feed_back_new', methods: ['GET', 'POST'])]
   
    public function new(Request $request): Response
{
    $feedback = new Feedback();
    $form = $this->createForm(FeedbackType::class, $feedback);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
       
        // Save the feedback to the database
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($feedback);
        $entityManager->flush();

        // Redirect to the feedback list or wherever appropriate
        return $this->redirectToRoute('app_feedback_front');
    }

    return $this->render('front/feed_back/add.html.twig', [
        'form' => $form->createView(),
    ]);
}
    #[Route('/avis', name: 'app_feedback_front', methods: ['GET'])]
    public function front(FeedbackRepository $feedbackRepository): Response
    {

        return $this->render('front/feed_back/showfront.html.twig', [
            'feedback' => $feedbackRepository->findAll(),
        ]);
       
    }
    #[Route('/listfeedBack', name: 'app_feedback_list', methods: ['GET'])]
    
    public function listFeedBackByRating(EntityManagerInterface $entityManager)
    {
        // Récupérer tous les feedbacks depuis la base de données
        $feedbacks = $entityManager->getRepository(FeedBack::class)->findAll();

        // Initialiser un tableau vide pour stocker les feedbacks triés par notation
        $feedbacksByRating = [];

        // Regrouper les feedbacks par leur notation respective
        foreach ($feedbacks as $feedback) {
            $rating = $feedback->getRating();
            $feedbacksByRating[$rating][] = $feedback;
        }

        // Passer les groupes de feedbacks triés à la vue Twig
        return $this->render('feed_back/listfeedback.html.twig', [
            'feedbacksByRating' => $feedbacksByRating,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_feed_back_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, Feedback $feedback, EntityManagerInterface $entityManager): Response
    {
        $form = $this->createForm(FeedbackType::class, $feedback);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();

            return $this->redirectToRoute('app_feed_back_index', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('front/feed_back/edit.html.twig', [
            'feedback' => $feedback,
            'form' => $form,
        ]);
    }

    #[Route('/{id}', name: 'app_feed_back_delete', methods: ['POST'])]
    public function delete(Request $request, Feedback $feedback, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$feedback->getId(), $request->request->get('_token'))) {
            $entityManager->remove($feedback);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_feed_back_index', [], Response::HTTP_SEE_OTHER);
    }
    
}
