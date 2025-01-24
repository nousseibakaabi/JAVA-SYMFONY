<?php

namespace App\Controller;

use App\Entity\Apprenants;
use App\Form\ApprenantsType;
use App\Repository\ApprenantsRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Sensio\Bundle\FrameworkExtraBundle\Configuration\IsGranted;



#[Route('/apprenants')]
class ApprenantsController extends AbstractController
{
    #[Route('/', name: 'app_apprenants_index', methods: ['GET'])]
public function index(ApprenantsRepository $apprenantsRepository): Response
    {
        return $this->render('back/apprenants/index.html.twig', [
            'apprenants' => $apprenantsRepository->findAll(),
        ]);
    } 

    #[Route('/listb', name: 'app_apprenants_listback')]
    public function listapprenantsback(): Response
    {

        $apprenants = $this->getDoctrine()->getRepository(Apprenants::class)->findAll();


        return $this->render('back/apprenants/listback.html.twig', [
            'apprenants' => $apprenants,
        ]);
    }



    #[Route('/add', name: 'app_apprenants_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager): Response
    {
        $apprenant = new Apprenants();
        $form = $this->createForm(ApprenantsType::class, $apprenant);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $apprenant = $form->getData();

            // Récupérer le fichier téléchargé depuis le formulaire
            $imageFile = $form['image']->getData();

            // Générer un nom unique pour le fichier
            $image = md5(uniqid()) . '.' . $imageFile->guessExtension();

            // Déplacer le fichier vers le répertoire de destination
            $imageFile->move(
                $this->getParameter('images_directory'),
                $image
            );

            // Mettre à jour le nom de fichier dans l'entité Apprenant
            $apprenant->setImage($image);

            // Enregistrer l'entité dans la base de données
            $entityManager->persist($apprenant);
            $entityManager->flush();

            $this->addFlash('success', 'L\'apprenant a été ajouté avec succès.');
            return $this->redirectToRoute('app_apprenants_index');
        }

        return $this->render('back/apprenants/add.html.twig', [
            'form' => $form->createView(),
        ]);
    }


    

    #[Route('/{id}', name: 'app_apprenants_show', methods: ['GET'])]
    public function show($id): Response
    {
        $apprenant = $this->getDoctrine()->getRepository(Apprenants::class)->find($id);

        return $this->render('back/apprenants/show.html.twig', [
            'apprenant' => $apprenant,
        ]);
    }

    #[Route('/{id}/edit', name: 'app_apprenants_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, $id, EntityManagerInterface $entityManager): Response
    {
        $apprenant = $this->getDoctrine()->getRepository(Apprenants::class)->find($id);

        $form = $this->createForm(ApprenantsType::class, $apprenant);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // Gérer l'image comme dans la méthode new
            $imageFile = $form['image']->getData();
            if ($imageFile) {
                $image = md5(uniqid()) . '.' . $imageFile->guessExtension();
                $imageFile->move(
                    $this->getParameter('images_directory'),
                    $image
                );
                $apprenant->setImage($image);
            }

            // Mettre à jour l'entité dans la base de données
            $entityManager->flush();

            $this->addFlash('success', 'L\'apprenant a été modifié avec succès.');
            return $this->redirectToRoute('app_apprenants_index'); // Retourner une réponse ici
        }

        // Retourner une réponse en cas où le formulaire n'est pas valide
        return $this->render('back/apprenants/edit.html.twig', [
            'apprenant' => $apprenant,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/{id}', name: 'app_apprenants_delete')]
    public function delete(Request $request, $id, EntityManagerInterface $entityManager): Response
    {
        $apprenant = $this->getDoctrine()->getRepository(Apprenants::class)->find($id);

            $entityManager->remove($apprenant);
            $entityManager->flush();


        return $this->redirectToRoute('app_apprenants_index', [], Response::HTTP_SEE_OTHER);
    }
        #[Route('/search', name:'search_apprenant', methods:['GET'])]
        public function search(Request $request): Response
        {
            $query = $request->query->get('query');

            // Perform the search query using your repository or ORM
            $entityManager = $this->getDoctrine()->getManager();
            $apprenants = $entityManager->getRepository(Apprenants::class)->findBySearchQuery($query);

            // Render the template with the search results
            return $this->render('back/apprenants/listback.html.twig', [
                'apprenants' => $apprenants,
            ]);
        }


}
