<?php

namespace App\Form;

use App\Entity\Formationn;
use App\Entity\Niveau;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\GreaterThan;
use Symfony\Component\Validator\Constraints\Regex;
use Symfony\Component\Validator\Constraints\Type;
use Symfony\Component\Validator\Constraints\Url;

use Symfony\Component\Validator\Constraints as Assert;

class FormationnType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('niveau', EntityType::class, [ // Add the niveau field
                'class' => Niveau::class, // Specify the class of the entity
                'choice_label' => 'name', // Display the name property of Niveau entity as options
                'placeholder' => 'Choose a level', // Placeholder text
                'constraints' => [
                    new NotBlank(['message' => 'Level must not be empty.']),
                ]
            ])
            ->add('categorie', ChoiceType::class, [
                'choices' => [
                    'Finance' => 'Finance',
                    'Santé' => 'Santé',
                    'Marketing' => 'Marketing',
                    'Éducation' => 'Éducation',
                    'Communication' => 'Communication',
                    'Ingénierie' => 'Ingénierie',
                    'Droit' => 'Droit',
                    'Sciences sociales' => 'Sciences sociales',
                    'Arts et design' => 'Arts et design',
                ],
            ])
            ->add('titre', null, [
                'constraints' => [
                    new NotBlank(['message' => 'Title must not be empty.']),
                    new Length([
                        'min' => 5,
                        'minMessage' => 'The minimum title name {{ limit }} characters.'
                    ]),
                ]
            ])
            ->add('description', null, [
                'constraints' => [
                    new NotBlank(['message' => 'Description must not be empty.']),
                    new Length([
                        'min' => 10,
                        'minMessage' => 'The minimum description {{ limit }} characters.'
                    ]),
                ]
            ])
            ->add('date_d', DateType::class, [
                'widget' => 'single_text',
                'required' => false,
                'placeholder' => [
                    'year' => 'YYYY',
                    'month' => 'MM',
                    'day' => 'DD',
                ],
                'empty_data' => function ($form) {
                    $entity = $form->getData();
                    if ($entity && $entity->getDateD() !== null) {
                        return $entity->getDateD()->format('Y-m-d');
                    } else {
                        return date('Y-m-d');
                    }
                },
                'constraints' => [
                    new NotBlank(['message' => 'Course debut date must not be empty.']),
                ]
            ])
            ->add('date_f', DateType::class, [
                'widget' => 'single_text',
                'required' => false,
                'placeholder' => [
                    'year' => 'YYYY',
                    'month' => 'MM',
                    'day' => 'DD',
                ],
                'empty_data' => function ($form) {
                    $entity = $form->getData();
                    if ($entity && $entity->getDateD() !== null) {
                        return $entity->getDateD()->format('Y-m-d');
                    } else {
                        return date('Y-m-d');
                    }
                },
                'constraints' => [
                    new NotBlank(['message' => 'Course last date must not be empty.']),
                    new GreaterThan([
                        'value' => 'today',
                        'message' => 'Course date must be after current date.'
                    ]),
                    new Type([
                        'type' => '\DateTimeInterface',
                        'message' => 'Course date must be of type date.'
                    ]),
                ]
            ])
            ->add('lien', null, [
                'constraints' => [
                    new Url(['message' => "The URL '{{ value }}' is not a valid URL."]),
                    new Regex([
                        'pattern' => '/^https:\/\//',
                        'message' => "The URL '{{ value }}' must start with 'https://'."
                    ])
                ]
            ])
            ->add('pdfFile', FileType::class, [
                'label' => 'PDF File',
                'mapped' => false,
                'required' => false,
                'constraints' => [
                    new Assert\File([
                        'maxSize' => '5M',
                        'mimeTypes' => ['application/pdf'],
                        'mimeTypesMessage' => 'Please upload a valid PDF file',
                    ]),
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Formationn::class,
        ]);
    }
}
