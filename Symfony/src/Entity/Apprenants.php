<?php

namespace App\Entity;

use App\Repository\ApprenantsRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: ApprenantsRepository::class)]
class Apprenants
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy:"IDENTITY")]
    #[ORM\Column(type: "integer")]
    private ?int $id = null;

    
    #[Assert\NotBlank(message: "Must be filled")]
    #[Assert\Length(
    min: 5,
    minMessage: "Enter a Name composed of at least 5 characters"
)]
#[Assert\Type(type: 'string', message: 'The student name must be a string.')]
    #[Assert\Regex(
        pattern: '/^[a-zA-Z ]+$/',
        message: 'The student name can only contain letters and spaces.'
    )]
    #[ORM\Column(type: "string", length: 255)]
    #[Groups("post:read")]
    private ?string $name = null;

    #[ORM\Column(name: "email", type: "string", length: 255, nullable: true)]
    #[Assert\Email(message: "The email '{{ value }}' is not a valid email.")]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Groups("post:read")]
    private ?string $email = null;

    #[ORM\Column(length: 255, nullable: true)]
    #[Assert\NotBlank(message: "Must be filled")]
    #[Assert\Length(
    min: 5,
    minMessage: "The status must have at least {{ limit }} characters"
)]
    #[Groups("post:read")]
    private ?string $statutNiveau = null;

   #[ORM\Column(length: 255, nullable: true)]
   #[Assert\NotBlank(message: "Must be filled")]
   #[Groups("post:read")]
   private ?string $formationEtudies = null;

   #[ORM\Column(name: "image", length: 255)]
    #[Groups("post:read")]
private ?string $image = null;



#[ORM\OneToMany(targetEntity: Participation1::class, mappedBy: 'student')]
private Collection $participation1s;

public function __construct()
{
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

    public function getEmail(): ?string
    {
        return $this->email;
    }

    public function setEmail(string $email): static
    {
        $this->email = $email;

        return $this;
    }

    public function getStatutNiveau(): ?string
    {
        return $this->statutNiveau;
    }

    public function setStatutNiveau(string $statutNiveau): static
    {
        $this->statutNiveau = $statutNiveau;

        return $this;
    }

    public function getFormationEtudies(): ?string
    {
        return $this->formationEtudies;
    }

    public function setFormationEtudies(string $formationEtudies): static
    {
        $this->formationEtudies = $formationEtudies;

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
            $participation1->setStudent($this);
        }

        return $this;
    }

    public function removeParticipation1(Participation1 $participation1): static
    {
        if ($this->participation1s->removeElement($participation1)) {
            // set the owning side to null (unless already changed)
            if ($participation1->getStudent() === $this) {
                $participation1->setStudent(null);
            }
        }

        return $this;
    }
    

    
    

       
    }

    

    

    

    

    

    

