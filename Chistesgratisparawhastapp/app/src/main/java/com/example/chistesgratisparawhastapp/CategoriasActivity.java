package com.example.chistesgratisparawhastapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

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


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorias);

        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");

        if(index_interstitalAd.equals("")){
            count_interstitalAd = 1;
            SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
            //obj_editor3.putString("index_interstitalAd", "0");
            obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
            obj_editor3.commit();
        }
        else{
            //incrementarIdInterstitial("activity");
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
        // ID EL BUENO ---> ca-app-pub-7642244438296434/5675855865
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        getSupportActionBar().setTitle("Categorias");

        sv_main = (ScrollView)findViewById(R.id.scrol);



        String id_usuario = getIntent().getStringExtra("id_usuario");

        final ImageView image_home1 = (ImageView)findViewById(R.id.image_home1);
        final ImageView image_home2 = (ImageView)findViewById(R.id.image_home2);
        final ImageView image_favoritos1 = (ImageView)findViewById(R.id.image_favoritos1);
        final ImageView image_busqueda1 = (ImageView)findViewById(R.id.image_busqueda1);

        mipreferencia_categoria = getSharedPreferences("datos_categoria", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor0  = mipreferencia_categoria.edit();
        obj_editor0.putString("id_categoria","");
        obj_editor0.commit();

        mipreferencia_user = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor1  = mipreferencia_user.edit();
        obj_editor1.putString("id_usuario",id_usuario);
        obj_editor1.commit();


        mostrarAlertaEspera();
        obtenerCategorias("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/obtener_categorias.php");

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



    }




    private void obtenerCategorias(String url){

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                final LinearLayout layout_categorias = (LinearLayout)findViewById(R.id.layout_categorias);

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

                                    Button BotonCategoria = (Button) findViewById(view.getId());
                                    String textoCategoria = BotonCategoria.getText().toString();

                                    Intent chistesCategoria = new Intent(getApplicationContext(),ChistesCategoriaActivity.class);

                                    chistesCategoria.putExtra("id_categoria",id_categoria);
                                    chistesCategoria.putExtra("titulo_categoria",textoCategoria);
                                    chistesCategoria.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                                    startActivity(chistesCategoria);

                                    // mostrando Intertitial
                                    pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
                                    String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");

                                    if(index_interstitalAd.equals("15")){

                                        // publicidad
                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        }else{
                                            //Toast.makeText(getApplicationContext(), "aun no se ha cargado el Intertitial", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    incrementarIdInterstitial("otro");

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

                        // Modals nuevaModal = new Modals("Mensaje", mipreferencia_TotalRows.getString("totalRows","")+"b", "Ok", MainActivity.this);
                        // nuevaModal.createModal();

                    }else{

                        Modals nuevaModal = new Modals("Mensaje", mensaje, "Ok", CategoriasActivity.this);
                        nuevaModal.createModal();

                    }

                    // PUBLICIDAD  mostrando Banner
                    mAdView.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "cayo en el catch", Toast.LENGTH_LONG).show();
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

        if(accion.equals("otro"))  // cuando se pulse un boton DIFERENTE a Whastapp
        {
            int a = Integer.parseInt(index_interstitalAd);
            if( a <= 14 ){
                count_interstitalAd = Integer.parseInt(index_interstitalAd) + 1;
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
                obj_editor3.commit();
            }
        }
        else if(accion.equals("whatsApp") || accion.equals("activity")){

            if(index_interstitalAd.equals("15")){
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd","0");
                obj_editor3.commit();
            }else{
                count_interstitalAd = Integer.parseInt(index_interstitalAd) + 1;
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
                obj_editor3.commit();
            }
        }

        //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();
    }

}
