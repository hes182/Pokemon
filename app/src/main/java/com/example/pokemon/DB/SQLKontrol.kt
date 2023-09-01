package com.example.pokemon.DB

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

class SQLKontrol(cntx: Context) {
    val sqlHelper = SQLHelper(cntx)
    lateinit var db: SQLiteDatabase

    fun setSaveResponse(response: String?) {
        db = sqlHelper.writableDatabase
        val query = "Select * from " + sqlHelper.TABLE_NAME
        val cursor = db.rawQuery(query, null)
        if (cursor.count > 0) {
            db.delete(sqlHelper.TABLE_NAME, null, null)
        }
        val valueIns = ContentValues()
        valueIns.put(sqlHelper.COLM_RESPONSE, response)
        db.insert(sqlHelper.TABLE_NAME, null, valueIns)
        db.close()
    }

    @SuppressLint("Range")
    fun getResponse() : String {
        var response = ""
        val query = "Select * from ${sqlHelper.TABLE_NAME}"
        db = sqlHelper.readableDatabase
        val cursor = db.rawQuery(query, null)
        if (cursor.count > 0) {
            cursor.moveToFirst()
            response = cursor.getString(cursor.getColumnIndex(sqlHelper.COLM_RESPONSE))
        }
        cursor.close()
        db.close()
        return response
    }

    fun cekData() : Boolean {
        db = sqlHelper.readableDatabase
        val query = "Select * from ${sqlHelper.TABLE_NAME}"
        val cursor = db.rawQuery(query, null)
        if (cursor.count <= 0) {
            cursor.close()
            db.close()
            return false
        }
        cursor.close()
        db.close()
        return true
    }
}