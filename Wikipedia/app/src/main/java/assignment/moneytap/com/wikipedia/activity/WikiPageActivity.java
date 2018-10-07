package assignment.moneytap.com.wikipedia.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.io.InputStream;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.WikiConstants.ColumnType;
import assignment.moneytap.com.wikipedia.Util.WikiLog;
import assignment.moneytap.com.wikipedia.app.WikiApplication;

public class WikiPageActivity extends AppCompatActivity {

    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wiki_page);

        Intent intent = getIntent();
        String url = intent.getStringExtra(ColumnType.URL.toString());
        String title = intent.getStringExtra(ColumnType.TITLE.toString());
        String icon = intent.getStringExtra(ColumnType.ICON.toString());

        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setLogo(R.drawable.ic_wiki);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayUseLogoEnabled(true);

        if(icon != null)
            new DownloadImageTask(actionBar).execute(icon);

        webView = findViewById(R.id.web_view);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView webView, String url) {
                return false;
            }
        });

        webView.loadUrl(url);
    }

    @Override
    public void onBackPressed() {
        if(webView.canGoBack())
            webView.goBack();
        else
            super.onBackPressed();
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
        ActionBar actionBar;
        DownloadImageTask(ActionBar actionBar) {
            this.actionBar = actionBar;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap bmp = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                bmp = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                WikiLog.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return bmp;
        }
        protected void onPostExecute(Bitmap result) {
            if(result == null)
                return ;
            Bitmap bitmap = getResizedBitmap(result);
            Drawable drawable = new BitmapDrawable(
                    WikiApplication.getInstance().getResources(), bitmap);
            actionBar.setLogo(drawable);
        }

        Bitmap getResizedBitmap(Bitmap bm) {
            int width = bm.getWidth();
            int height = bm.getHeight();
            float scaleWidth = (96f) / width;
            float scaleHeight = (96f) / height;
            // CREATE A MATRIX FOR THE MANIPULATION
            Matrix matrix = new Matrix();
            // RESIZE THE BIT MAP
            matrix.postScale(scaleWidth, scaleHeight);

            // "RECREATE" THE NEW BITMAP
            Bitmap resizedBitmap = Bitmap.createBitmap(
                    bm, 0, 0, width, height, matrix, false);
            bm.recycle();
            return resizedBitmap;
        }
    }
}
