package com.example.chistesgratisparawhastapp;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.text.Layout;
import android.view.Gravity;
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

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

//   PUBLICIDAD

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Integer.parseInt;

// PUBLICIDAD


/*
 import com.google.android.gms.ads.initialization.InitializationStatus;
 import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
*/

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, ViewTreeObserver.OnScrollChangedListener {

    SharedPreferences mipreferencia_user;
    SharedPreferences mipreferencia_TotalRows;
    SharedPreferences pref_Index_InterstitialAd;

    SwipeRefreshLayout miSwipeRefreshLayout;
    ProgressDialog dialog;
    TTSManager ttsManager = null;

    ScrollView sv_main;
    int x=0;
    boolean masChistes = true;
    String showAdIntertiWhatsOrScroll = "";

    // PUBLICIDAD
    private AdView mAdView;
    private AdView adView2;
    private InterstitialAd mInterstitialAd;

    int count_interstitalAd = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");

        //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();

           if(index_interstitalAd.equals("")){
               count_interstitalAd = 1;
               SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
               //obj_editor3.putString("index_interstitalAd", "1");
               obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
               obj_editor3.commit();
           }
           else{

               incrementarIdInterstitial("activity");
           }


        //Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();

        // Banner
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.setVisibility(View.GONE);

        // Interstitial
        mInterstitialAd = new InterstitialAd(this);
        // ID DE PRUEBA --->  ca-app-pub-3940256099942544/1033173712
        // ID EL BUENO ---> ca-app-pub-7642244438296434/5675855865
        mInterstitialAd.setAdUnitId("ca-app-pub-7642244438296434/5675855865");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
                // Load the next interstitial.

                if(showAdIntertiWhatsOrScroll.equals("Scroll")){

                    mAdView.setVisibility(View.GONE);
                    //Toast.makeText(getApplicationContext(), showAdIntertiWhatsOrScroll+"jajaja", Toast.LENGTH_LONG).show();
                    mostrarAlertaCargando();
                    obtenerChistes("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/obtener_chistes.php", "1");

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


        getSupportActionBar().setTitle("Inicio");

        FirebaseMessaging.getInstance().subscribeToTopic("humor");
        //String token = FirebaseInstanceId.getInstance().getToken();

        mipreferencia_user = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
        String id_usuario = mipreferencia_user.getString("id_usuario","");

        mipreferencia_TotalRows = getSharedPreferences("indexQuery", Context.MODE_PRIVATE);
        SharedPreferences.Editor obj_editor2  = mipreferencia_TotalRows.edit();
        obj_editor2.putString("totalRows","0");
        obj_editor2.commit();

       // Toast.makeText(getApplicationContext(),id_usuario,Toast.LENGTH_SHORT).show();

        sv_main = (ScrollView)findViewById(R.id.scrol);
        final LinearLayout layout_chistes = (LinearLayout)findViewById(R.id.layout_chistes);

        final ImageView image_categorias1 = (ImageView)findViewById(R.id.image_categorias1);
        final ImageView image_favoritos1 = (ImageView)findViewById(R.id.image_favoritos1);
        final ImageView image_busqueda1 = (ImageView)findViewById(R.id.image_busqueda1);


        // PUBLICIDAD que va aparecer cada vez que se carguen mas chistes


        image_categorias1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent categorias = new Intent(getApplicationContext(),CategoriasActivity.class);

                categorias.putExtra("id_usuario",mipreferencia_user.getString("id_usuario",""));

                startActivity(categorias);


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

        sv_main.setOnTouchListener(this);
        sv_main.getViewTreeObserver().addOnScrollChangedListener(this);

        mostrarAlertaEspera();
        if(id_usuario.equals("")){
            generarIdUsuario("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/generar_id_usuario.php");
        }
        else{
            obtenerChistes("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/obtener_chistes.php","2");
        }

    }

    private void obtenerChistes(String url, final String mostrar){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(String response) {

                final LinearLayout layout_chistes = (LinearLayout)findViewById(R.id.layout_chistes);

                ttsManager = new TTSManager();
                ttsManager.init(getApplicationContext());

                if(mostrar.equals("2")){
                    ocultarAlertaEspera();
                }

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
                            int id_chiste_db = parseInt(id_chiste);
                            String id_boton_favorito_rojo = chistesArray.getString("id_boton_favorito_rojo");
                            String id_boton_favorito_normal = chistesArray.getString("id_boton_favorito_normal");


                            // --------------------------------- Creando en Text View para colocar el texto del chiste ---------------------------------

                            TextView textViewChiste = new TextView(getApplicationContext());
                            textViewChiste.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                            textViewChiste.setText(chiste);
                            textViewChiste.setBackgroundColor(Color.rgb(0,0,0));
                            //textViewChiste.setBackgroundColor(Color.rgb(7,94,85));
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
                            //contenedor.setBackgroundColor(Color.rgb(20,50,90));
                            contenedor.setPadding(0,-30,0,0);
                            contenedor.setGravity(Gravity.CENTER_HORIZONTAL);
                            layout_chistes.addView(contenedor);


                            // --------------------------------- Creando el boton de Whatsapp -------------------------------------

                            ImageButton botonWhastapp = new ImageButton(getApplicationContext());
                            //botonWhastapp.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                            botonWhastapp.setLayoutParams(new ActionBar.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                            botonWhastapp.setImageResource(R.mipmap.icono_whatsapp);
                            botonWhastapp.setBackgroundColor(Color.TRANSPARENT);
                            botonWhastapp.setPadding(5,26,0,0);
                            botonWhastapp.setId(id_chiste_db);
                            //botonWhastapp.setMinimumWidth(50);
                            contenedor.addView(botonWhastapp);
                            //layout_chistes.addView(botonWhastapp);
                            //rel_layout_acciones.addView(botonWhastapp);
                            botonWhastapp.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    textViewChiste.setDrawingCacheEnabled(true);
                                    textViewChiste.buildDrawingCache();  // Creando un Bitmap del Texview el chiste
                                    Uri url = saveImageExternal(textViewChiste.getDrawingCache());

                                    if(Build.VERSION.SDK_INT>=24){
                                        try{
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                            Uri.fromFile(new File(String.valueOf(url)));
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                    Intent sendIntent1 = new Intent();
                                    sendIntent1.setAction(Intent.ACTION_SEND);
                                    sendIntent1.setData(url);
                                    sendIntent1.setType("image/*");
                                    sendIntent1.setPackage("com.whatsapp");
                                    sendIntent1.putExtra(Intent.EXTRA_STREAM, url);
                                    //sendIntent1.putExtra(Intent.EXTRA_STREAM, getResources().getIdentifier("com.my.app:drawable/"+parts[1], null, null));
                                    try {
                                        startActivity(sendIntent1);
                                    }
                                    catch (ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(),"Para poder compartir la imagen instale WhatsApp", Toast.LENGTH_LONG).show();
                                    }

                                    // mostrando Intertitial
                                    pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
                                    String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
                                    int a = Integer.parseInt(index_interstitalAd);

                                    if(a>=8){

                                        // publicidad
                                        showAdIntertiWhatsOrScroll = "Whats";
                                        incrementarIdInterstitial("whatsApp");

                                        if (mInterstitialAd.isLoaded()) {
                                            mInterstitialAd.show();
                                        }else{
                                            //Toast.makeText(getApplicationContext(), "aun no se ha cargado el Intertitial", Toast.LENGTH_LONG).show();
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
                            //botonFacebook.setMinimumHeight(50);
                            botonFacebook.setId(id_chiste_db);
                            contenedor.addView(botonFacebook);
                            botonFacebook.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("ResourceType")
                                @Override
                                public void onClick(View view) {

                                    TextView textViewChiste = (TextView) findViewById(view.getId());
                                    String textoChiste = textViewChiste.getText().toString();

                                    textViewChiste.setDrawingCacheEnabled(true);
                                    textViewChiste.buildDrawingCache();  // Creando un Bitmap del Texview el chiste
                                    Uri url = saveImageExternal(textViewChiste.getDrawingCache());


                                    if(Build.VERSION.SDK_INT>=24){
                                        try{
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                            Uri.fromFile(new File(String.valueOf(url)));
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                    Intent sendIntent1 = new Intent();
                                    //Intent sendIntent1=new Intent(Intent.ACTION_VIEW, FileProvider.getUriForFile(this, AUTHORITY, ));
                                    sendIntent1.setAction(Intent.ACTION_SEND);
//                                    sendIntent1.setData(url);
                                    sendIntent1.setType("image/*");
                                    sendIntent1.setPackage("com.facebook.orca");
                                    sendIntent1.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    sendIntent1.putExtra(Intent.EXTRA_STREAM, url);
                                    //sendIntent1.putExtra(Intent.EXTRA_STREAM, getResources().getIdentifier("com.my.app:drawable/"+parts[1], null, null));
                                    try {
                                        startActivity(Intent.createChooser(sendIntent1, "Compartir chiste..."));
                                    }
                                    catch (ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(),"Para poder compartir la imagen instale Facebook Messenger", Toast.LENGTH_LONG).show();
                                    }

                                    incrementarIdInterstitial("otro");

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

                                    ClipboardManager copiarTexto = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                                    ClipData clip = ClipData.newPlainText("text",  textoChiste);
                                    copiarTexto.setPrimaryClip(clip);

                                    Toast.makeText(getApplicationContext(),"El texto del chiste ha sido copiado",Toast.LENGTH_SHORT).show();

                                    incrementarIdInterstitial("otro");

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

                                    textViewChiste.setDrawingCacheEnabled(true);
                                    textViewChiste.buildDrawingCache();  // Creando un Bitmap del Texview el chiste
                                    Uri url = saveImageExternal(textViewChiste.getDrawingCache());

                                    if(Build.VERSION.SDK_INT>=24){
                                        try{
                                            Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                                            m.invoke(null);
                                            Uri.fromFile(new File(String.valueOf(url)));
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }
                                    }

                                    Intent sendIntent1 = new Intent();
                                    sendIntent1.setAction(Intent.ACTION_SEND);
                                    //sendIntent1.setData(url);
                                    sendIntent1.setType("image/*");
                                    sendIntent1.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                                    sendIntent1.putExtra(Intent.EXTRA_STREAM, url);
                                    try {
                                        startActivity(sendIntent1);
                                    }
                                    catch (ActivityNotFoundException ex) {
                                        Toast.makeText(getApplicationContext(),"Ocurrió un problema al compartir la imagen", Toast.LENGTH_LONG).show();
                                    }

                                    incrementarIdInterstitial("otro");

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
                                    //TextView textViewChiste = (TextView) findViewById(id_chiste);
                                    //String textoChiste = textViewChiste.getText().toString();
                                    //Toast.makeText(getApplicationContext(),textoChiste,Toast.LENGTH_LONG).show();

                                    eliminarChisteFavorito((id_chiste),mipreferencia_user.getString("id_usuario",""),view.getId(),val2,"https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/eliminar_chiste_favorito.php");

                                    incrementarIdInterstitial("otro");

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
                                    //TextView textViewChiste = (TextView) findViewById(id_chiste);
                                    //String textoChiste = textViewChiste.getText().toString();
                                    //Toast.makeText(getApplicationContext(),textoChiste,Toast.LENGTH_LONG).show();

                                    guardarChisteFavorito((id_chiste),mipreferencia_user.getString("id_usuario",""),view.getId(),val2,"https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/guardar_chiste_favorito.php");

                                    incrementarIdInterstitial("otro");

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

                                    incrementarIdInterstitial("otro");

                                }

                            });

                            // --------------------------------------- Creando el espacio entre chistes ---------------------------------

                                Space espacioEntreChiste = new Space(getApplicationContext());
                                //Space espacioEntreChiste = new Space((Context) context);
                                espacioEntreChiste.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                espacioEntreChiste.setMinimumHeight(150);
                                layout_chistes.addView(espacioEntreChiste);


                                if(i==4 || i==9){

                                    TextView textTitleAdd = new TextView(getApplicationContext());
                                    textTitleAdd.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                                    textTitleAdd.setText("Anuncio");
                                    textTitleAdd.setTextColor(Color.rgb(0,0,0));
                                    textTitleAdd.setGravity(Gravity.CENTER);
                                    layout_chistes.addView(textTitleAdd);

                                    //Publicidad  cada 5 chistes
                                    adView2 = new AdView(getApplicationContext());
                                    adView2.setAdSize(AdSize.MEDIUM_RECTANGLE);
                                    // ca-app-pub-7642244438296434/9400366508  --> ESTE ES EL BUENO
                                    // ca-app-pub-3940256099942544/6300978111  --> PARA PRUEBAS
                                    adView2.setAdUnitId("ca-app-pub-7642244438296434/9400366508");
                                    AdRequest adRequest2 = new AdRequest.Builder().build();
                                    adView2.loadAd(adRequest2);
                                    layout_chistes.addView(adView2);

                                    // --------------------------------------- Creando el espacio entre chistes ---------------------------------

                                    Space espacioEntreChiste4 = new Space(getApplicationContext());
                                    //Space espacioEntreChiste = new Space((Context) context);
                                    espacioEntreChiste4.setLayoutParams(new ActionBar.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                    espacioEntreChiste4.setMinimumHeight(150);
                                    layout_chistes.addView(espacioEntreChiste4);


                            }


                        }


                            String rowsPref = mipreferencia_TotalRows.getString("totalRows","");
                            int regs = parseInt(rowsPref)+10;
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

                        Modals nuevaModal = new Modals("Mensaje", mensaje, "Ok", MainActivity.this);
                        nuevaModal.createModal();
                        masChistes = false;

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
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Espere por favor...");
        dialog.setCancelable(false);
        dialog.show();
    }

    private void ocultarAlertaEspera(){
        if (dialog.isShowing())
            dialog.dismiss();
    }

    private void mostrarAlertaCargando(){
        dialog = new ProgressDialog(MainActivity.this);
        dialog.setMessage("Cargando mas chistes...");
        dialog.setCancelable(false);
        dialog.show();
    }



    private void generarIdUsuario(String url){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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

                        mipreferencia_user = getSharedPreferences("datos_usuario", Context.MODE_PRIVATE);
                        SharedPreferences.Editor obj_editor  = mipreferencia_user.edit();
                        obj_editor.putString("id_usuario",mensaje);
                        obj_editor.commit();

                        //Toast.makeText(getApplicationContext(), mensaje, Toast.LENGTH_LONG).show();

                        obtenerChistes("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/obtener_chistes.php","2");

                    } else if (resultado.equals("WARNING")) {

                        Modals nuevaModal = new Modals("Mensaje", mensaje, "OK", MainActivity.this);
                        nuevaModal.createModal();


                    }else{

                        Modals nuevaModal = new Modals("Mensaje", error, "OK", MainActivity.this);
                        nuevaModal.createModal();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "cayo en el catch", Toast.LENGTH_LONG).show();
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

                String id_usuario = mipreferencia_user.getString("id_usuario","");

                parametros.put("id_usuario",id_usuario);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }


    private void guardarChisteFavorito(final int id_chiste, final String id_usuario, final int id_boton_favorito_normal, final int id_boton_favorito_rojo, String url){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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

                        Modals nuevaModal = new Modals("Mensaje", error, "OK", MainActivity.this);
                        nuevaModal.createModal();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "cayo en el catch", Toast.LENGTH_LONG).show();
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

    private void eliminarChisteFavorito(final int id_chiste, final String id_usuario, final int id_boton_favorito_normal, final int id_boton_favorito_rojo, String url)
    {

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

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

                        Modals nuevaModal = new Modals("Mensaje", error, "OK", MainActivity.this);
                        nuevaModal.createModal();

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "cayo en el catch", Toast.LENGTH_LONG).show();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ocultarAlertaEspera();
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

            //Log.d(MainActivity.class.getSimpleName(),"Scroll View top reached");

        }
        else if(bottomDetector <= 15 ) {

            if(masChistes) {

                    x = x + 1;
                    String c = String.valueOf(x);

                    if (c.equals("1")) {

                        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
                        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
                        int a = Integer.parseInt(index_interstitalAd);

                        if(a>=8){

                            incrementarIdInterstitial("whatsApp");
                            showAdIntertiWhatsOrScroll = "Scroll";
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                        }
                        else{
                            mostrarAlertaCargando();
                            obtenerChistes("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/obtener_chistes.php", "1");
                            incrementarIdInterstitial("otro");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    ocultarAlertaEspera();
                                }
                            }, 1000);

                        }
                    } else {
                        //Toast.makeText(getBaseContext(),"has llegado hasta abajo pero cayo en el else"+c,Toast.LENGTH_SHORT).show();
                    }
            }

            //Log.d(MainActivity.class.getSimpleName(),"Scroll View bottom reached");
        }
        else {
            x=0;
            //Toast.makeText(getBaseContext(),"en medio",Toast.LENGTH_SHORT).show();
        }
    }

    private Uri saveImageExternal(Bitmap image) {
        //TODO - Should be processed in another thread

        Uri uri = null;
        try {
            File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "chiste.png");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.PNG, 90, stream);
            stream.close();
            uri = Uri.fromFile(file);
        } catch (IOException e) {
            //Log.d(TAG, "IOException while trying to write file for sharing: " + e.getMessage());
            Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
        }
        return uri;
    }

    public void incrementarIdInterstitial(String accion){

        pref_Index_InterstitialAd = getSharedPreferences("indexPublicidad", Context.MODE_PRIVATE);
        String index_interstitalAd = pref_Index_InterstitialAd.getString("index_interstitalAd","");
        int a = Integer.parseInt(index_interstitalAd);

        if(accion.equals("otro"))  // cuando se pulse un boton DIFERENTE a Whastapp
        {
            if( a <= 7 ){
                count_interstitalAd = Integer.parseInt(index_interstitalAd) + 1;
                SharedPreferences.Editor obj_editor3  = pref_Index_InterstitialAd.edit();
                obj_editor3.putString("index_interstitalAd", String.valueOf(count_interstitalAd));
                obj_editor3.commit();
            }
        }
        else if(accion.equals("whatsApp") || accion.equals("activity")){
            if(a>=8){
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
       // Toast.makeText(getApplicationContext(),index_interstitalAd,Toast.LENGTH_SHORT).show();
    }

}