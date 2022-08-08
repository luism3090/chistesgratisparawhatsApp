<?php 

  ignore_user_abort();
  ob_start();
include 'conexion.php';


mysqli_query($conexion,"SET character_set_client='utf8mb4'");
mysqli_query($conexion,"SET character_set_results='utf8mb4'");
mysqli_query($conexion,"set collation_connection='utf8mb4_general_ci'");


$query = "select id_chiste,chiste,fecha from chistes_revision order by id_chiste desc";
$resul = mysqli_query($conexion,$query);

?>
<!DOCTYPE html>

<html lang="es">
  <head>
    <!-- Required meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta charset="utf8mb4_general_ci">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="bootstrap.min.css">
    
    <title>Publicar chistes en revisión</title>
    
  </head>
  <body>
<nav class="navbar navbar-dark bg-primary" style="height:50px;background-color:green">
  <h2 style="margin-top:10px">Publicar chistes en revisión</h2>
</nav>
   
<div class="container">
    <br>
      

      <div class="content row" >
               <div class="col-xs-12">

                        <table class="table" id="tblChistesRevision">
                          <thead >
                            <th>Id</th>
                            <th>Chiste</th>
                            <th>Fecha</th>
                            <th>Publicar</th>
                            <th>Eliminar</th>
                          </thead>
                          <tbody>
                            <?php 

                                while ($row = mysqli_fetch_array($resul))
                                {
                                    ?>
                                          <tr>
                                              <td><?php echo $row['id_chiste']; ?></td>
                                              <td>
                                                  <textarea maxlength="552" id="txtChisteRevision" name="txtChisteRevision" rows="5" cols="50" ><?php echo $row['chiste']; ?></textarea>
                                                </td>
                                                <td><?php echo $row['fecha']; ?></td>
                                                <td> 
                                                      <button class="btn btn-primary btnPublicarChisteRevision" > 
                                                          Publicar chiste 
                                                        </button>
                                                </td>
                                                <td> 
                                                      <button class="btn btn-danger btnEliminarChisteRevision" > 
                                                          Eliminar chiste 
                                                        </button>
                                                </td>
                                            </tr>

                                     <?php 
                                  
                                }

                                 $registros = mysqli_num_rows($resul);

                                  if($registros == 0){

                                    ?>
                                      <tr>
                                        <td colspan="5" style="text-align: center;">
                                          <h4><strong>No hay chistes en revisión</strong><h4>
                                        </td>
                                      </tr>
                                     <?php

                                  }

                            ?>
                          </tbody>
                        </table>

                </div>
                
        </div>

        <br><br>

        
  
</div>

                <br>
                <br>

                <textarea maxlength="250" id="txtChisteRevisionPublicar" rows="5" cols="50" data-id_chiste="" style="display: none"></textarea>

                <br><br>

     
  <div id="modalPublicarChiste" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" >
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Alerta</h4>
      </div>
      <div class="modal-body">
        <center><p><i class="fas fa-exclamation-circle fa-lg"></i> <label class='mldMsj'></label> </p></center>
      </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMdlPublicarChiste">Aceptar</button>
      <button type="button" class="btn btn-default" data-dismiss="modal" >Cancelar</button>

      </div>
    </div>
  </div>
</div>

<div id="modalEliminarChiste" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" >
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Alerta</h4>
      </div>
      <div class="modal-body">
        <center><p><i class="fas fa-exclamation-circle fa-lg"></i> <label class='mldMsj'></label> </p></center>
      </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMdlEliminarChiste">Aceptar</button>
      <button type="button" class="btn btn-default" data-dismiss="modal" >Cancelar</button>

      </div>
    </div>
  </div>
</div>

<div id="modalAlertaSuccess" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" >
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">Alerta</h4>
      </div>
      <div class="modal-body">
        <center><p><i class="fas fa-check-circle fa-lg"></i> <label class='mldMsj'></label> </p></center>
      </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMdSucces">Aceptar</button>
      </div>
    </div>
  </div>
</div>
<div id="modalAlertaWarning" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" >
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Alerta</h4>
      </div>
      <div class="modal-body">
        <center><p><i class="fas fa-exclamation-circle fa-lg"></i> <label class='mldMsj'></label> </p></center>
        
      </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMdlWarning">Aceptar</button>
      </div>
    </div>
  </div>
</div>
<div id="modalAlertaError" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" >
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Alerta</h4>
      </div>
      <div class="modal-body">
        <center><p><i class="fas fa-times fa-lg"></i> <label class='mldMsj'></label> </p></center>
      </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMd">Aceptar</button>
      </div>
    </div>
  </div>
</div>


    <!-- Optional JavaScript -->
    <!-- jQuery first, then Popper.js, then Bootstrap JS -->
    <script src="jquery.min.js"></script>
    <script src="bootstrap.min.js"></script>
    <script src="publicar_chistes_revision.js"></script>
  </body>
</html>











