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
import android.util.Log

class PetAppContentProvider : ContentProvider() {
    companion object {
        // Defining authority so that other applications can access it
        const val PROVIDER_NAME = "com.example.finalproject"

        // Defining content URI
        const val URL = "content://$PROVIDER_NAME/pets"

        // Parsing the content URI for pets
        val PET_CONTENT_URI = Uri.parse(URL)
        const val id = "id"
        const val name = "name"
        const val uriCodePets = 1
        const val uriCodePetId = 2
        const val uriCodeUsers = 3
        const val uriCodeUserId = 4
        const val uriCodeEvents = 5
        const val uriCodeEventId = 6

        var uriMatcher: UriMatcher? = null

        // Declaring name and version of the database
        const val DATABASE_NAME = "PetAppDB"
        const val DATABASE_VERSION = 1

        // Declaring tables for users and pets
        const val USER_TABLE_NAME = "Users"
        const val PET_TABLE_NAME = "Pets"
        const val EVENT_TABLE_NAME = "Events"

        // Create user profile table
        const val CREATE_USER_PROFILE_TABLE = """
            CREATE TABLE $USER_TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT NOT NULL UNIQUE,
                name TEXT NOT NULL,
                password TEXT NOT NULL,
                bio TEXT
            );
        """

        // Create pet table
        const val CREATE_PET_TABLE = """
            CREATE TABLE $PET_TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                animal TEXT NOT NULL,
                colour TEXT NOT NULL,
                breed TEXT NOT NULL,
                owner TEXT NOT NULL,
                FOREIGN KEY (owner) REFERENCES $USER_TABLE_NAME(username)
            );
        """

        const val CREATE_EVENTS_TABLE = """
    CREATE TABLE $EVENT_TABLE_NAME (
        id INTEGER PRIMARY KEY AUTOINCREMENT,
        name TEXT NOT NULL,
        address TEXT NOT NULL,
        date TEXT NOT NULL,
        attendees TEXT
    );
"""

        init {
            uriMatcher = UriMatcher(UriMatcher.NO_MATCH)
            uriMatcher!!.addURI(PROVIDER_NAME, "pets", uriCodePets)
            uriMatcher!!.addURI(PROVIDER_NAME, "pets/#", uriCodePetId)
            uriMatcher!!.addURI(PROVIDER_NAME, "users", uriCodeUsers)
            uriMatcher!!.addURI(PROVIDER_NAME, "users/*", uriCodeUserId)
            uriMatcher!!.addURI(PROVIDER_NAME, "events", uriCodeEvents)
            uriMatcher!!.addURI(PROVIDER_NAME, "events/#", uriCodeEventId)
        }
    }

    private var db: SQLiteDatabase? = null

    override fun onCreate(): Boolean {
        val context = context
        val dbHelper = DatabaseHelper(context)
        db = dbHelper.writableDatabase
        return db != null
    }

    override fun query(
        uri: Uri, projection: Array<String>?, selection: String?,
        selectionArgs: Array<String>?, sortOrder: String?
    ): Cursor? {
        val qb = SQLiteQueryBuilder()
        when (uriMatcher!!.match(uri)) {
            uriCodePets -> qb.tables = PET_TABLE_NAME
            uriCodePetId -> {
                qb.tables = PET_TABLE_NAME
                qb.appendWhere("id = ?")
            }
            uriCodeUsers -> qb.tables = USER_TABLE_NAME
            uriCodeUserId -> {
                qb.tables = USER_TABLE_NAME
                qb.appendWhere("username = ?")
            }
            uriCodeEvents -> qb.tables = EVENT_TABLE_NAME
            uriCodeEventId -> {
                qb.tables = EVENT_TABLE_NAME
                qb.appendWhere("id = ?")
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        val c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder)
        c.setNotificationUri(context!!.contentResolver, uri)
        return c
    }

    override fun insert(uri: Uri, values: ContentValues?): Uri? {
        val rowID: Long
        when (uriMatcher!!.match(uri)) {
            uriCodePets -> {
                rowID = db!!.insert(PET_TABLE_NAME, "", values)
            }
            uriCodeUsers -> {
                rowID = db!!.insert(USER_TABLE_NAME, "", values)
            }
            uriCodeEvents -> {
                rowID = db!!.insert(EVENT_TABLE_NAME, "", values)
            }
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }

        if (rowID > 0) {
            val _uri = ContentUris.withAppendedId(uri, rowID)
            context!!.contentResolver.notifyChange(_uri, null)
            return _uri
        }
        throw SQLiteException("Failed to add a record into $uri")
    }

    override fun update(
        uri: Uri, values: ContentValues?, selection: String?,
        selectionArgs: Array<String>?
    ): Int {
        val count = when (uriMatcher!!.match(uri)) {
            uriCodePets -> db!!.update(PET_TABLE_NAME, values, selection, selectionArgs)
            uriCodeUsers -> db!!.update(USER_TABLE_NAME, values, selection, selectionArgs)
            uriCodeEvents -> db!!.update(EVENT_TABLE_NAME, values, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun delete(
        uri: Uri, selection: String?, selectionArgs: Array<String>?
    ): Int {
        val count = when (uriMatcher!!.match(uri)) {
            uriCodePets -> db!!.delete(PET_TABLE_NAME, selection, selectionArgs)
            uriCodeUsers -> db!!.delete(USER_TABLE_NAME, selection, selectionArgs)
            uriCodeEvents -> db!!.delete(EVENT_TABLE_NAME, selection, selectionArgs)
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
        context!!.contentResolver.notifyChange(uri, null)
        return count
    }

    override fun getType(uri: Uri): String? {
        return when (uriMatcher!!.match(uri)) {
            uriCodePets -> "vnd.android.cursor.dir/$PROVIDER_NAME.pets"
            uriCodePetId -> "vnd.android.cursor.item/$PROVIDER_NAME.pets"
            uriCodeUsers -> "vnd.android.cursor.dir/$PROVIDER_NAME.users"
            uriCodeUserId -> "vnd.android.cursor.item/$PROVIDER_NAME.users"
            uriCodeEvents -> "vnd.android.cursor.dir/$PROVIDER_NAME.events"
            uriCodeEventId -> "vnd.android.cursor.item/$PROVIDER_NAME.events"
            else -> throw IllegalArgumentException("Unknown URI: $uri")
        }
    }

    // Database helper class
    private class DatabaseHelper(context: Context?) : SQLiteOpenHelper(
        context, DATABASE_NAME, null, DATABASE_VERSION
    ) {
        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(CREATE_USER_PROFILE_TABLE)
            db.execSQL(CREATE_PET_TABLE)
            db.execSQL(CREATE_EVENTS_TABLE)
        }

        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $USER_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $PET_TABLE_NAME")
            db.execSQL("DROP TABLE IF EXISTS $EVENT_TABLE_NAME")
            onCreate(db)
        }
    }
}