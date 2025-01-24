<?php

namespace App\Controller;

use App\Entity\Formationn;
use App\Entity\Quiz;
use App\Form\QuizType;
use App\Repository\FormationnRepository;
use App\Repository\QuizRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

#[Route('/quizzes')]
class QuizController extends AbstractController
{
    #[Route('/edit/{id}/{formationId}', name: 'app_quiz_edit', methods: ['GET', 'POST'])]
    public function edit(Request $request, $id, $formationId): Response
    {
        $quiz = $this->getDoctrine()->getRepository(Quiz::class)->find($id);


        $form = $this->createForm(QuizType::class, $quiz);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('show_quizzes', ['formationId' => $formationId]);
        }

        return $this->render('back/quiz/edit.html.twig', [
            'quiz' => $quiz,
            'formationId' => $formationId,
            'form' => $form->createView(),
        ]);
    }

    #[Route('/delete/{id}/{formationId}', name: 'app_quiz_delete', methods: ['POST'])]
    public function delete(Request $request, $id,  $formationId): Response
    {
        $quiz = $this->getDoctrine()->getRepository(Quiz::class)->find($id);

            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($quiz);
            $entityManager->flush();

        return $this->redirectToRoute('show_quizzes', ['formationId' => $formationId]);
    }


    #[Route('/show_quizzes/{formationId}', name: 'show_quizzes')]
    public function showQuizzes(int $formationId): Response
    {
        // Fetch the formation by ID
        $formation = $this->getDoctrine()->getRepository(Formationn::class)->find($formationId);

        // Get the quizzes associated with the formation
        $quizzes = $formation->getQuizzes();

        return $this->render('back/quiz/show_quizzes.html.twig', [
            'formation' => $formation,
            'quizzes' => $quizzes,
        ]);
    }
    #[Route('/create/{formationId}', name: 'app_quiz_create', methods: ['GET', 'POST'])]
    public function createQuiz(Request $request, int $formationId): Response
    {
        $quiz = new Quiz();
        $form = $this->createForm(QuizType::class, $quiz);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();

            $formation = $entityManager->getRepository(Formationn::class)->find($formationId);
            if (!$formation) {
                throw $this->createNotFoundException('Formation not found');
            }
            $quiz->setFormation($formation);
            $entityManager->persist($quiz);
            $entityManager->flush();

            // Redirect to the quizzes page for the formation
            return $this->redirectToRoute('show_quizzes', ['formationId' => $formationId]);
        }

        return $this->render('back/quiz/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }
    #[Route('/show-quizzes_front/{formationId}', name: 'show_quizzes_front', methods: ['GET'])]
    public function showQuizzesFront(int $formationId, FormationnRepository $formationnRepository, QuizRepository $quizRepository): Response
    {
        $formation = $formationnRepository->find($formationId);
        $quizzes = $quizRepository->findBy(['formation' => $formation]);

        return $this->render('front/quiz/show_quizzes.html.twig', [
            'formation' => $formation,
            'quizzes' => $quizzes,
        ]);
    }

    #[Route('/quiz/pass/{quizId}', name: 'quiz_pass', methods: ['GET'])]
    public function passQuiz(int $quizId): Response
    {
        $quiz = $this->getDoctrine()->getRepository(Quiz::class)->find($quizId);
        if (!$quiz) {
            throw $this->createNotFoundException('Quiz not found');
        }

        $questions = [
            ['id' => 1, 'text' => $quiz->getQuestion1()],
            ['id' => 2, 'text' => $quiz->getQuestion2()],
            ['id' => 3, 'text' => $quiz->getQuestion3()],
        ];


        return $this->render('front/quiz/pass_quiz.html.twig', [
            'quiz' => $quiz,
            'questions' => $questions,
        ]);
    }

    #[Route('/submit-quiz/{quizId}', name: 'submit_quiz')]
    public function submitQuiz(Request $request, int $quizId): Response
    {
        $quiz = $this->getDoctrine()->getRepository(Quiz::class)->find($quizId);

        if (!$quiz) {
            throw $this->createNotFoundException('Quiz not found');
        }

        $submittedAnswers = $request->request->get('answers');
        $validationResults = [];

        for ($i = 1; $i <= 3; $i++) {
            $correctAnswer = $quiz->{'getAnswer' . $i}();

            // Retrieve the submitted answer from the submitted answers array
            $submittedAnswer = $submittedAnswers[$i] ?? null;

            // Check if submitted answer is empty or null
            $isEmptyOrNull = empty($submittedAnswer) && !is_numeric($submittedAnswer);

            if ($isEmptyOrNull) {
                $isCorrect = false;
            } else {
                $isCorrect = ($correctAnswer === $submittedAnswer);
            }

            $validationResults['question' . $i] = $isCorrect;
        }

        $allCorrect = !in_array(false, $validationResults, true);

        if ($allCorrect) {
            return $this->redirectToRoute('quiz_success', ['formationId' => $quiz->getFormation()->getIdformation()]);
        } else {
            return $this->redirectToRoute('quiz_failure', ['quizId' => $quiz->getId()]);
        }
    }
    #[Route('/quiz/success/{formationId}', name: 'quiz_success')]
    public function quizSuccess(int $formationId): Response
    {
        return $this->render('front/quiz/success.html.twig', ['formationId' => $formationId]);
    }

    #[Route('/quiz/failure/{quizId}', name: 'quiz_failure')]
    public function quizFailure(int $quizId): Response
    {
        return $this->render('front/quiz/failure.html.twig', ['quizId' => $quizId]);
    }


}
