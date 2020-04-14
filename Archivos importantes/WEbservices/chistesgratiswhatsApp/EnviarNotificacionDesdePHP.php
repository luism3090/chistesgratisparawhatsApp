<?php

  ignore_user_abort();
  ob_start();

  $url = 'https://fcm.googleapis.com/fcm/send';

 //$Token = 'YOUR-TOKEN';

  $fields = array('to' => '/topics/humor' ,
  'data' => array('chiste' => 'Que esta haciendo un perro con un taladro? Ta ladrando'));

  //define('GOOGLE_API_KEY', 'AIzaSyCOsuXHEIgpNj_gVjupiZT_uTtqAk8aTEI');
  define('GOOGLE_API_KEY', 'AIzaSyAdSHqR7YC4px3ao52Iti1zuHXYOQdahlU');
  

  $headers = array(
          'Authorization:key='.GOOGLE_API_KEY,
          'Content-Type: application/json'
  );      

  $ch = curl_init();
  curl_setopt($ch, CURLOPT_URL, $url);
  curl_setopt($ch, CURLOPT_POST, true);
  curl_setopt($ch, CURLOPT_HTTPHEADER, $headers);
  curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
  curl_setopt($ch, CURLOPT_POSTFIELDS, json_encode($fields));
 

  $result = curl_exec($ch);
  if($result === false)
    die('Curl failed ' . curl_error());
    curl_close($ch);
  return $result;
  
?>