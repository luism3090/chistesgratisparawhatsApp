package com.luismolina.chistesgratisparaWhatsAppV2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import com.google.android.gms.ads.rewarded.RewardedAd;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.rockerhieu.emojicon.EmojiconGridFragment;
import com.rockerhieu.emojicon.EmojiconsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import com.rockerhieu.emojicon.EmojiconEditText;
import com.rockerhieu.emojicon.emoji.Emojicon;

public class CrearChisteActivity extends AppCompatActivity implements EmojiconGridFragment.OnEmojiconClickedListener, EmojiconsFragment.OnEmojiconBackspaceClickedListener {

    private RewardedAd rewardedAd;
    private InterstitialAd mInterstitialAd;

    EmojiconEditText edtxCrearChiste;

    boolean showEmoticon = false;
    int totalChistesPorDia;
    int cant_chistes_dia;

    SharedPreferences pref_config_AppChistes;
    String band_crear_chistes_revision;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_chiste);

        ExtendedFloatingActionButton fabEnviarChistes = (ExtendedFloatingActionButton)findViewById(R.id.fabEnviarChistes);
        final ExtendedFloatingActionButton fabEmoticones = (ExtendedFloatingActionButton)findViewById(R.id.fabEmoticones);
        final EditText edtCrearChiste = (EditText)findViewById(R.id.edtCrearChiste);
        edtxCrearChiste = (EmojiconEditText)findViewById(R.id.edtCrearChiste);
        final FrameLayout emojicons = (FrameLayout)findViewById(R.id.emojicons);

        edtCrearChiste.requestFocus();
        crearAnuncios();
        reCargarInterstitialAd();

        edtxCrearChiste.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        setEmojiconFragment(false);

        fabEmoticones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                if(showEmoticon==false){
                    imm.hideSoftInputFromWindow(edtxCrearChiste.getWindowToken(),0);
                    fabEmoticones.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.emoji_2328));
                    emojicons.setVisibility(View.VISIBLE);
                    showEmoticon = true;
                }
                else{
                    emojicons.setVisibility(View.INVISIBLE);
                    imm.showSoftInput(edtxCrearChiste,InputMethodManager.SHOW_FORCED);
                    fabEmoticones.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_emoticon_risa));
                    showEmoticon = false;

                }

            }
        });

        fabEnviarChistes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // este metodo tambien traerá la bandera si es que se pasan los chistes a revision antes de publicarse
                ObtenerTotalChistesPorDia("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/obtener_total_chistes_por_dia.php");


            }
        });



    }

    public void crearAnuncios(){

        // ---------- creando Anuncio Interstitial ---------------

        mInterstitialAd = new InterstitialAd(this);
        //mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712"); // ID DE PRUEBA
        mInterstitialAd.setAdUnitId("ca-app-pub-7642244438296434/7732541519"); //ID EL BUENO
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



    }


    public void reCargarInterstitialAd(){

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                if(band_crear_chistes_revision.equals("0")){

                    Intent inicio = new Intent(getApplicationContext(), MainActivity.class);

                    startActivity(inicio);
                }

                mInterstitialAd.loadAd(new AdRequest.Builder().build());


            }
        });

    }


    public void enviarChiste(String url, final String chiste){

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                //ocultarAlertaEspera();
                try {

                    JSONObject responseJSON = new JSONObject(response);

                    //String message_id = "234";
                    String message_id = responseJSON.getString("message_id");

                    //Toast.makeText(getApplicationContext(), message_id, Toast.LENGTH_LONG).show();

                    //String mensaje = responseJSON.getString("mensaje");
                    //String error = responseJSON.getString("error");
                    //String resultado = responseJSON.getString("resultado");

                    if (message_id.equals("")) {
                        Toast.makeText(getApplicationContext(), "Ocurrió un problema a la hora de enviar el chiste", Toast.LENGTH_LONG).show();
                    }else{

                        //Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                        if(band_crear_chistes_revision.equals("1")){

                            AlertDialog.Builder builder = new AlertDialog.Builder(CrearChisteActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<center><h4>Mensaje</h4></center>"));
                            builder.setMessage(Html.fromHtml("<br><h5>El chiste fue enviado a revisión y si es aceptado se publicará dentro de poco tiempo</h5>"));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    }
                                    dialog.cancel();
                                }
                            });
                            builder.show();

                            //Toast.makeText(getApplicationContext(), "El chiste fue enviado a revisión y si es aceptado se publicará dentro de poco tiempo", Toast.LENGTH_LONG).show();

                        }
                        else{
                            AlertDialog.Builder builder = new AlertDialog.Builder(CrearChisteActivity.this);
                            builder.setCancelable(false);
                            builder.setTitle(Html.fromHtml("<center><h4>Mensaje</h4></center>"));
                            builder.setMessage(Html.fromHtml("<br><h5>Tu chiste ha sido publicado</h5>"));
                            builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (mInterstitialAd.isLoaded()) {
                                        mInterstitialAd.show();
                                    }
                                    dialog.cancel();
                                }
                            });
                            builder.show();
                            //Toast.makeText(getApplicationContext(), "El chiste fue publicado correctamente", Toast.LENGTH_LONG).show();
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Ocurrió un problema a la hora de enviar el chiste", Toast.LENGTH_LONG).show();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(getApplicationContext(), error.toString(), Toast.LENGTH_LONG).show();
                Toast.makeText(getApplicationContext(), "Error al conectarse a internet, por favor intente de nuevo más tarde", Toast.LENGTH_LONG).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();

                //String id_usuario = mipreferencia_user.getString("id_usuario","");

                parametros.put("chiste",chiste);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    @Override
    public void onEmojiconClicked(Emojicon emojicon) {
        EmojiconsFragment.input(edtxCrearChiste,emojicon);
    }

    @Override
    public void onEmojiconBackspaceClicked(View v) {
        EmojiconsFragment.backspace(edtxCrearChiste);
    }

    public void setEmojiconFragment(boolean useSystemDefault){
        getSupportFragmentManager().beginTransaction().replace(R.id.emojicons,EmojiconsFragment.newInstance(useSystemDefault)).commit();
    }


    @Override
    public void onBackPressed() {

        FrameLayout emojicons = (FrameLayout)findViewById(R.id.emojicons);
        ExtendedFloatingActionButton fabEmoticones = (ExtendedFloatingActionButton)findViewById(R.id.fabEmoticones);

       if(showEmoticon){
            emojicons.setVisibility(View.INVISIBLE);
           fabEmoticones.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_emoticon_risa));
           showEmoticon = false;
        }
       else{
           super.onBackPressed();
       }


    }

    public void ObtenerTotalChistesPorDia(String url){

        com.android.volley.toolbox.StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                //Toast.makeText(getApplicationContext(), response, Toast.LENGTH_LONG).show();

                final EditText edtCrearChiste = (EditText)findViewById(R.id.edtCrearChiste);

                //ocultarAlertaEspera();
                try {

                    JSONObject responseJSON = new JSONObject(response);

                    //String message_id = "234";
                    String mensaje = responseJSON.getString("mensaje");

                    JSONArray array = responseJSON.getJSONArray("mensaje");
                    JSONObject arrayChistes = array.getJSONObject(0);
                    cant_chistes_dia = Integer.parseInt(arrayChistes.getString("cant_chistes_dia"));
                    totalChistesPorDia = Integer.parseInt(arrayChistes.getString("total_chistes"));
                    band_crear_chistes_revision = arrayChistes.getString("band_crear_chistes_revision");

                    //        5              >     5
                    if(totalChistesPorDia > cant_chistes_dia)
                    {

                        final  String chiste = edtCrearChiste.getText().toString().trim();

                        //Toast.makeText(getApplicationContext(), "Se cargó una nueva intertial ? "+String.valueOf(mInterstitialAd.isLoaded()), Toast.LENGTH_LONG).show();

                        if(chiste.equals("")){
                            //cargarVideoRecompensa();
                        }
                        else {

                            // Toast.makeText(getApplicationContext(), String.valueOf(rewardedAd.isLoaded()), Toast.LENGTH_SHORT).show();

                            //if (rewardedAd.isLoaded()) {

                                AlertDialog.Builder builder = new AlertDialog.Builder(CrearChisteActivity.this);
                                builder.setCancelable(false);
                                builder.setTitle(Html.fromHtml("<center><h4>Mensaje</h4></center>"));
                                builder.setMessage(Html.fromHtml("<br><h5>¿Desea enviar su chiste ahora?</h5>"));
                                builder.setNegativeButton("Enviar chiste", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //mostrarVideoRecompensa(chiste);

                                        edtCrearChiste.setText("");

                                        //if (mInterstitialAd.isLoaded()) {
                                            //mInterstitialAd.show();
                                            enviarChiste("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/crear_chiste_public_new.php",chiste);

                                            dialog.cancel();
                                        //}



                                    }
                                });
                                builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                });
                                builder.show();

                            /*}
                            else{

                                if (mInterstitialAd.isLoaded()) {
                                    mInterstitialAd.show();
                                    //enviarChiste("https://chistesgratis.lmeapps.com/chistesgratiswhatsApp/crear_chiste_public.php",chiste);
                                }

                            }*/

                        }

                    }
                    else{

                        AlertDialog.Builder builder = new AlertDialog.Builder(CrearChisteActivity.this);
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
                    Toast.makeText(getApplicationContext(), "Ocurrió un problema a la hora de obtener los datos", Toast.LENGTH_LONG).show();
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
