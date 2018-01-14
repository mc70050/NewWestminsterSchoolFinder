package comp3717.bcit.ca.daodatabase.database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;



public class SchoolContentProvider extends ContentProvider {
    private static final UriMatcher uriMatcher;
    private static final int NAMES_URI = 1;
    public static final Uri CONTENT_URI;
    private DatabaseHelper helper;

    static
    {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI("comp3717.bcit.ca.daodatabase.database", "names", NAMES_URI);
    }

    static
    {
        CONTENT_URI = Uri.parse("content://comp3717.bcit.ca.daodatabase.database/names");
    }

    @Override
    public boolean onCreate()
    {
        helper = DatabaseHelper.getInstance(getContext());

        return true;
    }

    @Override
    public Cursor query(final Uri uri,
                        final String[] projection,
                        final String selection,
                        final String[] selectionArgs,
                        final String sortOrder)
    {
        final Cursor cursor;

        switch (uriMatcher.match(uri))
        {
            case NAMES_URI:
            {
                final SQLiteDatabase db;

                helper.openDatabaseForReading(getContext());
                cursor = helper.getSchoolsCursor();
                helper.close();
                break;
            }
            default:
            {
                throw new IllegalArgumentException("Unsupported URI: " + uri);
            }
        }

        return (cursor);
    }

    @Override
    public String getType(final Uri uri)
    {
        final String type;

        switch(uriMatcher.match(uri))
        {
            case NAMES_URI:
                type = "vnd.android.cursor.dir/vnd.comp3717.bcit.ca.daodatabase.names";
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }

        return (type);
    }

    @Override
    public int delete(final Uri uri,
                      final String selection,
                      final String[] selectionArgs)
    {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(final Uri uri,
                      final ContentValues values)
    {
        // TODO: Implement this to handle requests to insert a new row.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int update(final Uri uri,
                      final ContentValues values,
                      final String selection,
                      final String[]      selectionArgs)
    {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
