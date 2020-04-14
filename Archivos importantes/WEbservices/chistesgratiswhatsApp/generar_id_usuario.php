<?php
include 'conexion.php';

$id_usuario = $_REQUEST["id_usuario"];

try
{

			if($id_usuario==""){

				$query = "insert into usuarios (
												id_usuario,
												fecha
											    ) 
										values  (
												null,
												NOW()
												);
												";

				$resul = mysqli_query($conexion,$query);
				if($resul==false)
			    {
			    	$msjError = mysqli_error($conexion);
			        throw new Exception();
			    }

			    	$query = "select max(id_usuario) as id_usuario from usuarios";


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
			    									'mensaje'=>'No se encontró un maximo id de usuario'
			    								);

			    		echo json_encode($resultado_query);

			       }
			       else{

			       		$datosArrayQuery = array();

			    		while ($row = mysqli_fetch_array($resul))
			    		{
			    			    
			    			    $id_usuario_db = $row['id_usuario'];
			    						
			    		}

			       		$resultado_query = array(	
			       									'resultado'=>'OK',	
			       									'error'=> '',		
			    									'mensaje'=> $id_usuario_db
			    								);

			    		echo json_encode($resultado_query);
			       }

			    

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
