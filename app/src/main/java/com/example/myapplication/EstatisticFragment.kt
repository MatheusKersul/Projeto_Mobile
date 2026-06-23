package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment

class EstatisticFragment : Fragment(R.layout.estatistica) {

    private lateinit var bd: BD

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bd = BD(requireContext(), "bd", null, 3)

        val tvTotal = view.findViewById<TextView>(R.id.tvTotal)
        val tvAbertos = view.findViewById<TextView>(R.id.tvAbertos)
        val tvAndamento = view.findViewById<TextView>(R.id.tvAndamento)
        val tvConcluidos = view.findViewById<TextView>(R.id.tvConcluidos)

        val lista = bd.lista

        val total = lista.size
        val abertos = lista.count { it.status == 1 }
        val andamento = lista.count { it.status == 2 }
        val concluidos = lista.count { it.status == 3 }


        tvTotal.text = total.toString()
        tvAbertos.text = abertos.toString()
        tvAndamento.text = andamento.toString()
        tvConcluidos.text = concluidos.toString()
    }
}