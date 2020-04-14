<?php
include 'conexion.php';


$codigo = $_REQUEST['codigo'];


try
{

			$query = " select codigo from productos where codigo = '".$codigo."' ";

			//echo $query;

			//exit();

			$resul = mysqli_query($conexion,$query);
			if($resul==false)
		    {
		    	$msjError = mysqli_error($conexion);
		        throw new Exception();
		    }

		    $registros = mysqli_num_rows($resul);

		   if($registros > 0){

		   		$resultado_query = array(	
		   									'resultado'=>'OK',
		   									'error'=> '',			
											'mensaje'=>'El producto es valido'
										);

				echo json_encode($resultado_query);

		   }
		   else{

		   		$resultado_query = array(	
		   									'resultado'=>'WARNING',	
		   									'error'=> '',		
											'mensaje'=>'El código del producto NO existe, por favor ingrese otro diferente'
										);

				echo json_encode($resultado_query);
		   }


		

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
