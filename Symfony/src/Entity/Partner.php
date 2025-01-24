<?php

namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\PartnerRepository;
use Symfony\Component\Serializer\Annotation\Groups;


#[ORM\Entity(repositoryClass: PartnerRepository::class)]
class Partner
{
    
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(name: "idPartner", type: "integer")]
    #[Groups(['event:read'])]
    private ?int $idpartner;

    #[ORM\Column(length :255)]
    #[Groups(['event:read'])]
    private ?string $namepartner;

    #[ORM\Column(length :255)]
    #[Groups(['event:read'])]
    private ?string $typepartner;

    #[ORM\Column(length :255)]
    #[Groups(['event:read'])]
    private ?string $email;

    #[ORM\Column]
    #[Groups(['event:read'])]
    private ?int $tel;

    #[ORM\Column(length :255)]
    #[Groups(['event:read'])]
    private ?string $description;

    #[ORM\Column(length :255)]
    #[Groups(['event:read'])]
    private ?string $image;

    public function getIdpartner(): ?int
    {
        return $this->idpartner;
    }

    public function getNamepartner(): ?string
    {
        return $this->namepartner;
    }

    public function setNamepartner(string $namepartner): static
    {
        $this->namepartner = $namepartner;

        return $this;
    }

    public function getTypepartner(): ?string
    {
        return $this->typepartner;
    }

    public function setTypepartner(string $typepartner): static
    {
        $this->typepartner = $typepartner;

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

    public function getTel(): ?int
    {
        return $this->tel;
    }

    public function setTel(int $tel): static
    {
        $this->tel = $tel;

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


}
