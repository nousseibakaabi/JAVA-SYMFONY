<?php
namespace App\Entity;

use Doctrine\ORM\Mapping as ORM;
use App\Repository\RemiseRepository;
use Doctrine\DBAL\Types\Types;


#[ORM\Table(name: "remiseentry")]
#[ORM\Entity(repositoryClass: "App\Repository\RemiseRepository")]
class Remiseentry
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(name: "id", type: "integer", nullable: false)]
    private $id;

    #[ORM\Column(name: "code", type: "string", length: 10, nullable: false)]
    private $code;

    #[ORM\Column(name: "pourcentage", type: "float", nullable: false)]
    private $pourcentage;

    public function getId(): ?int
    {
        return $this->id;
    }

    public function getCode(): ?string
    {
        return $this->code;
    }

    public function setCode(string $code): static
    {
        $this->code = $code;

        return $this;
    }

    public function getPourcentage(): ?float
    {
        return $this->pourcentage;
    }

    public function setPourcentage(float $pourcentage): static
    {
        $this->pourcentage = $pourcentage;

        return $this;
    }
}
