<?php
include 'conexion.php';


$cant_show_interstitial = $_POST['cant_show_interstitial'];
$band_show_boton_crear_chiste = $_POST['band_show_boton_crear_chiste'];
$band_new_version_app = $_POST['band_new_version_app'];
$new_version_app = $_POST['new_version_app'];
$mjs_new_version_app = $_POST['mjs_new_version_app'];
$band_old_version_app = $_POST['band_old_version_app'];
$old_version_app = $_POST['old_version_app'];
$mjs_old_version_app = $_POST['mjs_old_version_app'];
$total_chistes_por_dia = $_POST['total_chistes_por_dia'];
$band_crear_chistes_revision = $_POST['band_crear_chistes_revision'];


try
{

	$query = "update config_app set	
									cant_show_interstitial = $cant_show_interstitial,   
									band_show_boton_crear_chiste = '$band_show_boton_crear_chiste',
									band_new_version_app = '$band_new_version_app',
									new_version_app = '$new_version_app',
									mjs_new_version_app = '$mjs_new_version_app',
									band_old_version_app = '$band_old_version_app',
									old_version_app = '$old_version_app',
									mjs_old_version_app = '$mjs_old_version_app',
									total_chistes_por_dia = $total_chistes_por_dia,
									band_crear_chistes_revision = '$band_crear_chistes_revision'";


			$resul = mysqli_query($conexion,$query);
			if($resul==false)
		    {
		    	$msjError = mysqli_error($conexion);
		        throw new Exception();
		    }


		   		$resultado_query = array(	
		   									'resultado'=>'OK',	
		   									'error'=> '',		
											'data'=> '',
											'mensaje'=>'Los datos de configuración fueron guardados correctamente'
										);

				echo json_encode($resultado_query);

}
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> "ERROR",
							'error'=> $msjError,
							'data' => '',
							'mensaje'=>'Ocurrió un error a la hora de consultar los datos favor de contactar a soporte'
						);

    echo json_encode($resultado_query);


}

?>
