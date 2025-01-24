<?php

namespace App\Repository;

use App\Entity\Participation1;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Participation1>
 *
 * @method Participation1|null find($id, $lockMode = null, $lockVersion = null)
 * @method Participation1|null findOneBy(array $criteria, array $orderBy = null)
 * @method Participation1[]    findAll()
 * @method Participation1[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class Participation1Repository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Participation1::class);
    }

//    /**
//     * @return Participation1[] Returns an array of Participation1 objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('p.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Participation1
//    {
//        return $this->createQueryBuilder('p')
//            ->andWhere('p.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }

}
