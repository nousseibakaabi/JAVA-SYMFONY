<?php 
namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\ReservationRepository;
use Doctrine\DBAL\Types\Types;
use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;

#[ORM\Entity(repositoryClass: "App\Repository\ReservationRepository")]
class Reservation
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(name: "id", type: "integer", nullable: false)]
    private $id;

    #[ORM\Column(name: "name", type: "string", length: 100, nullable: true)]
    private $name;

    #[ORM\Column(name: "email", type: "string", length: 100, nullable: true)]
    private $email;

    #[ORM\Column(name: "nb_places", type: "integer", nullable: false)]
    private $nbPlaces;

    #[ORM\Column(name: "imageSrc", type: "string", length: 100, nullable: true)]
    private $imagesrc;

    #[ORM\Column(name: "nameE", type: "string", length: 100, nullable: true)]
    private $namee;

    #[ORM\Column(name: "eventPrice", type: "float", nullable: true)]
    private $eventprice;

    #[ORM\ManyToOne(targetEntity: "User")]
    #[ORM\JoinColumn(name: "id_user", referencedColumnName: "id")]
    private $idUser;

    #[ORM\ManyToOne(targetEntity: "Event")]
    #[ORM\JoinColumn(name: "id_event", referencedColumnName: "idevent")]
    private $idEvent;

    private ?Paiement $paiement =null;

   
    public function __construct()
    {
        $this->paiements = new ArrayCollection();
    }


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(?string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(?string $email): static
    {
        $this->email = $email;

        return $this;
    }

    public function getNbPlaces(): ?int
    {
        return $this->nbPlaces;
    }

    public function setNbPlaces(int $nbPlaces): static
    {
        $this->nbPlaces = $nbPlaces;

        return $this;
    }

    public function getImagesrc(): ?string
    {
        return $this->imagesrc;
    }

    public function setImagesrc(?string $imagesrc): static
    {
        $this->imagesrc = $imagesrc;

        return $this;
    }

    public function getNamee(): ?string
    {
        return $this->namee;
    }

    public function setNamee(?string $namee): static
    {
        $this->namee = $namee;

        return $this;
    }

    public function getEventprice(): ?float
    {
        return $this->eventprice;
    }

    public function setEventprice(?float $eventprice): static
    {
        $this->eventprice = $eventprice;

        return $this;
    }

    public function getIdEvent(): ?Event
    {
        return $this->idEvent;
    }

    public function setIdEvent(?Event $idEvent): static
    {
        $this->idEvent = $idEvent;

        return $this;
    }

    public function getIdUser(): ?User
    {
        return $this->idUser;
    }

    public function setIdUser(?User $idUser): static
    {
        $this->idUser = $idUser;

        return $this;
    }

   
    public function getPaiement(): ?Paiement
    {
        return $this->paiement;
    }

    public function setPaiement(?Paiement $paiement): void
    {
        $this->paiement = $paiement;
    }
   

    public function getPaiements(): Collection
    {
        return $this->paiements;
    }
  
}
