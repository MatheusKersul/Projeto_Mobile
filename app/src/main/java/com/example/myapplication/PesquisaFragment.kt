package com.example.myapplication

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import java.util.ArrayList
import java.util.Calendar

class PesquisaFragment : Fragment(R.layout.pesquisa) {

    private lateinit var bd: BD
    private lateinit var adapter: Recycler
    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bd = BD(requireContext(), "bd", null, 3)
        recycler = view.findViewById(R.id.idRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val titulo = view.findViewById<TextInputEditText>(R.id.pTitulo)
        val dataInicio = view.findViewById<TextInputEditText>(R.id.pInicio)
        val dataFim = view.findViewById<TextInputEditText>(R.id.pFim)
        val tipo = view.findViewById<RadioGroup>(R.id.pTipo)
        val status = view.findViewById<RadioGroup>(R.id.pStatus)
        val btnPesquisar = view.findViewById<Button>(R.id.pProcurar)
        val btnVoltar = view.findViewById<Button>(R.id.pVoltar)

        btnVoltar.visibility = View.GONE

        dataInicio.setOnClickListener { mostrarCalendario(dataInicio) }
        dataFim.setOnClickListener { mostrarCalendario(dataFim) }

        // Inicializa o recycler vazio
        adapter = Recycler(ArrayList()) { pessoa ->
            val i = Intent(requireContext(), Status::class.java)
            i.putExtra("titulo", pessoa.titulo)
            i.putExtra("data", pessoa.data)
            i.putExtra("descricao", pessoa.descricao)
            i.putExtra("local", pessoa.local)
            i.putExtra("tipo", pessoa.tipo)
            i.putExtra("status", pessoa.status)
            i.putExtra("id", pessoa.id)
            i.putExtra("solucao", pessoa.solucao)
            startActivity(i)
        }
        recycler.adapter = adapter

        btnPesquisar.setOnClickListener {
            val sTitulo = titulo.text.toString()
            val sInicio = dataInicio.text.toString()
            val sFim = dataFim.text.toString()

            var numStatus = 0
            when (status.checkedRadioButtonId) {
                R.id.pAberto -> numStatus = 1
                R.id.pCurso -> numStatus = 2
                R.id.pFechado -> numStatus = 3
            }

            var numTipo = 0
            when (tipo.checkedRadioButtonId) {
                R.id.pInfra -> numTipo = 1
                R.id.pTI -> numTipo = 2
            }

            val lista = bd.buscaPorFiltro(sTitulo, sInicio, sFim, numTipo, numStatus)
            adapter.atualizarLista(lista)
        }
    }

    private fun mostrarCalendario(campo: TextInputEditText) {
        val calendar = Calendar.getInstance()
        val picker = DatePickerDialog(
            requireContext(),
            { _, year, month, day -> campo.setText("$year/${month + 1}/$day") },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        picker.show()
    }
}