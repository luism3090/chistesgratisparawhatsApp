$(document).on('ready',function()
{


	$("#btnCrearNotificacion").on("click",function(event)
	{
		
		var contenido = $("#txtContenido").val().trim();
		var titulo = $('select[name="slTitulo"] option:selected').text().trim();
		var id_titulo = $('select[name="slTitulo"] option:selected').val();

		if(contenido != "")
		{
			
				let datos = {
				 	contenido: contenido,
				 	titulo: titulo,
				 	id_titulo: id_titulo
				}

				$("#btnCrearNotificacion").prop("disabled",true);

				    $.ajax(
		            {
			               type: "POST",
			               url: "crear_notificacion_app.php",
			               data:{datos:datos},
			               async: true,
			               dataType:"json",
			               success: function(result)
			               {              
			   

			               	$("#btnCrearNotificacion").prop("disabled",false);
			               	
			               	if(result.message_id !== ""){
			               	    
			               	     $("#txtContenido").val("");
			               	  	 $('#modalAlertaSuccess .modal-body .mldMsj').text("La notificacion ha sido enviada correctamente");
				          	  	 $('#modalAlertaSuccess').modal('show');
				          	  	 
			               	}

			               	 
			               },
			               error:function(result)
			               {
			                    console.log(result.responseText);
			                    //$("#error").html(data.responseText); 
			               }
		          
		           });

		}
		else{

			$('#modalAlertaWarning .modal-body .mldMsj').html("<span style='font-size:20px;font-weight:bold'> Debe escribir la notificación </span>");
		    $('#modalAlertaWarning').modal('show');
		    
		}


	});

	

});