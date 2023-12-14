package com.example.ordinario

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBHelperUsuarios (context: Context) : SQLiteOpenHelper(context, DB_name, null, DB_version){
    companion object{
        private var DB_version = 1
        private val DB_name = "dbUsuarios.db"
        private val nomTabla = "usuarios"
        private val keyid   = "id"
        private val nom = "nombre"
        private val correo = "correo"
        private val password = "password"
    }
    val sqlCreate: String = "CREATE TABLE $nomTabla ($keyid INTEGER PRIMARY KEY, $nom TEXT, $correo TEXT, $password TEXT)"


    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(sqlCreate)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $nomTabla")
        onCreate(db)
    }

    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }
}