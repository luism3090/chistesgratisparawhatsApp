package com.example.chistesgratisparawhastapp.Services;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.text.Html;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.chistesgratisparawhastapp.BusquedaChistesActivity;
import com.example.chistesgratisparawhastapp.MainActivity;
import com.example.chistesgratisparawhastapp.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Random;

public class FireBaseServiceMensajes extends FirebaseMessagingService {
    //private Context context;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        String chiste = remoteMessage.getData().get("chiste");
        mostrarNotificacion(chiste);

    }

    private void mostrarNotificacion(String chiste) {

        // Creando un builder para la notificacion
        NotificationCompat.Builder mBuilder;

        // creando un string id del canal
        String channelId = "my_channel_01";

        //NotificationManager notificationManager = (NotificationManager) context.getSystemService(Activity.NOTIFICATION_SERVICE);


        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBuilder = new NotificationCompat.Builder(this,null);

        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

            CharSequence name = "humor";

            String description = "Humor para los usuarios";
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

        Intent intent = new Intent(this, MainActivity.class);
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
                .setStyle(new NotificationCompat.BigTextStyle().bigText(Html.fromHtml("<b>Nuevo chiste:</b><br>"+chiste)))
                .setContentText("Nuevo chiste "+chiste)
                .setSound(soundUri)
                .setSmallIcon(R.drawable.ic_emoticon_risa)
                .setContentIntent(pendingIntent);

        if(android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            mBuilder.setContentTitle("Chistes gratis parar whatsApp");
        }

        Random random = new Random();
        notificationManager.notify(random.nextInt(), mBuilder.build());

    }
}
