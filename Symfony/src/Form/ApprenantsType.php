<?php

namespace App\Form;

use App\Entity\Apprenants;
use App\Entity\Niveau;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;

class ApprenantsType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name',TextType::class,['label' => 'Name : '])
            ->add('email',TextType::class,['label' => 'Email : '])
            ->add('statutNiveau',TextType::class,['label' => 'Statut : '])
            ->add('formationEtudies',TextType::class,['label' => 'Training Name : '])
            ->add('image', FileType::class, [
                'label' => 'Picture', 
                'data_class' => null,
                'attr' => ['class' => 'form-control'],
            ])
           
            ->add('save', SubmitType::class, [
                'label' => 'Save',
                'attr' => ['class' => 'btn btn-primary']
            ]);
        ;
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Apprenants::class,
        ]);
    }
}
