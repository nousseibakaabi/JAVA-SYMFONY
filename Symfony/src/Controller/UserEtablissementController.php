<?php

namespace App\Controller;
use App\Entity\User;
use App\Entity\Etablissement;
use App\Entity\UserEtablissement;
use Symfony\Component\HttpFoundation\Request;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\UserEtablissementRepository;
use Symfony\Component\HttpFoundation\JsonResponse;



class UserEtablissementController extends AbstractController
{
    private $userEtablissementRepository;

    public function __construct(UserEtablissementRepository $userEtablissementRepository)
    {
        $this->userEtablissementRepository = $userEtablissementRepository;
    }

    #[Route('/views/{id}', name: 'view')]
    public function view(int $id, EntityManagerInterface $entityManager): Response
    {
        $etablissement = $this->getDoctrine()->getRepository(Etablissement::class)->find($id);
        $user = $this->getUser();
    
        if (!$etablissement) {
            $this->addFlash('error', 'Établissement introuvable');
            return $this->redirectToRoute('app_etablissement_index');
        }
    
        // Récupérer le nombre de vues à partir de la relation entre UserEtablissement et Etablissement
        $views = $etablissement->getViews();
    
        // Incrémenter le nombre de vues
        $views++;
    
        // Mettre à jour le nombre de vues dans l'entité Etablissement
        $etablissement->setViews($views);
    
        // Enregistrer les changements dans la base de données
        $entityManager->flush();
    
        // Rediriger vers la page d'accueil ou une autre page appropriée
        $this->addFlash('success', 'Vue enregistrée avec succès pour cet établissement.');
        return $this->redirectToRoute('app_etablissement_index');
    }
    


    #[Route('/like/{id}', name: 'like')]
    public function likeAction(int $id, EntityManagerInterface $entityManager): Response
    {
        $etablissement = $entityManager->getRepository(Etablissement::class)->find($id);

        if (!$etablissement) {

            $this->addFlash('error', 'Établissement introuvable');
            return $this->redirectToRoute('app_etablissement_index', [], Response::HTTP_SEE_OTHER);
        }

        $user = $this->getUser();

        if (!$user) {
            $this->addFlash('error', 'Vous devez vous connecter pour aimer un établissement');
            return $this->redirectToRoute('app_etablissement_index', [], Response::HTTP_SEE_OTHER);
        }

        $userEtablissement = $entityManager->getRepository(UserEtablissement::class)->findOneBy([
            'user' => $user,
            'etablissement' => $etablissement
        ]);

        if (!$userEtablissement) {
            $userEtablissement = new UserEtablissement();
            $userEtablissement->setUser($user);
            $userEtablissement->setEtablissement($etablissement);
        }

        $userEtablissement->setLiked(true);
        $userEtablissement->setDisliked(false);

        $entityManager->persist($userEtablissement);
        $entityManager->flush();


        $this->addFlash('success', 'Vous avez aimé cet établissement');
        return $this->redirectToRoute('app_etablissement_index', [], Response::HTTP_SEE_OTHER);

    }

    #[Route('/dislike/{id}', name: 'dislike')]
    public function dislikeAction(int $id, EntityManagerInterface $entityManager): Response
    {
        $etablissement = $entityManager->getRepository(Etablissement::class)->find($id);

        if (!$etablissement) {
            $this->addFlash('error', 'Établissement introuvable');
            return $this->redirectToRoute('app_etablissement_index', [], Response::HTTP_SEE_OTHER);
        }

        $user = $this->getUser();

        if (!$user) {
            $this->addFlash('error', 'Vous devez vous connecter pour ne pas aimer un établissement.');
            return $this->redirectToRoute('app_etablissement_index', [], Response::HTTP_SEE_OTHER);
        }

        $userEtablissement = $entityManager->getRepository(UserEtablissement::class)->findOneBy([
            'user' => $user,
            'etablissement' => $etablissement
        ]);

        if (!$userEtablissement) {
            $userEtablissement = new UserEtablissement();
            $userEtablissement->setUser($user);
            $userEtablissement->setEtablissement($etablissement);
        }

        $userEtablissement->setLiked(false);
        $userEtablissement->setDisliked(true);

        $entityManager->persist($userEtablissement);
        $entityManager->flush();

        $this->addFlash('success', 'Vous n\'avez pas aimé cet établissement.');
        return $this->redirectToRoute('app_etablissement_index', [], Response::HTTP_SEE_OTHER);

    }


    #[Route('/add-to-favorites/{etablissementId}', name: 'user_add_to_favorites')]
    public function addToFavorites(int $etablissementId, EntityManagerInterface $entityManager): JsonResponse
    {
        // Récupérer l'utilisateur actuellement connecté
        $user = $this->getUser();
    
        // Vérifier si l'utilisateur est connecté
        if (!$user) {
            // Gérer le cas où l'utilisateur n'est pas connecté
            return new JsonResponse(['error' => 'You must be logged in to add favorites.'], Response::HTTP_UNAUTHORIZED);
        }
    
        // Récupérer l'établissement à partir de son ID
        $etablissement = $entityManager->getRepository(Etablissement::class)->find($etablissementId);
    
        // Vérifier si l'établissement existe
        if (!$etablissement) {
            // Gérer le cas où l'établissement n'existe pas
            return new JsonResponse(['error' => 'The establishment does not exist.'], Response::HTTP_NOT_FOUND);
        }
    
        // Vérifier si l'établissement est déjà marqué comme favori pour l'utilisateur
        $userEtablissement = $entityManager->getRepository(UserEtablissement::class)->findOneBy([
            'user' => $user,
            'etablissement' => $etablissement
        ]);
    
        // Si l'établissement est déjà dans les favoris de l'utilisateur
        if ($userEtablissement) {
            // Si l'établissement est déjà marqué comme favori, retirez-le des favoris
            if ($userEtablissement->isFavoris()) {
                $userEtablissement->setFavoris(false);
                $entityManager->persist($userEtablissement);
                $entityManager->flush();
                return new JsonResponse(['success' => 'Etablissement removed from favorites.']);
            }
            // Si l'établissement n'est pas marqué comme favori, ajoutez-le aux favoris
            else {
                $userEtablissement->setFavoris(true);
                $entityManager->persist($userEtablissement);
                $entityManager->flush();
                return new JsonResponse(['success' => 'Etablissement added to favorites.']);
            }
        } else {
            // Créer une nouvelle relation UserEtablissement pour l'utilisateur actuel et l'établissement
            $userEtablissement = new UserEtablissement();
            $userEtablissement->setUser($user);
            $userEtablissement->setEtablissement($etablissement);
            $userEtablissement->setFavoris(true);
    
            // Enregistrer la nouvelle relation dans la base de données
            $entityManager->persist($userEtablissement);
            $entityManager->flush();
    
            // Retourner une réponse JSON avec un message de succès
            return new JsonResponse(['success' => 'Etablissement added to favorites.']);
        }
    }
    
    

    #[Route('/favorites', name: 'user_favorites')]
    public function showFavorites(): Response
    {
        // Récupérer l'utilisateur actuellement connecté
        $user = $this->getUser();

        // Vérifier si l'utilisateur est connecté
        if (!$user) {
            // Rediriger vers la page de connexion si l'utilisateur n'est pas connecté
            return $this->redirectToRoute('app_login');
        }

        // Récupérer les établissements favoris de l'utilisateur en utilisant la méthode getFavorites()
        $favorites = $user->getFavorites(); 

        // Afficher les établissements favoris dans un template
        return $this->render('front/user/favorites.html.twig', [
            'favorites' => $favorites,
        ]);
    }
}
