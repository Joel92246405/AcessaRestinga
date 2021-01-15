package com.joel.a0800restinga.Uteis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;

public class BancoController {
    private android.database.sqlite.SQLiteDatabase db;
    private SQLiteDatabase banco;


    public BancoController(Context context){
        banco = new SQLiteDatabase(context);
    }
    public Cursor carregaDados(){
        Cursor cursor;
        String[] campos =  {banco.DESCRICAO, banco.LINK, banco.TELEFONE};
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public String insereDado(String descricao, String link, String telefone){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        valores.put(SQLiteDatabase.DESCRICAO, descricao);
        valores.put(SQLiteDatabase.LINK, link);
        valores.put(SQLiteDatabase.TELEFONE, telefone);

        resultado = db.insert(SQLiteDatabase.TABELA, null, valores);
        db.close();

        if (resultado ==-1)
            return "Erro ao inserir registro";
        else
            return "Registro Inserido com sucesso";


    }

    public String deletaDados(){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();


        resultado = db.delete(SQLiteDatabase.TABELA, null, null);
        db.close();

        if (resultado ==-1)
            return "Erro ao deletar registro";
        else
            return "Registro deletado com sucesso";


    }

}
