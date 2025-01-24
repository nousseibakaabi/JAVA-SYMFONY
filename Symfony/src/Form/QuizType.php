<?php

namespace App\Form;

use App\Entity\Formationn;
use App\Entity\Quiz;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\NotBlank;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\Regex;

class QuizType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('question1', TextType::class, [
                'label' => 'Question 1',
                'constraints' => [
                    new NotBlank(['message' => 'Question 1 is required']),
                    new Regex([
                        'pattern' => '/\?$/',
                        'message' => 'Question 1 must end with a question mark "?"',
                    ]),
                    new Length([
                        'min' => 5,
                        'minMessage' => 'Question 1 must have at least {{ limit }} characters',
                    ]),
                ],
            ])
            ->add('answer1', TextType::class, [
                'label' => 'Answer 1',
                'constraints' => [
                    new NotBlank(['message' => 'Answer 1 is required']),
                    new Length([
                        'min' => 3,
                        'minMessage' => 'Answer 1 must have at least {{ limit }} characters',
                    ]),
                ],
            ])
            ->add('question2', TextType::class, [
                'label' => 'Question 2',
                'constraints' => [
                    new NotBlank(['message' => 'Question 2 is required']),
                    new Regex([
                        'pattern' => '/\?$/',
                        'message' => 'Question 2 must end with a question mark "?"',
                    ]),
                    new Length([
                        'min' => 5,
                        'minMessage' => 'Question 2 must have at least {{ limit }} characters',
                    ]),
                ],
            ])
            ->add('answer2', TextType::class, [
                'label' => 'Answer 2',
                'constraints' => [
                    new NotBlank(['message' => 'Answer 2 is required']),
                    new Length([
                        'min' => 3,
                        'minMessage' => 'Answer 2 must have at least {{ limit }} characters',
                    ]),
                ],
            ])
            ->add('question3', TextType::class, [
                'label' => 'Question 3',
                'constraints' => [
                    new NotBlank(['message' => 'Question 3 is required']),
                    new Regex([
                        'pattern' => '/\?$/',
                        'message' => 'Question 3 must end with a question mark "?"',
                    ]),
                    new Length([
                        'min' => 5,
                        'minMessage' => 'Question 3 must have at least {{ limit }} characters',
                    ]),
                ],
            ])
            ->add('answer3', TextType::class, [
                'label' => 'Answer 3',
                'constraints' => [
                    new NotBlank(['message' => 'Answer 3 is required']),
                    new Length([
                        'min' => 3,
                        'minMessage' => 'Answer 3 must have at least {{ limit }} characters',
                    ]),
                ],
            ]);
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Quiz::class,
        ]);
    }
}
