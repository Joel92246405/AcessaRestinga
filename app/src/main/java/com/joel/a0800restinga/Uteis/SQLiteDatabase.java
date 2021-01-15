package com.joel.a0800restinga.Uteis;

import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

public class SQLiteDatabase extends SQLiteOpenHelper {


    private static final String NOME_BANCO = "database.db";
    public static final String TABELA = "carrossel";
    public static final String ID = "id";
    public static final String DESCRICAO = "descricao";
    public static final String LINK = "link ";
    public static final String TELEFONE = "telefone";
    private static final int VERSAO = 1;

/*
*
* */

    public SQLiteDatabase(@Nullable Context context, @Nullable String name, @Nullable android.database.sqlite.SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public SQLiteDatabase(@Nullable Context context) {
        super(context, NOME_BANCO, null, VERSAO);
    }

    @Override
    public void onCreate(android.database.sqlite.SQLiteDatabase db) {
        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + DESCRICAO + " text,"
                + LINK + " text,"
                + TELEFONE + " text"
                +")";



        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(android.database.sqlite.SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }
}
