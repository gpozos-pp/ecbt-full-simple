<?php

header("Content-Type:application/json");
require_once 'braintree-php-3.25.0/lib/Braintree.php';

$gateway = new Braintree_Gateway(array(
    'accessToken' => 'access_token$sandbox$pszj8b4ncdkd7hwt$12e6539822d79d8cba0b921a704dcb07',
));

$result = $gateway->transaction()->sale([
    "amount" => $_POST['amount'],
    "merchantAccountId" => "MXN",
    "paymentMethodNonce" => $_POST['payment_method_nonce'],
    "shipping" => [
      "firstName" => $_POST['first_name'],
      "lastName" => $_POST['lastname'],
      "company" => $_POST['company'],
      "streetAddress" => $_POST['street'],
      "extendedAddress" => $_POST['extended_address'],
      "locality" => $_POST['locality'],
      "region" => $_POST['region'],
      "postalCode" => $_POST['postal_code'],
      "countryCodeAlpha2" => "MX"
    ],
    "options" => [
      "submitForSettlement" => True,
      "paypal" => [
        "description" => $_POST['description']
      ],
    ]

]);
if ($result->success) {
  echo $result->transaction->paypalDetails;
} else {
  echo $result->message;
}