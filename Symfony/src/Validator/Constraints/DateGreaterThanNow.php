<?php

namespace App\Validator\Constraints;

use Symfony\Component\Validator\Constraint;

class DateGreaterThanNow extends Constraint
{
    public $message = 'The date "{{ date }}" should be greater than the current date.';
}
