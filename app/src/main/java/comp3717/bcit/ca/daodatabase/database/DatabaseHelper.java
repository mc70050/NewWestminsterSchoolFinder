package comp3717.bcit.ca.daodatabase.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.greenrobot.greendao.database.Database;

import java.util.List;

import comp3717.bcit.ca.daodatabase.database.schema.DaoMaster;
import comp3717.bcit.ca.daodatabase.database.schema.DaoSession;
import comp3717.bcit.ca.daodatabase.database.schema.School;
import comp3717.bcit.ca.daodatabase.database.schema.SchoolDao;


/**
 * Created by darcy on 2016-10-19.
 */
public class DatabaseHelper
{
    private final static String TAG = DatabaseHelper.class.getName();
    private static DatabaseHelper          instance;
    private        SQLiteDatabase          db;
    private        DaoMaster               daoMaster;
    private        DaoSession              daoSession;
    private        SchoolDao               schoolDao;
    private        DaoMaster.DevOpenHelper helper;

    private DatabaseHelper(final Context context)
    {
        openDatabaseForWriting(context);
    }

    public synchronized static DatabaseHelper getInstance(final Context context)
    {
        if(instance == null)
        {
            instance = new DatabaseHelper(context);
        }

        return (instance);
    }

    public static DatabaseHelper getInstance()
    {
        if(instance == null)
        {
            throw new Error();
        }

        return (instance);
    }

    private void openDatabase()
    {
        daoMaster  = new DaoMaster(db);
        daoSession = daoMaster.newSession();
        schoolDao  = daoSession.getSchoolDao();
    }

    public void openDatabaseForWriting(final Context context)
    {
        helper = new DaoMaster.DevOpenHelper(context,
                "names.db",
                null);
        db = helper.getWritableDatabase();
        openDatabase();
    }

    public void openDatabaseForReading(final Context context)
    {
        final DaoMaster.DevOpenHelper helper;

        helper = new DaoMaster.DevOpenHelper(context,
                "names.db",
                null);
        db = helper.getReadableDatabase();
        openDatabase();
    }

    public void close()
    {
        helper.close();
    }

    public School createSchool(final String nm, final double longi, final double lati, final int level)
    {
        final School school;

        school = new School(null,
                nm, longi, lati, level);
        schoolDao.insert(school);

        return (school);
    }

    public School getSchoolFromCursor(final Cursor cursor)
    {
        final School school;

        school = schoolDao.readEntity(cursor,
                0);

        return (school);
    }

    public School getSchoolByObjectId(final long id)
    {
        final List<School> schools;
        final School       school;

        schools = schoolDao.queryBuilder().where(SchoolDao.Properties.Id.eq(id)).limit(1).list();

        if(schools.isEmpty())
        {
            school = null;
        }
        else
        {
            school = schools.get(0);
        }

        return (school);
    }

    public School getSchoolByObjectName(final String name)
    {
        final List<School> schools;
        final School       school;

        schools = schoolDao.queryBuilder().where(SchoolDao.Properties.Name.eq(name)).limit(1).list();

        if(schools.isEmpty())
        {
            school = null;
        }
        else
        {
            school = schools.get(0);
        }

        return (school);
    }

    public List<School> getSchools()
    {
        return (schoolDao.loadAll());
    }

    public Cursor getSchoolsCursor()
    {
        final Cursor cursor;

        String orderBy = SchoolDao.Properties.Level.columnName + " ASC";
        cursor = db.query(schoolDao.getTablename(),
                schoolDao.getAllColumns(),
                null,
                null,
                null,
                null,
                orderBy);

        return (cursor);
    }

    public static void upgrade(final Database db,
                               final int      oldVersion,
                               final int      newVersion)
    {
    }

    public long getNumberOfSchools()
    {
        return (schoolDao.count());
    }

    public void clearDatabase(String TABLE_NAME) {
        String clearDBQuery = "DELETE FROM "+TABLE_NAME;
        db.execSQL(clearDBQuery);
    }
}

