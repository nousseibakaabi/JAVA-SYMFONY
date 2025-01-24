<?php

namespace App\Controller;

use App\Entity\Certificat;
use App\Entity\Etablissement;
use App\Form\CertificatType;
use Dompdf\Dompdf;
use Dompdf\Options;
use App\Repository\ActionEtabRepository;
use Symfony\Component\HttpFoundation\File\File;
use Symfony\Component\HttpFoundation\BinaryFileResponse;
use App\Repository\CertificatRepository;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use PhpOffice\PhpSpreadsheet\Spreadsheet;
use PhpOffice\PhpSpreadsheet\Writer\Xlsx;

#[Route('/certificat')]
class CertificatController extends AbstractController
{
    #[Route('/etablissement/{id}', name: 'show_certificats_by_etablissement')]
    public function showCertificatsByEtablissement(int $id): Response
    {

        $entityManager = $this->getDoctrine()->getManager();
        $etablissement = $entityManager->getRepository(Etablissement::class)->find($id);

        if (!$etablissement) {
            throw $this->createNotFoundException('Etablissement not found');
        }

        // Fetch Certificat entities associated with the given establishment ID
        $certificats = $entityManager->getRepository(Certificat::class)->findBy(['idEtablissement' => $etablissement]);

        // Render the Twig template and pass the establishment and its associated certificates
        return $this->render('front/certificat/show_certificats_by_etablissement.html.twig', [
            'etablissement' => $etablissement,
            'certificats' => $certificats,
        ]);
    }

    #[Route('/', name: 'app_certificat_index', methods: ['GET'])]
    public function index(CertificatRepository $certificatRepository): Response
    {
        return $this->render('front/certificat/index.html.twig', [
            'certificats' => $certificatRepository->findAll(),

        ]);
    }
    #[Route('/admin', name: 'app_certificat_index_admin', methods: ['GET'])]
    public function indexAdmin(CertificatRepository $certificatRepository, ActionEtabRepository $actionEtabRepository): Response
    {
        $certificats = $certificatRepository->findAll();
        $notifications = $actionEtabRepository->findAll();

        return $this->render('back/certificat/index.html.twig', [
            'certificats' => $certificats,
            'notifications' => $notifications
        ]);
    }


    #[Route('/new', name: 'app_certificat_new', methods: ['GET', 'POST'])]
    public function new(Request $request, EntityManagerInterface $entityManager,ActionEtabRepository $actionEtabRepository): Response
    {
        $certificat = new Certificat();
        $form = $this->createForm(CertificatType::class, $certificat);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->persist($certificat);
            $entityManager->flush();
            $text = "Le certificat " . $certificat->getNomCertificat() . " a été créé le " . date('Y-m-d H:i:s');
            $actionEtabRepository->insert($text);

            return $this->redirectToRoute('app_certificat_index_admin', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('back/certificat/new.html.twig', [
            'certificat' => $certificat,
            'form' => $form,

        ]);
    }

    #[Route('/showCertif/{idCertificat}', name: 'app_certificat_show', methods: ['GET'])]
    public function show($idCertificat): Response
    {
        $certificat = $this->getDoctrine()->getRepository(Certificat::class)->find($idCertificat);

        return $this->render('back/certificat/show.html.twig', [
            'certificat' => $certificat,
        ]);
    }

    #[Route('/edit/{idCertificat}', name: 'app_certificat_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, $idCertificat, EntityManagerInterface $entityManager, ActionEtabRepository $actionEtabRepository): Response
    {
        $certificat = $this->getDoctrine()->getRepository(Certificat::class)->find($idCertificat);

        $form = $this->createForm(CertificatType::class, $certificat);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager->flush();
            $text = "Le certificat " . $certificat->getNomCertificat() . " a été modifié le " . date('Y-m-d H:i:s');
            $actionEtabRepository->insert($text);

            return $this->redirectToRoute('app_certificat_index_admin', [], Response::HTTP_SEE_OTHER);
        }

        return $this->renderForm('back/certificat/edit.html.twig', [
            'certificat' => $certificat,
            'form' => $form,
        ]);
    }

    #[Route('/delete/{id}', name: 'app_certificat_delete', methods: ['POST'])]
    public function delete(Request $request, $id, EntityManagerInterface $entityManager, ActionEtabRepository $actionEtabRepository): Response
    {
        $certificat = $this->getDoctrine()->getRepository(Certificat::class)->find($id);

        if ($this->isCsrfTokenValid('delete'.$certificat->getIdCertificat(), $request->request->get('_token'))) {
            $entityManager->remove($certificat);
            $entityManager->flush();
            $text = "Le certificat " . $certificat->getNomCertificat() . " a été supprimé le " . date('Y-m-d H:i:s');
            $actionEtabRepository->insert($text);
        }

        return $this->redirectToRoute('app_certificat_index_admin', [], Response::HTTP_SEE_OTHER);
    }

    #[Route('/detail/{id}', name: 'certificat_detail')]
    public function detailCertificat($id): Response
    {
        // Récupérez le certificat depuis la base de données ou tout autre source de données
        $certificat = $this->getDoctrine()->getRepository(Certificat::class)->find($id);

        // Vérifiez si le certificat existe
        if (!$certificat) {
            throw $this->createNotFoundException('Certificat not found');
        }

        // Passez le certificat au template Twig pour l'affichage
        return $this->render('front/certificat/detail_certificat.html.twig', [
            'certificat' => $certificat,
        ]);
    }

    #[Route('/certificat/{idCertificat}/pdf', name: 'generate_pdff')]
    public function generatePdf($idCertificat): Response
    {
        // Tentative de trouver le certificat par son ID
        $certificat = $this->getDoctrine()->getRepository(Certificat::class)->find($idCertificat);

        // Vérifie si le certificat n'est pas trouvé
        if (!$certificat) {
            // Lance une exception plus descriptive avec la valeur ID réelle
            throw $this->createNotFoundException('Certificat non trouvé pour l\'ID : ' . $idCertificat);
        }

        // Récupère l'établissement associé au certificat
        $etablissement = $certificat->getIdEtablissement();

        // Rendu du PDF avec toutes les données nécessaires
        $html = $this->renderView('front/certificat/generate_pdf.html.twig', [
            'certificat' => $certificat,
            'etablissement' => $etablissement,
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
        $pdfFileName = 'certificat_' . $idCertificat . '.pdf';

        $pdfPath = $this->getParameter('pdfs_directory'). $pdfFileName;
        // Enregistre le PDF généré dans le répertoire des téléchargements
        file_put_contents($pdfPath, $dompdf->output());

        // Renvoie le PDF généré au navigateur (téléchargement en ligne)
        return new BinaryFileResponse($pdfPath);
    }


        /**
     * @Route("/excel/export",  name="export")
     */
    public function export()
    {
        $spreadsheet = new Spreadsheet();

        $sheet = $spreadsheet->getActiveSheet();

        $sheet->setTitle('Certificat List');

        $sheet->getCell('A1')->setValue('Nom');
        $sheet->getCell('B1')->setValue('Domaine');
        $sheet->getCell('C1')->setValue('Niveau');
        $sheet->getCell('D1')->setValue('Etablissement');

        // Increase row cursor after header write
        $sheet->fromArray($this->getData(), null, 'A2', true);
        $writer = new Xlsx($spreadsheet);

        // Save the Excel file to a temporary directory
        $filename = 'Certificats' . date('m-d-Y_his') . '.xlsx';
        $tempFilePath = sys_get_temp_dir() . '/' . $filename;
        $writer->save($tempFilePath);

        // Send the Excel file as a response
        return new BinaryFileResponse($tempFilePath);
    }

    public function getData()
    {
        $list = [];
        $certificats = $this->getDoctrine()->getRepository(Certificat::class)->findAll();

        foreach ($certificats as $certificat) {
            $list[] = [
                $certificat->getNomCertificat(),
                $certificat->getDomaineCertificat(),
                $certificat->getNiveau(),
                $certificat->getIdEtablissement()->getNomEtablissement(),

            ];
        }
        return $list;
    }
}
