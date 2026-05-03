package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.widget.AdapterView.OnItemClickListener;

import java.util.ArrayList;


public class Listar extends AppCompatActivity  implements View.OnClickListener{

    private BD bd;
    private Button home;

    private Recycler adapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.listar);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bd = new BD(this, "bd", null, 2);
        recycler  = findViewById(R.id.idRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        home = findViewById(R.id.idVoltar);
        home.setOnClickListener(this);
    }

    @Override
    public void onResume(){
        super.onResume();

        ArrayList<Pessoa> lista = bd.getLista();

        adapter = new Recycler(lista, new Recycler.OnItemClickListener() {
            @Override
            public void onItemClick(Pessoa pessoa) {
                Intent i = new Intent(Listar.this, Status.class);
                i.putExtra("titulo",    pessoa.getTitulo());
                i.putExtra("data",      pessoa.getData());
                i.putExtra("descricao", pessoa.getDescricao());
                i.putExtra("local",     pessoa.getLocal());
                i.putExtra("tipo",      pessoa.getTipo());
                i.putExtra("status",    pessoa.getStatus());
                i.putExtra("id", pessoa.getId());
                i.putExtra("solucao", pessoa.getSolucao());
                startActivity(i);
            }
        });


        recycler.setAdapter(adapter);
    }
    @Override
    public void onClick(View view){


        if(view == home){

            Intent i = new Intent(this, MainActivity.class);
            startActivity(i);
        }
    }


}



