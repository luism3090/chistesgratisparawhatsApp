package com.example.chistesgratisparawhastapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

public class Modals
{
    public String title;
    public String msgBody;
    public String msgPositiveButton;
    public Activity someActivity;


    public Modals(String title, String msgBody, String msgPositiveButton, Activity someActivity) {
        this.title = title;
        this.msgBody = msgBody;
        this.msgPositiveButton = msgPositiveButton;
        this.someActivity = someActivity;
    }


    public void createModal()
    {

        AlertDialog.Builder builder =
                new AlertDialog.Builder(someActivity);

        builder.setMessage(msgBody)
                .setTitle(title)
                .setPositiveButton(msgPositiveButton, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {

                    }
                });

        builder.create();
        builder.show();


    }

}