<?php

namespace App\Controller;

use App\Entity\Certificat;
use App\Entity\Etablissement;
use App\Entity\UserEtablissement;
use App\Form\EtablissementType;
use App\Repository\ActionEtabRepository;
use App\Repository\EtablissementRepository;
use App\Repository\UserEtablissementRepository;
use CMEN\GoogleChartsBundle\GoogleCharts\Charts\PieChart;
use Doctrine\ORM\EntityManagerInterface;
use Dompdf\Dompdf;
use Dompdf\Options;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\File\Exception\FileException;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Normalizer\NormalizerInterface;
use Symfony\Component\HttpFoundation\JsonResponse;
use Symfony\Component\Serializer\SerializerInterface;

#[Route('/etablissement')]
class EtablissementController extends AbstractController
{

    #[Route('/', name: 'app_etablissement_index')]
    public function showEtablissement(
        EtablissementRepository $etablissementRepository,
        UserEtablissementRepository $userEtablissementRepository
    ): Response {
        $etablissements = $etablissementRepository->findAll();
    
        foreach ($etablissements as $etablissement) {
            $userEtablissements = $userEtablissementRepository->findBy(['etablissement' => $etablissement]);
    
            $likes = 0;
            $dislikes = 0;
            $views = 0; // Ajout de cette ligne pour compter les vues
    
            foreach ($userEtablissements as $userEtablissement) {
                if ($userEtablissement->getLiked()) {
                    $likes++;
                }
                if ($userEtablissement->getDisliked()) {
                    $dislikes++;
                }
                // Compter les vues
                $views += $userEtablissement->getViews();
            }
    
            $etablissement->setLikes($likes);
            $etablissement->setDislikes($dislikes);
            $etablissement->setViews($views); // Définir le nombre de vues pour chaque établissement
        }

        return $this->render('front/etablissement/index.html.twig', [
            'etablissements' => $etablissements,
        ]);
    }

    
    #[Route('/details/{id}', name: 'details')]
    public function viewDetails(int $id, EntityManagerInterface $entityManager): Response
    {
        $etablissement = $this->getDoctrine()->getRepository(Etablissement::class)->find($id);
        $user = $this->getUser();
    
        if (!$etablissement) {
            $this->addFlash('error', 'Établissement introuvable');
            return $this->redirectToRoute('app_etablissement_index');
        }
    
        // Vérifier si l'utilisateur a déjà visité cet établissement
        $userEtablissement = $entityManager->getRepository(UserEtablissement::class)->findOneBy([
            'user' => $user,
            'etablissement' => $etablissement
        ]);
    
        // Si l'utilisateur n'a pas encore visité cet établissement, enregistrer la vue
        if (!$userEtablissement) {
            $userEtablissement = new UserEtablissement();
            $userEtablissement->setUser($user);
            $userEtablissement->setEtablissement($etablissement);
            $userEtablissement->setViews(1); // Définir le nombre de vues à 1 pour la première visite
            $entityManager->persist($userEtablissement);
        } else {
            // l'utilisateur a déjà visité cet établissement, ne pas incrémenter le nombre de vues
        }
    
        // Enregistrer les changements dans la base de données
        $entityManager->flush();
    
        // Rediriger vers la page appropriée
        return $this->redirectToRoute('show_certificats_by_etablissement', ['id' => $id]);
    }
    
    

#[Route('/views/{id}', name: 'etablissement_views', methods: ['GET'])]
public function updateViews(int $id, EntityManagerInterface $entityManager): JsonResponse
{
    // Récupérer l'établissement à partir de l'ID
    $etablissement = $entityManager->getRepository(Etablissement::class)->find($id);

    if (!$etablissement) {
        // Gérer le cas où l'établissement n'est pas trouvé
        return new JsonResponse(['error' => 'Établissement introuvable'], JsonResponse::HTTP_NOT_FOUND);
    }

    // Incrémenter le nombre de vues
    $etablissement->setViews($etablissement->getViews() + 1);

    // Enregistrer les changements dans la base de données
    $entityManager->flush();

    // Répondre avec un JSON pour indiquer que la mise à jour a réussi
    return new JsonResponse(['success' => true]);
}



#[Route('/admin', name: 'app_etablissement_index_admin')]
    public function showEtablissementAdmin(EtablissementRepository $etablissementRepository, UserEtablissementRepository $userEtablissementRepository,ActionEtabRepository $actionEtabRepository): Response
    {
        $etablissements = $etablissementRepository->findAll();
        $notifications = $actionEtabRepository->findAll();

        foreach ($etablissements as $etablissement) {
            $userEtablissements = $userEtablissementRepository->findBy(['etablissement' => $etablissement]);

            $likes = 0;
            $dislikes = 0;

            foreach ($userEtablissements as $userEtablissement) {
                if ($userEtablissement->getLiked()) {
                    $likes++;
                }
                if ($userEtablissement->getDisliked()) {
                    $dislikes++;
                }
            }

            $etablissement->setLikes($likes);
            $etablissement->setDislikes($dislikes);
        }
        $data= array();
        $stat=['Les Types', '%'];
        array_push($data,$stat);

        foreach($etablissements as $tmp)
        {
                // Initialise un tableau pour stocker les statistiques de chaque établissement
            $stat=array();
            $cmp = new Certificat();
                // Récupère tous les certificats associés à l'établissement en cours
            $cmp = $this->getDoctrine()->getManager()->getRepository(Certificat::class)->findBy([
                'idEtablissement' => $tmp
            ]);
                // Calcule le nombre total de certificats pour cet établissement
            $total = count($cmp);
                // Stocke le nom de l'établissement et le nombre total de certificats dans le tableau de statistiques
            $stat=[$tmp->getNomEtablissement(),$total];
                // Ajoute les statistiques de cet établissement au tableau de données
            array_push($data,$stat);
        }
        $pieChart = new PieChart();
        $pieChart->getData()->setArrayToDataTable(
            $data
        );
        $pieChart->getOptions()->setTitle('Les Certificats');
        $pieChart->getOptions()->setHeight(500);
        $pieChart->getOptions()->setWidth(900);
        $pieChart->getOptions()->getTitleTextStyle()->setBold(true);
        $pieChart->getOptions()->getTitleTextStyle()->setColor('#009900');
        $pieChart->getOptions()->getTitleTextStyle()->setItalic(true);
        $pieChart->getOptions()->getTitleTextStyle()->setFontName('Arial');
        $pieChart->getOptions()->getTitleTextStyle()->setFontSize(20);
        return $this->render('back/etablissement/index.html.twig', [
            'etablissements' => $etablissements, // Correction ici
            'piechart' => $pieChart,
            'notifications' => $notifications
        ]);
    }


    
    #[Route('/etablissement/{idEtablissement}/pdf', name: 'generate_pdf_etab')]
    public function generatePdf($idEtablissement): Response
    {
        // Récupérer l'établissement par son ID
        $etablissement = $this->getDoctrine()->getRepository(Etablissement::class)->find($idEtablissement);
    
        // Vérifier si l'établissement existe
        if (!$etablissement) {
            throw $this->createNotFoundException('Etablissement non trouvé pour l\'ID : ' . $idEtablissement);
        }
    
        // Récupérer les certificats associés à cet établissement
        $certificats = $this->getDoctrine()->getRepository(Certificat::class)->findBy(['idEtablissement' => $idEtablissement]);
    
        // Rendu du PDF avec toutes les données nécessaires
        $html = $this->renderView('front/etablissement/generate_pdf.html.twig', [
            'etablissement' => $etablissement,
            'certificats' => $certificats,
        ]);
        
        // Configuration de Dompdf
        $options = new Options();
        $options->set('isHtml5ParserEnabled', true);
        $options->set('defaultFont', 'Arial');
        
        // Instancie Dompdf avec les options configurées
        $dompdf = new Dompdf($options);
        $dompdf->loadHtml($html);
        
        // Définit la taille et l'orientation du papier
        $dompdf->setPaper('A4', 'portrait');
        
        // Rendu du PDF
        $dompdf->render();
        
        // Nom du fichier PDF généré
        $pdfFileName = 'etablissement_' . $idEtablissement . '.pdf';
        
        // Chemin du répertoire temporaire où sauvegarder le PDF
        $pdfPath = tempnam(sys_get_temp_dir(), 'etablissement_pdf_');
        
        // Enregistre le PDF généré dans le répertoire temporaire
        file_put_contents($pdfPath, $dompdf->output());
        
        // Renvoie le PDF généré au navigateur (téléchargement en ligne)
        return new BinaryFileResponse($pdfPath);
    }
    
    

    #[Route('/new', name: 'app_etablissement_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager,ActionEtabRepository $actionEtabRepository): Response
    {
        $etablissement = new Etablissement();
        $form = $this->createForm(EtablissementType::class, $etablissement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('imgEtablissement')->getData();
            if ($file) {
                $fileName = md5(uniqid()) . '.' . $file->guessExtension();
                try {
                    $file->move(
                        $this->getParameter('images_directory'),
                        $fileName
                    );
                } catch (FileException $e) {

                }
                $etablissement->setImgEtablissement($fileName);
            } else {
                $etablissement->setImgEtablissement("NoImage.png");
            }
            $entityManager->persist($etablissement);
            $entityManager->flush();

            $text = "L'établissement " . $etablissement->getNomEtablissement() . " a été créé le " . date('Y-m-d H:i:s');
            $actionEtabRepository->insert($text);

            return $this->redirectToRoute('app_etablissement_index_admin', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('back/etablissement/new.html.twig', [
            'etablissement' => $etablissement,
            'form' => $form,
        ]);
    }

    #[Route('/show/{id}', name: 'app_etablissement_show', methods: ['GET'])]
    public function show($id): Response
    {
        $etablissement = $this->getDoctrine()->getRepository(Etablissement::class)->find($id);

        return $this->render('back/etablissement/show.html.twig', [
            'etablissement' => $etablissement,
        ]);
    }

    #[Route('/edit/{id}', name: 'app_etablissement_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, $id, EntityManagerInterface $entityManager,ActionEtabRepository $actionEtabRepository): Response
    {
        $etablissement = $this->getDoctrine()->getRepository(Etablissement::class)->find($id);

        $form = $this->createForm(EtablissementType::class, $etablissement);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $file = $form->get('imgEtablissement')->getData(); // Access uploaded file correctly
            if ($file) {
                $fileName = md5(uniqid()) . '.' . $file->guessExtension();
                try {
                    $file->move(
                        $this->getParameter('images_directory'),
                        $fileName
                    );
                } catch (FileException $e) {
                    // Handle file upload error
                }
                $etablissement->setImgEtablissement($fileName);
            }
            $text = "L'établissement " . $etablissement->getNomEtablissement() . " a été modifié le " . date('Y-m-d H:i:s');
            $actionEtabRepository->insert($text);

            $entityManager->flush();

            return $this->redirectToRoute('app_etablissement_index_admin', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('back/etablissement/edit.html.twig', [
            'etablissement' => $etablissement,
            'form' => $form,
        ]);
    }

    #[Route('/delete/{id}', name: 'app_etablissement_delete', methods: ['POST'])]
    public function deleteEtablissement(int $id,ActionEtabRepository $actionEtabRepository): Response
    {
        $entityManager = $this->getDoctrine()->getManager();
        $etablissementRepository = $entityManager->getRepository(Etablissement::class);
        $etablissement = $etablissementRepository->find($id);

        if (!$etablissement) {
            throw $this->createNotFoundException('Etablissement not found');
        }

        // Retrieve associated UserEtablissement entities
        $userEtablissementRepository = $entityManager->getRepository(UserEtablissement::class);
        $userEtablissements = $userEtablissementRepository->findBy(['etablissement' => $etablissement]);

        // Remove associated UserEtablissement entities
        foreach ($userEtablissements as $userEtablissement) {
            $entityManager->remove($userEtablissement);
        }

        // Flush the changes before deleting the Etablissement
        $entityManager->flush();

        // Delete the Etablissement
        $entityManager->remove($etablissement);
        $entityManager->flush();
        $text = "L'établissement " . $etablissement->getNomEtablissement() . " a été suprimé le " . date('Y-m-d H:i:s');
        $actionEtabRepository->insert($text);

        return $this->redirectToRoute('app_etablissement_index_admin', [], Response::HTTP_SEE_OTHER);
    }

    private $entityManager;
    public function __construct(EntityManagerInterface $entityManager)
    {
        $this->entityManager = $entityManager;
    }

    #[Route('/r/search_etablissement', name: 'search_etablissement', methods: ['GET'])]
    public function searchEtablissement(Request $request,SerializerInterface $serializer): Response {
        $searchValue = $request->query->get('searchValue');
        $orderId = $request->query->get('orderid');

        $qb = $this->entityManager->createQueryBuilder();

        $qb->select('e')
            ->from(Etablissement::class, 'e')
            ->where($qb->expr()->like('e.nomEtablissement', ':value'))
            ->orWhere($qb->expr()->like('e.adresseEtablissement', ':value'))
            ->setParameter('value', '%' . $searchValue . '%');

        if ($orderId === 'DESC') {
            $qb->orderBy('e.ID_Etablissement', 'DESC');
        } else {
            $qb->orderBy('e.ID_Etablissement', 'ASC');
        }

        $query = $qb->getQuery();
        $etablissements = $query->getResult();

        // Calculate likes and dislikes for each establishment
        foreach ($etablissements as $etablissement) {
            $likes = 0;
            $dislikes = 0;

            foreach ($etablissement->getUserEtablissements() as $userEtablissement) {
                if ($userEtablissement->getLiked()) {
                    $likes++;
                }
                if ($userEtablissement->getDisliked()) {
                    $dislikes++;
                }
            }

            $etablissement->setLikes($likes);
            $etablissement->setDislikes($dislikes);
        }

        // Serialize the data into JSON format
        $jsonData = $serializer->serialize($etablissements, 'json', [
            'groups' => ['etablissement:read']
        ]);
        return new JsonResponse($jsonData);
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
