package assignment.moneytap.com.wikipedia.view;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.WikiConstants.ColumnType;
import assignment.moneytap.com.wikipedia.Util.WikiLog;

public class SuggestionAdapter extends CursorAdapter {

    public SuggestionAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_page, viewGroup, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        if(view == null)
            return ;

        SuggestionHolder holder = new SuggestionHolder(view);

        String imageUrl = cursor.getString(ColumnType.ICON.ordinal());
        new DownloadImageTask(holder.icon).execute(imageUrl);
        holder.title.setText(cursor.getString(ColumnType.TITLE.ordinal()));
        holder.desc.setText(cursor.getString(ColumnType.DESC.ordinal()));
    }

    static class SuggestionHolder extends RecyclerView.ViewHolder {

        protected ImageView icon;
        protected TextView title;
        protected TextView desc;

        public SuggestionHolder(View itemView) {
            super(itemView);

            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    private static class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {

        private ImageView bmImage;

        DownloadImageTask(ImageView bmImage) {
            this.bmImage = bmImage;
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
            if(result != null)
                bmImage.setImageBitmap(result);
        }
    }
}
