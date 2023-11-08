package com.quique.agendabinsa;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

/**
 * Created by Quique on 06/11/2023.
 */
public class DataBaseSQLiteHelper extends SQLiteOpenHelper {

    public DataBaseSQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table clients(clientId integer primary key autoincrement, name text, address text, postalCod text, district text)");
        db.execSQL("create table contacts(contactId integer primary key autoincrement, clientId int, name text, phone text, email text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
