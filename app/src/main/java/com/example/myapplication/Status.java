package com.example.myapplication;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputEditText;

public class Status extends AppCompatActivity implements View.OnClickListener {

    private int id;
    private Button btnCadastro, btnDeletar;
    private TextInputEditText nome, data, descricao, local, solucao;
    private RadioGroup tipo, status;
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.status);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        bd = new BD(this, "bd", null, 3 );

        btnCadastro = findViewById(R.id.idButton);
        btnCadastro.setOnClickListener(this);

        btnDeletar = findViewById(R.id.idDeletar);

        nome = findViewById(R.id.idTitulo);
        data = findViewById(R.id.idData);
        descricao = findViewById(R.id.idDescricao);
        local = findViewById(R.id.idLocal);
        solucao = findViewById(R.id.idSolucao);
        tipo = findViewById(R.id.idTipo);
        status = findViewById(R.id.idStatus);


        id = getIntent().getIntExtra("id", 0);
        String sTitulo = getIntent().getStringExtra("titulo");
        String sData = getIntent().getStringExtra("data");
        String sDescricao = getIntent().getStringExtra("descricao");
        String sLocal = getIntent().getStringExtra("local");
        String sSolucao = getIntent().getStringExtra("solucao");
        int iTipo = getIntent().getIntExtra("tipo", 1);
        int iStatus = getIntent().getIntExtra("status", 2);

        nome.setText(sTitulo);
        data.setText(sData);
        descricao.setText(sDescricao);
        local.setText(sLocal);
        solucao.setText(sSolucao);

        if(iTipo == 1)
            tipo.check(R.id.infra);
        else
            tipo.check(R.id.ti);

        if(iStatus == 1)
            status.check(R.id.aberto);
        else if(iStatus == 2)
            status.check(R.id.curso);
        else
            status.check(R.id.fechado);

        EditText etData = findViewById(R.id.idData);

        etData.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            int ano = calendar.get(Calendar.YEAR);
            int mes = calendar.get(Calendar.MONTH);
            int dia = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog picker = new DatePickerDialog(this,
                    (v, year, month, day) -> {
                        etData.setText(year + "/" + (month + 1) + "/" + day);
                    },
                    ano, mes, dia
            );
            picker.show();
        });


        btnDeletar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int idParaApagar = getIntent().getIntExtra("id", -1);
                String tituloParaApagar = getIntent().getStringExtra("titulo");

                if (bd != null && idParaApagar != -1) {
                    bd.deletar(idParaApagar);
                }

                if (tituloParaApagar != null && !tituloParaApagar.isEmpty()) {
                    com.parse.ParseQuery<com.parse.ParseObject> query = com.parse.ParseQuery.getQuery("Chamados");
                    query.whereEqualTo("titulo", tituloParaApagar);
                    query.findInBackground(new com.parse.FindCallback<com.parse.ParseObject>() {
                        @Override
                        public void done(java.util.List<com.parse.ParseObject> objects, com.parse.ParseException e) {
                            if (e == null && objects != null && !objects.isEmpty()) {
                                for (com.parse.ParseObject obj : objects) {
                                    obj.deleteInBackground();
                                }
                            }
                        }
                    });
                }

                android.widget.Toast.makeText(Status.this, "Chamado excluído com sucesso!", android.widget.Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }


    @Override
    public void onClick(View view) {

        int id = getIntent().getIntExtra("id", 0);

        if(view == btnCadastro){
            if(camposVazios(nome, data, descricao, local)){
                Toast.makeText(this, "Preencha todos os campos!!!", Toast.LENGTH_SHORT).show();
            } else {

                int numTipo = (tipo.getCheckedRadioButtonId() == R.id.infra) ? 1 : 2;

                int numStatus = 3;
                int statusSelecionado = status.getCheckedRadioButtonId();
                if(statusSelecionado == R.id.aberto) numStatus = 1;
                else if(statusSelecionado == R.id.curso) numStatus = 2;

                String sNome = nome.getText().toString();
                String sData = data.getText().toString();
                String sDescricao = descricao.getText().toString();
                String sLocal = local.getText().toString();
                String sSolucao = solucao.getText().toString();

                bd.atualizarTitulo(id, sNome);
                bd.atualizarData(id, sData);
                bd.atualizarDescricao(id, sDescricao);
                bd.atualizarLocal(id, sLocal);
                bd.atualizarSolucao(id, sSolucao);
                bd.atualizarTipo(id, numTipo);
                bd.atualizarStatus(id, numStatus);

                Toast.makeText(this, "Chamado alterado com sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            }
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