package com.luismolina.chistesgratisparaWhatsAppV2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.widget.EditText;
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
import com.google.android.gms.ads.doubleclick.PublisherAdRequest;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdCallback;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class CrearChisteActivity extends AppCompatActivity {

    private RewardedAd rewardedAd;
    private InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_chiste);

        ExtendedFloatingActionButton fabEnviarChistes = (ExtendedFloatingActionButton)findViewById(R.id.fabEnviarChistes);
        final EditText edtCrearChiste = (EditText)findViewById(R.id.edtCrearChiste);

        crearAnuncios();
        reCargarInterstitialAd();

        fabEnviarChistes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final  String chiste = edtCrearChiste.getText().toString().trim();

                //Toast.makeText(getApplicationContext(), "Se cargó una nueva intertial ? "+String.valueOf(mInterstitialAd.isLoaded()), Toast.LENGTH_LONG).show();

                if(chiste.equals("")){
                    //cargarVideoRecompensa();
                }
                else {

                   // Toast.makeText(getApplicationContext(), String.valueOf(rewardedAd.isLoaded()), Toast.LENGTH_SHORT).show();

                    if (rewardedAd.isLoaded()) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(CrearChisteActivity.this);
                        builder.setTitle(Html.fromHtml("<center><h3>Mensaje</h4></center>"));
                        builder.setMessage(Html.fromHtml("<br><h5>Antes de enviar el chiste debes ver un vídeo</h5><br>"));
                        builder.setNegativeButton("Ver vídeo", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mostrarVideoRecompensa(chiste);
                            }
                        });
                        builder.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                        builder.show();

                    }
                    else{

                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                                enviarChiste("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/crear_chiste_public.php",chiste);
                            }

                    }

                }

            }
        });



    }

    public void crearAnuncios(){

        // ------------------ creando Auncion Bonificado ----------------

        // Prueba -->    ca-app-pub-3940256099942544/5224354917
        // El bueno -->  ca-app-pub-7642244438296434/9902069894

        rewardedAd = new RewardedAd(this, "ca-app-pub-7642244438296434/9902069894");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

        };
        rewardedAd.loadAd(new PublisherAdRequest.Builder().build(), adLoadCallback);

        //Toast.makeText(getApplicationContext(), String.valueOf(rewardedAd.isLoaded()), Toast.LENGTH_LONG).show();


        // ---------- creando Anuncio Interstitial ---------------

        mInterstitialAd = new InterstitialAd(this);
        // ID DE PRUEBA --->  ca-app-pub-3940256099942544/1033173712
        // ID EL BUENO ---> ca-app-pub-7642244438296434/7732541519
        mInterstitialAd.setAdUnitId("ca-app-pub-7642244438296434/7732541519");
        mInterstitialAd.loadAd(new AdRequest.Builder().build());



    }

    public void reCargarVideoRecompensa()
    {

        // Prueba -->    ca-app-pub-3940256099942544/5224354917
        // El bueno -->  ca-app-pub-7642244438296434/9902069894

        rewardedAd = new RewardedAd(this, "ca-app-pub-7642244438296434/9902069894");

        RewardedAdLoadCallback adLoadCallback = new RewardedAdLoadCallback() {
            @Override
            public void onRewardedAdLoaded() {
                // Ad successfully loaded.
            }

        };
        rewardedAd.loadAd(new PublisherAdRequest.Builder().build(), adLoadCallback);
    }

    public void reCargarInterstitialAd(){

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

                mInterstitialAd.loadAd(new AdRequest.Builder().build());

            }
        });

    }


    public void mostrarVideoRecompensa(final String chiste){

        if (rewardedAd.isLoaded()) {
            Activity activityContext = CrearChisteActivity.this;

            RewardedAdCallback adCallback = new RewardedAdCallback() {
                @Override
                public void onRewardedAdOpened() {
                    Toast.makeText(getApplicationContext(),"Termina de ver el vídeo para que se envie tu chiste",Toast.LENGTH_LONG).show();
                }

                @Override
                public void onRewardedAdClosed() {

                    reCargarVideoRecompensa();

                }

                @Override
                public void onUserEarnedReward(@NonNull RewardItem reward) {
                    // se manda la recompensa del chiste

                    enviarChiste("https://practicaproductos.000webhostapp.com/chistesgratiswhatsApp/crear_chiste_public.php",chiste);

                    //Toast.makeText(getApplicationContext(),"Se manda la recompensa",Toast.LENGTH_SHORT).show();
                }

            };
            rewardedAd.show(activityContext, adCallback);
        }
        else{
            //Toast.makeText(getApplicationContext(),"NO se a cargado",Toast.LENGTH_SHORT).show();
        }

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
                    //String mensaje = responseJSON.getString("mensaje");
                    //String error = responseJSON.getString("error");
                    //String resultado = responseJSON.getString("resultado");

                    if (message_id.equals("")) {
                        Toast.makeText(getApplicationContext(), "Ocurrió un error a la hora de enviar el chiste", Toast.LENGTH_LONG).show();
                    }else{

                        //Toast.makeText(getApplicationContext(), resultado, Toast.LENGTH_LONG).show();
                        Toast.makeText(getApplicationContext(), "El chiste fue enviado correctamente", Toast.LENGTH_LONG).show();

                        Intent inicio = new Intent(getApplicationContext(), MainActivity.class);

                        startActivity(inicio);


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

                //String id_usuario = mipreferencia_user.getString("id_usuario","");

                parametros.put("chiste",chiste);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }
}
