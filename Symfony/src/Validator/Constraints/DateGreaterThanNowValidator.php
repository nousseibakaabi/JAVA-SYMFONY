<?php

namespace App\Validator\Constraints;

use Symfony\Component\Validator\Constraint;
use Symfony\Component\Validator\ConstraintValidator;
use Symfony\Component\Validator\Exception\UnexpectedTypeException;

class DateGreaterThanNowValidator extends ConstraintValidator
{
    public function validate($value, Constraint $constraint)
    {
        if (!$constraint instanceof DateGreaterThanNow) {
            throw new UnexpectedTypeException($constraint, DateGreaterThanNow::class);
        }

        // Check if the value is null
        if ($value === null) {
            // No validation needed if the value is null
            return;
        }

        if ($value <= new \DateTime()) {
            $this->context->buildViolation($constraint->message)
                ->setParameter('{{ date }}', $value->format('Y-m-d')) // Format the date for the message
                ->addViolation();
        }
    }
}
