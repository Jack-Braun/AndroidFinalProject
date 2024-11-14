package com.example.finalproject.data

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.content.UriMatcher
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.net.Uri

class PetAppContentProvider : ContentProvider() {
    companion object {
        // defining authority so that other application can access it
        const val PROVIDER_NAME = "com.example.finalproject"

        // defining content URI
        const val URL = "content://$PROVIDER_NAME/users"

        // parsing the content URI
        val CONTENT_URI = Uri.parse(URL)
        const val id = "id"
        const val name = "name"
        const val uriCodeUsers = 1
        const val uriCodeUserId = 2
        var uriMatcher: UriMatcher? = null
        private val values: HashMap<String, String>? = null

        // declaring name of the database
        const val DATABASE_NAME = "PetAppDB"

        // declaring version of the database
        const val DATABASE_VERSION = 1

        // declaring table name of the database
        const val TABLE_NAME = "Users"

        // Table for UserProfile
        const val CREATE_USER_PROFILE_TABLE = """
    CREATE TABLE $TABLE_NAME (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        username TEXT NOT NULL UNIQUE,
        name TEXT NOT NULL,
        password TEXT NOT NULL,
        bio TEXT
    );
"""

        // Table for Pets
        object PetTable {
            const val TABLE_NAME = "Pets"
        }

        const val CREATE_PET_TABLE = """
    CREATE TABLE ${PetTable.TABLE_NAME} (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        age INTEGER NOT NULL,
        animal TEXT NOT NULL,
        colour TEXT NOT NULL,
        breed TEXT NOT NULL,
        owner TEXT NOT NULL,
        FOREIGN KEY (owner) REFERENCES $TABLE_NAME(username)
    );
"""

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(PROVIDER_NAME, "users", uriCodeUsers)
            uriMatcher!!.addURI(PROVIDER_NAME, "users/*", uriCodeUserId)
        }
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher!!.match(uri)) {
            uriCodeUsers -> "vnd.android.cursor.dir/users"
            uriCodeUserId -> "vnd.android.cursor.item/user"
            else -> throw IllegalArgumentException("Unsupported URI: $uri")
        }
    }

    // creating the database
    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return db != null
    }

    override fun query(uri: Uri, projection: Array<String>?, selection: String?,
                       selectionArgs: Array<String>?, sortOrder: String?): Cursor? {
        val qb = SQLiteQueryBuilder()
        when (uriMatcher!!.match(uri)) {
            uriCodeUsers -> qb.tables = TABLE_NAME
            uriCodeUserId -> {
                qb.tables = TABLE_NAME
                qb.appendWhere("username = ?") // For a specific user
            }
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }

        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    // adding data to the database
    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowID = db!!.insert(TABLE_NAME, "", values)
        if (rowID > 0) {
            val _uri =
                ContentUris.withAppendedId(CONTENT_URI, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLiteException("Failed to add a record into $uri")
    }

    override fun update(uri: Uri, values: ContentValues?, selection: String?,
                        selectionArgs: Array<String>?): Int {
        val count = when (uriMatcher!!.match(uri)) {
            uriCodeUsers -> db!!.update(TABLE_NAME, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun delete(uri: Uri, selection: String?, selectionArgs: Array<String>?): Int {
        val count = when (uriMatcher!!.match(uri)) {
            uriCodeUsers -> db!!.delete(TABLE_NAME, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    // creating object of database
    // to perform query
    private var db: SQLiteDatabase? = null

    // creating a database
    private class DatabaseHelper(context: Context?) : SQLiteOpenHelper(
        context, DATABASE_NAME, null, DATABASE_VERSION
    ) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_USER_PROFILE_TABLE)
            db.execSQL(CREATE_PET_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS ${PetTable.TABLE_NAME}")
            onCreate(db)
        }
    }
}