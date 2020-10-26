package com.joel.a0800restinga;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerAdapter_VcSabia extends RecyclerView.Adapter<RecyclerAdapter_VcSabia.ViewHolder>{

    private List<String> Titulo;
    private List<String> Descricao;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerAdapter_VcSabia(Context context, List<String> titulo, List<String> descricao) {
        this.mInflater = LayoutInflater.from(context);
        this.Titulo = titulo;
        this.Descricao = descricao;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_vcsabia, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String titulo = Titulo.get(position);
        String descricao = Descricao.get(position);

        holder.titulo.setText(titulo);
        holder.descricao.setText(descricao);

        holder.titulo.setTag(position);
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
        TextView titulo;
        TextView descricao;


        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_covid);
            descricao = itemView.findViewById(R.id.descritivo_covid);
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