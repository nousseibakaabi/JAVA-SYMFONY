<?php

namespace App\Form;

use App\Entity\Tuteur;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\FileType; 
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\BirthdayType;


class TuteurType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('nom')
        ->add('prenom')
        ->add('date_naisc', BirthdayType::class, [
            'label' => 'Date de naissance',
            'format' => 'dd-MM-yyyy', // Format complet de la date
            // Configuration pour inclure des années antérieures à 2019
            'years' => range(1950, 2003),
        ])
        ->add('tlf')
        ->add('profession')
        ->add('email')
        ->add('image', FileType::class, [
            'label' => false,
            'mapped' => false,
            'required' => false,
        ]);
        
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Tuteur::class,
        ]);
    }
}
