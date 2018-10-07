package assignment.moneytap.com.wikipedia.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;

import java.io.InputStream;
import java.util.ArrayList;

import assignment.moneytap.com.wikipedia.R;
import assignment.moneytap.com.wikipedia.Util.WikiLog;
import assignment.moneytap.com.wikipedia.client.DownloadImageTask;
import assignment.moneytap.com.wikipedia.data.Page;

public class CachedPageAdapter extends RecyclerView.Adapter<CachedPageAdapter.ItemHolder> {

    private ArrayList<Page> pages;
    LayoutInflater inflater;
    OnItemClickListener mItemClickListener;

    public CachedPageAdapter(ArrayList<Page> pages, OnItemClickListener mItemClickListener) {
        this.mItemClickListener = mItemClickListener;
        this.pages = pages;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_page, parent, false);
        return new ItemHolder(v);
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, final int position) {
        final Page page = pages.get(position);
        String imageUrl = page.getThumbnail().getSource();
        new DownloadImageTask().execute(new DownloadImageTask.MyIcon(imageUrl, holder.icon, (Activity) mItemClickListener));
        holder.title.setText(page.getTitle());
        holder.desc.setText(page.getTerms().getDescription().get(0));
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mItemClickListener.onItemClick(position, page);
            }
        });
    }

    @Override
    public int getItemCount() {
        return pages.size();
    }

    public class ItemHolder extends RecyclerView.ViewHolder {

        ConstraintLayout layout;
        ImageView icon;
        TextView title;
        TextView desc;

        ItemHolder(View itemView) {
            super(itemView);
            layout = itemView.findViewById(R.id.root_layout);
            icon = itemView.findViewById(R.id.icon);
            title = itemView.findViewById(R.id.title);
            desc = itemView.findViewById(R.id.desc);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, Page page);
    }
}
