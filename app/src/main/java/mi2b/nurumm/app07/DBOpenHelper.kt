package mi2b.nurumm.app07

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBOpenHelper(context: Context): SQLiteOpenHelper(context,DB_Name,null,DB_Ver) {
    companion object{
        val DB_Name = "mahasiswa"
        val DB_Ver = 1
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val tMhs = "create table mhs(nim text primary key,nama text not null,prodi text not null)"
        db?.execSQL(tMhs)
    }

    override fun onUpgrade(db: SQLiteDatabase?, p1: Int, p2: Int) {

    }
}