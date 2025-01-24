<?php

namespace App\Entity;

use App\Repository\QuizRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use DateTimeInterface;

#[ORM\Entity(repositoryClass: QuizRepository::class)]
class Quiz
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    private ?int $id;

    #[ORM\ManyToOne(targetEntity: Formationn::class)]
    #[ORM\JoinColumn(name:"id_formation", referencedColumnName:"id_formation")]
    private ?Formationn $formation;

    #[ORM\Column(length: 255)]
    private ?string $question1;

    #[ORM\Column(length: 255)]
    private ?string $answer1;

    #[ORM\Column(length: 255)]
    private ?string $question2;

    #[ORM\Column(length: 255)]
    private ?string $answer2;

    #[ORM\Column(length: 255)]
    private ?string $question3;

    #[ORM\Column(length: 255)]
    private ?string $answer3;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getFormation(): ?Formationn
    {
        return $this->formation;
    }

    public function setFormation(?Formationn $formation): self
    {
        $this->formation = $formation;
        return $this;
    }

    public function getQuestion1(): ?string
    {
        return $this->question1;
    }

    public function setQuestion1(string $question1): self
    {
        $this->question1 = $question1;
        return $this;
    }

    public function getAnswer1(): ?string
    {
        return $this->answer1;
    }

    public function setAnswer1(string $answer1): self
    {
        $this->answer1 = $answer1;
        return $this;
    }

    public function getQuestion2(): ?string
    {
        return $this->question2;
    }

    public function setQuestion2(string $question2): self
    {
        $this->question2 = $question2;
        return $this;
    }

    public function getAnswer2(): ?string
    {
        return $this->answer2;
    }

    public function setAnswer2(string $answer2): self
    {
        $this->answer2 = $answer2;
        return $this;
    }

    public function getQuestion3(): ?string
    {
        return $this->question3;
    }

    public function setQuestion3(string $question3): self
    {
        $this->question3 = $question3;
        return $this;
    }

    public function getAnswer3(): ?string
    {
        return $this->answer3;
    }

    public function setAnswer3(string $answer3): self
    {
        $this->answer3 = $answer3;
        return $this;
    }
}
