<?php

namespace App\Entity;

use DateTime;
use Doctrine\DBAL\Types\Types;
use App\Repository\EventRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;

use Doctrine\Common\Collections\Collection;
use Doctrine\Common\Collections\ArrayCollection;


#[ORM\Entity(repositoryClass: EventRepository::class)]
class Event
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(type: "integer")]
    #[Groups(['event:read'])]
    private ?int $idevent;

    #[ORM\ManyToOne(targetEntity:"Etablissement")]
    #[ORM\JoinColumn(name:"idEstab", referencedColumnName:"ID_Etablissement")]
    #[Assert\NotBlank(message: "L'ID de l'établissement ne doit pas être vide.")]
    #[Groups(['event:read'])]
    private $idestab;

    #[ORM\Column(length :255)]
    #[Assert\NotBlank(message: "Le nom de l'événement ne doit pas être vide.")]
    #[Assert\Length(max: 255, maxMessage: "Le nom de l'événement ne peut pas dépasser {{ limit }} caractères.")]
    #[Groups(['event:read'])]
    private ?string $nameevent;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Assert\NotBlank(message: "La date de l'événement ne doit pas être vide.")]
    #[Assert\GreaterThan("today", message: "La date de l'événement doit être ultérieure à la date actuelle.")]
    #[Assert\Type(type: "\DateTimeInterface", message: "La date de l'événement doit être de type date.")]
    #[Groups(['event:read'])]
    private ?\DateTimeInterface $dateevent;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Le nombre maximum ne doit pas être vide.")]
    #[Assert\GreaterThan(value: 0, message: "Le nombre maximum doit être supérieur à zéro.")]
    #[Groups(['event:read'])]
    private ?int $nbrmax;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Le prix ne doit pas être vide.")]
    #[Assert\GreaterThan(value: 0, message: "Le prix doit être supérieur à zéro.")]
    #[Groups(['event:read'])]
    private ?float $prix;

    #[ORM\Column(length :500)]
    #[Assert\NotBlank(message: "La description ne doit pas être vide.")]
    #[Assert\Length(max: 500, maxMessage: "La description ne peut pas dépasser {{ limit }} caractères.")]
    #[Groups(['event:read'])]
    private ?string $description;

    #[ORM\Column(length :255)]
    //#[Assert\NotBlank(message: "Le chemin de l'image ne doit pas être vide.")]
    //#[Assert\Length(max: 255, maxMessage: "Le chemin de l'image ne peut pas dépasser {{ limit }} caractères.")]
    #[Groups(['event:read'])]
    private ?string $image;

    
    #[ORM\ManyToOne(targetEntity:"Partner")]
    #[ORM\JoinColumn(name:"idPartnerCE", referencedColumnName:"idPartner")]
    #[Groups(['event:read'])]
    private ?Partner $idpartnerce;

    public function getIdevent(): ?int
    {
        return $this->idevent;
    }

       /**
     * @return mixed
     */
    public function getIdestab()
    {
        return $this->idestab;
    }

    /**
     * @param mixed $idestab
     */
    public function setIdestab($idestab): void
    {
        $this->idestab = $idestab;
    }


    public function getNameevent(): ?string
    {
        return $this->nameevent;
    }

    public function setNameevent(string $nameevent): static
    {
        $this->nameevent = $nameevent;

        return $this;
    }

    public function getDateevent(): ?\DateTimeInterface
    {
        return $this->dateevent;
    }

    public function setDateevent(\DateTimeInterface $dateevent): static
    {
        $this->dateevent = $dateevent;

        return $this;
    }

    public function getNbrmax(): ?int
    {
        return $this->nbrmax;
    }

    public function setNbrmax(int $nbrmax): static
    {
        $this->nbrmax = $nbrmax;

        return $this;
    }

    public function getPrix(): ?float
    {
        return $this->prix;
    }

    public function setPrix(float $prix): static
    {
        $this->prix = $prix;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(string $description): static
    {
        $this->description = $description;

        return $this;
    }

    public function getImage(): ?string
    {
        return $this->image;
    }

    public function setImage(string $image): static
    {
        $this->image = $image;

        return $this;
    }

    public function getIdpartnerce(): ?Partner
    {
        return $this->idpartnerce;
    }

    public function setIdpartnerce(?Partner $idpartnerce): static
    {
        $this->idpartnerce = $idpartnerce;

        return $this;
    }


// **************************************************************************************
    #[ORM\OneToMany(targetEntity:"Reservation", mappedBy:"idEvent")]
    private $reservations;

    public function __construct()
    {
        $this->reservations = new ArrayCollection();
    }

   
    public function getReservations(): Collection
    {
        return $this->reservations;
    }
    
    public function getTotalReservedPlaces(): int
{
    $reservations = $this->getReservations(); // Assuming you have a method to get reservations associated with the event
    $totalReservedPlaces = 0;

    foreach ($reservations as $reservation) {
        $totalReservedPlaces += $reservation->getNbPlaces();
    }

    return $totalReservedPlaces;
}

public function isFullyBooked(): bool
{
    return $this->getTotalReservedPlaces() >= $this->getNbrmax();
}

}