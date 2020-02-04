package com.soshi.todo

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class SQLite(context: Context) : SQLiteOpenHelper(context, "TODO_DB", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("CREATE TABLE ITEM_TABLE (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "content TEXT not null, " +
                "is_active INTEGER not null)") //0:Active 1:Completed
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
    }
}