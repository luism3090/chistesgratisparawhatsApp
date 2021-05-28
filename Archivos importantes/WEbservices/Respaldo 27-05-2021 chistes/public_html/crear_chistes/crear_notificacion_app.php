<?php
  ignore_user_abort();
  ob_start();

include 'conexion.php';


$datos = $_POST['datos'];

$titulo = addslashes($datos["titulo"]);

$titulo_notificacion = $titulo.': ';
$content_notificacion = addslashes($datos["contenido"]);
$topic = 'informacion'; // por defecto se queda así ya que en la app esta el topic informacion
$id_titulo = $datos["id_titulo"];


mysqli_query($conexion,"SET character_set_client='utf8mb4'");
mysqli_query($conexion,"SET character_set_results='utf8mb4'");
mysqli_query($conexion,"set collation_connection='utf8mb4_general_ci'");

if( is_null($content_notificacion) || empty($content_notificacion) ) {
    
    exit();
}
else{
    
    try
    {

    	   enviarNotificacion($topic,$titulo_notificacion,$content_notificacion,$id_titulo);

    }
    catch(Exception $e){
    	

    	$resultado_query = array(
    							'error'=> true,
    							'resultado'=> $msjError,
    							'mensaje'=>'Ocurrió un error a la hora de enviar la notificación',
    							'message_id'=>'Ocurrió un error a la hora de enviar la notificación'
    						);

        echo json_encode($resultado_query);


    }
    
}


function enviarNotificacion($topic,$titulo_notificacion,$content_notificacion,$id_titulo){

  $url = 'https://fcm.googleapis.com/fcm/send';

 //$Token = 'YOUR-TOKEN';

  $fields = array('to' => "/topics/$topic",
    'data' => array('topic' => $topic, 
                    'content_notificacion' => $content_notificacion,
                    'titulo_notificacion' => $titulo_notificacion,
                    'id_titulo' => $id_titulo
                ));

   //define('GOOGLE_API_KEY', 'AIzaSyCOsuXHEIgpNj_gVjupiZT_uTtqAk8aTEI');
  //define('GOOGLE_API_KEY', 'AIzaSyAdSHqR7YC4px3ao52Iti1zuHXYOQdahlU');
  define('GOOGLE_API_KEY', 'AAAAEhA40n4:APA91bGSgNsTiT7kGqAiE3HxM6M4ZrGeN1FUJjxga40hWI8sA5A5FD3is-9MC4oX2fSxIzPE5dWpbwqAAdnog0qp-udpbNewi0qugIrQlv44QiPvI9Q4tQBhzZSA_xnUwvXBzvflCKZx');
  

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

  $result = json_encode(curl_exec($ch)); 
  if($result === false)
    die('Curl failed ' . curl_error());
  curl_close($ch);
  return $result;

}
