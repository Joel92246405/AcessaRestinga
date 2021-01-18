package com.joel.a0800restinga.Uteis;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;

import java.util.concurrent.ExecutionException;

public class BancoController {
    private android.database.sqlite.SQLiteDatabase db;
    private SQLiteDatabase banco;


    public BancoController(Context context){
        banco = new SQLiteDatabase(context);
    }

    public Cursor carregaDados(){
        Cursor cursor = null;
        try {

            String[] campos = {banco.DESCRICAO, banco.LINK, banco.TELEFONE};
            db = banco.getReadableDatabase();
            cursor = db.query(banco.TABELA, campos, null, null, null, null, null, null);

            if (cursor != null) {
                cursor.moveToFirst();
            }
            db.close();
        }catch (Exception ex){
            Log.d("ExcepDpGet", ex.toString());
        }
        return cursor;
    }

    public String insereDado(String descricao, String link, String telefone){
        ContentValues valores;
        long resultado;
        String result = "";
        try {
            db = banco.getWritableDatabase();
            valores = new ContentValues();
            valores.put(SQLiteDatabase.DESCRICAO, descricao);
            valores.put(SQLiteDatabase.LINK, link);
            valores.put(SQLiteDatabase.TELEFONE, telefone);

            resultado = db.insert(SQLiteDatabase.TABELA, null, valores);
            db.close();

        if (resultado ==-1)
            result=  "Erro ao inserir registro";
        else
            result =  "Registro Inserido com sucesso";

        }catch (Exception ex){
            result = "Erro ao inserir registro";
            Log.d("ExcepDpIns", ex.toString());
        }
        return result;
    }

    public String deletaDados(){
        ContentValues valores;
        long resultado;
        String result = "";
        try {
            db = banco.getWritableDatabase();


            resultado = db.delete(SQLiteDatabase.TABELA, null, null);
            db.close();

            if (resultado == -1)
                result= "Erro ao deletar registro";
            else
                result= "Registro deletado com sucesso";
        }catch (Exception ex){
            result = "Erro ao inserir registro";
            Log.d("ExcepDpDel", ex.toString());
        }
        return result;

    }

}
