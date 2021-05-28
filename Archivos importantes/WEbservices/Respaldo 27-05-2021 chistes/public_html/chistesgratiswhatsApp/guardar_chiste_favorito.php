<?php
include 'conexion.php';

$id_chiste = $_REQUEST["id_chiste"];
$id_usuario = $_REQUEST["id_usuario"];
$id_boton_favorito_rojo = $_REQUEST["id_boton_favorito_rojo"];
$id_boton_favorito_normal = $_REQUEST["id_boton_favorito_normal"];


try
{

			$query = "insert into favoritos 
											(	fecha_id_favorito,
												id_chiste,
												id_usuario,
												id_boton_favorito_rojo,
												id_boton_favorito_normal	
											)
									values 
											(	NOW(),
												".$id_chiste.",
												".$id_usuario.",
												'".$id_boton_favorito_rojo."',
												'".$id_boton_favorito_normal."'
											)";


			$resul = mysqli_query($conexion,$query);
			if($resul==false)
		    {
		    	$msjError = mysqli_error($conexion);
		        throw new Exception();
		    }


		   		$resultado_query = array(	
		   									'resultado'=>'OK',	
		   									'error'=> '',		
											'mensaje'=> "El chiste ha sido agregado a tu lista de favoritos"
										);

				echo json_encode($resultado_query);
		 
		

}
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> "ERROR",
							'error'=> $msjError,
							'mensaje'=>'Ocurrió un error a la hora de consultar los datos favor de contactar al programador'
						);

    echo json_encode($resultado_query);


}

?>
