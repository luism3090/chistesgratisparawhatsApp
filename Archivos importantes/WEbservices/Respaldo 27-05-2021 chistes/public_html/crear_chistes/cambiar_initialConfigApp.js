$(document).on('ready',function()
{

	$.ajax(
    {
           type: "POST",
           url: "obtener_initialConfigApp.php",
           data:{datos:""},
           async: true,
           dataType:"json",
           success: function(info)
           {              

           	console.log(info);
           	
           	if(info.resultado === "OK"){

           		if(info.data.length > 0){

           			$("#cant_show_interstitial").val(info.data[0].cant_show_interstitial);
           			$("#band_show_boton_crear_chiste").val(info.data[0].band_show_boton_crear_chiste);
           			$("#band_new_version_app").val(info.data[0].band_new_version_app);
           			$("#new_version_app").val(info.data[0].new_version_app);
           			$("#mjs_new_version_app").val(info.data[0].mjs_new_version_app);
           			$("#band_old_version_app").val(info.data[0].band_old_version_app);
           			$("#old_version_app").val(info.data[0].old_version_app);
           			$("#mjs_old_version_app").val(info.data[0].mjs_old_version_app);
           			$("#total_chistes_por_dia").val(info.data[0].total_chistes_por_dia);
           			$("#band_crear_chistes_revision").val(info.data[0].band_crear_chistes_revision);
           		}
           	    else{
           	  	 $('#modalAlertaError .modal-body .mldMsj').text("Ocurrio un error a la hora de plasmar la informacion de la configuracion");
          	  	 $('#modalAlertaError').modal('show');
           		}
           	     
          	  	 
           	}
           	else{
           	  	 $('#modalAlertaError .modal-body .mldMsj').text("Ocurrio un error a la hora de plasmar la informacion de la configuracion");
          	  	 $('#modalAlertaError').modal('show');
           	}

           	 
           },
           error:function(result)
           {
                console.log(result.responseText);
                //$("#error").html(data.responseText); 
           }
  
   });

	

	$("#btnCambiarConfiguracioApp").on("click",function(event)
	{
		
		let datos = {
				 	cant_show_interstitial: $("#cant_show_interstitial").val().trim(),
				 	band_show_boton_crear_chiste: $("#band_show_boton_crear_chiste").val(),
					band_new_version_app: $("#band_new_version_app").val(),
					new_version_app: $("#new_version_app").val().trim(),
					mjs_new_version_app: $("#mjs_new_version_app").val().trim(),
					band_old_version_app: $("#band_old_version_app").val(),
					old_version_app: $("#old_version_app").val().trim(),
					mjs_old_version_app: $("#mjs_old_version_app").val().trim(),
					total_chistes_por_dia: $("#total_chistes_por_dia").val().trim(),
					band_crear_chistes_revision: $("#band_crear_chistes_revision").val()
				}

		

		if(datos.cant_show_interstitial === "" || datos.new_version_app === "" || datos.mjs_new_version_app === "" || datos.old_version_app === "" || datos.mjs_old_version_app === "" || datos.total_chistes_por_dia === "" ){

			$('#modalAlertaWarning .modal-body .mldMsj').html("<span style='font-size:20px;font-weight:bold'> Debe escribir datos para todos los campos </span>");
		    $('#modalAlertaWarning').modal('show');

		}else{


			$.ajax(
    {
           type: "POST",
           url: "cambiar_initialConfigApp.php",
           data:datos,
           async: true,
           dataType:"json",
           success: function(info)
           {              
           	
           	if(info.resultado === "OK"){

           	     $('#modalAlertaSuccess .modal-body .mldMsj').text("Los datos de configuración fueron actualizados correctamente");
          	  	 $('#modalAlertaSuccess').modal('show');
          	  	 
           	}
           	else{
           	  	 $('#modalAlertaError .modal-body .mldMsj').text(info.error);
          	  	 $('#modalAlertaError').modal('show');
           	}

           	 
           },
           error:function(result)
           {
                console.log(result.responseText);
                //$("#error").html(data.responseText); 
           }
  
   });


		}


	});
	

});