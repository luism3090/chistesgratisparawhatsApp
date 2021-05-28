package com.luismolina.chistesgratisparaWhatsAppV2.Services;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.luismolina.chistesgratisparaWhatsAppV2.MainActivity;
import com.luismolina.chistesgratisparaWhatsAppV2.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.luismolina.chistesgratisparaWhatsAppV2.SplashActivity;

import java.util.Random;

public class FireBaseServiceMensajes extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String topic = remoteMessage.getData().get("topic");
        String titulo_notificacion = remoteMessage.getData().get("titulo_notificacion");
        String chiste = remoteMessage.getData().get("chiste");
        String content_notificacion = remoteMessage.getData().get("content_notificacion");
        String id_titulo = remoteMessage.getData().get("id_titulo");

        if(topic.equals("diversion")){
            mostrarNotificacionChiste(chiste,titulo_notificacion);
        }
        else if(topic.equals("informacion")){
            mostrarNotificacionInfo(titulo_notificacion,content_notificacion,id_titulo);
        }

    }

    private void mostrarNotificacionChiste(String chiste, String titulo_notificacion) {

        // Creando un builder para la notificacion
        NotificationCompat.Builder mBuilder;

        // creando un string id del canal
        String channelId = "my_channel_01";

        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this,null);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            //CharSequence name = "humor";    //antes

            CharSequence name = "chistes";

            //String description = "Humor para los usuarios";  //antes

            String description = "chistes para los usuarios";

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel miChannel = new NotificationChannel(channelId,name,importance);

            miChannel.setDescription(description);
            miChannel.enableLights(true);

            miChannel.setLightColor(Color.RED);
            miChannel.enableVibration(true);
            miChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(miChannel);

            mBuilder = new NotificationCompat.Builder(this,channelId);

        }


        Intent intent = new Intent(this, SplashActivity.class);
        PendingIntent pendingIntent;
        Random random1 = new Random();

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
             pendingIntent = PendingIntent.getActivity(this, random1.nextInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        else{
             pendingIntent = PendingIntent.getActivity(this, random1.nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+titulo_notificacion+"</b><br>"+chiste)))
                .setContentText(titulo_notificacion+chiste)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_emoticon_risa)
                .setContentIntent(pendingIntent);

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            mBuilder.setContentTitle("Chistes gratis para whatsApp");
        }

        Random random = new Random();
        notificationManager.notify(random.nextInt(), mBuilder.build());

    }

    private void mostrarNotificacionInfo(String titulo_notificacion, String content_notificacion, String id_titulo) {

        // Creando un builder para la notificacion
        NotificationCompat.Builder mBuilder;

        // creando un string id del canal
        String channelId = "my_channel_02";

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this,null);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = "informacion";

            String description = "informacion para los usuarios";

            int importance = NotificationManager.IMPORTANCE_HIGH;

            NotificationChannel miChannel = new NotificationChannel(channelId,name,importance);

            miChannel.setDescription(description);
            miChannel.enableLights(true);

            miChannel.setLightColor(Color.RED);
            miChannel.enableVibration(true);
            miChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});
            notificationManager.createNotificationChannel(miChannel);

            mBuilder = new NotificationCompat.Builder(this,channelId);

        }

        Intent intent;

        if(id_titulo.equals("1")){ // value del option del select titulo de la notificacion  1 = Nueva actualizacion y abre la google play

            Uri _link = Uri.parse("market://details?id=com.luismolina.chistesgratisparaWhatsAppV2");
             intent = new Intent(Intent.ACTION_VIEW,_link);

        }
        else{ // retorna hacia la misma aplicacion a la  SplashActivity

             intent = new Intent(this, SplashActivity.class);

        }

        PendingIntent pendingIntent;
        Random random1 = new Random();

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            pendingIntent = PendingIntent.getActivity(this, random1.nextInt(), intent, PendingIntent.FLAG_CANCEL_CURRENT);
        }
        else{
            pendingIntent = PendingIntent.getActivity(this, random1.nextInt(), intent, PendingIntent.FLAG_ONE_SHOT);
        }

        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        mBuilder.setAutoCancel(true)
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>"+ titulo_notificacion +"</b><br>"+ content_notificacion)))
                .setContentText(titulo_notificacion+content_notificacion)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_emoticon_risa)
                .setContentIntent(pendingIntent);

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            mBuilder.setContentTitle("Chistes gratis para whatsApp");
        }

        Random random = new Random();
        notificationManager.notify(random.nextInt(), mBuilder.build());

    }


}
