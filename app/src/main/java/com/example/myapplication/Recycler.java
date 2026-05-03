package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Recycler extends RecyclerView.Adapter<Recycler.ViewHolder> {

    private ArrayList<Pessoa> lista;
    private OnItemClickListener listener;

    // interface com nome e método corretos
    public interface OnItemClickListener {
        void onItemClick(Pessoa pessoa);
    }

    public Recycler(ArrayList<Pessoa> lista, OnItemClickListener listener) {
        this.lista    = lista;
        this.listener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.itensrecycler, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pessoa p = lista.get(position);

        holder.tvTitulo.setText(p.getTitulo());
        holder.tvData.setText(p.getData());
        holder.tvLocal.setText(p.getLocal());
        holder.tvDesc.setText(p.getDescricao());
        holder.tvSolucao.setText(p.getSolucao());

        switch (p.getStatus()) {
            case 1: holder.tvStatus.setText("Aberto"); break;
            case 2: holder.tvStatus.setText("Em curso"); break;
            case 3: holder.tvStatus.setText("Fechado"); break;
            default: holder.tvStatus.setText("Desconhecido"); break;
        }

        switch (p.getTipo()) {
            case 1: holder.tvTipo.setText("Infraestrutura"); break;
            case 2: holder.tvTipo.setText("TI"); break;
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(p));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvStatus, tvTipo, tvData, tvDesc, tvSolucao, tvLocal;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTipo   = itemView.findViewById(R.id.tvTipo);
            tvData   = itemView.findViewById(R.id.tvData);
            tvDesc   = itemView.findViewById(R.id.idDesc);
            tvSolucao = itemView.findViewById(R.id.tvSolucao);
            tvLocal = itemView.findViewById(R.id.tvLocal);
        }
    }

    public void atualizarLista(ArrayList<Pessoa> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }
}