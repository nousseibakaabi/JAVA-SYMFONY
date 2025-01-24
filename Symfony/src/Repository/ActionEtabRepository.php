<?php

namespace App\Repository;

use App\Entity\ActionEtab;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<ActionEtab>
 *
 * @method ActionEtab|null find($id, $lockMode = null, $lockVersion = null)
 * @method ActionEtab|null findOneBy(array $criteria, array $orderBy = null)
 * @method ActionEtab[]    findAll()
 * @method ActionEtab[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class ActionEtabRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, ActionEtab::class);
    }

//    /**
//     * @return ActionEtab[] Returns an array of ActionEtab objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('a')
//            ->andWhere('a.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('a.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?ActionEtab
//    {
//        return $this->createQueryBuilder('a')
//            ->andWhere('a.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }

    /**
     * Insert a new ActionEtab entity into the database.
     *
     * @param string $text The text to set for the new entity.
     * @return ActionEtab The newly created entity.
     */
    public function insert(string $text): ActionEtab
    {
        $actionEtab = new ActionEtab();
        $actionEtab->setText($text);

        $this->_em->persist($actionEtab);
        $this->_em->flush();

        return $actionEtab;
    }

}
