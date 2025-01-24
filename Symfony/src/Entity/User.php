<?php

namespace App\Entity;

use App\Repository\UserRepository;
use Doctrine\Common\Collections\ArrayCollection;
use Doctrine\Common\Collections\Collection;
use Doctrine\ORM\Mapping as ORM;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;
use Symfony\Component\Security\Core\User\PasswordAuthenticatedUserInterface;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Serializer\Annotation\Groups;
use Symfony\Component\Validator\Constraints as Assert;

#[ORM\Entity(repositoryClass: UserRepository::class)]
#[ORM\Table(name: '`user`')]
class User implements UserInterface, PasswordAuthenticatedUserInterface
{
    #[ORM\Id]
    #[ORM\GeneratedValue]
    #[ORM\Column]
    #[Groups(['user:read'])]
    private ?int $id ;

    #[ORM\Column(length: 255)]
    #[Groups(['user:read'])]
    private ?string $name = null;

    #[ORM\Column(length: 180, unique: true)]
    #[Groups(['user:read'])]
    private ?string $email = null;

    #[ORM\Column(length: 255)]
    #[Groups(['user:read'])]
    private ?string $address = null ;

    #[ORM\Column]
    #[Groups(['user:read'])]
    private array $roles = [];

    #[ORM\Column(length: 255)]
    #[Groups(['user:read'])]
    private ?string $image = null;

    #[ORM\Column(type: 'datetime')]
    #[Groups(['user:read'])]
    private ?\DateTimeInterface $lastlogin = null;

    #[ORM\Column(type: 'datetime')]
    #[Groups(['user:read'])]
    private ?\DateTimeInterface $createdAt = null;

    #[ORM\Column]
    #[Groups(['user:read'])]
    private ?string $password ;

    #[ORM\Column(length: 255)]
    #[Groups(['user:read'])]
    private ?string $question ;

    #[ORM\Column(length: 255)]
    #[Groups(['user:read'])]
    private ?string $answer ;

    #[ORM\Column(length: 255)]
    #[Groups(['user:read'])]
    private ?string $status = 'active'; // Valeur par défaut "active"

    #[ORM\Column(name: "reset_code", nullable: true)]
    #[Groups(['user:read'])]
    private ?string $resetCode = null;

    #[ORM\OneToMany(mappedBy: "user", targetEntity: UserEtablissement::class)]
    private $userEtablissements;


    public function __construct()
    {
        $this->question = '';
        $this->answer = '';
        $this->userEtablissements = new ArrayCollection();
        $this->createdAt = new \DateTime();
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
     * @return \DateTimeInterface|null
     */
    public function getLastlogin(): ?\DateTimeInterface
    {
        return $this->lastlogin;
    }

    /**
     * @param \DateTimeInterface|null $lastlogin
     */
    public function setLastlogin(?\DateTimeInterface $lastlogin): void
    {
        $this->lastlogin = $lastlogin;
    }

    /**
     * @return \DateTimeInterface|null
     */
    public function getCreatedAt(): ?\DateTimeInterface
    {
        return $this->createdAt;
    }

    /**
     * @param \DateTimeInterface|null $createdAt
     */
    public function setCreatedAt(?\DateTimeInterface $createdAt): void
    {
        $this->createdAt = $createdAt;
    }

    /**
     */
    private ?string $confirmPassword = null;

    /**
     * @return string|null
     */
    public function getConfirmPassword(): ?string
    {
        return $this->confirmPassword;
    }

    /**
     * @param string|null $confirmPassword
     */
    public function setConfirmPassword(?string $confirmPassword): void
    {
        $this->confirmPassword = $confirmPassword;
    }

    /**
     * @return string|null
     */
    public function getResetCode(): ?string
    {
        return $this->resetCode;
    }

    /**
     * @param string|null $resetCode
     */
    public function setResetCode(?string $resetCode): void
    {
        $this->resetCode = $resetCode;
    }
    public function getId(): ?int
    {
        return $this->id;
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

    /**
     * @return Collection|UserEtablissement[]
     */
    public function getUserEtablissements(): Collection
    {
        // Assurez-vous que $userEtablissements est initialisé avant d'y accéder
        return $this->userEtablissements;
    }


    /**
     * @return Collection|Etablissement[]
     */
    public function getFavorites(): Collection
    {
        $favorites = new ArrayCollection();
        foreach ($this->userEtablissements ?? [] as $userEtablissement) {
            if ($userEtablissement->isFavoris()) {
                $favorites[] = $userEtablissement->getEtablissement();
            }
        }
        return $favorites;
    }

    /**
     * A visual identifier that represents this user.
     *
     * @see UserInterface
     */
    public function getUserIdentifier(): string
    {
        return (string) $this->email;
    }

    /**
     * @deprecated since Symfony 5.3, use getUserIdentifier instead
     */
    public function getUsername(): string
    {
        return (string) $this->email;
    }

    /**
     * @see UserInterface
     */
    public function getRoles(): array
    {
        $roles = $this->roles;
        // guarantee every user at least has ROLE_USER
        $roles[] = 'ROLE_USER';

        return array_unique($roles);
    }

    public function setRoles(array $roles): static
    {
        $this->roles = $roles;

        return $this;
    }

    /**
     * @see PasswordAuthenticatedUserInterface
     */
    public function getPassword(): ?string
    {
        return $this->password;
    }

    public function setPassword(?string $password): static
    {
        $this->password = $password;

        return $this;
    }

    /**
     * Returning a salt is only needed, if you are not using a modern
     * hashing algorithm (e.g. bcrypt or sodium) in your security.yaml.
     *
     * @see UserInterface
     */
    public function getSalt(): ?string
    {
        return null;
    }

    /**
     * @see UserInterface
     */
    public function eraseCredentials(): void
    {
        // If you store any temporary, sensitive data on the user, clear it here
        // $this->plainPassword = null;
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

    public function getAddress(): ?string
    {
        return $this->address;
    }

    public function setAddress(string $address): static
    {
        $this->address = $address;

        return $this;
    }

    public function getQuestion(): ?string
    {
        return $this->question;
    }

    public function setQuestion(?string $question): static
    {
        $this->question = $question;

        return $this;
    }

    public function getAnswer(): ?string
    {
        return $this->answer;
    }

    public function setAnswer(?string $answer): static
    {
        $this->answer = $answer;

        return $this;
    }

    public function getStatus(): ?string
    {
        return $this->status;
    }

    public function setStatus(string $status): static
    {
        $this->status = $status;

        return $this;
    }
}
