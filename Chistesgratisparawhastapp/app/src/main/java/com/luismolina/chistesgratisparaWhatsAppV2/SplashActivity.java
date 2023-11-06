package com.luismolina.chistesgratisparaWhatsAppV2;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SplashActivity extends AppCompatActivity {

    SharedPreferences pref_config_AppChistes;
    String version_app_usuario = "";
    int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
             version_app_usuario = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        pref_config_AppChistes = getSharedPreferences("configAppChistes", Context.MODE_PRIVATE);
        String cant_show_interstitial = pref_config_AppChistes.getString("cant_show_interstitial","");
        String band_show_boton_crear_chiste = pref_config_AppChistes.getString("band_show_boton_crear_chiste","");

        String band_new_version_app = pref_config_AppChistes.getString("band_new_version_app","");
        String new_version_app = pref_config_AppChistes.getString("new_version_app","");
        String mjs_new_version_app = pref_config_AppChistes.getString("mjs_new_version_app","");

        String band_old_version_app = pref_config_AppChistes.getString("band_old_version_app","");
        String old_version_app = pref_config_AppChistes.getString("old_version_app","");
        String mjs_old_version_app = pref_config_AppChistes.getString("mjs_old_version_app","");

        obtenerInicialConfigApp("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/initialConfigApp.php");

    }


    public void obtenerInicialConfigApp(String url){

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                //ocultarAlertaEspera();
                try {

                    JSONObject responseJSON = new JSONObject(response);

                    String resultado = responseJSON.getString("resultado");
                    String msjError = responseJSON.getString("msjError");
                    String msjClient = responseJSON.getString("msjClient");
                    String cant_show_interstitial = responseJSON.getString("cant_show_interstitial");
                    String band_show_boton_crear_chiste = responseJSON.getString("band_show_boton_crear_chiste");

                    String band_new_version_app = responseJSON.getString("band_new_version_app");
                    String new_version_app = responseJSON.getString("new_version_app");
                    String mjs_new_version_app = responseJSON.getString("mjs_new_version_app");

                    String band_old_version_app = responseJSON.getString("band_old_version_app");
                    String old_version_app = responseJSON.getString("old_version_app");
                    String mjs_old_version_app = responseJSON.getString("mjs_old_version_app");

                    //String cantidad_show_interstitial = pref_cant_interstitial.getString("cantidad_show_Ad_interstitial","");

                    if(resultado.equals("OK")){

                       SharedPreferences.Editor obj_editor3  = pref_config_AppChistes.edit();
                       obj_editor3.putString("cant_show_interstitial", cant_show_interstitial);
                        obj_editor3.putString("band_show_boton_crear_chiste", band_show_boton_crear_chiste);

                        obj_editor3.putString("band_new_version_app", band_new_version_app);
                        obj_editor3.putString("new_version_app", new_version_app);
                        obj_editor3.putString("mjs_new_version_app", mjs_new_version_app);

                        obj_editor3.putString("band_old_version_app", band_old_version_app);
                        obj_editor3.putString("old_version_app", old_version_app);
                        obj_editor3.putString("mjs_old_version_app", mjs_old_version_app);
                       obj_editor3.commit();


                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                String band_new_version_app = pref_config_AppChistes.getString("band_new_version_app","");
                                String new_version_app = pref_config_AppChistes.getString("new_version_app","");
                                String mjs_new_version_app = pref_config_AppChistes.getString("mjs_new_version_app","");

                                String band_old_version_app = pref_config_AppChistes.getString("band_old_version_app","");
                                String old_version_app = pref_config_AppChistes.getString("old_version_app","");
                                String mjs_old_version_app = pref_config_AppChistes.getString("mjs_old_version_app","");

                                if(band_old_version_app.equals("1")){
                                    if(old_version_app.equals(version_app_usuario)){

                                        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                                        builder.setCancelable(false);
                                        builder.setMessage(Html.fromHtml(mjs_old_version_app));
                                        builder.setPositiveButton("Actualizar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                                try {
                                                    Uri _link = Uri.parse("market://details?id=com.luismolina.chistesgratisparaWhatsAppV2");

                                                    Intent i = new Intent(Intent.ACTION_VIEW,_link);
                                                    startActivity(i);
                                                } catch (android.content.ActivityNotFoundException anfe) {

                                                    Toast.makeText(getApplicationContext(), "Ha ocurrido un problema al abrir google play", Toast.LENGTH_LONG).show();
                                                }

                                                finish();
                                            }
                                        });
                                        builder.show();
                                    }
                                    else{
                                        mostrarAvisoNewVersionApp(new_version_app, mjs_new_version_app, band_new_version_app);
                                    }
                                }
                                else{
                                    mostrarAvisoNewVersionApp(new_version_app, mjs_new_version_app, band_new_version_app);
                                }


                            }
                        },1500);


                    }else{
                        Toast.makeText(getApplicationContext(), msjClient, Toast.LENGTH_SHORT).show();
                    }

                    //int cant_mostrar_Ad_Interstitial = Integer.parseInt(arrayCantidad.getString("cantidad"));


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Error al conectarse a internet por favor intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                //String id_usuario = mipreferencia_user.getString("id_usuario","");

                // parametros.put("chiste",chiste);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    public void mostrarAvisoNewVersionApp(String new_version_app , String mjs_new_version_app, String band_new_version_app){

        if(!new_version_app.equals(version_app_usuario)){

            if(band_new_version_app.equals("1")){  // muestra el toast

                Toast.makeText(getApplicationContext(), mjs_new_version_app, Toast.LENGTH_LONG).show();

                Intent main = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(main);
                finish();

            }
            else if(band_new_version_app.equals("2")){

                AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this);
                builder.setMessage(Html.fromHtml(mjs_new_version_app));
                builder.setCancelable(false);
                builder.setNegativeButton("Actualizar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //dialog.cancel();

                        try {
                            Uri _link = Uri.parse("market://details?id=com.luismolina.chistesgratisparaWhatsAppV2");

                            Intent i = new Intent(Intent.ACTION_VIEW,_link);
                            startActivity(i);
                        } catch (android.content.ActivityNotFoundException anfe) {

                            Toast.makeText(getApplicationContext(), "Ha ocurrido un problema al abrir google play", Toast.LENGTH_LONG).show();
                        }

                        finish();

                    }
                });
                builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent main = new Intent(getApplicationContext(),MainActivity.class);

                        startActivity(main);
                        finish();

                        //dialog.cancel();

                    }
                });
                builder.show();

            }
            else{
                Intent main = new Intent(getApplicationContext(),MainActivity.class);

                startActivity(main);
                finish();
            }


        }
        else{
            Intent main = new Intent(getApplicationContext(),MainActivity.class);

            startActivity(main);
            finish();
        }



    }


}
