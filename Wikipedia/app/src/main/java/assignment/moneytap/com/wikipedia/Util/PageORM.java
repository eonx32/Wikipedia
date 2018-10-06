package assignment.moneytap.com.wikipedia.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import assignment.moneytap.com.wikipedia.data.Query;
import assignment.moneytap.com.wikipedia.helper.DatabaseWrapper;

public class PageORM {

    private static final String TAG = "PageORM";

    private static final String TABLE_NAME = "page";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_PAGE_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_PAGE_ID = "pageid";

    private static final String COLUMN_NS_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_NS = "ns";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_INDEX_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_INDEX = "index";

    private static final String COLUMN_THUMBNAIL_SOURCE_TYPE = "TEXT";
    private static final String COLUMN_THUMBNAIL_SOURCE = "source";

    private static final String COLUMN_THUMBNAIL_WIDTH_TYPE = "TEXT";
    private static final String COLUMN_THUMBNAIL_WIDTH = "width";

    private static final String COLUMN_THUMBNAIL_HEIGHT_TYPE = "TEXT";
    private static final String COLUMN_THUMBNAIL_HEIGHT = "height";

    private static final String COLUMN_TERMS_DESCRIPTION_0_TYPE = "TEXT";
    private static final String COLUMN_TERMS_DESCRIPTION_0 = "description";


    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_PAGE_ID + " " + COLUMN_PAGE_ID_TYPE + COMMA_SEP +
                    COLUMN_NS  + " " + COLUMN_NS_TYPE + COMMA_SEP +
                    COLUMN_TITLE + " " + COLUMN_TITLE_TYPE + COMMA_SEP +
                    COLUMN_INDEX + " " + COLUMN_INDEX_TYPE + COMMA_SEP +
                    COLUMN_THUMBNAIL_SOURCE + " " + COLUMN_THUMBNAIL_SOURCE_TYPE + COMMA_SEP +
                    COLUMN_THUMBNAIL_WIDTH + " " + COLUMN_THUMBNAIL_WIDTH_TYPE +
                    COLUMN_THUMBNAIL_HEIGHT + " " + COLUMN_THUMBNAIL_HEIGHT_TYPE +
                    COLUMN_TERMS_DESCRIPTION_0 + " " + COLUMN_TERMS_DESCRIPTION_0_TYPE +
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertPage(Context context, Query.Page page) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getWritableDatabase();

        ContentValues values = pageToContentValues(page);
        long pageId = database.insert(TABLE_NAME, "null", values);
        WikiLog.d(TAG, "Inserted new Page with ID: " + pageId);

        database.close();
    }

    /**
     * Packs a Page object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues pageToContentValues(Query.Page page) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAGE_ID, page.getPageId());
        values.put(COLUMN_NS, page.getNs());
        values.put(COLUMN_TITLE, page.getTitle());
        values.put(COLUMN_INDEX, page.getIndex());
        values.put(COLUMN_THUMBNAIL_SOURCE, page.getThumbnail().getSource());
        values.put(COLUMN_THUMBNAIL_WIDTH, page.getThumbnail().getWidth());
        values.put(COLUMN_THUMBNAIL_HEIGHT, page.getThumbnail().getHeight());
        values.put(COLUMN_TERMS_DESCRIPTION_0, page.getTerms().getDescription().get(0));

        return values;
    }

    public static ArrayList<Query.Page> getPosts(Context context) {
        DatabaseWrapper databaseWrapper = new DatabaseWrapper(context);
        SQLiteDatabase database = databaseWrapper.getReadableDatabase();

        Cursor cursor = database.rawQuery("SELECT * FROM " + PageORM.TABLE_NAME, null);

        WikiLog.d(TAG, "Loaded " + cursor.getCount() + " Posts...");
        ArrayList<Query.Page> postList = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Query.Page page = cursorToPage(cursor);
                postList.add(page);
                cursor.moveToNext();
            }
            WikiLog.d(TAG, "Posts loaded successfully.");
        }

        database.close();

        return postList;
    }

    /**
     * Populates a Post object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Query.Page cursorToPage(Cursor cursor) {
        Query.Page page = new Query().new Page();
        Query.Thumbnail thumbnail = new Query().new Thumbnail();
        Query.Term term = new Query().new Term();
        ArrayList<String> description = new ArrayList<>();

        page.setPageId(cursor.getInt(cursor.getColumnIndex(COLUMN_PAGE_ID)));
        page.setNs(cursor.getInt(cursor.getColumnIndex(COLUMN_NS)));
        page.setIndex(cursor.getInt(cursor.getColumnIndex(COLUMN_INDEX)));
        page.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));

        thumbnail.setSource(cursor.getString(cursor.getColumnIndex(COLUMN_THUMBNAIL_SOURCE)));
        thumbnail.setWidth(cursor.getInt(cursor.getColumnIndex(COLUMN_THUMBNAIL_WIDTH)));
        thumbnail.setHeight(cursor.getInt(cursor.getColumnIndex(COLUMN_THUMBNAIL_HEIGHT)));

        description.add(cursor.getString(cursor.getColumnIndex(COLUMN_TERMS_DESCRIPTION_0)));

        term.setDescription(description);

        page.setThumbnail(thumbnail);
        page.setTerms(term);

        return page;
    }
}
