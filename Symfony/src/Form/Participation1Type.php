<?php

namespace App\Form;

use App\Entity\Apprenants;
use App\Entity\Niveau;
use App\Entity\Participation1;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use FOS\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;

class Participation1Type extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
        ->add('student', EntityType::class, [
            'class' => Apprenants::class,
            'choice_label' => 'name', 
            'attr' => ['class' => 'form-control'],
        ])
        ->add('level', EntityType::class, [
            'class' => Niveau::class,
            'choice_label' => 'name', 
            'attr' => ['class' => 'form-control'],
        ])
            ->add('startdate')
            ->add('participationstatus',ChoiceType::class, [
                'choices' => [
                    'New' => 'New',
                    'In progress' => 'In progress',
                    'Finished' => 'Finished', 
                    
                ]
                ])
            ->add('comment', CKEditorType::class,['label' => 'Description : '])
            
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Participation1::class,
        ]);
    }
}
