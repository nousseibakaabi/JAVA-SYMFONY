<?php

namespace App\Entity;

use App\Repository\UserEtablissementRepository;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Component\Serializer\Annotation\Groups;

#[ORM\Entity(repositoryClass: UserEtablissementRepository::class)]
class UserEtablissement
{
    #[ORM\Id]
    #[ORM\GeneratedValue(strategy: "AUTO")]
    #[ORM\Column(name: "id", type: "integer")]
    #[Groups(['etablissement', 'posts:read'])]
    private $id;

    #[ORM\ManyToOne(targetEntity: User::class)]
    #[Groups(['etablissement', 'posts:read'])]
    private $user;

    #[ORM\ManyToOne(targetEntity: Etablissement::class)]
    #[ORM\JoinColumn(name:"ID_Etablissement", referencedColumnName:"ID_Etablissement")]
    #[Groups(['etablissement', 'posts:read'])]
    private $etablissement;

    #[ORM\Column(name: "liked", type: "boolean")]
    #[Groups(['etablissement', 'posts:read'])]
    private $liked;

    #[ORM\Column(name: "disliked", type: "boolean")]
    #[Groups(['etablissement', 'posts:read'])]
    private $disliked;

    #[ORM\Column(name: "views", type: "integer")]
    #[Groups(['etablissement', 'posts:read'])]
    private $views;

    #[ORM\Column(name: "favoris", type: "boolean")]
    private $favoris = false;

    // Autres getters et setters...

    public function isFavoris(): ?bool
    {
        return $this->favoris;
    }

    public function setFavoris(bool $favoris): self
    {
        $this->favoris = $favoris;
        return $this;
    }

    /**
     * @return mixed
     */
    public function getId()
    {
        return $this->id;
    }

    /**
     * @param mixed $id
     */
    public function setId($id): void
    {
        $this->id = $id;
    }

    /**
     * @return mixed
     */
    public function getUser()
    {
        return $this->user;
    }

    /**
     * @param mixed $user
     */
    public function setUser($user): void
    {
        $this->user = $user;
    }

    /**
     * @return mixed
     */
    public function getEtablissement()
    {
        return $this->etablissement;
    }

    /**
     * @param mixed $etablissement
     */
    public function setEtablissement($etablissement): void
    {
        $this->etablissement = $etablissement;
    }

    /**
     * @return mixed
     */
    public function getLiked()
    {
        return $this->liked;
    }

    /**
     * @param mixed $liked
     */
    public function setLiked($liked): void
    {
        $this->liked = $liked;
    }

    /**
     * @return mixed
     */
    public function getDisliked()
    {
        return $this->disliked;
    }

    /**
     * @param mixed $disliked
     */
    public function setDisliked($disliked): void
    {
        $this->disliked = $disliked;
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
}
