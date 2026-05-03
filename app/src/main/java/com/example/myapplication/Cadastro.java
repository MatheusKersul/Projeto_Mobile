package com.example.myapplication;

import android.app.DatePickerDialog;
import android.content.Intent;
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

public class Cadastro extends AppCompatActivity implements View.OnClickListener {


    private Button btnCadastro;
    private TextInputEditText nome, data, descricao, local;
    private RadioGroup tipo, status;
    private BD bd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        bd = new BD(this, "bd", null, 2);
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cadastro);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnCadastro = findViewById(R.id.idButton);
        btnCadastro.setOnClickListener(this);
        nome = findViewById(R.id.idTitulo);
        data = findViewById(R.id.idData);
        descricao = findViewById(R.id.idDescricao);
        local = findViewById(R.id.idLocal);
        tipo = findViewById(R.id.idTipo);
        status = findViewById(R.id.idStatus);


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
    }

    @Override
    public void onClick(View view) {

        if(view == btnCadastro){
            if(camposVazios(nome, data, descricao, local)){

                Toast.makeText(this, "Preencha todos os campos!!!", Toast.LENGTH_SHORT).show();
            }

            else{

                //1 é infra, 2 é ti
                //1 é aberto, 2 é em curso, 3 é fechado
                int numTipo = 0, numStatus = 0;
                int tipoSelecionado = tipo.getCheckedRadioButtonId();
                if(tipoSelecionado == R.id.infra)
                    numTipo = 1;
                else
                    numTipo = 2;

                int statusSelecionado = status.getCheckedRadioButtonId();
                if(statusSelecionado == R.id.aberto)
                    numStatus = 1;
                else if(statusSelecionado == R.id.curso)
                    numStatus = 2;
                else
                    numStatus = 3;

                String sNome = nome.getText().toString();
                String sData = data.getText().toString();
                String sDescricao = descricao.getText().toString();
                String sLocal = local.getText().toString();

                Pessoa pessoa = new Pessoa(sNome, sData, sDescricao, sLocal, numTipo, numStatus);
                bd.salvarDados(pessoa);

                Toast.makeText(this, "Chamado registrado com sucesso!", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);

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