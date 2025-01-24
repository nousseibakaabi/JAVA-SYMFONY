<?php

namespace App\Form;

use App\Entity\Partner;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\Choice;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Regex;

class PartnerType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('namepartner', TextType::class, [
            'constraints' => [
                new NotBlank(['message' => 'Ce champ ne peut pas être vide.']),
            ],
        ])
            ->add('typepartner', ChoiceType::class, [
                'label' => "Partner's Type",
                'choices' => [
                    'Default' => 'Default',
                    'University' => 'University',
                    'Business' => 'Business',
                    'NGO' => 'NGO',
                ],
                'attr' => [
                    'class' => 'form-control',
                ],
                'constraints' => [
                    new Choice(['choices' => ['University', 'Business','NGO'], 'message' => 'Choose a valid partner type.']),
                ],
            ])
            
            ->add('description', TextareaType::class, [
                'constraints' => [
                    new NotBlank(['message' => 'Ce champ ne peut pas être vide.']),
                ],
            ])
            ->add('email', TextType::class, [
                'constraints' => [
                    new NotBlank(['message' => 'Ce champ ne peut pas être vide.']),
                    new Email(['message' => 'Veuillez saisir une adresse email valide.']),
                ],
            ])
            ->add('tel', TextType::class, [
                'constraints' => [
                    new NotBlank(['message' => 'Ce champ ne peut pas être vide.']),
                    new Length(['min' => 8, 'max' => 8, 'exactMessage' => 'Le numéro de téléphone doit contenir exactement {{ limit }} chiffres.']),
                    new Regex(['pattern' => '/^\d{8}$/', 'message' => 'Le numéro de téléphone doit contenir uniquement des chiffres.']),
                ],
            ])
            ->add('image', FileType::class, [
                'label' => 'Image',
                'required' => false, // Indique si le champ est obligatoire ou non
                'mapped' => false, // Indique que ce champ n'est pas mappé directement à une propriété de l'entité
                'constraints' => [
                    new File([
                        'maxSize' => '1024k', // Taille maximale autorisée du fichier
                        'mimeTypes' => [ // Types MIME autorisés
                            'image/jpeg',
                            'image/png',
                            // Ajoutez d'autres types MIME si nécessaire
                        ],
                        'mimeTypesMessage' => 'Please upload a valid image file', // Message en cas de type MIME invalide
                    ])
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Partner::class,
        ]);
    }
}
