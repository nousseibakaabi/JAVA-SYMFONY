<?php

namespace App\Controller;

use App\Entity\Tuteur;
use App\Form\TuteurType;
use App\Repository\TuteurRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/tuteur')]
class TuteurController extends AbstractController
{
    #[Route('/', name: 'app_tuteur_index', methods: ['GET'])]
    public function index(TuteurRepository $tuteurRepository): Response
    {
        return $this->render('tuteur/index.html.twig', [
            'tuteurs' => $tuteurRepository->findAll(),
        ]);
    }
    #[Route('/new', name: 'app_tuteur_new', methods: ['GET', 'POST'])]
public function new(Request $request, EntityManagerInterface $entityManager): Response
{
    $tuteur = new Tuteur();
    $form = $this->createForm(TuteurType::class, $tuteur);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $entityManager->persist($tuteur);
        $entityManager->flush();

        return $this->redirectToRoute('app_tuteur_index', [], Response::HTTP_SEE_OTHER);
    }

    return $this->render('tuteur/new.html.twig', [
        'form' => $form->createView(),
    ]);
}



    #[Route('/list', name: 'app_tuteur')]
public function listTuteurs(): Response
{
    
    $tuteur = $this->getDoctrine()->getRepository(Tuteur::class)->findAll();

    
    return $this->render('tuteur/show.html.twig', [
        'tuteur' => $tuteur,
    ]);
}

#[Route('/listb', name: 'back')]
public function listTuteur(): Response
{
    
    $tuteur = $this->getDoctrine()->getRepository(Tuteur::class)->findAll();

    
    return $this->render('tuteur/listback.html.twig', [
        'tuteur' => $tuteur,
    ]);
}

#[Route('/{id_tuteur}/edit', name: 'app_tuteur_edit', methods: ['GET', 'POST'])]
public function edit(Request $request, Tuteur $tuteur, EntityManagerInterface $entityManager): Response
{
    $form = $this->createForm(TuteurType::class, $tuteur);
    $form->handleRequest($request);

    if ($form->isSubmitted() && $form->isValid()) {
        $entityManager->flush();

        return $this->redirectToRoute('app_tuteur_index', [], Response::HTTP_SEE_OTHER);
    }

    return $this->renderForm('tuteur/edit.html.twig', [
        'tuteur' => $tuteur,
        'form' => $form,
    ]);
}


    #[Route('/{id_tuteur}', name: 'app_tuteur_delete', methods: ['POST'])]
    public function delete(Request $request, Tuteur $tuteur, EntityManagerInterface $entityManager): Response
    {
        if ($this->isCsrfTokenValid('delete'.$tuteur->getId_tuteur(), $request->request->get('_token'))) {
            $entityManager->remove($tuteur);
            $entityManager->flush();
        }

        return $this->redirectToRoute('app_tuteur_index', [], Response::HTTP_SEE_OTHER);
    }
}
