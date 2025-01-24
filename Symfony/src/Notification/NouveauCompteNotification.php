<?php


namespace App\Notification;

use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Twig\Environment;
use Twig\Error\LoaderError;
use Twig\Error\RuntimeError;
use Twig\Error\SyntaxError;

class NouveauCompteNotification
{
    /**
     * @var  MailerInterface
     */
    private $mailer;
    /**
     * @var  Environment
     */
    private $renderer;

    /**
     * @param MailerInterface $mailer
     * @param Environment $renderer
     */
    public function __construct(MailerInterface $mailer, Environment $renderer)
    {
        $this->mailer = $mailer;
        $this->renderer = $renderer;
    }
    public function notify($niveaux,MailerInterface $mailer)
    {
        // On construit le mail
        $message = (new TemplatedEmail())
            // Expéditeur
            ->from('chaymariahi961@gmail.com')
            // Destinataire
            ->to('chayma.riahi@esprit.tn')
            // Corps du message (créé avec twig)
            ->subject('A new level is added')
            ->htmlTemplate('niveau/ajout_niveau.html.twig')
            ->context(['niveaux' => $niveaux
            ]);
        // On envoie le mail
        $mailer->send($message);
    }
}