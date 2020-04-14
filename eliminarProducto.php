<?php
include 'conexion.php';

$codigo = $_POST['codigo'];

try
{

			$query = " delete from productos where codigo = '".$codigo."' ";

			// echo $query;

			// exit();

			$resul = mysqli_query($conexion,$query);
			if($resul==false)
		    {
		    	$msjError = mysqli_error($conexion);
		        throw new Exception();
		    }


		$resultado_query = array(				
									'resultado'=>'OK',
		   							'error'=> '',
								    'mensaje'=>'El producto ha sido eliminado correctamente'
								);

		echo json_encode($resultado_query);

}
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> 'ERROR',
							'error'=> $msjError,
							'mensaje'=>'Ocurrió un error a la hora de guardar los datos favor de contactar al programador'
						);


    echo json_encode($resultado_query);


}

?>
