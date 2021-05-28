<?php
include 'conexion.php';

try
{

	$query = "select 	cant_show_interstitial,   
						band_show_boton_crear_chiste,
						band_new_version_app,
						new_version_app,
						mjs_new_version_app,
						band_old_version_app,
						old_version_app,
						mjs_old_version_app,
						total_chistes_por_dia,
						band_crear_chistes_revision 
			from config_app";


			$resul = mysqli_query($conexion,$query);
			if($resul==false)
		    {
		    	$msjError = mysqli_error($conexion);
		        throw new Exception();
		    }

		    
		   		$datosArrayQuery = array();

				while ($row = mysqli_fetch_array($resul))
				{

					 array_push($datosArrayQuery,array( 	
					 								'cant_show_interstitial' =>$row['cant_show_interstitial'],
					 								'band_show_boton_crear_chiste' =>$row['band_show_boton_crear_chiste'],
					 								'band_new_version_app' =>$row['band_new_version_app'],
					 								'new_version_app' =>$row['new_version_app'],
					 								'mjs_new_version_app' =>$row['mjs_new_version_app'],
					 								'band_old_version_app' =>$row['band_old_version_app'],
					 								'old_version_app' =>$row['old_version_app'],
					 								'mjs_old_version_app' =>$row['mjs_old_version_app'],
					 								'total_chistes_por_dia' =>$row['total_chistes_por_dia'],
					 								'band_crear_chistes_revision' =>$row['band_crear_chistes_revision']
											 )
					);
				}

		   		$resultado_query = array(	
		   									'resultado'=>'OK',	
		   									'error'=> '',		
											'data'=> $datosArrayQuery,
											'mensaje'=>''
										);

				echo json_encode($resultado_query);

}
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> "ERROR",
							'error'=> $msjError,
							'data' => '',
							'mensaje'=>'Ocurrió un error a la hora de consultar los datos favor de contactar al programador'
						);

    echo json_encode($resultado_query);


}

?>
