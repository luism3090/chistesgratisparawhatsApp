<?php
header('Content-type text/html; charset=UTF-8');
include 'conexion.php';

$id_usuario = $_REQUEST["id_usuario"];



try
{

mysqli_query($conexion,"SET character_set_client='utf8mb4'");
mysqli_query($conexion,"SET character_set_results='utf8mb4'");
mysqli_query($conexion,"set collation_connection='utf8mb4_general_ci'");



			$query = " select chi.id_chiste,chiste,id_categoria,fecha, 
					    (  select id_boton_favorito_normal from favoritos favo 
					       where favo.id_usuario = ".$id_usuario." and favo.id_chiste = chi.id_chiste  
					    ) as id_boton_favorito_normal,
	                    ( 
						   select id_boton_favorito_rojo from favoritos favo 
						   where favo.id_usuario = ".$id_usuario." and favo.id_chiste = chi.id_chiste 
					    ) as id_boton_favorito_rojo 
					      from chistes chi order by chi.id_chiste desc limit 1 OFFSET 0 ";


			$resul = mysqli_query($conexion,$query);
			if($resul==false)
		    {
		    	$msjError = mysqli_error($conexion);
		        throw new Exception();
		    }

		    $registros = mysqli_num_rows($resul);

		   if($registros == 0){

		   		$resultado_query = array(	
		   									'resultado'=>'WARNING',
		   									'error'=> '',			
											'mensaje'=>'No se encontraron mas chistes'
										);

				echo json_encode($resultado_query);

		   }
		   else{

		   		$datosArrayQuery = array();
		   		
		   		$chiste="";

				while ($row = mysqli_fetch_array($resul))
				{
					 array_push($datosArrayQuery,array( 	
					 								'id_chiste' =>$row['id_chiste'],
													'chiste' => $row['chiste'],
													//'chiste' => json_decode('"\ud83d\ude00"'),
													'id_categoria' =>$row['id_categoria'],
													'fecha' =>$row['fecha'],
													'id_boton_favorito_normal' =>$row['id_boton_favorito_normal'],
													'id_boton_favorito_rojo' =>$row['id_boton_favorito_rojo']
											 )
					);
					
					$chiste = $row['chiste'];
					
					 
				}

		   		$resultado_query = array(	
		   									'resultado'=>'OK',	
		   									'error'=> '',		
											'mensaje'=> $datosArrayQuery
										);
										
				
				echo json_encode($chiste);
				echo "<br>";
				echo $chiste;
                echo "<br>";
				echo json_encode($resultado_query);
				
				//echo json_decode('"\ud83e\udd23 Hola \ud83e\udd23 \ud83d\udc4d"');
				
				
		   }


		

}
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> "ERROR",
							'error'=> $msjError,
							'mensaje'=> 'Ocurrió un error a la hora de consultar los datos favor de contactar al programador'
						);

    echo json_encode($resultado_query);


}

function decodeEmoticons($src) {
    $replaced = preg_replace("/\\\\u([0-9A-F]{1,4})/i", "&#x$1;", $src);
    $result = mb_convert_encoding($replaced, "UTF-16", "HTML-ENTITIES");
    $result = mb_convert_encoding($result, 'utf-8', 'utf-16');
    return $result;
}



?>
