<?php

namespace App\Controller;

use App\Repository\UserRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use App\Repository\EtablissementRepository;
use App\Entity\Reservation;
use App\Form\ReservationType;
use App\Entity\Paiement;
use App\Entity\Certificat;
use App\Entity\Etablissement;
use App\Repository\ActionEtabRepository;
use App\Repository\UserEtablissementRepository;
use CMEN\GoogleChartsBundle\GoogleCharts\Charts\PieChart;
use App\Form\PaiementType;
use App\Repository\PaiementRepository;
use App\Repository\ReservationRepository;

class Home extends AbstractController
{
    #[Route('/', name: 'app_index')]
    public function index(): Response
    {
        return $this->render('front/home.html.twig');
    }


    
    #[Route('/admin', name: 'app_index_admin')]
    public function indexAdmin(UserRepository $userRepository, ReservationRepository $reservationRepository, EtablissementRepository $etablissementRepository): Response
    {
        // Fetch the number of reservations for each event
        $eventReservations = $reservationRepository->getEventReservationsCount();
    
        dump($eventReservations); // Debug statement
    
        // Format data for the chart
        $chartData = [
            'labels' => [],
            'datasets' => [
                [
                    'data' => [],
                    'backgroundColor' => ['#FF6384', '#36A2EB', '#FFCE56'], // Add more colors as needed
                ],
            ],
        ];
    
        // Populate the data arrays
        foreach ($eventReservations as $eventReservation) {
            $chartData['labels'][] = $eventReservation['eventName'];
            $chartData['datasets'][0]['data'][] = $eventReservation['totalPlaces'];
        }
    
        dump($chartData); // Debug statement
    
        // Other data for your admin dashboard
        $totalReservations = $this->getDoctrine()->getRepository(Reservation::class)->getTotalReservations();
        $totalAmount = $this->getDoctrine()->getRepository(Paiement::class)->getTotalAmount();
        $usersConnected = $userRepository->findUsersOrderedByDate();
    
        // Retrieve all establishments
        $etablissements = $etablissementRepository->findAll();
    
        // Initialize data array for pie chart
        $data = array();
        $stat = ['Les Types', '%'];
        array_push($data, $stat);
    
        // Calculate certificate statistics for each establishment
        foreach ($etablissements as $tmp) {
            $stat = array();
            $cmp = $this->getDoctrine()->getManager()->getRepository(Certificat::class)->findBy([
                'idEtablissement' => $tmp
            ]);
            $total = count($cmp);
            $stat = [$tmp->getNomEtablissement(), $total];
            array_push($data, $stat);
        }
    
        // Create and configure the pie chart
        $pieChart = new PieChart();
        $pieChart->getData()->setArrayToDataTable($data);
        $pieChart->getOptions()->setTitle('Certificates Per School');
        $pieChart->getOptions()->setHeight(500);
        $pieChart->getOptions()->setWidth(900);
        $pieChart->getOptions()->getTitleTextStyle()->setBold(true);
        $pieChart->getOptions()->getTitleTextStyle()->setColor('#009900');
        $pieChart->getOptions()->getTitleTextStyle()->setItalic(true);
        $pieChart->getOptions()->getTitleTextStyle()->setFontName('Arial');
        $pieChart->getOptions()->getTitleTextStyle()->setFontSize(20);
    
        // Render the admin dashboard view with necessary data
        return $this->render('back/home.html.twig', [
            'totalReservations' => $totalReservations,
            'totalAmount' => $totalAmount,
            'usersConnected' => $usersConnected,
            'chartData' => $chartData,
            'etablissements' => $etablissements,
            'piechart' => $pieChart,
        ]);
    }
    
    

}
