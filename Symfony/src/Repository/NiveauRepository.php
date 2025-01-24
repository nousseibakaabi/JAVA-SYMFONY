<?php

namespace App\Repository;

use App\Entity\Niveau;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Niveau>
 *
 * @method Niveau|null find($id, $lockMode = null, $lockVersion = null)
 * @method Niveau|null findOneBy(array $criteria, array $orderBy = null)
 * @method Niveau[]    findAll()
 * @method Niveau[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class NiveauRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Niveau::class);
    }

//    /**
//     * @return Niveau[] Returns an array of Niveau objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('n')
//            ->andWhere('n.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('n.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Niveau
//    {
//        return $this->createQueryBuilder('n')
//            ->andWhere('n.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
public function statistiquesNiveau(): array
    {
        // Créez une requête avec le QueryBuilder
        $qb = $this->createQueryBuilder('n')
            ->select('COUNT(n.prerequis) AS countPrerequis, n.prerequis AS prerequis')
            ->groupBy('prerequis');

        // Exécutez la requête et retournez les résultats
        return $qb->getQuery()->getResult();
    }

//    private $lastNotifiedIds = [];

public function notifyNewNiveau()
{
    $em = $this->getEntityManager();
    
    // Récupérer les ID des niveaux actuels
    $currentIds = $em->createQueryBuilder()
        ->select('n.id')
        ->from('App\Entity\Niveau', 'n')
        ->getQuery()
        ->getResult();

    // Identifier les nouveaux niveaux en comparant avec les ID déjà notifiés
    $newNiveauIds = array_diff($currentIds, $this->lastNotifiedIds);

    // Mettre à jour les ID déjà notifiés
    $this->lastNotifiedIds = $currentIds;

    // Récupérer les entités Niveau correspondantes aux nouveaux ID
    $newNiveaux = $em->createQueryBuilder()
        ->select('n')
        ->from('App\Entity\Niveau', 'n')
        ->where('n.id IN (:ids)')
        ->setParameter('ids', $newNiveauIds)
        ->getQuery()
        ->getResult();

    return $newNiveaux;
}
public function Order()
    {
        $em = $this->getEntityManager();
        $query = $em->createQuery('select m from App\Entity\Niveau m order by m.nbformation ASC');
        return $query->getResult();
    }

public function findByName($num){
        return $this->createQueryBuilder('niveau')
            ->where('niveau.name LIKE :name')
            ->setParameter('name', '%'.$num.'%')
            ->getQuery()
            ->getResult();
    }
    public function findBySearchQuery(string $query): array
    {
        return $this->createQueryBuilder('a')
            ->andWhere('a.name LIKE :query')
            ->setParameter('query', $query . '%')
            ->getQuery()
            ->getResult();
    }

}
