package assignment.moneytap.com.wikipedia.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import assignment.moneytap.com.wikipedia.Util.PageORM;
import assignment.moneytap.com.wikipedia.Util.WikiLog;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseWrapper";

    private static final String DATABASE_NAME = "WikiInfo.db";
    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Called if the database named DATABASE_NAME doesn't exist in order to create it.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        WikiLog.d(TAG, "Creating database [" + DATABASE_NAME + " v." + DATABASE_VERSION + "]...");

        sqLiteDatabase.execSQL(PageORM.SQL_CREATE_TABLE);
    }

    /**
     * Called when the DATABASE_VERSION is increased.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        WikiLog.d(TAG, "Upgrading database ["+DATABASE_NAME+" v." + oldVersion+"] to ["+DATABASE_NAME+" v." + newVersion+"]...");

        sqLiteDatabase.execSQL(PageORM.SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }
}
