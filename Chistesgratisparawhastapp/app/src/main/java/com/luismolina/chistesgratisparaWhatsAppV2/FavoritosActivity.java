package com.luismolina.chistesgratisparaWhatsAppV2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.Settings;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TextView;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

// PUBLICIDAD
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import static java.lang.Float.parseFloat;

public class FavoritosActivity extends AppCompatActivity implements View.OnTouchListener, ViewTreeObserver.OnScrollChangedListener {

    ProgressDialog dialog;

    SharedPreferences mipreferencia_user, mipreferencia_TotalRows, mipreferencia_categoria;
    SharedPreferences pref_Index_InterstitialAd;

    ScrollView sv_main;
    int x=0;
    boolean masChistes = true;
    int count_interstitalAd = 0;
    String showAdIntertiWhatsOrScroll = "";

    // PUBLICIDAD
    private AdView mAdView;
    private AdView adView2;
    private InterstitialAd mInterstitialAd;

    // cambio
    SharedPreferences pref_config_AppChistes;
    int cant_max_Ad = 0;
    String band_show_boton_crear_chiste = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos);

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
        }
        else{
            int a = Integer.parseInt(index_interstitalAd);
            if(a<= (cant_max_Ad-1)){   // a<=4  // cambio
                incrementarIdInterstitial("activity");
            }
        }

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

                if(showAdIntertiWhatsOrScroll.equals("Scroll")){

                    mAdView.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), showAdIntertiWhatsOrScroll+"jajaja", Toast.LENGTH_LONG).show();
                    mostrarAlertaCargando();
                    obtenerChistesFavoritos("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/obtener_chistes_favoritos.php",false);

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            ocultarAlertaEspera();
                            mAdView.setVisibility(View.VISIBLE);
                        }
                    }, 1000);

                }

                mInterstitialAd.loadAd(new AdRequest.Builder().build());
            }

        });

        String id_usuario = getIntent().getStringExtra("id_usuario");

        sv_main = (ScrollView)findViewById(R.id.scrol);

        sv_main.setOnTouchListener(this);
        sv_main.getViewTreeObserver().addOnScrollChangedListener(this);

        getSupportActionBar().setTitle("Favoritos");

        final ImageView image_home1 = (ImageView)findViewById(R.id.image_home1);
        final ImageView image_categorias1 = (ImageView)findViewById(R.id.image_categorias1);
        final ImageView image_busqueda1 = (ImageView)findViewById(R.id.image_busqueda1);
        final ExtendedFloatingActionButton fab_PublicarChistes = (ExtendedFloatingActionButton)findViewById(R.id.fabPublicarChistes);
        fab_PublicarChistes.hide();

        fab_PublicarChistes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ObtenerTotalChistesPorDia("https://chistesgratis.lmeapps.com/crear_chistes/obtener_total_chistes_por_dia.php");

            }
        });

        mipreferencia_user = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor1  = mipreferencia_user.edit();
        obj_editor1.putString("id_usuario",id_usuario);
        obj_editor1.commit();

        mipreferencia_TotalRows = getSharedPreferences("indexQuery", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor2  = mipreferencia_TotalRows.edit();
        obj_editor2.putString("totalRows","0");
        obj_editor2.commit();

        mostrarAlertaEspera();
        obtenerChistesFavoritos("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/obtener_chistes_favoritos.php",true);


        image_home1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inicio = new Intent(getApplicationContext(),MainActivity.class);
                startActivity(inicio);

            }
        });

        image_categorias1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categorias = new Intent(getApplicationContext(),CategoriasActivity.class);

                categorias.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                startActivity(categorias);

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

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        return true;
    }

    private void obtenerChistesFavoritos(String url, final boolean showMensajeNoHayChistes){

        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @SuppressLint("ResourceType")
            @Override
            public void onResponse(String response) {

                final LinearLayout layout_chistes = (LinearLayout)findViewById(R.id.layout_chistes);
                final ExtendedFloatingActionButton fab_PublicarChistes = (ExtendedFloatingActionButton)findViewById(R.id.fabPublicarChistes);

                final TTSManager ttsManager = new TTSManager();
                ttsManager.init(getApplicationContext());

                ocultarAlertaEspera();

                try {

                    JSONObject responseJSON = new JSONObject(response);

                    String mensaje = responseJSON.getString("mensaje");
                    String error = responseJSON.getString("error");
                    String resultado = responseJSON.getString("resultado");

                    if (resultado.equals("OK")) {

                        JSONArray datosChistesArray = responseJSON.getJSONArray("mensaje");

                        Space espacioEntreChiste2 = new Space(getApplicationContext());
                        espacioEntreChiste2.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                        String totalRows = mipreferencia_TotalRows.getString("totalRows","");

                        if(totalRows.equals("0")){
                            espacioEntreChiste2.setMinimumHeight(170);
                            layout_chistes.addView(espacioEntreChiste2);
                        }


                        for (int i = 0; i < datosChistesArray.length(); i++) {

                            JSONObject chistesArray = datosChistesArray.getJSONObject(i);
                            String chiste = chistesArray.getString("chiste");
                            String id_chiste = chistesArray.getString("id_chiste");
                            int id_chiste_db = Integer.parseInt(id_chiste);
                            String id_boton_favorito_rojo = chistesArray.getString("id_boton_favorito_rojo");
                            String id_boton_favorito_normal = chistesArray.getString("id_boton_favorito_normal");


                            // --------------------------------- Creando en Text View para colocar el texto del chiste ---------------------------------

                            TextView textViewChiste = new TextView(getApplicationContext());
                            textViewChiste.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            textViewChiste.setText(chiste);
                            textViewChiste.setBackgroundColor(Color.rgb(0,0,0));
                            textViewChiste.setTextColor(Color.rgb(255,255,255));
                            textViewChiste.setMinHeight(700);
                            textViewChiste.setGravity(Gravity.CENTER);
                            textViewChiste.setTextSize(24);
                            textViewChiste.setPadding(30,50,30,50);
                            textViewChiste.setId(id_chiste_db);  //
                            layout_chistes.addView(textViewChiste);
                            textViewChiste.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    //Toast.makeText(getApplicationContext(),String.valueOf(view.getId()+" Texto"),Toast.LENGTH_SHORT).show();
                                }
                            });



                            // --------------------------------- Creando un table layout como contenedor para colocar los botones de redes sociales ---------------------------------

                            LinearLayout contenedor = new LinearLayout(getApplicationContext());
                            contenedor.setLayoutParams(new LinearLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
                            contenedor.setOrientation(LinearLayout.HORIZONTAL);
                            contenedor.setId(id_chiste_db);
                            contenedor.setPadding(0,-30,0,0);
                            contenedor.setGravity(Gravity.CENTER_HORIZONTAL);
                            layout_chistes.addView(contenedor);


                            // --------------------------------- Creando el boton de Whatsapp -------------------------------------

                            ImageButton botonWhastapp = new ImageButton(getApplicationContext());
                            botonWhastapp.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            botonWhastapp.setImageResource(R.mipmap.icono_whatsapp);
                            botonWhastapp.setBackgroundColor(Color.TRANSPARENT);
                            botonWhastapp.setPadding(5,26,0,0);
                            botonWhastapp.setId(id_chiste_db);
                            contenedor.addView(botonWhastapp);
                            botonWhastapp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    // Obtén el servicio ClipboardManager
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                                    // Crea un ClipData object para almacenar el texto
                                    ClipData clip = ClipData.newPlainText("label", textoChiste);

                                    // Copia el texto al portapapeles
                                    clipboard.setPrimaryClip(clip);

                                    // Convierte el TextView en una imagen
                                    Bitmap bitmap = convertViewToBitmap(textViewChiste);

                                    // Save the bitmap to a file
                                    File file = saveBitmap(bitmap);

                                    shareImageOnWhatsApp(file);

                                    // mostrando Intertitial
                                    pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
                                    String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
                                    int a = Integer.parseInt(index_interstitalAd);

                                    //Toast.makeText(getApplicationContext(),String.valueOf(a),Toast.LENGTH_SHORT).show();

                                    if(a>=cant_max_Ad){ // a>=5  // cambio

                                        // publicidad

                                        showAdIntertiWhatsOrScroll = "Whats";
                                        incrementarIdInterstitial("whatsApp");

                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        }else{
                                           // Toast.makeText(getApplicationContext(), "aun no se ha cargado el Intertitial", Toast.LENGTH_LONG).show();
                                        }
                                    }

                                }
                            });


                            // --------------------------------- Creando el boton de Facebook ---------------------------------

                            ImageButton botonFacebook = new ImageButton(getApplicationContext());
                            //botonFacebook.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            botonFacebook.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            botonFacebook.setImageResource(R.mipmap.icono_messenger);
                            botonFacebook.setBackgroundColor(Color.TRANSPARENT);
                            botonFacebook.setPadding(22,28,0,0);
                            //botonFacebook.setMaxHeight(55);
                            botonFacebook.setId(id_chiste_db);
                            contenedor.addView(botonFacebook);
                            //layout_chistes.addView(botonFacebook);
                            //rel_layout_acciones.addView(botonFacebook);
                            botonFacebook.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("ResourceType")
                                @Override
                                public void onClick(View view) {

                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    // Obtén el servicio ClipboardManager
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                                    // Crea un ClipData object para almacenar el texto
                                    ClipData clip = ClipData.newPlainText("label", textoChiste);

                                    // Copia el texto al portapapeles
                                    clipboard.setPrimaryClip(clip);

                                    // Convierte el TextView en una imagen
                                    Bitmap bitmap = convertViewToBitmap(textViewChiste);

                                    File file = saveBitmap(bitmap);

                                    shareImageOnFacebook(file);

                                }
                            });



                            ImageButton botonCopiar = new ImageButton(getApplicationContext());
                            botonCopiar.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            botonCopiar.setImageResource(R.mipmap.icono_copiar);
                            botonCopiar.setBackgroundColor(Color.TRANSPARENT);
                            botonCopiar.setPadding(24,32,0,0);
                            botonCopiar.setId(id_chiste_db);
                            contenedor.addView(botonCopiar);
                            botonCopiar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    shareImageOnTextPlain(textoChiste);

                                }
                            });

                            ImageButton botonCompartir = new ImageButton(getApplicationContext());
                            botonCompartir.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            botonCompartir.setImageResource(R.mipmap.icono_compartir);
                            botonCompartir.setBackgroundColor(Color.TRANSPARENT);
                            botonCompartir.setPadding(26,32,0,0);
                            botonCompartir.setId(id_chiste_db);
                            contenedor.addView(botonCompartir);
                            botonCompartir.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    // Obtén el servicio ClipboardManager
                                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

                                    // Crea un ClipData object para almacenar el texto
                                    ClipData clip = ClipData.newPlainText("label", textoChiste);

                                    // Copia el texto al portapapeles
                                    clipboard.setPrimaryClip(clip);

                                    // Convierte el TextView en una imagen
                                    Bitmap bitmap = convertViewToBitmap(textViewChiste);

                                    File file = saveBitmap(bitmap);

                                    shareImageOnAnywhere(file);

                                }
                            });

                            ImageButton botonCorazonFavoritos = new ImageButton(getApplicationContext());
                            botonCorazonFavoritos.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            botonCorazonFavoritos.setImageResource(R.mipmap.icono_corazon_favoritos);
                            botonCorazonFavoritos.setBackgroundColor(Color.TRANSPARENT);
                            botonCorazonFavoritos.setPadding(30,26,0,0);
                            botonCorazonFavoritos.setId(1000000+id_chiste_db);
                            if(id_boton_favorito_rojo.equals("null")){
                                botonCorazonFavoritos.setVisibility(View.GONE);
                            }
                            else{
                                botonCorazonFavoritos.setVisibility(View.VISIBLE);
                            }
                            contenedor.addView(botonCorazonFavoritos);
                            botonCorazonFavoritos.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    // OBTENIENDO EL ID DEL ELEMENTO QUE SE LE DIO CLICK Y OCULTARLO

                                    view.setVisibility(View.GONE);  // ocultando el elemento al que se le dio click
                                    int val = view.getId(); // obteniendo el id del elemento al que se le dio click


                                    // HACIENDO VISIBLE EL CORAZON SIN RELLENO
                                    //        1000017
                                    int val2 = val + 1000000;  // obteniendo el id del boton de corazon sin relleno rojo
                                    ImageButton botonCorazonSinRelleno = (ImageButton) findViewById(val2);
                                    botonCorazonSinRelleno.setVisibility(View.VISIBLE);

                                    // OBTENIENDO EL ID DEL TEXVIEW DEL CHISTE PARA LLEVARLO A LA TABLA DE FAVORITOS
                                    int id_chiste = val - 1000000;

                                    eliminarChisteFavorito((id_chiste),mipreferencia_user.getString("id_usuario",""),view.getId(),val2,"https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/eliminar_chiste_favorito.php");

                                    // ocultando al linearlayout padre
                                    //((View) view.getParent()).setVisibility(View.GONE);
                                    layout_chistes.removeView((View) view.getParent());

                                    // ocultando al textView del chiste
                                    TextView textViewChiste = (TextView)findViewById(id_chiste);
                                    //textViewChiste.setVisibility(View.GONE);
                                    layout_chistes.removeView(textViewChiste);

                                    Space espacio = (Space)findViewById(id_chiste);

                                    //Toast.makeText(getApplicationContext(),String.valueOf(espacio),Toast.LENGTH_SHORT).show();
                                    layout_chistes.removeView(espacio);


                                }
                            });


                            ImageButton botonCorazon = new ImageButton(getApplicationContext());
                            botonCorazon.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            botonCorazon.setImageResource(R.mipmap.icono_corazon);
                            botonCorazon.setBackgroundColor(Color.TRANSPARENT);
                            botonCorazon.setPadding(30,26,0,0);
                            botonCorazon.setId(2000000+id_chiste_db);
                            if(id_boton_favorito_normal.equals("null")){
                                botonCorazon.setVisibility(View.VISIBLE);
                            }
                            else{
                                botonCorazon.setVisibility(View.GONE);
                            }
                            contenedor.addView(botonCorazon);
                            botonCorazon.setOnClickListener(new View.OnClickListener() {
                                @Override

                                public void onClick(View view) {

                                    // OBTENIENDO EL ID DEL ELEMENTO QUE SE LE DIO CLICK Y OCULTARLO
                                    view.setVisibility(View.GONE);
                                    int val = view.getId();  // 2000000

                                    //Toast.makeText(getApplicationContext(),String.valueOf(val2),Toast.LENGTH_SHORT).show();

                                    // VOLVIENDO VISIBLE EL ELEMENTO DE CORAZON ROJO PARA MOSTRARLO

                                    //         17  - 1000000
                                    int val2 = val - 1000000;
                                    ImageButton botonCorazonRojo = (ImageButton) findViewById(val2);
                                    botonCorazonRojo.setVisibility(View.VISIBLE);

                                    // OBTENIENDO EL ID DEL TEXVIEW DEL CHISTE PARA LLEVARLO A LA TABLA DE FAVORITOS
                                    int id_chiste = val - 2000000;

                                    guardarChisteFavorito((id_chiste),mipreferencia_user.getString("id_usuario",""),view.getId(),val2,"https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/guardar_chiste_favorito.php");

                                }
                            });

                            ImageButton botonAudio = new ImageButton(getApplicationContext());
                            botonAudio.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            botonAudio.setImageResource(R.mipmap.icono_altavoz2);
                            botonAudio.setBackgroundColor(Color.TRANSPARENT);
                            botonAudio.setPadding(35,26,0,0);
                            botonAudio.setId(id_chiste_db);
                            contenedor.addView(botonAudio);
                            botonAudio.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    ttsManager.initQueue(String.valueOf(textoChiste));

                                    //incrementarIdInterstitial("otro");

                                }

                            });

                            // --------------------------------------- Creando el espacio entre chistes ---------------------------------

                            Space espacioEntreChiste = new Space(getApplicationContext());
                            //Space espacioEntreChiste = new Space((Context) context);
                            espacioEntreChiste.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            espacioEntreChiste.setMinimumHeight(150);
                            espacioEntreChiste.setId(id_chiste_db);
                            layout_chistes.addView(espacioEntreChiste);

                            if(i==4 || i == 9){

                                TextView textTitleAdd = new TextView(getApplicationContext());
                                textTitleAdd.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                textTitleAdd.setText("Anuncio");
                                textTitleAdd.setTextColor(Color.rgb(0,0,0));
                                textTitleAdd.setGravity(Gravity.CENTER);
                                layout_chistes.addView(textTitleAdd);

                                //Publicidad  cada 5 chistes
                                adView2 = new AdView(getApplicationContext());
                                adView2.setAdSize(AdSize.MEDIUM_RECTANGLE);
                                // ca-app-pub-7642244438296434/7476556491  --> ESTE ES EL BUENO
                                // ca-app-pub-3940256099942544/6300978111  --> PARA PRUEBAS
                                adView2.setAdUnitId("ca-app-pub-7642244438296434/7476556491");
                                AdRequest adRequest2 = new AdRequest.Builder().build();
                                adView2.loadAd(adRequest2);
                                layout_chistes.addView(adView2);

                                // --------------------------------------- Creando el espacio entre chistes ---------------------------------

                                Space espacioEntreChiste3 = new Space(getApplicationContext());
                                //Space espacioEntreChiste = new Space((Context) context);
                                espacioEntreChiste3.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                espacioEntreChiste3.setMinimumHeight(150);
                                layout_chistes.addView(espacioEntreChiste3);

                            }



                        }
                        String rowsPref = mipreferencia_TotalRows.getString("totalRows","");
                        int regs = Integer.parseInt(rowsPref)+10;
                        String TotalRows = String.valueOf(regs);

                        mipreferencia_TotalRows = getSharedPreferences("indexQuery", Context.MODE_PRIVATE);
                        SharedPreferences.Editor obj_editor2  = mipreferencia_TotalRows.edit();
                        obj_editor2.putString("totalRows","");
                        obj_editor2.commit();

                        //Toast.makeText(getApplicationContext(), mipreferencia_TotalRows.getString("totalRows","")+"_b", Toast.LENGTH_LONG).show();

                        mipreferencia_TotalRows = getSharedPreferences("indexQuery", Context.MODE_PRIVATE);
                        SharedPreferences.Editor obj_editor1  = mipreferencia_TotalRows.edit();
                        obj_editor1.putString("totalRows",String.valueOf(TotalRows));
                        obj_editor1.commit();

                        //Toast.makeText(getApplicationContext(), mipreferencia_TotalRows.getString("totalRows","")+"_c", Toast.LENGTH_LONG).show();

                        if(datosChistesArray.length()<10){
                            masChistes = false;
                        }

                    }else{

                        if(showMensajeNoHayChistes){

                            TextView textViewNoHayChistes = new TextView(getApplicationContext());
                            textViewNoHayChistes.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            textViewNoHayChistes.setText("No tienes chistes favoritos");
                            textViewNoHayChistes.setGravity(Gravity.CENTER);
                            textViewNoHayChistes.setPadding(0,300,0,0);
                            textViewNoHayChistes.setTextSize(24);
                            textViewNoHayChistes.setTextColor(Color.rgb(0,0,0));
                            layout_chistes.addView(textViewNoHayChistes);

                            Space espacioEntreChiste = new Space(getApplicationContext());
                            espacioEntreChiste.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                            espacioEntreChiste.setMinimumHeight(1500);
                            layout_chistes.addView(espacioEntreChiste);
                            masChistes = false;
                        }
                        else{
                            Modals nuevaModal = new Modals("Mensaje", mensaje, "Ok", FavoritosActivity.this);
                            nuevaModal.createModal();
                            masChistes = false;
                        }


                    }

                    // PUBLICIDAD
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
//                Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                ocultarAlertaEspera();
                Toast.makeText(getApplicationContext(), "Error al conectarse a internet", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                String id_usuario = mipreferencia_user.getString("id_usuario","");

                String totalRows = mipreferencia_TotalRows.getString("totalRows","");

                parametros.put("id_usuario",id_usuario);
                parametros.put("totalRows",totalRows);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void mostrarAlertaEspera(){
        dialog = new ProgressDialog(FavoritosActivity.this);
        dialog.setMessage("Espere por favor...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ocultarAlertaEspera(){
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void mostrarAlertaCargando(){
        dialog = new ProgressDialog(FavoritosActivity.this);
        dialog.setMessage("Cargando mas chistes...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void guardarChisteFavorito(final int id_chiste, final String id_usuario, final int id_boton_favorito_normal, final int id_boton_favorito_rojo, String url){

        com.android.volley.toolbox.StringRequest stringRequest = new com.android.volley.toolbox.StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                //ocultarAlertaEspera();
                try {

                    JSONObject responseJSON = new JSONObject(response);

                    String mensaje = responseJSON.getString("mensaje");
                    String error = responseJSON.getString("error");
                    String resultado = responseJSON.getString("resultado");

                    if (resultado.equals("OK")) {

                        //Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_SHORT).show();

                    }
                    else{

                        Modals nuevaModal = new Modals("Mensaje", error, "OK", FavoritosActivity.this);
                        nuevaModal.createModal();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "ocurrió un error a la hora de obtener los datos", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error al conectarse a internet", Toast.LENGTH_LONG).show();
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                parametros.put("id_chiste", String.valueOf(id_chiste));
                parametros.put("id_usuario",id_usuario);
                parametros.put("id_boton_favorito_normal", String.valueOf(id_boton_favorito_normal));
                parametros.put("id_boton_favorito_rojo", String.valueOf(id_boton_favorito_rojo));

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    private void eliminarChisteFavorito(final int id_chiste, final String id_usuario, final int id_boton_favorito_normal, final int id_boton_favorito_rojo, String url)
    {

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                final LinearLayout layout_chistes = (LinearLayout)findViewById(R.id.layout_chistes);

                //ocultarAlertaEspera();
                try {

                    JSONObject responseJSON = new JSONObject(response);

                    String mensaje = responseJSON.getString("mensaje");
                    String error = responseJSON.getString("error");
                    String resultado = responseJSON.getString("resultado");

                    if (resultado.equals("OK")) {

                    }
                    else{

                        Modals nuevaModal = new Modals("Mensaje", error, "OK", FavoritosActivity.this);
                        nuevaModal.createModal();

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

                parametros.put("id_chiste", String.valueOf(id_chiste));
                parametros.put("id_usuario",id_usuario);
                parametros.put("id_boton_favorito_normal", String.valueOf(id_boton_favorito_normal));
                parametros.put("id_boton_favorito_rojo", String.valueOf(id_boton_favorito_rojo));

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public void onScrollChanged() {

        View view = (View) sv_main.getChildAt(sv_main.getChildCount() - 1);
        int topDetector = sv_main.getScrollY();
        int bottomDetector = view.getBottom() -  (sv_main.getHeight() + sv_main.getScrollY());

        if(topDetector <= 0) {

        }
        else if(bottomDetector <= 15 ) {

            if (masChistes) {

                x=x+1;
                String c = String.valueOf(x);

                if(c.equals("1")){

                    pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
                    String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
                    int a = Integer.parseInt(index_interstitalAd);

                    if(a>=cant_max_Ad){  // a>=5 // cambio

                        incrementarIdInterstitial("whatsApp");
                        showAdIntertiWhatsOrScroll = "Scroll";
                        if (mInterstitialAd.isLoaded()) {
                            mInterstitialAd.show();
                        }
                    }
                    else{
                        mostrarAlertaCargando();
                        obtenerChistesFavoritos("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/obtener_chistes_favoritos.php",false);
                        incrementarIdInterstitial("otro");

                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {

                                ocultarAlertaEspera();
                            }
                        }, 1000);

                    }

                }
                else{
                    //Toast.makeText(getBaseContext(),"has llegado hasta abajo pero cayo en el else"+c,Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            x=0;
        }
    }

    public void incrementarIdInterstitial(String accion){

        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
        int a = Integer.parseInt(index_interstitalAd);

        if(accion.equals("otro"))  // cuando se pulse un boton DIFERENTE a Whastapp
        {
            if( a <= (cant_max_Ad-1) ){  // a <= 4 // cambio
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

                        AlertDialog.Builder builder = new AlertDialog.Builder(FavoritosActivity.this);
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

    private File saveBitmap(Bitmap bitmap) {
        // Save the bitmap to a file
        File file = new File(getExternalCacheDir(), "sampleImage.png");

        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    private void shareImageOnFacebook(File file) {

        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.setPackage("com.facebook.orca");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Obtén más chistes en ==> https://bit.ly/chistes-gratis");

        try {
            startActivity(Intent.createChooser(shareIntent, "Compartir chiste..."));
        }
        catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),"Para poder compartir la imagen instale Facebook Messenger", Toast.LENGTH_LONG).show();
        }


    }

    private void shareImageOnWhatsApp(File file) {

        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.setPackage("com.whatsapp");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Obtén más chistes en ==> https://bit.ly/chistes-gratis");

        try {
            startActivity(Intent.createChooser(shareIntent, "Compartir chiste..."));
        }
        catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),"Para poder compartir la imagen instale WhatsApp", Toast.LENGTH_LONG).show();
        }


    }

    public void shareImageOnTextPlain(String textoChiste) {

        Intent sendIntent1 = new Intent();
        sendIntent1.setAction(Intent.ACTION_SEND);
        sendIntent1.setType("text/plain");
        sendIntent1.putExtra(android.content.Intent.EXTRA_TEXT, textoChiste + "\n\n Obtén más chistes en ==> https://bit.ly/chistes-gratis");
        try {
            startActivity(sendIntent1);
        }
        catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),"Ocurrió un problema al compartir el chiste", Toast.LENGTH_LONG).show();
        }

    }

    private void shareImageOnAnywhere(File file) {

        Uri uri = FileProvider.getUriForFile(this, getApplicationContext().getPackageName() + ".provider", file);

        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
        shareIntent.setType("image/*");
        shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Obtén más chistes en ==> https://bit.ly/chistes-gratis");

        try {
            startActivity(Intent.createChooser(shareIntent, "Compartir chiste..."));
        }
        catch (ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(),"Ocurrió un problema al compartir la imagen", Toast.LENGTH_LONG).show();
        }


    }

    private Bitmap convertViewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

}
