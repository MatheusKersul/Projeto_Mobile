package com.example.myapplication;

import android.app.DatePickerDialog;
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
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Calendar;

public class Pesquisa extends AppCompatActivity  implements View.OnClickListener{

    private BD bd;
    private Button home, pesquisa;

    private TextInputEditText titulo, dataInicio, dataFim;

    private RadioGroup tipo, status;

    private Recycler adapter;
    private RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.pesquisa);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bd = new BD(this, "bd", null, 2);
        recycler  = findViewById(R.id.idRecycler);
        recycler.setLayoutManager(new LinearLayoutManager(this));
        home = findViewById(R.id.pVoltar);
        home.setOnClickListener(this);
        pesquisa = findViewById(R.id.pProcurar);
        pesquisa.setOnClickListener(this);
        titulo = findViewById(R.id.pTitulo);
        dataInicio = findViewById(R.id.pInicio);
        dataFim = findViewById(R.id.pFim);
        tipo = findViewById(R.id.pTipo);
        status = findViewById(R.id.pStatus);

        EditText etDataInicio = findViewById(R.id.pInicio);

        etDataInicio.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(this,
                    (v, year, month, day) -> {
                        etDataInicio.setText(year + "/" + (month + 1) + "/" + day);
                    },
                    ano, mes, dia
            );
            picker.show();
        });

        EditText etDataFim = findViewById(R.id.pFim);

        etDataFim.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(this,
                    (v, year, month, day) -> {
                        etDataFim.setText(year + "/" + (month + 1) + "/" + day);
                    },
                    ano, mes, dia
            );
            picker.show();
        });

    }

    @Override
    public void onResume(){
        super.onResume();


        ArrayList<Pessoa> listaVazia = new ArrayList<>();

        adapter = new Recycler(listaVazia, new Recycler.OnItemClickListener() {
            @Override
            public void onItemClick(Pessoa pessoa) {
                Intent i = new Intent(Pesquisa.this, Status.class);
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
        else if (view == pesquisa){

            String sTitulo = titulo.getText().toString();
            String sInicio = dataInicio.getText().toString();
            String sFim = dataFim.getText().toString();

            int numStatus = 0;
            int statusSelecionado = status.getCheckedRadioButtonId();
            if(statusSelecionado == R.id.pAberto)
                numStatus = 1;
            else if(statusSelecionado == R.id.pCurso)
                numStatus = 2;
            else if(statusSelecionado == R.id.pFechado)
                numStatus = 3;


            int numTipo = 0;
            int tipoSelecionado = tipo.getCheckedRadioButtonId();
            if (tipoSelecionado == R.id.pInfra)
                numTipo = 1;
            else if (tipoSelecionado == R.id.pTI)
                numTipo = 2;





            ArrayList<Pessoa> lista = bd.buscaPorFiltro(sTitulo, sInicio, sFim, numTipo, numStatus);

            Intent i = new Intent(this, Pesquisa.class);

            adapter.atualizarLista(lista);
        }

    }

    private boolean camposVazios(TextInputEditText... campos) {
        for (TextInputEditText campo : campos) {
            if (campo.getText().toString().isEmpty()) {
                return true;
            }
        }
        return false;
    }
}
