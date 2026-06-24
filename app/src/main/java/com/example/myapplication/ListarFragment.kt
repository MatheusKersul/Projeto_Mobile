package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.parse.ParseQuery
import com.parse.ParseObject

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

        // 1. Carrega o que já tem no celular imediatamente
        carregarListaLocal()

        // 2. Vai na nuvem buscar se tem chamados antigos para baixar
        sincronizarComNuvem()
    }

    private fun carregarListaLocal() {
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

    private fun sincronizarComNuvem() {
        val query = ParseQuery.getQuery<ParseObject>("Chamados")

        query.findInBackground { objects, e ->
            if (e == null && objects != null) {
                val listaLocalAtual = bd.lista
                var teveAtualizacao = false

                for (obj in objects) {
                    val tituloNuvem = obj.getString("titulo") ?: ""

                    val jaExiste = listaLocalAtual.any { it.titulo == tituloNuvem }

                    if (!jaExiste) {
                        // Se achou um chamado na nuvem que não está no celular, faz o download e salva!
                        val novaPessoa = Pessoa(
                            "Não solucionado",
                            tituloNuvem,
                            obj.getString("data") ?: "",
                            obj.getString("descricao") ?: "",
                            obj.getString("local") ?: "",
                            obj.getInt("tipo"),
                            obj.getInt("status"),
                            0, // O SQLite gera o ID automaticamente
                            obj.getString("fotoTextual") ?: "" // Puxa a nossa foto em Base64
                        )
                        bd.salvarDados(novaPessoa)
                        teveAtualizacao = true
                    }
                }

                // Se baixou chamados novos, recarrega a tela para eles aparecerem na lista
                if (teveAtualizacao) {
                    carregarListaLocal()
                }
            }
        }
    }
}