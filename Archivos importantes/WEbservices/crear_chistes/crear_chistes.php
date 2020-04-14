<?php
  ignore_user_abort();
  ob_start();

include 'conexion.php';


$datos = $_POST['datos'];

$chiste = $datos['chiste'];
$id_categoria = $datos['id_categoria'];


try
{

 	   $query = " insert into chistes (id_chiste,
 	   									chiste,
 	   									id_categoria,
										fecha
 	   									) 
 	   							values (null,
 	   									'".$chiste."',
 	   									".$id_categoria.",
 	   									NOW()
 	   									)"; 

		$resul = mysqli_query($conexion,$query);

		if($resul==false)
	    {	
	    	$msjError = mysqli_error($conexion);
	        throw new Exception();
	    }

	    enviarNotificacion($chiste);

		
	/*	$resultado_query = array(				
								'error'=> false,
								'resultado'=> "OK",
								'mensaje'=>'El chiste ha sido creado correctamente'
							   );

		echo json_encode($resultado_query);
		*/

}
catch(Exception $e){
	

	$resultado_query = array(
							'error'=> true,
							'resultado'=> $msjError,
							'mensaje'=>'Ocurrió un error a la hora de guardar los datos favor de contactar al programador'
						);

    echo json_encode($resultado_query);


}


function enviarNotificacion($chiste){

  $url = 'https://fcm.googleapis.com/fcm/send';

 //$Token = 'YOUR-TOKEN';

  $fields = array('to' => '/topics/humor' ,
  'data' => array('chiste' => $chiste));

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

  $result = json_encode(curl_exec($ch)); 
  if($result === false)
    die('Curl failed ' . curl_error());
  curl_close($ch);
  return $result;

}

?>
