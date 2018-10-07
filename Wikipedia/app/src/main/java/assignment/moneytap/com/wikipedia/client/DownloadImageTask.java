package assignment.moneytap.com.wikipedia.client;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.InputStream;

import assignment.moneytap.com.wikipedia.Util.WikiLog;

public class DownloadImageTask extends AsyncTask<DownloadImageTask.MyIcon, Void, Void> {

    protected Void doInBackground(MyIcon... icons) {
        final MyIcon icon = icons[0];
        String urldisplay = icon.src;
        if(urldisplay == null)
            return null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            final Bitmap bmp = BitmapFactory.decodeStream(in);
            icon.activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    icon.getIcon().setImageBitmap(bmp);
                }
            });
        } catch (Exception e) {
            WikiLog.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static class MyIcon {
        private String src;
        private ImageView icon;
        private Activity activity;

        public MyIcon(String src, ImageView icon, Activity activity) {
            this.src = src;
            this.icon = icon;
            this.activity = activity;
        }

        public String getSrc() {
            return src;
        }

        public ImageView getIcon() {
            return icon;
        }
    }
}
