package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class BD extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME = "bd";

    public BD(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(
                "CREATE TABLE IF NOT EXISTS pessoa (" +
                        "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                        "titulo TEXT,"+
                        "data TEXT,"+
                        "descricao TEXT,"+
                        "local TEXT,"+
                        "solucao TEXT,"+
                        "tipo INTEGER, "+
                        "status INTEGER,"+
                        "foto TEXT" +
                        ")"
        );
        Log.i("##", "Tabela Pessoa Criada");
    }



    public void salvarDados(Pessoa p) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", p.getTitulo());
        valores.put("data", p.getData());
        valores.put("descricao", p.getDescricao());
        valores.put("local", p.getLocal());
        valores.put("tipo", p.getTipo());
        valores.put("status", p.getStatus());
        valores.put("solucao", p.getSolucao());
        valores.put("foto", p.getFoto());
        valores.put("foto", p.getFoto());

        db.insert("pessoa", null, valores);
        db.close();

        Log.i("##", "Dados da Pessoa (Evento/Tarefa) Salvos com Sucesso");
    }
    public ArrayList<Pessoa> getLista() {
        ArrayList<Pessoa> lista = new ArrayList<Pessoa>();
        Cursor cursor = null;

        SQLiteDatabase db = getReadableDatabase();
        cursor = db.query(
                "pessoa", null, null, null,
                null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String lTitulo = cursor.getString(cursor.getColumnIndexOrThrow("titulo"));
                String lData = cursor.getString(cursor.getColumnIndexOrThrow("data"));
                String lDescricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"));
                String lLocal = cursor.getString(cursor.getColumnIndexOrThrow("local"));
                String lSolucao = cursor.getString(cursor.getColumnIndexOrThrow("solucao"));
                int lTipo = cursor.getInt(cursor.getColumnIndexOrThrow("tipo"));
                int lStatus = cursor.getInt(cursor.getColumnIndexOrThrow("status"));
                int lId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                String lFoto = "";
                int fotoIndex = cursor.getColumnIndex("foto");
                if (fotoIndex != -1 && !cursor.isNull(fotoIndex)) {
                    lFoto = cursor.getString(fotoIndex);
                }

                // Cria o objeto passando a foto no final
                Pessoa p = new Pessoa(lSolucao, lTitulo, lData, lDescricao, lLocal, lTipo, lStatus, lId, lFoto);


                lista.add(p);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        for (int i = 0; i < lista.size(); i++) {
            Log.i("##", lista.get(i).getTitulo());
            Log.i("##", lista.get(i).getData());
            Log.i("##", lista.get(i).getDescricao());
            Log.i("##", lista.get(i).getLocal());
            Log.i("##", "Tipo: " + lista.get(i).getTipo());
            Log.i("##", "Status: " + lista.get(i).getStatus());
            Log.i("##", "----------");
        }

        return lista;
    }

    public ArrayList<Pessoa> buscaPorFiltro(String titulo, String dataInicio, String dataFim, int tipo, int status){

        ArrayList<Pessoa> lista = getLista();
        ArrayList<Pessoa> listaFiltrada = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

        for (Pessoa p : lista){

            boolean check = true;

            if (!titulo.isEmpty()){
                if(!p.getTitulo().toLowerCase().startsWith(titulo.toLowerCase()))
                    check = false;
            }

            if(!dataInicio.isEmpty() && !dataFim.isEmpty()){

                try {
                    Date inicio = sdf.parse(dataInicio);
                    Date fim    = sdf.parse(dataFim);
                    Date data   = sdf.parse(p.getData());
                    if (data.before(inicio) || data.after(fim))
                        check = false;
                } catch (Exception e) {
                    Log.i("##", "Erro ao filtrar datas");
                }
            }

            if(tipo != 0){
                if(p.getTipo() != tipo)
                    check = false;
            }

            if(status != 0){
                if(p.getStatus() != status)
                    check = false;
            }

            if (check)
                listaFiltrada.add(p);

        }
        return listaFiltrada;
    }

    /// //////////////////////////////////////////////////////////////////////////////////////////////

    // 1. Atualizar Título
    public void atualizarTitulo(int id, String novoTitulo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("titulo", novoTitulo);
        // Atualiza onde o _id for igual ao id passado no método
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Título Atualizado");
    }

    // 2. Atualizar Data
    public void atualizarData(int id, String novaData){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("data", novaData);
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Data Atualizada");
    }

    // 3. Atualizar Descrição
    public void atualizarDescricao(int id, String novaDescricao){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("descricao", novaDescricao);
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Descrição Atualizada");
    }

    // 4. Atualizar Local
    public void atualizarLocal(int id, String novoLocal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("local", novoLocal);
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Local Atualizado");
    }

    // 5. Atualizar Tipo (Recebe um int)
    public void atualizarTipo(int id, int novoTipo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("tipo", novoTipo);
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Tipo Atualizado");
    }

    // 6. Atualizar Status (Recebe um int)
    public void atualizarStatus(int id, int novoStatus){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("status", novoStatus);
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Status Atualizado");
    }

    public void atualizarSolucao(int id, String novaSolucao){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues valores = new ContentValues();
        valores.put("solucao", novaSolucao);
        db.update("pessoa", valores, "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Local Atualizado");
    }

    public void deletar(int id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete("pessoa", "_id=?", new String[]{String.valueOf(id)});
        db.close();
        Log.i("##", "Registro deletado");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL("ALTER TABLE pessoa ADD COLUMN solucao TEXT");
        }
        if (oldVersion < 3) {
            db.execSQL("ALTER TABLE pessoa ADD COLUMN foto TEXT");
        }
    }
}
