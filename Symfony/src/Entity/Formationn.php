<?php

namespace App\Entity;

use App\Repository\FormationnRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Doctrine\DBAL\Types\Types;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: FormationnRepository::class)]
class Formationn
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(name: "id_formation", type: "integer")]
    #[Groups("post:read")]
    private ?int $id_formation;

    #[ORM\ManyToOne(targetEntity: Niveau::class)]
    #[ORM\JoinColumn(name: "id_niveau", referencedColumnName: "id")]
    #[Groups("post:read")]
    private ?Niveau $niveau = null;

    #[ORM\Column(length: 255)]
    #[Groups("post:read")]
    private ?string $categorie = null;

    #[ORM\Column(length: 255)]
    #[Groups("post:read")]
    private ?string $titre = null;

    #[ORM\Column(length: 500)]
    #[Groups("post:read")]
    private ?string $description = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Groups("post:read")]
    private ?\DateTimeInterface $date_d = null;

    #[ORM\Column(type: Types::DATE_MUTABLE)]
    #[Groups("post:read")]
    private ?\DateTimeInterface $date_f = null;

    #[ORM\Column(length: 500)]
    #[Groups("post:read")]
    private ?string $lien = null;

    #[ORM\Column(length: 255)]
    #[Groups("post:read")]
    private ?string $pdfFilename = null;

    #[ORM\OneToMany(targetEntity: "App\Entity\Quiz", mappedBy: "formation")]
    #[Groups("post:read")]
    private $quizzes;

    public function __construct()
    {
        $this->quizzes = new ArrayCollection();
    }

    /**
     * @return Collection|Quiz[]
     */
    public function getQuizzes(): Collection
    {
        return $this->quizzes;
    }

    public function addQuiz(Quiz $quiz): self
    {
        if (!$this->quizzes->contains($quiz)) {
            $this->quizzes[] = $quiz;
            $quiz->setFormation($this);
        }

        return $this;
    }

    public function removeQuiz(Quiz $quiz): self
    {
        if ($this->quizzes->contains($quiz)) {
            $this->quizzes->removeElement($quiz);
            // set the owning side to null (unless already changed)
            if ($quiz->getFormation() === $this) {
                $quiz->setFormation(null);
            }
        }

        return $this;
    }

    public function getPdfFilename(): ?string
    {
        return $this->pdfFilename;
    }

    public function setPdfFilename(?string $pdfFilename): self
    {
        $this->pdfFilename = $pdfFilename;

        return $this;
    }


    public function getIdformation(): ?int
    {
        return $this->id_formation;
    }

    public function getNiveau(): ?Niveau
    {
        return $this->niveau;
    }

    public function setNiveau(?Niveau $niveau): self
    {
        $this->niveau = $niveau;

        return $this;
    }

    public function getCategorie(): ?string
    {
        return $this->categorie;
    }

    public function setCategorie(string $categorie): static
    {
        $this->categorie = $categorie;

        return $this;
    }

    public function getTitre(): ?string
    {
        return $this->titre;
    }

    public function setTitre(string $titre): static
    {
        $this->titre = $titre;

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

    public function getDateD(): ?\DateTimeInterface
    {
        return $this->date_d;
    }

    public function setDateD(\DateTimeInterface $date_d): static
    {
        $this->date_d = $date_d;

        return $this;
    }

    public function getDateF(): ?\DateTimeInterface
    {
        return $this->date_f;
    }

    public function setDateF(\DateTimeInterface $date_f): static
    {
        $this->date_f = $date_f;

        return $this;
    }

    public function getLien(): ?string
    {
        return $this->lien;
    }

    public function setLien(string $lien): static
    {
        $this->lien = $lien;

        return $this;
    }

}
