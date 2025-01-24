<?php

namespace App\Repository;

use App\Entity\UserEtablissement;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<UserEtablissement>
 *
 * @method UserEtablissement|null find($id, $lockMode = null, $lockVersion = null)
 * @method UserEtablissement|null findOneBy(array $criteria, array $orderBy = null)
 * @method UserEtablissement[]    findAll()
 * @method UserEtablissement[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class UserEtablissementRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, UserEtablissement::class);
    }

//    /**
//     * @return UserEtablissement[] Returns an array of UserEtablissement objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('u')
//            ->andWhere('u.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('u.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?UserEtablissement
//    {
//        return $this->createQueryBuilder('u')
//            ->andWhere('u.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
