<?php

namespace App\Form;

use App\Entity\User;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Validator\Constraints\Email;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Length;

class UserRecoveryType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options): void
    {
        $builder
            ->add('email', null, [
                'constraints' => [
                    new NotBlank(['message' => 'L\'adresse e-mail ne peut pas être vide']),
                    new Email(['message' => 'L\'adresse e-mail "{{ value }}" n\'est pas valide']),
                ],
            ])
            ->add('question', ChoiceType::class, [
                'choices' => [
                    'Quel est votre plat préféré?' => 'Quel est votre plat préféré?',
                    'Quelle est votre couleur préférée?' => 'Quelle est votre couleur préférée?',
                    'Quel est le nom de votre animal de compagnie?' => 'Quel est le nom de votre animal de compagnie?',
                    'Quel est votre sport préféré?' => 'Quel est votre sport préféré?',
                ],
                'placeholder' => 'Choisir une question', // Optional: add a selection option
                'required' => true, // Optional: set to true if you want to make the field required
                'label' => 'Question de sécurité', // Label in French
                'constraints' => [
                    new NotBlank(['message' => 'La question ne peut pas être vide']),
                    new Length([
                        'min' => 5,
                        'max' => 255,
                        'minMessage' => 'La question doit contenir au moins {{ limit }} caractères',
                        'maxMessage' => 'La question ne peut pas dépasser {{ limit }} caractères',
                    ]),
                ],
            ])
            ->add('answer', null, [
                'constraints' => [
                    new NotBlank(['message' => 'La réponse ne peut pas être vide']),
                    new Length([
                        'min' => 2,
                        'max' => 255,
                        'minMessage' => 'La réponse doit contenir au moins {{ limit }} caractères',
                        'maxMessage' => 'La réponse ne peut pas dépasser {{ limit }} caractères',
                    ]),
                ],
                'label' => 'Réponse', // Label in French
            ]);
    }

    public function configureOptions(OptionsResolver $resolver): void
    {
        $resolver->setDefaults([
            'data_class' => User::class,
        ]);
    }
}
