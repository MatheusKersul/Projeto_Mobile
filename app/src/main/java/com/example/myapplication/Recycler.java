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
        String fotoBase64 = p.getFoto();

        if (fotoBase64 != null && !fotoBase64.isEmpty()) {
            holder.imgFotoLista.setVisibility(View.VISIBLE);
            byte[] decodedString = android.util.Base64.decode(fotoBase64, android.util.Base64.DEFAULT);
            android.graphics.Bitmap decodedByte = android.graphics.BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            holder.imgFotoLista.setImageBitmap(decodedByte);

            holder.imgFotoLista.setOnClickListener(v -> {
                android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(v.getContext());
                android.widget.ImageView imageView = new android.widget.ImageView(v.getContext());
                imageView.setImageBitmap(decodedByte);
                imageView.setPadding(10, 10, 10, 10);

                builder.setView(imageView);
                builder.setPositiveButton("Fechar", null);
                builder.show();
            });
        } else {
            holder.imgFotoLista.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> listener.onItemClick(p));
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvStatus, tvTipo, tvData, tvDesc, tvSolucao, tvLocal;
        android.widget.ImageView imgFotoLista;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvStatus = itemView.findViewById(R.id.tvStatus);
            tvTipo   = itemView.findViewById(R.id.tvTipo);
            tvData   = itemView.findViewById(R.id.tvData);
            tvDesc   = itemView.findViewById(R.id.idDesc);
            tvSolucao = itemView.findViewById(R.id.tvSolucao);
            tvLocal = itemView.findViewById(R.id.tvLocal);
            imgFotoLista = itemView.findViewById(R.id.imgFotoLista);
        }
    }


    public void atualizarLista(ArrayList<Pessoa> novaLista) {
        this.lista = novaLista;
        notifyDataSetChanged();
    }
}