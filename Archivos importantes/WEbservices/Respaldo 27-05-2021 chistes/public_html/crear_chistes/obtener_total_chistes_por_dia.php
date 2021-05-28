<?php
include 'conexion.php';

try
{

	$query = "select count(*) as cant_chistes_dia, (select cantidad from total_chistes) as total_chistes FROM `chistes` WHERE DATE_FORMAT(fecha, '%Y/%m/%d') = DATE_FORMAT(NOW(), '%Y/%m/%d')";


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
					 								'cant_chistes_dia' =>$row['cant_chistes_dia'],
					 								'total_chistes' =>$row['total_chistes'],
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
catch(Exception $e){
	

	$resultado_query = array(
							
							'resultado'=> "ERROR",
							'error'=> $msjError,
							'mensaje'=>'Ocurrió un error a la hora de consultar los datos favor de contactar al programador'
						);

    echo json_encode($resultado_query);


}

?>
