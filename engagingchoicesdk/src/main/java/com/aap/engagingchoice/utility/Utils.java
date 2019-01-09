package com.aap.engagingchoice.utility;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.UiThread;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.widget.Toast;

import com.aap.engagingchoice.R;

/**
 * Utility class contains common method
 */
public class Utils {
    public static boolean isNetworkAvailable(Context context, boolean isShowMessage) {

        boolean isConnected;

        if (context == null) return false;

        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = cm.getActiveNetworkInfo();

        isConnected = activeNetworkInfo != null && activeNetworkInfo.isConnected();

        if (!isConnected && isShowMessage) {
            showMessageS(context.getString(R.string.no_network), context);
        }

        return isConnected;

    }

    @UiThread
    public static void showMessageS(@NonNull String message, Context context) {
        if (TextUtils.isEmpty(message)) return;
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showDialog(final Activity activity, String msg, final boolean isProfileSuccessfull) {
        AlertDialog alertDialog = new AlertDialog.Builder(new ContextThemeWrapper(activity, R.style.AlertDialogCustom)).create();
        alertDialog.setMessage(msg);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if (isProfileSuccessfull) {
                            activity.finish();
                        }
                    }
                });
        alertDialog.show();
    }

}
