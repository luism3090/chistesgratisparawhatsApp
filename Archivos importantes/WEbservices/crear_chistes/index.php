
<!DOCTYPE html>

<html lang="es">
  <head>
    <!-- Required meta tags -->
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <meta charset="utf-8">
    <!-- Bootstrap CSS -->
    <link rel="stylesheet" href="bootstrap.min.css">
    
    <title>Crear chistes para WhatsApp</title>
    
  </head>
  <body>
<nav class="navbar navbar-dark bg-primary" style="height:50px;background-color:green">
  <h2 style="margin-top:10px">Crear Chistes</h2>
</nav>
   
<div class="container">
    <br>
    
      
        <div class="content row" >
               <div class="col-xs-12">
                       <div class="form-group">
                         <label for="txtChiste">Escriba el chiste:</label>
                         <textarea class="form-control" id="txtChiste" name="txtChiste" rows="10" cols="50"></textarea>
                          <br>
                        </div>
                </div>
                      
        </div>

        <div class="content row" >
               <div class="col-xs-12">
                         <label for="slCategoria">Elija la categoría:</label>
                         <select class="form-control" id="slCategoria" name="slCategoria">
                          <option value="1">Niños</option>
                          <option value="2">Animales</option>
                          <option value="3">Bebés</option>
                          <option value="4">Borrachos</option>
                          <option value="5">Actos</option>
                          <option value="6">Amigos</option>
                          <option value="7">Colmos</option>
                          <option value="8">Comidas</option>
                          <option value="9">Computación</option>
                          <option value="11">Deportes</option>
                          <option value="12">Doctores</option>
                          <option value="13">En qué se parece... ?</option>
                          <option value="14">Jaimito</option>
                          <option value="15">Mamá..! Papá...!</option>
                          <option value="16">Hombres</option>
                          <option value="17">Mujeres</option>
                          <option value="18">Matrinonios</option>
                          <option value="19">Navidad</option>
                          <option value="20">No es lo mismo</option>
                          <option value="21">Novios</option>
                          <option value="22">Pepito</option>
                          <option value="23">Profesiones</option>
                          <option value="24">Profesores</option>
                          <option value="25">Qué le dice...?</option>
                          <option value="26">VIRUS</option>
                         </select>
                          <br>
                </div>
                      
        </div>
        
        <br>
          <div class="content row" >
               <div class="col-xs-12">
                  <center>
                    <button id='btnCrearChistes' name="btnCrearChistes"  class="btn btn-primary" style="width:150px;height:50px;font-size:20px;font-weight:bold;background-color:green">Crear Chiste</button>
                  </center>
               </div>
          </div>
  
</div>
     
  
<div id="modalAlertaSuccess" class="modal fade" role="dialog" data-backdrop="static" data-keyboard="false" >
  <div class="modal-dialog">
    <!-- Modal content-->
    <div class="modal-content">
      <div class="modal-header">
       <button type="button" class="close" data-dismiss="modal">&times;</button>
        <h4 class="modal-title">Alerta</h4>
      </div>
      <div class="modal-body">
        <center><p><i class="fas fa-check-circle fa-lg"></i> <label class='mldMsj'></label> </p></center>
      </div>
      <div class="modal-footer">
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMd">Aceptar</button>
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
      <button type="button" class="btn btn-primary" data-dismiss="modal" id="btnMd">Aceptar</button>
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
    <script src="crear_chistes.js"></script>
  </body>
</html>











