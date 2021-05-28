$(document).on('ready',function()
{
	

	$(".btnPublicarChisteRevision").on("click",function(event)
	{

    let datos = {
        id_chiste : $(this).parent().siblings().eq(0).text(),
        chiste : $(this).parent().siblings().eq(1).find("textarea").val().trim(),
        publicar: "1",
        eliminar: "0"
      }

      if(datos.chiste === ""){

        $('#modalAlertaWarning .modal-body .mldMsj').html("<span style='font-size:20px;font-weight:bold'> Debe escribir datos para el chiste</span>");
        $('#modalAlertaWarning').modal('show');

      }
      else{

        $('#txtChisteRevisionPublicar').attr("data-id_chiste",datos.id_chiste);
        $('#txtChisteRevisionPublicar').attr("data-publicar",datos.publicar);
        $('#txtChisteRevisionPublicar').attr("data-eliminar",datos.eliminar);
        $('#txtChisteRevisionPublicar').val(datos.chiste);

        $('#modalPublicarChiste .modal-body .mldMsj').html("<span style='font-size:20px;font-weight:bold'> ¿Deseas publicar el chiste ahora?</span>");
        $('#modalPublicarChiste').modal('show');

      }

  });



  $(".btnEliminarChisteRevision").on("click",function(event)
  {

    let datos = {
        id_chiste : $(this).parent().siblings().eq(0).text(),
        chiste : $(this).parent().siblings().eq(1).find("textarea").val().trim(),
        publicar: "0",
        eliminar: "1"
      }    

        $('#txtChisteRevisionPublicar').attr("data-id_chiste",datos.id_chiste);
        $('#txtChisteRevisionPublicar').attr("data-publicar",datos.publicar);
        $('#txtChisteRevisionPublicar').attr("data-eliminar",datos.eliminar);
        $('#txtChisteRevisionPublicar').val(datos.chiste);

        $('#modalEliminarChiste .modal-body .mldMsj').html("<span style='font-size:20px;font-weight:bold'> ¿Deseas eliminar el chiste?</span>");
        $('#modalEliminarChiste').modal('show');


  });

$("#btnMdlEliminarChiste").on("click",function(event)
{

 let datos = {
        id_chiste : $('#txtChisteRevisionPublicar').attr("data-id_chiste"),
        chiste : $('#txtChisteRevisionPublicar').val(),
        publicar: $('#txtChisteRevisionPublicar').attr("data-publicar"),
        eliminar: $('#txtChisteRevisionPublicar').attr("data-eliminar")
      }

      //console.log(datos);

      //return;

        $.ajax(
              {
                     type: "POST",
                     url: "crear_chistes_revision.php",
                     data:{datos:datos},
                     async: true,
                     dataType:"json",
                     success: function(info)
                     {              
                      
                     if(info.message_id !== ""){
                          
                           $('#modalAlertaSuccess .modal-body .mldMsj').text("El chiste ha sido eliminado correctamente");
                           $('#modalAlertaSuccess').modal('show');
                         
                      }
                      else{
                           $('#modalAlertaError .modal-body .mldMsj').text("Ocurrio un error a la hora de eliminar el chiste");
                           $('#modalAlertaError').modal('show');
                      }

                       
                     },
                     error:function(result)
                     {
                          console.log(result.responseText);
                          //$("#error").html(data.responseText); 
                     }
            
             });

});

$("#btnMdSucces").on("click",function(event)
{

      location.reload();

});


$("#btnMdlPublicarChiste").on("click",function(event)
{

 let datos = {
        id_chiste : $('#txtChisteRevisionPublicar').attr("data-id_chiste"),
        chiste : $('#txtChisteRevisionPublicar').val(),
        publicar: $('#txtChisteRevisionPublicar').attr("data-publicar"),
        eliminar: $('#txtChisteRevisionPublicar').attr("data-eliminar")
      }

      //console.log(datos);

        $.ajax(
              {
                     type: "POST",
                     url: "crear_chistes_revision.php",
                     data:{datos:datos},
                     async: true,
                     dataType:"json",
                     success: function(info)
                     {              
                      
                     if(info.message_id !== ""){
                          
                           $('#modalAlertaSuccess .modal-body .mldMsj').text("El chiste ha sido publicado correctamente");
                           $('#modalAlertaSuccess').modal('show');
                         
                      }
                      else{
                           $('#modalAlertaError .modal-body .mldMsj').text("Ocurrio un error a la hora de publicar el chiste");
                           $('#modalAlertaError').modal('show');
                      }

                       
                     },
                     error:function(result)
                     {
                          console.log(result.responseText);
                          //$("#error").html(data.responseText); 
                     }
            
             });

});


		
	

});