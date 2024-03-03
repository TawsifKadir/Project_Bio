
/*
 * Class Name: DbHelper | FI
 * Created by Xplo on 1/4/2016.
 * Copyright (C) 2016 xCode
 * A full Independent Class
 */


package com.xplo.code.core.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {

    private static final String TAG = "DbHelper";

    private String dbName;          //will initialize in constructor
    private SQLiteDatabase db;
    private String dbPath;
    private Context context;

    public DbHelper(Context context, String dbName) {
        super(context, dbName, null, 1);
        this.dbName = dbName;
        dbPath = "/data/data/" + context.getPackageName() + "/databases/";
        this.context = context;

        //Log.d(TAG, "DbHelper: dbPath: " + dbPath);
        //Log.d(TAG, "DbHelper: context.getFilesDir(): " + context.getFilesDir());

    }

    /**
     * Creates a empty database on the system and rewrites it with your own
     * database.
     */
    public void createDataBase() throws IOException {

        boolean dbExist = isDbExist();
        Log.d(TAG, "createDataBase: dbExist: " + dbExist);

        if (dbExist) return; // do nothing - database already exist

        /*
         * By calling this method an empty database will be created
         * into the default system path of your application
         * so we are gonna be able to overwrite that database with our database.
         */
        this.getReadableDatabase();

        try {
            copyDataBase();
        } catch (IOException e) {
            throw new Error("createDataBase: Error copying database");
        }

    }

    /**
     * Check if the database already exist to avoid re-copying the file each
     * time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    public boolean isDbExist() {

        SQLiteDatabase checkDB = null;

        Log.d(TAG, "isDbExist: dbName: " + dbName);

        try {
            String myPath = dbPath + dbName;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            // database doesn't exist yet.
        }

        if (checkDB != null) {
            checkDB.close();
        }

        return checkDB != null;
    }

    /**
     * Copies your database from your local assets-folder to the just created
     * empty database in the system folder, from where it can be accessed and
     * handled. This is done by transferring bytestream.
     */
    private void copyDataBase() throws IOException {

        // Open your local db as the input stream
        InputStream myInput = context.getAssets().open(dbName);

        // Path to the just created empty db
        String outFileName = dbPath + dbName;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // transfer bytes from the input file to the output file
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    /**
     * Method to open or create a database in read-write mode
     *
     * @throws SQLException exception
     */
    public void openDataBase() throws SQLException {

        // Open the database
        //String myPath = dbPath + dbName;
        //db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
//        db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);  // read & write

        db = this.getWritableDatabase();
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.disableWriteAheadLogging();
    }

    @Override
    public synchronized void close() {

        if (db != null)
            db.close();

        super.close();

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        db.setForeignKeyConstraintsEnabled(true);
    }

    /**
     * Method to run sql command
     *
     * @param sql the sql command, execute db.execSQL(sql)
     * @return boolean value
     */
    public boolean runSql(String sql) {


        //String sql = "update " + table + " set bookmark= " + value + " where _id = " + id;
//        db.rawQuery(sql, null);
        try {
            db.execSQL(sql);
            Log.d(TAG, "runSql: successful");
            return true;
        } catch (Exception e) {
            Log.e(TAG, "runSql: failed");
            e.printStackTrace();
            return false;
        }

    }

    /**
     * Convenience method for updating rows in the database.
     *
     * @param table         the table to update in
     * @param cv            a map from column names to new column values. null is a
     *                      valid value that will be translated to NULL.
     * @param selection     the optional WHERE clause to apply when updating.
     *                      Passing null will update all rows.
     * @param selectionArgs You may include ?s in the where clause, which
     *                      will be replaced by the values from whereArgs. The values
     *                      will be bound as Strings.
     * @return the number of rows affected
     */
    public int runUpdate(String table, ContentValues cv, String selection,
                         String[] selectionArgs) {


        //String sql = "update " + table + " set bookmark= " + value + " where _id = " + id;
        //db.rawQuery(sql, null);

        int n = 0;
        try {

            n = db.update(table, cv, selection, selectionArgs);
            Log.d(TAG, "runUpdate: successful");

        } catch (Exception e) {
            Log.e(TAG, "runUpdate: failed");
            e.printStackTrace();

        }

        Log.d(TAG, "runUpdate: count: " + n);
        return n;

    }


    /**
     * Runs the provided SQL and returns a {@link Cursor} over the result set.
     *
     * @param sql           the SQL query. The SQL string must not be ; terminated
     * @param selectionArgs You may include ?s in where clause in the query,
     *                      which will be replaced by the values from selectionArgs. The
     *                      values will be bound as Strings.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     */
    public Cursor runQueryRaw(String sql, String[] selectionArgs) {

        //String sql = "select * from " + table + " where category = ?";
        //String[] selectionArgs = {category};
        Cursor c = db.rawQuery(sql, selectionArgs);
        Log.d(TAG, "runQueryRaw: Count: " + c.getCount());
        return c;

    }

    /**
     * Query the given table, returning a {@link Cursor} over the result set.
     *
     * @param table         The table name to compile the query against.
     * @param columns       A list of which columns to return. Passing null will
     *                      return all columns, which is discouraged to prevent reading
     *                      data from storage that isn't going to be used.
     * @param selection     A filter declaring which rows to return, formatted as an
     *                      SQL WHERE clause (excluding the WHERE itself). Passing null
     *                      will return all rows for the given table.
     * @param selectionArgs You may include ?s in selection, which will be
     *                      replaced by the values from selectionArgs, in order that they
     *                      appear in the selection. The values will be bound as Strings.
     * @param groupBy       A filter declaring how to group rows, formatted as an SQL
     *                      GROUP BY clause (excluding the GROUP BY itself). Passing null
     *                      will cause the rows to not be grouped.
     * @param having        A filter declare which row groups to include in the cursor,
     *                      if row grouping is being used, formatted as an SQL HAVING
     *                      clause (excluding the HAVING itself). Passing null will cause
     *                      all row groups to be included, and is required when row
     *                      grouping is not being used.
     * @param orderBy       How to order the rows, formatted as an SQL ORDER BY clause
     *                      (excluding the ORDER BY itself). Passing null will use the
     *                      default sort order, which may be unordered.
     * @return A {@link Cursor} object, which is positioned before the first entry. Note that
     * {@link Cursor}s are not synchronized, see the documentation for more details.
     * @see Cursor
     */
    public Cursor runQuery(String table, String[] columns, String selection,
                           String[] selectionArgs, String groupBy, String having,
                           String orderBy) {

//        Cursor c = db.query(table, null, selection, selectionArgs, null, null,
//                null);

        Cursor c = db.query(table, columns, selection, selectionArgs, groupBy, having,
                having);

        Log.d(TAG, "runQuery: Count: " + c.getCount());
        return c;

    }


    /**
     * Method to insert a row in a table
     *
     * @param table  table name
     * @param values ContentValues
     * @return returning the primary key value of the new row
     */
    public long insertRow(String table, ContentValues values) {

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(table, null, values);
        //long newRowId = db.insertOrThrow(table, null, values);
        Log.d(TAG, "insertRow: newRowID: " + newRowId);
        return newRowId;
    }


    /**
     * Method for deleting rows in the database.
     *
     * @param table       the table to delete from
     * @param whereClause the optional WHERE clause to apply when deleting.
     *                    Passing null will delete all rows.
     * @param whereArgs   You may include ?s in the where clause, which
     *                    will be replaced by the values from whereArgs. The values
     *                    will be bound as Strings.
     * @return the number of rows affected if a whereClause is passed in, 0
     * otherwise. To remove all rows and get a count pass "1" as the
     * whereClause.
     */
    public int deleteRow(String table, String whereClause, String[] whereArgs) {

        int deletedRow = db.delete(table, whereClause, whereArgs);
        Log.d(TAG, "deleteRow: " + deletedRow);
        return deletedRow;
    }

    /**
     * Method for deleting all rows in a table
     *
     * @param table the table to be cleared
     */
    public void clearTable(String table) {

        //truncet is not work in in sqlite, so use delete command
        db.delete(table, null, null);

    }

    /**
     * Method to drop a table
     *
     * @param table the table have to drop
     */
    public void dropTable(String table) {
        //db = this.getWritableDatabase();
        String sql = "DROP TABLE IF EXISTS " + table + ";";

        try {
            db.execSQL(sql);
            Log.d(TAG, "dropTable: " + table + " Dropped");
        } catch (Exception e) {
            Log.e(TAG, "dropTable: " + table + " Dropping Failed");
        }
    }

    /**
     * Method for counting row in a table
     *
     * @param table the table name
     * @return an int value, total no of row in that table
     */
    public int getRowCount(String table) {

        String sql = "select * from " + table;
        Cursor c = db.rawQuery(sql, null);
        int count = c.getCount();
        c.close();

        Log.d(TAG, "getRowCount: " + count);

        return count;

    }

    /**
     * Method to create metadata
     */
    public void createMetadata() {

        String sql = "CREATE TABLE `android_metadata` (\n" +
                "\t`locale`\tTEXT DEFAULT 'en_US'\n" +
                ");";

        sql = "CREATE TABLE android_metadata (locale TEXT DEFAULT 'en_US')";

        Log.d(TAG, "createMetadata: sql: " + sql);

        try {
            db.execSQL(sql);
            Log.d(TAG, "createMetadata: successful");

        } catch (Exception e) {
            Log.e(TAG, "createMetadata: failed", e);

        }

    }

    /**
     * Method to create a demo table consisting id,title,body,bookmark,category
     * <p>
     * id           int
     * title        text
     * body         text
     * bookmark     int
     * category     text
     *
     * @return boolean
     */
    public boolean createTableDemo() {

        String sql = "CREATE TABLE `demo` (\n" +
                "\t`_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT UNIQUE,\n" +
                "\t`title`\tTEXT,\n" +
                "\t`body`\tTEXT,\n" +
                "\t`bookmark`\tINTEGER,\n" +
                "\t`category`\tTEXT\n" +
                ");";

        Log.d(TAG, "createTableDemo: sql: " + sql);

        try {
            db.execSQL(sql);
            Log.d(TAG, "createTableDemo: successful");
            return true;

        } catch (Exception e) {
            Log.e(TAG, "createTableDemo: failed");
            e.printStackTrace();
            return false;

        }

    }

    /**
     * demo method to insert data
     */
    private void insertDemoItem() {

        ContentValues contentValues = new ContentValues();
        contentValues.put("title", "title");
        contentValues.put("body", "body");
        contentValues.put("bookmark", 1);
        contentValues.put("category", "aaa");

        insertRow("demo", contentValues);
    }


}
