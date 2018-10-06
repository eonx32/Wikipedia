package assignment.moneytap.com.wikipedia.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.WikiLog;
import assignment.moneytap.com.wikipedia.data.Page;

public class CachedPageAdapter extends ArrayAdapter<Page> {

    private Context mContext;

    public CachedPageAdapter(@NonNull Context context, ArrayList<Page> pages) {
        super(context, R.layout.item_page, pages);
        this.mContext = context;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View v = view;
        if(v == null) {
            v = LayoutInflater.from(mContext).inflate(R.layout.item_page,
                    viewGroup,
                    false);
        }

        ItemHolder holder = new ItemHolder(v);

        Page page = getItem(i);
        String imageUrl = page.getThumbnail().getSource();
        if(imageUrl != null)
            new DownloadImageTask(holder.icon).execute(imageUrl);
        holder.title.setText(page.getTitle());
        holder.desc.setText(page.getTerms().getDescription().get(0));

        return v;
    }

    static class ItemHolder extends RecyclerView.ViewHolder {

        protected ImageView icon;
        protected TextView title;
        protected TextView desc;

        public ItemHolder(View itemView) {
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
