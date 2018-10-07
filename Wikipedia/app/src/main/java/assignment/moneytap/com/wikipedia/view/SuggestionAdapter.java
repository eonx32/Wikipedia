package assignment.moneytap.com.wikipedia.view;

import android.app.Activity;
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
import assignment.moneytap.com.wikipedia.client.DownloadImageTask;

public class SuggestionAdapter extends CursorAdapter {

    private Activity activity;

    public SuggestionAdapter(Context context, Cursor c, Activity activity) {
        super(context, c, false);
        this.activity = activity;
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
        new DownloadImageTask().execute(new DownloadImageTask.MyIcon(imageUrl, holder.icon, activity));
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
}
