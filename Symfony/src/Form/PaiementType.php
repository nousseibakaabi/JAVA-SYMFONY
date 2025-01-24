<?php

namespace App\Form;

use App\Entity\Paiement;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints as Assert;
use App\Validator\Constraints\DateGreaterThanNow;
use Symfony\Component\Validator\Constraints\Regex;

class PaiementType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('name', TextType::class, [
            'label' => 'Name',
            'mapped' => false,
            'attr' => ['class' => 'form-control', 'readonly' => 'readonly'], // Make it read-only
            'data' => 'John Doe', // Set default value
            
        ])
        ->add('cardNumber', TextType::class, [
            'label' => 'Card Number',
            'mapped' => false,
            'attr' => ['class' => 'form-control', 'readonly' => 'readonly'], // Make it read-only
            'data' => '4242 4242 4242 4242', // Set default value
            
        ])
        ->add('cvv', TextType::class, [
            'label' => 'CVV',
            'mapped' => false,
            'attr' => ['class' => 'form-control', 'readonly' => 'readonly'], // Make it read-only
            'data' => '123', // Set default value
            
        ])
        ->add('expireDate', TextType::class, [
            'label' => 'Expire Date',
            'mapped' => false,
            'attr' => ['class' => 'form-control', 'readonly' => 'readonly'], // Make it read-only
            'data' => '2025-12-31', // Set default value
            
        ])


        ->add('phoneNumber', TextType::class, [
            'label' => 'Phone Number',
            'mapped' => false,
            'attr' => ['class' => 'form-control'],
            'constraints' => [
                new Assert\NotBlank(['message' => 'Phone Number cannot be blank.']),
                new Assert\Regex([
                    'pattern' => '/^\+[\d]{11}$/',
                    'message' => 'Phone Number must start with "+" and be followed by 11 digits.',
                ]),
            ],
        ]);
        
        

    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Paiement::class,
        ]);
    }
}
