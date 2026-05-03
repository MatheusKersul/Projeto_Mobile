package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import android.widget.Button;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btnCadastro, btnListar, btnStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        btnCadastro = findViewById(R.id.chamado);
        btnListar = findViewById(R.id.listar);
        btnStatus = findViewById(R.id.idPesquisa);

        btnCadastro.setOnClickListener(this);
        btnListar.setOnClickListener(this);
        btnStatus.setOnClickListener(this);

    }
    @Override
    public void onClick(View view){

        if (view == btnCadastro){

            Intent i = new Intent(this, Cadastro.class);
            startActivity(i);
        }

        else if(view == btnListar){

            Intent i = new Intent(this, Listar.class);
            startActivity(i);
        }

        else if(view == btnStatus){

            Intent i = new Intent(this, Pesquisa.class);
            startActivity(i);
        }

    }
}