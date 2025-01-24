<?php

namespace App\Entity;

use App\Repository\EtablissementRepository;
use DateTime;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: EtablissementRepository::class)]
class Etablissement
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(name: "ID_Etablissement", type: "integer")]
    #[Groups(['etablissement:read'])]
    private ?int $ID_Etablissement;

    #[ORM\Column(length :255)]
    #[Groups(['etablissement:read'])]
    private ?string $imgEtablissement ;

    #[ORM\Column(length :255)]
    #[Assert\NotBlank(message: 'The establishment name cannot be blank.')]
    #[Assert\Type(type: 'string', message: 'The establishment name must be a string.')]
    #[Assert\Regex(
        pattern: '/^[a-zA-Z ]+$/',
        message: 'The establishment name can only contain letters and spaces.'
    )]
    #[Groups(['etablissement:read'])]
    private ?string $nomEtablissement;

    #[ORM\Column(length :255)]
    #[Groups(['etablissement:read'])]
    private ?string $adresseEtablissement;

    #[ORM\Column(length :255)]
    #[Groups(['etablissement:read'])]
    private ?string $typeEtablissement;

    #[Assert\Regex(
        pattern: '/^\d{8}$/',
        message: 'The telephone number must contain exactly 8 digits.'
    )]
    #[ORM\Column(type: "integer")]
    #[Groups(['etablissement:read'])]
    private ?int $telEtablissement;

    #[ORM\Column(length :255)]
    #[Assert\NotBlank(message: 'The director name cannot be blank.')]
    #[Assert\Type(type: 'string', message: 'The director name must be a string.')]
    #[Assert\Regex(
        pattern: '/^[a-zA-Z ]+$/',
        message: 'The director name can only contain letters and spaces.'
    )]
    #[Groups(['etablissement:read'])]
    private ?string $directeurEtablissement;

    #[ORM\Column(type: "datetime")]
    #[Assert\NotBlank(message: "La date  ne doit pas être vide.")]
    #[Assert\LessThan("today", message: "Date must be less than today.")]
    #[Assert\Type(type: "\DateTimeInterface", message: "La date  doit être de type date.")]
    #[Groups(['etablissement:read'])]
    private ?DateTime $dateFondation;

    #[ORM\OneToMany(mappedBy: "etablissement", targetEntity: UserEtablissement::class)]
    #[Groups(['etablissement:read'])]
    private $userEtablissements;

    #[ORM\Column(type: "integer", name: "views")]
    #[Groups(['etablissement:read'])]
    private ?int $views = 0;

    #[Groups(['etablissement:read'])]
    private $likes = 0;

    #[Groups(['etablissement:read'])]
    private $dislikes = 0;


    /**
     * @ORM\Column(type="boolean", options={"default": false})
     */
    private $favoris = false;

    // Other getters and setters...

    public function isFavoris(): ?bool
    {
        return $this->favoris;
    }

    public function setFavoris(bool $favoris): self
    {
        $this->favoris = $favoris;

        return $this;
    }


    public function getViews(): ?int
    {
        return $this->views;
    }

    public function setViews(int $views): self
    {
        $this->views = $views;

        return $this;
    }

    public function __construct()
    {
        $this->userEtablissements = new ArrayCollection();
    }

    public function getUserEtablissements(): ArrayCollection
    {
        return new ArrayCollection($this->userEtablissements->toArray());
    }

    public function setUserEtablissements(ArrayCollection $userEtablissements): void
    {
        $this->userEtablissements = $userEtablissements;
    }

    public function getLikes(): int
    {
        return $this->likes;
    }

    public function setLikes(int $likes): void
    {
        $this->likes = $likes;
    }

    public function getDislikes(): int
    {
        return $this->dislikes;
    }

    public function setDislikes(int $dislikes): void
    {
        $this->dislikes = $dislikes;
    }

    public function getIdEtablissement(): ?int
    {
        return $this->ID_Etablissement;
    }

    public function getImgEtablissement(): ?string
    {
        return $this->imgEtablissement;
    }

    public function setImgEtablissement(string $imgEtablissement): static
    {
        $this->imgEtablissement = $imgEtablissement;
        return $this;
    }

    public function getNomEtablissement(): ?string
    {
        return $this->nomEtablissement;
    }

    public function setNomEtablissement(string $nomEtablissement): static
    {
        $this->nomEtablissement = $nomEtablissement;
        return $this;
    }

    public function getAdresseEtablissement(): ?string
    {
        return $this->adresseEtablissement;
    }

    public function setAdresseEtablissement(string $adresseEtablissement): static
    {
        $this->adresseEtablissement = $adresseEtablissement;
        return $this;
    }

    public function getTypeEtablissement(): ?string
    {
        return $this->typeEtablissement;
    }

    public function setTypeEtablissement(string $typeEtablissement): static
    {
        $this->typeEtablissement = $typeEtablissement;
        return $this;
    }

    public function getTelEtablissement(): ?int
    {
        return $this->telEtablissement;
    }

    public function setTelEtablissement(int $telEtablissement): static
    {
        $this->telEtablissement = $telEtablissement;
        return $this;
    }

    public function getDirecteurEtablissement(): ?string
    {
        return $this->directeurEtablissement;
    }

    public function setDirecteurEtablissement(string $directeurEtablissement): static
    {
        $this->directeurEtablissement = $directeurEtablissement;
        return $this;
    }

    public function getDateFondation(): ?\DateTimeInterface
    {
        return $this->dateFondation;
    }

    public function setDateFondation(\DateTimeInterface $dateFondation): static
    {
        $this->dateFondation = $dateFondation;
        return $this;
    }
}
