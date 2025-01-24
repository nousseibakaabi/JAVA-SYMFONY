<?php

namespace App\Form;

use App\Entity\Certificat;
use App\Entity\Niveau;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use FOS\CKEditorBundle\Form\Type\CKEditorType;
use Symfony\Component\Validator\Constraints as Assert;
use Doctrine\ORM\EntityManagerInterface; // Add this

class NiveauType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('name', TextType::class, ['label' => 'Name : '])
            ->add('prerequis', TextType::class, ['label' => 'Prerequisites : '])
            ->add('duree', TextType::class, ['label' => 'Duration : '])
            ->add('nbformation', TextType::class, ['label' => 'Number of training : '])
            ->add('certificat', EntityType::class, [
                'label' => 'Certificate',
                'class' => Certificat::class,
                'choice_label' => 'nomCertificat', // Display the name of the certificate
                'placeholder' => 'Select a certificate',
            ])
            ->add('description', CKEditorType::class, ['label' => 'Description : '])
            ->add('image', FileType::class, [
                'label' => 'Image',
                'data_class' => null,
                'required' => true,
                'constraints' => [
                    new Assert\Image([
                        'maxSize' => '5M',
                    ]),
                ],
            ])
            ->add('save', SubmitType::class, [
                'label' => 'Save',
                'attr' => ['class' => 'btn btn-primary']
            ]);
    }


    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Niveau::class,
        ]);
    }
}
