<?php

header("Content-Type:application/json");
require_once 'braintree-php-3.25.0/lib/Braintree.php';

$gateway = new Braintree_Gateway(array(
    'accessToken' => 'access_token$sandbox$pszj8b4ncdkd7hwt$12e6539822d79d8cba0b921a704dcb07',
));

$clientToken = $gateway->clientToken()->generate();
echo $clientToken;

// response(200,NULL,$clientToken);

// function response($status,$status_message,$data)
// {
//     header("HTTP/1.1 ".$status);
    
//     $response['status']=$status;
//     $response['header']=$status_message;
//     $response['data']=$data;
    
//     $json_response = json_encode($response);
//     echo $json_response;
// }