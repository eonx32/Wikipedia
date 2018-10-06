package assignment.moneytap.com.wikipedia.Util;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;

import java.util.ArrayList;

import assignment.moneytap.com.wikipedia.data.Page;
import assignment.moneytap.com.wikipedia.data.Response;
import assignment.moneytap.com.wikipedia.db.WikiDBManager;

import static assignment.moneytap.com.wikipedia.Util.WikiConstants.COLUMN;
import static assignment.moneytap.com.wikipedia.Util.WikiConstants.ColumnType;

public class PageORM {

    private static final String TAG = "PageORM";

    private static final String TABLE_NAME = "page";

    private static final String COMMA_SEP = ", ";

    private static final String COLUMN_PAGE_ID_TYPE = "INTEGER PRIMARY KEY";
    private static final String COLUMN_PAGE_ID = "pageid";

    private static final String COLUMN_TITLE_TYPE = "TEXT";
    private static final String COLUMN_TITLE = "title";

    private static final String COLUMN_ICON_TYPE = "TEXT";
    private static final String COLUMN_ICON = "icon";

    private static final String COLUMN_DESCRIPTION_TYPE = "TEXT";
    private static final String COLUMN_DESCRIPTION = "desc";

    private static final String COLUMN_URL_TYPE = "TEXT";
    private static final String COLUMN_URL = "url";

    public static final String SQL_CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + " (" +
                    COLUMN_PAGE_ID + " " + COLUMN_PAGE_ID_TYPE + COMMA_SEP +
                    COLUMN_TITLE + " " + COLUMN_TITLE_TYPE + COMMA_SEP +
                    COLUMN_ICON + " " + COLUMN_ICON_TYPE + COMMA_SEP +
                    COLUMN_DESCRIPTION + " " + COLUMN_DESCRIPTION_TYPE + COMMA_SEP +
                    COLUMN_URL + " " + COLUMN_URL_TYPE +
                    ")";

    public static final String SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS " + TABLE_NAME;

    public static void insertPage(Cursor cursor) {
        ContentValues values = cursorToContentValues(cursor);
        long pageId = WikiDBManager.getInstance().insert(TABLE_NAME, values);
        WikiLog.d(TAG, "Inserted new Page with ID: " + pageId);
    }

    /**
     * Packs a Page object into a ContentValues map for use with SQL inserts.
     */
    private static ContentValues cursorToContentValues(Cursor cursor) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_PAGE_ID, cursor.getInt(ColumnType.PAGEID.ordinal()));
        values.put(COLUMN_TITLE, cursor.getString(ColumnType.TITLE.ordinal()));
        values.put(COLUMN_ICON, cursor.getString(ColumnType.ICON.ordinal()));
        values.put(COLUMN_DESCRIPTION, cursor.getString(ColumnType.DESC.ordinal()));
        values.put(COLUMN_URL, cursor.getString(ColumnType.URL.ordinal()));

        return values;
    }

    public static ArrayList<Page> getPages() {

        Cursor cursor = WikiDBManager.getInstance().fetchAll(TABLE_NAME);

        WikiLog.d(TAG, "Loaded " + cursor.getCount() + " Pages...");
        ArrayList<Page> pages = new ArrayList<>();

        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                Page page = cursorToPage(cursor);
                pages.add(page);
                cursor.moveToNext();
            }
            WikiLog.d(TAG, "Pages loaded successfully.");
        }

        return pages;
    }

    /**
     * Populates a Page object with data from a Cursor
     * @param cursor
     * @return
     */
    private static Page cursorToPage(Cursor cursor) {
        Page page = new Page();
        Page.Thumbnail thumbnail = new Page().new Thumbnail();
        Page.Term term = new Page().new Term();
        ArrayList<String> description = new ArrayList<>();

        page.setPageId(cursor.getInt(cursor.getColumnIndex(COLUMN_PAGE_ID)));
        page.setTitle(cursor.getString(cursor.getColumnIndex(COLUMN_TITLE)));
        page.setUrl(cursor.getString(cursor.getColumnIndex(COLUMN_URL)));

        thumbnail.setSource(cursor.getString(cursor.getColumnIndex(COLUMN_ICON)));

        description.add(cursor.getString(cursor.getColumnIndex(COLUMN_DESCRIPTION)));

        term.setDescription(description);

        page.setThumbnail(thumbnail);
        page.setTerms(term);

        return page;
    }

    public static MatrixCursor getCursor(Response response) {
        MatrixCursor matrixCursor = new MatrixCursor(COLUMN);

        if(!response.isBatchComplete() || response.getQuery() == null)
            return null;

        for(Page page : response.getQuery().getPages()) {

            MatrixCursor.RowBuilder rowBuilder = matrixCursor.newRow();
            rowBuilder.add(WikiConstants.ColumnType.PAGEID.toString(), page.getPageId())
                    .add(WikiConstants.ColumnType.TITLE.toString(), page.getTitle())
                    .add(WikiConstants.ColumnType.ICON.toString(), (page.getThumbnail()!=null)?page.getThumbnail().getSource():null)
                    .add(WikiConstants.ColumnType.DESC.toString(), (page.getTerms()!=null)?page.getTerms().getDescription().get(0):null)
                    .add(WikiConstants.ColumnType.URL.toString(), page.getUrl());
        }

        return matrixCursor;
    }
}
