package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ListarFragment : Fragment(R.layout.listar) {

    private lateinit var bd: BD
    private lateinit var adapter: Recycler
    private lateinit var recycler: RecyclerView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bd = BD(requireContext(), "bd", null, 3)
        recycler = view.findViewById(R.id.idRecycler)
        recycler.layoutManager = LinearLayoutManager(requireContext())

        val btnVoltar = view.findViewById<Button>(R.id.idVoltar)
        btnVoltar.visibility = View.GONE
    }

    override fun onResume() {
        super.onResume()
        val lista = bd.lista

        adapter = Recycler(lista) { pessoa ->
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
    }
}