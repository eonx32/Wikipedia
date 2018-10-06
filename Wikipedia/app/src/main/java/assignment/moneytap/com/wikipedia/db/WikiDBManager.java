package assignment.moneytap.com.wikipedia.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import assignment.moneytap.com.wikipedia.app.WikiApplication;

public class WikiDBManager {

    private static WikiDBManager sInstance;
    private DatabaseHelper dbHelper;
    private Context mContext;
    private SQLiteDatabase database;

    private WikiDBManager(Context mContext) {
        this.mContext = mContext;
    }

    public static WikiDBManager getInstance () {
        if(sInstance == null)
            sInstance = new WikiDBManager(WikiApplication.getInstance());
        return sInstance;
    }

    public WikiDBManager open() throws SQLException {
        dbHelper = new DatabaseHelper(mContext);
        database = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        dbHelper.close();
    }

    public long insert(String table, ContentValues values) {
        return database.insert(table, "null", values);
    }

    public Cursor fetchAll(String table) {
        return database.rawQuery("SELECT * FROM " + table, null);
    }
}
