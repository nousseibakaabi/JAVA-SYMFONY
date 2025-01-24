<?php

namespace App\Form;

use App\Entity\Remiseentry;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\NumberType;
use Symfony\Component\Validator\Constraints as Assert;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Regex;
use Symfony\Bridge\Doctrine\Validator\Constraints\UniqueEntity;

class RemiseType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('code', TextType::class, [
                'label' => 'Code',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Code cannot be blank.']),
                ],
            ])
            ->add('pourcentage', NumberType::class, [
                'label' => 'Pourcentage',
                'attr' => ['class' => 'form-control'],
                'constraints' => [
                    new NotBlank(['message' => 'Pourcentage cannot be blank.']),
                    new Regex([
                        'pattern' => '/^\d+(\.\d+)?$/',
                        'message' => 'Pourcentage must be a valid float value.',
                    ]),
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Remiseentry::class,
            'constraints' => [
                new UniqueEntity([
                    'fields' => 'code',
                    'message' => 'This code is already in use.',
                ]),
            ],
        ]);
    }
    }

