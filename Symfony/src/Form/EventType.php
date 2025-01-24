<?php

namespace App\Form;

use App\Entity\Etablissement;
use App\Entity\Event;
use App\Entity\Partner;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\DateType;
use Symfony\Component\Form\Extension\Core\Type\FileType;
use Symfony\Component\Form\Extension\Core\Type\TextareaType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\File;
use Symfony\Component\Validator\Constraints\GreaterThan;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Positive;
use Symfony\Component\Validator\Constraints as Assert;

class EventType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('idpartnerce', EntityType::class, [
                'class' => Partner::class,
                'choice_label' => 'namepartner',
                'label' => 'Nom du partenaire :'
            ])
            ->add('idestab', EntityType::class, [
                'class' => Etablissement::class,
                'choice_label' => 'nomEtablissement',
                'placeholder' => 'Select an etablissement',
            ])
            ->add('nameevent')
            ->add('dateevent', DateType::class, [
                'widget' => 'single_text',
                'required' => false,
                'placeholder' => [
                    'year' => 'YYYY',
                    'month' => 'MM',
                    'day' => 'DD',
                ],
                'empty_data' => function ($form) {
                    $entity = $form->getData();
                    if ($entity && $entity->getDateObtentionCertificat() !== null) {
                        return $entity->getDateObtentionCertificat()->format('Y-m-d');
                    } else {
                        return date('Y-m-d');
                    }
                },
                ])
            ->add('nbrmax')
            ->add('prix')
            ->add('description', TextareaType::class)
            ->add('image', FileType::class, [
                'label' => 'Image',
                'required' => false,
                'mapped' => false,
                'constraints' => [
                    new File([
                        'maxSize' => '1024k',
                        'mimeTypes' => ['image/jpeg', 'image/png'],
                        'mimeTypesMessage' => 'Please upload a valid image file',
                    ])
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => Event::class,
        ]);
    }
}
