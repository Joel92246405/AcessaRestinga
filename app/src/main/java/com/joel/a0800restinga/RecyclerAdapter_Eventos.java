package com.joel.a0800restinga;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.R;

import java.util.List;

public class RecyclerAdapter_Eventos extends RecyclerView.Adapter<RecyclerAdapter_Eventos.ViewHolder>{

    private List<String> Titulo, Descricao, Data, Link;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerAdapter_Eventos(Context context, List<String> titulo, List<String> descricao, List<String> data, List<String> link) {
        this.mInflater = LayoutInflater.from(context);
        this.Titulo = titulo;
        this.Descricao = descricao;
        this.Data = data;
        this.Link = link;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_eventos, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String titulo = Titulo.get(position);
        String descricao = Descricao.get(position);
        String data = "Data:" + Data.get(position);
        String link = "Saiba Mais";
        final String SaibaMais = Link.get(position);

        //.setText(Html.fromHtml(getResources().getString(R.string.string_with_links)));

        holder.titulo.setText(titulo);
        holder.descricao.setText(descricao);

        holder.data.setText(data);

        holder.link.setText(Html.fromHtml(link));

        holder.titulo.setTag(position);
        holder.link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(SaibaMais);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                v.getContext().startActivity(intent);
            }
        });
        /*holder.ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String telefone = holder.telefone.getText().toString();
                Uri uri = Uri.parse("tel:"+telefone);
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                view.getContext().startActivity(intent);
                //startActivity(intent);

            }
        });*/

    }

    // total number of rows
    @Override
    public int getItemCount() {
        return Titulo.size();
    }

    private static ItemClickListener itemClickListener;

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void onItemClick(int position);
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView link, data, titulo, descricao;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_eventos);
            descricao = itemView.findViewById(R.id.descritivo_eventos);
            link = itemView.findViewById(R.id.link_evento);
            data = itemView.findViewById(R.id.data_evento);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null)
                mClickListener.onItemClick(getAdapterPosition());
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return Titulo.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }

}