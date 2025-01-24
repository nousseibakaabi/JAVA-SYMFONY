package com.example.testuser1;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;
import com.stripe.param.PaymentIntentConfirmParams;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        // Set your Stripe API key
        Stripe.apiKey = "sk_test_51Oo7aQLjJljsz2MFpuJDsDFI9BXxos0TFBtXvLTgy0ycVNuHsv7ywF2IoRmrTmglV4OeFtCBeIzjeYsBAghDsULh00jbXIVHgu";

        // Create PaymentIntentCreateParams with desired parameters
        PaymentIntentCreateParams params =
                PaymentIntentCreateParams.builder()
                        .setAmount(500L)
                        .setCurrency("gbp")
                        .setPaymentMethod("pm_card_visa")
                        .setConfirm(true)
                        .putExtraParam("automatic_payment_methods[enabled]", true)
                        .putExtraParam("automatic_payment_methods[allow_redirects]", "never")
                        .build();

        try {
            // Create the PaymentIntent
            PaymentIntent paymentIntent = PaymentIntent.create(params);

            // Confirm the PaymentIntent
            PaymentIntentConfirmParams confirmParams = PaymentIntentConfirmParams.builder()
                    .setPaymentMethod(paymentIntent.getPaymentMethod())
                    .build();

            PaymentIntent confirmedIntent = paymentIntent.confirm(confirmParams);

            // Handle successful confirmation within your application
            System.out.println("PaymentIntent confirmed: " + confirmedIntent);

            // Add your logic here to handle the payment confirmation within your application
            // For example, you can display a success message or update the UI accordingly
        } catch (StripeException e) {
            // Handle error
            e.printStackTrace();
            System.out.println("Error confirming PaymentIntent: " + e.getMessage());
        }
    }
}
