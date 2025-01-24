<?php

namespace App\Notification;

use Symfony\Component\HttpFoundation\Session\Flash\FlashBagInterface;

class MessageService
{
    const TYPE_SUCCESS = "success";
    

    /**
     * @var FlashBagInterface
     */
    protected $flashBag;

    /**
     * @param FlashBagInterface $flashBag
     */
    public function __construct(FlashBagInterface $flashBag)
    {
        $this->flashBag = $flashBag;
    }

    /**
     * @param string $message
     * @return mixed
     */
    public function addSuccess(string $message): void
    {
        $this->flashBag->add(self::TYPE_SUCCESS, $message);
    }

    /**
     * @param string $message
     * @return mixed
     */
   
}