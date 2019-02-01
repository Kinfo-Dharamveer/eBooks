package com.kinfoitsolutions.ebooks.ui;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.widget.SeekBar;
import com.google.android.material.snackbar.Snackbar;

public class Utils {


    public static ProgressDialog showProgressDialog(Context context, String message) {
        ProgressDialog m_Dialog = new ProgressDialog(context);
        m_Dialog.setMessage(message);
        m_Dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        m_Dialog.setCancelable(false);
        m_Dialog.show();
        return m_Dialog;
    }


    public static void showSnackBar(Context context, String message, View view){
        Snackbar snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
        snackbar.show();

    }


}
