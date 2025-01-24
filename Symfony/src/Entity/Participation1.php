<?php

namespace App\Entity;

use App\Repository\Participation1Repository;
use Doctrine\DBAL\Types\Types;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: Participation1Repository::class)]
class Participation1
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column]
    private ?int $id = null;

    #[ORM\Column(type: Types::DATETIME_MUTABLE)]
    #[Assert\GreaterThan("today", message: "The date of obtaining the certificate must be greater than today")]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Groups("post:read")]
    private ?\DateTimeInterface $startdate = null;

    

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Groups("post:read")]
    private ?string $participationstatus = null;

    #[ORM\Column(length: 255)]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Groups("post:read")]
    private ?string $comment = null;

    #[ORM\ManyToOne(inversedBy: 'participation1s')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Apprenants $student = null;

    #[ORM\ManyToOne(inversedBy: 'participation1s')]
    #[ORM\JoinColumn(nullable: false)]
    private ?Niveau $level = null;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getStartdate(): ?\DateTimeInterface
    {
        return $this->startdate;
    }

    public function setStartdate(\DateTimeInterface $startdate): static
    {
        $this->startdate = $startdate;

        return $this;
    }

   

    public function getParticipationstatus(): ?string
    {
        return $this->participationstatus;
    }

    public function setParticipationstatus(string $participationstatus): static
    {
        $this->participationstatus = $participationstatus;

        return $this;
    }

    public function getComment(): ?string
    {
        return $this->comment;
    }

    public function setComment(string $comment): static
    {
        $this->comment = $comment;

        return $this;
    }

    public function getStudent(): ?Apprenants
    {
        return $this->student;
    }

    public function setStudent(?Apprenants $student): static
    {
        $this->student = $student;

        return $this;
    }

    public function getLevel(): ?Niveau
    {
        return $this->level;
    }

    public function setLevel(?Niveau $level): static
    {
        $this->level = $level;

        return $this;
    }
}
