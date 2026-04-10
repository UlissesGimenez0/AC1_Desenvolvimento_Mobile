package com.example.ac1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BancoHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ac1.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "cadastro_contato";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NOME = "nome";
    private static final String COLUMN_EMAIL = "email";
    private static final String COLUMN_TELEFONE = "telefone";
    private static final String COLUMN_CIDADE = "cidade";
    private static final String COLUMN_FAVORITO = "favorito";
    private static final String COLUMN_CATEGORIA = "categoria";


    public BancoHelper (Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NOME + " TEXT, "
                + COLUMN_EMAIL + " TEXT,"
                + COLUMN_TELEFONE + " TEXT,"
                + COLUMN_CIDADE + " TEXT,"
                + COLUMN_FAVORITO + " TEXT,"
                + COLUMN_CATEGORIA + " TEXT)";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long InserirContato(String nome, String email,String telefone, String cidade, String favorito, String categoria)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_TELEFONE, telefone);
        values.put(COLUMN_CIDADE, cidade);
        values.put(COLUMN_FAVORITO, favorito);
        values.put(COLUMN_CATEGORIA, categoria);
        return db.insert(TABLE_NAME, null, values);
    }
    public Cursor listarContatos()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }

    public int atualizarUsuario(int id, String nome, String email,String telefone, String cidade, String favorito, String categoria)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_NOME, nome);
        values.put(COLUMN_EMAIL, email);
        values.put(COLUMN_TELEFONE, telefone);
        values.put(COLUMN_CIDADE, cidade);
        values.put(COLUMN_FAVORITO, favorito);
        values.put(COLUMN_CATEGORIA, categoria);
        return db.update(TABLE_NAME, values, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
    public int excluirUsuario(int id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_ID + "=?", new String[]{String.valueOf(id)});
    }
}
