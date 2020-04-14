<?php
include 'conexion.php';


try
{

			$query = " select id_categoria,categoria,fecha from categorias ";


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
											'mensaje'=>'No se encontraron categorias'
										);

				echo json_encode($resultado_query);

		   }
		   else{

		   		$datosArrayQuery = array();

				while ($row = mysqli_fetch_array($resul))
				{
					 array_push($datosArrayQuery,array( 	
					 								'id_categoria' =>$row['id_categoria'],
													'categoria' =>$row['categoria'],
													'fecha' =>$row['fecha']
											 )
					);
				}

		   		$resultado_query = array(	
		   									'resultado'=>'OK',	
		   									'error'=> '',		
											'mensaje'=> $datosArrayQuery
										);

				echo json_encode($resultado_query);
		   }


		

}
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> "ERROR",
							'error'=> $msjError,
							'mensaje'=> 'Ocurrió un error a la hora de consultar los datos favor de contactar al programador 2'
						);

    echo json_encode($resultado_query);


}

?>
