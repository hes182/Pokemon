package com.example.pokemon.DB

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteException
import android.database.sqlite.SQLiteOpenHelper

class SQLHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val TABLE_NAME = "responjson"
    val COLM_RESPONSE = "response"

    val CREATE_TABLE_RESPONSE = "CREATE TABLE $TABLE_NAME($COLM_RESPONSE TEXT)"

    companion object {
        const val DATABASE_NAME = "pokemon.db"
        const val DATABASE_VERSION = 1
        }

    override fun onCreate(db: SQLiteDatabase?) {
        try {
            db?.execSQL(CREATE_TABLE_RESPONSE)
        }catch (e: SQLiteException) {
            e.printStackTrace()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        super.onDowngrade(db, oldVersion, newVersion)
    }
}