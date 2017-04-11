package rsu.siriwimon.pakdeeporn.alertbusstop;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * Created by ADMIN on 1/11/2559.
 */

public class MyAlert {

    private Context context;
    private int anInt;
    private  String titString, messString;

    public MyAlert(Context context,
                   int anInt,
                   String titString,
                   String messString) {
        this.context = context;
        this.anInt = anInt;
        this.titString = titString;
        this.messString = messString;
    } // method หลัก

    public boolean confirmChangeStatus() {

        final boolean[] result = {false}; // Not Change


        return result[0];
    }



    public void myDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(false);
        builder.setTitle(titString);
        builder.setMessage(messString);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }   // myDialog
} // Main Class
