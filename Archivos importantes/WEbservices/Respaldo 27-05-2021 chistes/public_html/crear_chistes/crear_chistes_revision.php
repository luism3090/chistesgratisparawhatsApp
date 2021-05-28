<?php
  ignore_user_abort();
  ob_start();

include 'conexion.php';

$datos = $_POST['datos'];

$chiste = addslashes($datos['chiste']);
$id_chiste = $datos['id_chiste'];
$publicar = $datos['publicar'];
$eliminar = $datos['eliminar'];

$topic = 'diversion'; // cambiar de pruebas a diversion para probar con los demas usuarios de la app   cambio__
$titulo_notificacion = 'Nuevo chiste: ';


mysqli_query($conexion,"SET character_set_client='utf8mb4'");
mysqli_query($conexion,"SET character_set_results='utf8mb4'");
mysqli_query($conexion,"set collation_connection='utf8mb4_general_ci'");

if( is_null($chiste) || empty($chiste) ) {
    
    exit();
}
else
{
    
    try
    {
        
        if($publicar == "1"){

           	   $query = " insert into chistes ( id_chiste,
                           	   									chiste,
                           	   									id_categoria,
                          										  fecha,
                          										  chiste_bueno
                           	   								) 
                       	   							values (  null,
                             	   									'".$chiste."',
                             	   									50,
                             	   									NOW(),
                             	   									10
                       	   									    )"; 

          		$resul = mysqli_query($conexion,$query);

          		if($resul==false)
          	    {	
          	    	$msjError = mysqli_error($conexion);
          	        throw new Exception();
          	    }


                $query = "delete from chistes_revision where id_chiste = $id_chiste "; 

                $resul = mysqli_query($conexion,$query);

                if($resul==false)
                  { 
                    $msjError = mysqli_error($conexion);
                      throw new Exception();
                  }
          	    
          	  enviarNotificacion($topic,$chiste,$titulo_notificacion);	

        }
        else{
          
              if($eliminar == "1"){

                   $query = "delete from chistes_revision where id_chiste = $id_chiste "; 

                    $resul = mysqli_query($conexion,$query);

                    if($resul==false)
                      { 
                        $msjError = mysqli_error($conexion);
                          throw new Exception();
                      }

                      $resultado_query = array(       
                                    'error'=> false,
                                    'resultado'=> "OK",
                                    'mensaje'=>'El chiste ha sido eliminado correctamente',
                                    'message_id'=> 'El chiste ha sido eliminado correctamente'
                                     );

                        echo json_encode($resultado_query);

              }  

        }

    }
    catch(Exception $e){
    	

    	$resultado_query = array(
    							'error'=> true,
    							'resultado'=> $msjError,
    							'mensaje'=>'Ocurrió un error a la hora de enviar el chiste',
    							'message_id'=>''
    						);

        echo json_encode($resultado_query);


    }
    
}


function enviarNotificacion($topic,$chiste,$titulo_notificacion){

  $url = 'https://fcm.googleapis.com/fcm/send';

 //$Token = 'YOUR-TOKEN';

  $fields = array('to' => "/topics/$topic",
    'data' => array('topic' => $topic, 
                    'chiste' => $chiste,
                    'titulo_notificacion' => $titulo_notificacion
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