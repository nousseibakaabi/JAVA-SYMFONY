<?php

namespace App\Entity;

use App\Repository\NiveauRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: NiveauRepository::class)]
class Niveau
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(type: "integer")]
    private $id;

    #[Assert\NotBlank(message: "Must be filled")]
    #[Assert\Length(
        min: 5,
        minMessage: "Enter a Name composed of at least 5 characters"
    )]
    #[Assert\Type(type: 'string', message: 'The level name must be a string.')]
    #[Assert\Regex(
        pattern: '/^[a-zA-Z ]+$/',
        message: 'The level name can only contain letters and spaces.'
    )]
    #[ORM\Column(type: "string", length: 255)]
    #[Groups("post:read")]
    private $name;

    #[Assert\NotBlank(message: "Must be filled")]
    #[ORM\Column(name: "prerequis", length: 255)]
    #[Groups("post:read")]
    private $prerequis;

    #[Assert\NotBlank(message: "Must be filled")]
    #[ORM\Column(name: "duree", type: "string")]
    #[Groups("post:read")]
    private $duree;

    #[ORM\Column(name: "nbformation", type: "integer")]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Assert\Positive(message: "The training number must be a positive number")]
    #[Assert\GreaterThan(value: 0, message: "The number of training must be greater than zero")]
    #[Groups("post:read")]
    private $nbformation;

    #[ORM\ManyToOne(targetEntity: Certificat::class)]
    #[ORM\JoinColumn(name: "id_certificat", referencedColumnName: "id_certificat", nullable: false)]
    private ?Certificat $certificat;

    #[ORM\Column(name: "description", length: 255)]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Groups("post:read")]
    private $description;

    #[ORM\Column(name: "image", length: 255)]
    #[Groups("post:read")]
    private ?string $image = null;

    #[ORM\OneToMany(targetEntity: Apprenants::class, mappedBy: 'niveau')]
    private $apprenants;

    #[ORM\OneToMany(targetEntity: Participation1::class, mappedBy: 'level')]
    private $participation1s;

    public function __construct()
    {
        $this->apprenants = new ArrayCollection();
        $this->participation1s = new ArrayCollection();
    }


    public function getId(): ?int
    {
        return $this->id;
    }

    public function getName(): ?string
    {
        return $this->name;
    }

    public function setName(string $name): static
    {
        $this->name = $name;

        return $this;
    }

    public function getPrerequis(): ?string
    {
        return $this->prerequis;
    }

    public function setPrerequis(string $prerequis): static
    {
        $this->prerequis = $prerequis;

        return $this;
    }

    public function getDuree(): ?string
    {
        return $this->duree;
    }

    public function setDuree(string $duree): static
    {
        $this->duree = $duree;

        return $this;
    }

    public function getNbformation(): ?int
    {
        return $this->nbformation;
    }

    public function setNbformation(int $nbformation): static
    {
        $this->nbformation = $nbformation;

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

    /**
     * @return string|null
     */
    public function getImage(): ?string
    {
        return $this->image;
    }

    /**
     * @param string|null $image
     */
    public function setImage(?string $image): void
    {
        $this->image = $image;
    }


    /**
     * @return Collection<int, Participation1>
     */
    public function getParticipation1s(): Collection
    {
        return $this->participation1s;
    }


    public function addParticipation1(Participation1 $participation1): static
    {
        if (!$this->participation1s->contains($participation1)) {
            $this->participation1s->add($participation1);
            $participation1->setLevel($this);
        }

        return $this;
    }

    public function removeParticipation1(Participation1 $participation1): static
    {
        if ($this->participation1s->removeElement($participation1)) {
            // set the owning side to null (unless already changed)
            if ($participation1->getLevel() === $this) {
                $participation1->setLevel(null);
            }
        }

        return $this;
    }
    public function getCertificat(): ?Certificat
    {
        return $this->certificat;
    }

    public function setCertificat(?Certificat $certificat): self
    {
        $this->certificat = $certificat;

        return $this;
    }

}
