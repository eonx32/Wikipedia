package assignment.moneytap.com.wikipedia.Util;

import android.widget.Toast;

import assignment.moneytap.com.wikipedia.app.WikiApplication;

public class WikiAppUtil {

    public static void showToast(String msg) {
        Toast.makeText(WikiApplication.getInstance(), msg, Toast.LENGTH_LONG).show();
    }
}
