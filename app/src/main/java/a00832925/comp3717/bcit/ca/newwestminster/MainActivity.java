package a00832925.comp3717.bcit.ca.newwestminster;

import android.app.ListActivity;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


import comp3717.bcit.ca.daodatabase.database.DatabaseHelper;
import comp3717.bcit.ca.daodatabase.database.SchoolContentProvider;
import comp3717.bcit.ca.daodatabase.database.schema.School;
import comp3717.bcit.ca.daodatabase.database.schema.SchoolDao;

/**
 * The main page for this application.
 * Contains a drop down menu which contains schools that are in a database.
 * Have two buttons
 * 1. First button will open the Map and pass information of school selected to the Map
 * 2. Second button will open the Map with no information of school and point at New Westminster.
 */
public class MainActivity extends ListActivity {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final DatabaseHelper helper;
        final LoaderManager manager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        helper = DatabaseHelper.getInstance(this);
        // Open databse for reading
        helper.openDatabaseForReading(this);

        Cursor c = helper.getSchoolsCursor();
        String[] rows = new String[]{SchoolDao.Properties.Name.columnName};
        int[] rowViews = new int[]{android.R.id.text1};
        adapter = new SimpleCursorAdapter(this, android.R.layout.simple_list_item_1, c,
                                          rows, rowViews, 0);
        setListAdapter(adapter);
        manager = getLoaderManager();
        manager.initLoader(0, null, new NameLoaderCallbacks());
        init();
    }


    /**
     * Initialize the database with all the school information.
     */
    private void init()
    {
        final DatabaseHelper helper;
        final long           numEntries;


        helper = DatabaseHelper.getInstance(this);
        /*  This line is used while testing, leaving it for future use if needed.
         *  It deletes the database called School.
        helper.clearDatabase("School");
        */
        helper.openDatabaseForWriting(this);
        numEntries = helper.getNumberOfSchools();

        // The following lines inside if statement will only run the first time this app
        // runs. All school data is created.
        if (numEntries == 0) {
            helper.createSchool("Richard McBride Elementary", 49.2265, -122.8995, 1);
            helper.createSchool("Lord Tweedsmuir Elementary School", 49.2057, -122.9426, 1);
            helper.createSchool("Justice Institute of BC", 49.2223, -122.9101, 4);
            helper.createSchool("FW Howay Elementary", 49.2261, -122.9124, 1);
            helper.createSchool("Queen Elizabeth Elementary", 49.1846, -122.9436, 1);
            helper.createSchool("Queensborough Middle", 49.1863, -122.9409, 2);
            helper.createSchool("Connaught Heights Elementary", 49.2026, -122.9548, 1);
            helper.createSchool("Ecole Fraser River Middle", 49.2047, -122.9164, 2);
            helper.createSchool("Douglas College", 49.2036, -122.9127, 4);
            helper.createSchool("Lord Kelvin Elementary", 49.2110, -122.9302, 1);
            helper.createSchool("Glenbrook Middle", 49.220556, -122.911111, 2);
            helper.createSchool("New Westminster Secondary", 49.215658, -122.929, 3);
            helper.createSchool("Herbert Spencer Elementary", 49.2174, -122.9137, 1);
            helper.createSchool("Qayqayt Elementary", 49.2080, -122.9051, 1);
        }
        helper.close();
    }

    private class NameLoaderCallbacks
            implements LoaderManager.LoaderCallbacks<Cursor>
    {
        @Override
        public Loader<Cursor> onCreateLoader(final int    id,
                                             final Bundle args)
        {
            final Uri uri;
            final CursorLoader loader;

            uri    = SchoolContentProvider.CONTENT_URI;
            loader = new CursorLoader(MainActivity.this, uri, null, null, null, null);

            return (loader);
        }

        @Override
        public void onLoadFinished(final Loader<Cursor> loader,
                                   final Cursor         data)
        {
            adapter.swapCursor(data);
        }

        @Override
        public void onLoaderReset(final Loader<Cursor> loader)
        {
            adapter.swapCursor(null);
        }
    }

    @Override
    protected void onListItemClick(ListView list, View view, int position, long id) {
        super.onListItemClick(list, view, position, id);
        Intent intent = new Intent(this, Map.class);
        Cursor cur = (Cursor)list.getItemAtPosition(position);
        if (cur == null) {
            Log.i("null cursor", "is null");
        }
        DatabaseHelper helper = DatabaseHelper.getInstance(this);
        helper.openDatabaseForReading(this);
        School school = helper.getSchoolFromCursor(cur);
        intent.putExtra("longitude", school.getLongitude());
        intent.putExtra("latitude", school.getLatitude());
        intent.putExtra("name", school.getName());
        intent.putExtra("searchSchool", true);
        helper.close();
        startActivity(intent);
    }
}