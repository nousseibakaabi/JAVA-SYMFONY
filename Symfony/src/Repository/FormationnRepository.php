<?php

namespace App\Repository;

use App\Entity\Formationn;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @extends ServiceEntityRepository<Formationn>
 *
 * @method Formationn|null find($id, $lockMode = null, $lockVersion = null)
 * @method Formationn|null findOneBy(array $criteria, array $orderBy = null)
 * @method Formationn[]    findAll()
 * @method Formationn[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class FormationnRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Formationn::class);
    }

    /**
     * Search for formations based on the search value and order by the specified criteria.
     *
     * @param string|null $searchValue
     * @param string|null $orderBy
     * @return Formationn[]
     */
    public function searchAndOrder( $searchValue,  $orderBy): array
    {
        $em = $this->getEntityManager();
        if ($orderBy == 'DESC') {
            $query = $em->createQuery(
                'SELECT f FROM App\Entity\Formationn f   
            WHERE f.categorie LIKE :suj
            OR f.titre LIKE :suj
            OR f.description LIKE :suj
            OR f.lien LIKE :suj
            ORDER BY f.id_formation DESC'
            );
        } else {
            $query = $em->createQuery(
                'SELECT f FROM App\Entity\Formationn f   
            WHERE f.categorie LIKE :suj
            OR f.titre LIKE :suj
            OR f.description LIKE :suj
            OR f.lien LIKE :suj
            ORDER BY f.id_formation ASC'
            );
        }
        $query->setParameter('suj', '%' . $searchValue . '%');

        return $query->getResult();
    }


    /* public function getTuteurFullNameByFormationId(int $id)
     {
         return $this->createQueryBuilder('f')
             ->join('f.tuteur', 't')
             ->select('CONCAT(t.nom, \' \', t.prenom) AS fullName')
             ->where('f.id = :id')
             ->setParameter('id', $id)
             ->getQuery()
             ->getSingleScalarResult();
     }*/
    
    

//    /**
//     * @return Formationn[] Returns an array of Formationn objects
//     */
//    public function findByExampleField($value): array
//    {
//        return $this->createQueryBuilder('f')
//            ->andWhere('f.exampleField = :val')
//            ->setParameter('val', $value)
//            ->orderBy('f.id', 'ASC')
//            ->setMaxResults(10)
//            ->getQuery()
//            ->getResult()
//        ;
//    }

//    public function findOneBySomeField($value): ?Formationn
//    {
//        return $this->createQueryBuilder('f')
//            ->andWhere('f.exampleField = :val')
//            ->setParameter('val', $value)
//            ->getQuery()
//            ->getOneOrNullResult()
//        ;
//    }

public function findAll(): array
{
    return $this->createQueryBuilder('f')
        ->orderBy('f.categorie', 'ASC')
        ->getQuery()
        ->getResult();
}


}
