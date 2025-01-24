<?php

namespace App\Entity;

use App\Repository\TuteurRepository;
use DateTimeInterface;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: TuteurRepository::class)]
class Tuteur
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id_tuteur = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Name is required")]
    #[Assert\Length(min: 3, minMessage: "Name must be at least {{ limit }} characters")]
    private ?string $nom = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "First name is required")]
    #[Assert\Length(min: 4, minMessage: "First name must be at least {{ limit }} characters")]
    private ?string $prenom = null;

    #[ORM\Column(type: "datetime")]
    #[Assert\LessThan("today", message: "The date of birth must be in the past.")]
    private ?DateTimeInterface $date_naisc;

    #[ORM\Column]
    #[Assert\NotBlank(message: "Phone number is required")]
    #[Assert\Regex(pattern: "/^\d{8}$/", message: "Phone number must be exactly 8 digits")]
    private ?int $tlf = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Profession is required")]
    #[Assert\Length(min: 3, minMessage: "Profession must be at least {{ limit }} characters")]
    private ?string $profession = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Email is required")]
    #[Assert\Email(message: "Please enter a valid email address")]
    private ?string $email = null;

    #[ORM\Column(nullable: false, length: 255)]
    private ?string $image;




    public function getId_tuteur(): ?int
    {
        return $this->id_tuteur;
    }

    public function getNom(): ?string
    {
        return $this->nom;
    }

    public function setNom(string $nom): static
    {
        $this->nom = $nom;

        return $this;
    }

    public function getPrenom(): ?string
    {
        return $this->prenom;
    }

    public function setPrenom(string $prenom): static
    {
        $this->prenom = $prenom;

        return $this;
    }

    public function getDateNaisc(): ?\DateTimeInterface
    {
        return $this->date_naisc;
    }

    public function setDateNaisc(\DateTimeInterface $date_naisc): static
    {
        $this->date_naisc = $date_naisc;

        return $this;
    }

    public function getTlf(): ?int
    {
        return $this->tlf;
    }

    public function setTlf(int $tlf): static
    {
        $this->tlf = $tlf;

        return $this;
    }

    public function getProfession(): ?string
    {
        return $this->profession;
    }

    public function setProfession(string $profession): static
    {
        $this->profession = $profession;

        return $this;
    }

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;

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
}
