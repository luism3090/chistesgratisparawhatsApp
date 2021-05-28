$(document).on('ready',function()
{



	$("#btnCrearChistes").on("click",function(event)
	{
		
		var chiste = $("#txtChiste").val().trim();
		var id_categoria = $("#slCategoria").val();

		if(chiste != "")
		{
			
				let datos = {
				 	chiste: chiste,
				 	id_categoria: id_categoria
				}

				$("#btnCrearChistes").prop("disabled",true);

				    $.ajax(
		            {
			               type: "POST",
			               url: "crear_chistes.php",
			               data:{datos:datos},
			               async: true,
			               dataType:"json",
			               success: function(result)
			               {              
			   

			               	$("#btnCrearChistes").prop("disabled",false);
			               	
			               	if(result.message_id !== ""){
			               	    
			               	     $("#txtChiste").val("");
			               	  	 $('#modalAlertaSuccess .modal-body .mldMsj').text("El chiste ha sido creado correctamente");
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

			$('#modalAlertaWarning .modal-body .mldMsj').html("<span style='font-size:20px;font-weight:bold'> Debe escribir el chiste </span>");
		    $('#modalAlertaWarning').modal('show');
		    
		}


	});

	

});