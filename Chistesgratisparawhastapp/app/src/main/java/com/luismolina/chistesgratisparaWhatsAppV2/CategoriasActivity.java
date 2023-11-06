package com.luismolina.chistesgratisparaWhatsAppV2;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

// PUBLICIDAD
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

public class CategoriasActivity extends AppCompatActivity {

    ProgressDialog dialog;

    TTSManager ttsManager = null;

    SharedPreferences mipreferencia_user, mipreferencia_TotalRows, mipreferencia_categoria;
    SharedPreferences pref_Index_InterstitialAd;

//    ImageView image_home1,image_home2,image_categorias1,image_categorias2,image_favoritos1,image_favoritos2,image_nuevos1,image_nuevos2;

    ScrollView sv_main;
    int x=0;
    int count_interstitalAd = 0;

    // PUBLICIDAD
    private AdView mAdView;
    private InterstitialAd mInterstitialAd;

    // cambio
    SharedPreferences pref_config_AppChistes;
    int cant_max_Ad = 0;

    String band_show_boton_crear_chiste = "";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");

        // cambio
        pref_config_AppChistes = getSharedPreferences("configAppChistes", Context.MODE_PRIVATE);

        band_show_boton_crear_chiste  = pref_config_AppChistes.getString("band_show_boton_crear_chiste","");
        cant_max_Ad = Integer.parseInt(pref_config_AppChistes.getString("cant_show_interstitial",""));

        //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();

        if(index_interstitalAd.equals("")){
            count_interstitalAd = 1;
            SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
            //obj_editor3.putString("index_interstitalAd", "0");
            obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
            obj_editor3.commit();

            //Toast.makeText(getApplicationContext(),"a : " + String.valueOf(count_interstitalAd),Toast.LENGTH_SHORT).show();
        }
        else{

            //Toast.makeText(getApplicationContext(),"b : " + index_interstitalAd,Toast.LENGTH_SHORT).show();

            int a = Integer.parseInt(index_interstitalAd);
            if(a<=(cant_max_Ad-1)){ // a<=4  // cambio
                incrementarIdInterstitial("activity");
            }
        }

        //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();

        // PUBLICIDAD

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.GONE);

        // Interstitial
        mInterstitialAd = new InterstitialAd(this);
        // ID DE PRUEBA --->  ca-app-pub-3940256099942544/1033173712
        // ID EL BUENO ---> ca-app-pub-7642244438296434/7732541519
        mInterstitialAd.setAdUnitId("ca-app-pub-7642244438296434/7732541519");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.

                mAdView.setVisibility(View.GONE);
                mostrarAlertaEspera();
                obtenerCategorias("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/obtener_categorias.php");


                    //Toast.makeText(getApplicationContext(), showAdIntertiWhatsOrScroll+"jajaja", Toast.LENGTH_LONG).show();

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            mipreferencia_categoria = getSharedPreferences("datos_categoria", Context.MODE_PRIVATE);
                            String id_categoria = mipreferencia_categoria.getString("id_categoria","");

                            Button BotonCategoria = (Button) findViewById(Integer.parseInt(id_categoria));
                            String textoCategoria = BotonCategoria.getText().toString();

                            mAdView.setVisibility(View.VISIBLE);

                            Intent chistesCategoria = new Intent(getApplicationContext(),ChistesCategoriaActivity.class);

                            chistesCategoria.putExtra("id_categoria",id_categoria);
                            chistesCategoria.putExtra("titulo_categoria",textoCategoria);
                            chistesCategoria.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                            startActivity(chistesCategoria);


                        }
                    }, 200);

                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }

        });

        getSupportActionBar().setTitle("Categorias");

        sv_main = (ScrollView)findViewById(R.id.scrol);



        String id_usuario = getIntent().getStringExtra("id_usuario");

        final ImageView image_home1 = (ImageView)findViewById(R.id.image_home1);
        final ImageView image_home2 = (ImageView)findViewById(R.id.image_home2);
        final ImageView image_favoritos1 = (ImageView)findViewById(R.id.image_favoritos1);
        final ImageView image_busqueda1 = (ImageView)findViewById(R.id.image_busqueda1);
        final ExtendedFloatingActionButton fab_PublicarChistes = (ExtendedFloatingActionButton)findViewById(R.id.fabPublicarChistes);

        fab_PublicarChistes.hide();

        mipreferencia_categoria = getSharedPreferences("datos_categoria", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor0  = mipreferencia_categoria.edit();
        obj_editor0.putString("id_categoria","");
        obj_editor0.commit();

        mipreferencia_user = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor1  = mipreferencia_user.edit();
        obj_editor1.putString("id_usuario",id_usuario);
        obj_editor1.commit();


        mostrarAlertaEspera();
        obtenerCategorias("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/obtener_categorias.php");


        fab_PublicarChistes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ObtenerTotalChistesPorDia("https://chistesgratis.lmeapps.com/crear_chistes/obtener_total_chistes_por_dia.php");

            }
        });

        image_home1.setOnClickListener(new View.OnClickListener() {
                @Override
                        public void onClick(View v) {

                    Intent inicio = new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(inicio);

                        }
        });

        image_favoritos1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent favoritos = new Intent(getApplicationContext(),FavoritosActivity.class);

                favoritos.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                startActivity(favoritos);

            }
        });

        image_busqueda1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent nuevosChistes = new Intent(getApplicationContext(), BusquedaChistesActivity.class);

                nuevosChistes.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                startActivity(nuevosChistes);


            }
        });

        //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();

    }

    private void obtenerCategorias(String url){

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                final LinearLayout layout_categorias = (LinearLayout)findViewById(R.id.layout_categorias);
                final ExtendedFloatingActionButton fab_PublicarChistes = (ExtendedFloatingActionButton)findViewById(R.id.fabPublicarChistes);

                TTSManager ttsManager = new TTSManager();
                ttsManager.init(getApplicationContext());

                    ocultarAlertaEspera();

                try {

                    JSONObject responseJSON = new JSONObject(response);

                    String mensaje = responseJSON.getString("mensaje");
                    String error = responseJSON.getString("error");
                    String resultado = responseJSON.getString("resultado");

                    if (resultado.equals("OK")) {

                        JSONArray datosArray = responseJSON.getJSONArray("mensaje");

                        for (int i = 0; i < datosArray.length(); i++) {


                            JSONObject datosRow = datosArray.getJSONObject(i);
                            String categoria = datosRow.getString("categoria");
                            String id_categoria = datosRow.getString("id_categoria");
                            int id_categoria_db = Integer.parseInt(id_categoria);
                            String fecha = datosRow.getString("fecha");


                            Space espacioEntreChiste = new Space(getApplicationContext());
                            espacioEntreChiste.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            if(i == 0){
                                espacioEntreChiste.setMinimumHeight(200);
                                layout_categorias.addView(espacioEntreChiste);
                            }


                            // --------------------------------- Creando en Text View para colocar el texto del chiste ---------------------------------

                            Button botonCategoria = new Button(getApplicationContext());
                            botonCategoria.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            botonCategoria.setText(categoria);
                            botonCategoria.setBackgroundColor(Color.rgb(0,0,0));
                            //botonCategoria.setBackgroundColor(Color.rgb(7,94,85));
                            botonCategoria.setTextColor(Color.rgb(255,255,255));
                            botonCategoria.setMinHeight(100);
                            //botonCategoria.setGravity(Gravity.CENTER);
                            botonCategoria.setTextSize(24);
                            //botonCategoria.setPadding(30,0,30,0);
                            botonCategoria.setId(id_categoria_db);  //
                            layout_categorias.addView(botonCategoria);
                            botonCategoria.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Toast.makeText(getApplicationContext(),String.valueOf(view.getId()+" Texto"),Toast.LENGTH_SHORT).show();

                                    String id_categoria = String.valueOf(view.getId());

                                    mipreferencia_categoria = getSharedPreferences("datos_categoria", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor obj_editor0  = mipreferencia_categoria.edit();
                                    obj_editor0.putString("id_categoria",id_categoria);
                                    obj_editor0.commit();


                                    // mostrando Intertitial
                                    pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
                                    String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
                                    int a = Integer.parseInt(index_interstitalAd);

                                    if(a>=cant_max_Ad){ // a>=5 // cambio

                                        incrementarIdInterstitial("whatsApp");
                                        // publicidad
                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        }else{
                                            //Toast.makeText(getApplicationContext(), "aun no se ha cargado el Intertitial", Toast.LENGTH_LONG).show();
                                        }
                                    }else{

                                        Button BotonCategoria = (Button) findViewById(view.getId());
                                        String textoCategoria = BotonCategoria.getText().toString();

                                        Intent chistesCategoria = new Intent(getApplicationContext(),ChistesCategoriaActivity.class);

                                        chistesCategoria.putExtra("id_categoria",id_categoria);
                                        chistesCategoria.putExtra("titulo_categoria",textoCategoria);
                                        chistesCategoria.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                                        startActivity(chistesCategoria);

                                    }

                                }
                            });

                            // --------------------------------------- Creando el espacio entre categorias ---------------------------------

                            Space espacioEntreChiste2 = new Space(getApplicationContext());
                            //Space espacioEntreChiste = new Space((Context) context);
                            espacioEntreChiste2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            if(i == datosArray.length()-1){
                                espacioEntreChiste2.setMinimumHeight(150);
                            }
                            else{
                                espacioEntreChiste2.setMinimumHeight(50);
                            }
                            layout_categorias.addView(espacioEntreChiste2);


                        }

                        //Toast.makeText(getApplicationContext(), mipreferencia_TotalRows.getString("totalRows","")+"_c", Toast.LENGTH_LONG).show();

                    }else{

                        Modals nuevaModal = new Modals("Mensaje", mensaje, "Ok", CategoriasActivity.this);
                        nuevaModal.createModal();

                    }

                    // PUBLICIDAD  mostrando Banner
                    mAdView.setVisibility(View.VISIBLE);

                    if(band_show_boton_crear_chiste.equals("1")){
                        fab_PublicarChistes.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ocurrió un error a la hora de obtener los datos", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                ocultarAlertaEspera();
                Toast.makeText(getApplicationContext(), "Error al conectarse a internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                parametros.put("a","");

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void mostrarAlertaEspera(){
        dialog = new ProgressDialog(CategoriasActivity.this);
        dialog.setMessage("Espere por favor...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ocultarAlertaEspera(){
        if (dialog.isShowing())
            dialog.dismiss();
    }

    public void incrementarIdInterstitial(String accion){

        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
        int a = Integer.parseInt(index_interstitalAd);

        //Toast.makeText(getApplicationContext(),"x: " + index_interstitalAd,Toast.LENGTH_SHORT).show();

        if(accion.equals("otro"))  // cuando se pulse un boton DIFERENTE a Whastapp
        {
            if( a <= (cant_max_Ad-1) ){ // a <= 4 // cambio
                count_interstitalAd = Integer.parseInt(index_interstitalAd) + 1;
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
                obj_editor3.commit();

            }
        }
        else if(accion.equals("whatsApp") || accion.equals("activity")){
            if(a>=cant_max_Ad){ // a>=5 // cambio
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd","1");
                obj_editor3.commit();

            }else{
                count_interstitalAd = Integer.parseInt(index_interstitalAd) + 1;
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
                obj_editor3.commit();
            }
        }

       // index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
       //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu1, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.id_item0:
                Intent intent = new Intent();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    intent.setAction(Settings.ACTION_APP_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, getPackageName());
                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    intent.setAction("android.settings.APP_NOTIFICATION_SETTINGS");
                    intent.putExtra("app_package", getPackageName());
                    intent.putExtra("app_uid", getApplicationInfo().uid);
                } else {
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    intent.addCategory(Intent.CATEGORY_DEFAULT);
                    intent.setData(Uri.parse("package:" + getPackageName()));
                }

                startActivity(intent);

                return true;
            case R.id.id_item1:

                Intent sendIntent1 = new Intent();
                sendIntent1.setAction(Intent.ACTION_SEND);
                sendIntent1.setType("text/plain");
                sendIntent1.putExtra(android.content.Intent.EXTRA_TEXT, "Descarga ya la aplicación chistes gratis para WhatsApp desde ==> https://bit.ly/chistes-gratis riete y comparte los mejores chistes con tus amigos");
                try {
                    startActivity(sendIntent1);
                }
                catch (ActivityNotFoundException ex) {
                    Toast.makeText(getApplicationContext(),"Ocurrió un problema al compartir la aplicación", Toast.LENGTH_LONG).show();
                }

                return true;
            case R.id.id_item2:

                String url;

                url="https://play.google.com/store/apps/details?id=com.luismolina.chistesgratisparaWhatsAppV2";

                Uri uri = Uri.parse(url);
                Intent intent1 = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent1);

                return true;
            case R.id.id_item3:

                Intent infoAplicacion = new Intent(getApplicationContext(),InfoAplicacionActivity.class);

                startActivity(infoAplicacion);

                return true;
            case R.id.id_item4:

                String url2;

                url2="https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/politica.html";

                Uri uri2 = Uri.parse(url2);
                Intent intent2 = new Intent(Intent.ACTION_VIEW, uri2);
                startActivity(intent2);

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    public void ObtenerTotalChistesPorDia(String url){

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                //ocultarAlertaEspera();
                try {

                    JSONObject responseJSON = new JSONObject(response);

                    //String message_id = "234";
                    String mensaje = responseJSON.getString("mensaje");

                    JSONArray array = responseJSON.getJSONArray("mensaje");
                    JSONObject arrayChistes = array.getJSONObject(0);

                    int cant_chistes_dia_creados = Integer.parseInt(arrayChistes.getString("cant_chistes_dia"));
                    int totalChistesPorDia_BD = Integer.parseInt(arrayChistes.getString("total_chistes"));

                    if(totalChistesPorDia_BD > cant_chistes_dia_creados)
                    {
                        Intent crearChiste = new Intent(getApplicationContext(),CrearChisteActivity.class);

                        startActivity(crearChiste);
                    }
                    else
                    {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CategoriasActivity.this);
                        builder.setMessage(Html.fromHtml("Ya no se pueden publicar más chistes por hoy"));
                        builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ocurrió un error a la hora de obtener los datos", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Error al conectarse a internet", Toast.LENGTH_LONG).show();
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

}

