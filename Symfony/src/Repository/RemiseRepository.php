<?php

namespace App\Repository;

use App\Entity\Remiseentry;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use Doctrine\ORM\EntityManagerInterface;
use Doctrine\ORM\Query\Expr\Func;

/**
 * @extends ServiceEntityRepository<Remiseentry>
 *
 * @method Remiseentry|null find($id, $lockMode = null, $lockVersion = null)
 * @method Remiseentry|null findOneBy(array $criteria, array $orderBy = null)
 * @method Remiseentry[]    findAll()
 * @method Remiseentry[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class RemiseRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Remiseentry::class);
    }


    public function getRandomRemise(): ?Remiseentry
    {
        $entityManager = $this->getEntityManager();
        $repository = $entityManager->getRepository(Remiseentry::class);
    
        // Get all IDs from the database
        $allIds = $repository->createQueryBuilder('r')
                            ->select('r.id')
                            ->getQuery()
                            ->getResult();
    
        // Extract IDs from the result
        $ids = array_column($allIds, 'id');
    
        // Check if there are IDs in the array
        if (empty($ids)) {
            return null; // or throw an exception
        }
    
        // Select a random ID from the array
        $randomId = $ids[array_rand($ids)];
    
        // Retrieve the entity by its ID
        $randomRemise = $repository->find($randomId);
    
        return $randomRemise;
    }
        

    

    

//    /**
//     * @return Remiseentry[] Returns an array of Remiseentry objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('r')
//            ->andWhere('r.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('r.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Remiseentry
//    {
//        return $this->createQueryBuilder('r')
//            ->andWhere('r.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }
}
