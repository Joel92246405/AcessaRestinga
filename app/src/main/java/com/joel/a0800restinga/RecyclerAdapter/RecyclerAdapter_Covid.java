package com.joel.a0800restinga.RecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.R;

import java.util.List;

public class RecyclerAdapter_Covid extends RecyclerView.Adapter<RecyclerAdapter_Covid.ViewHolder>{

    private List<String> Titulo, Descricao, Data, InfoAdd;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerAdapter_Covid(Context context, List<String> titulo, List<String> descricao, List<String> data, List<String> infoAdd) {
        this.mInflater = LayoutInflater.from(context);
        this.Titulo = titulo;
        this.Descricao = descricao;
        this.Data = data;
        this.InfoAdd = infoAdd;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_covid, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String titulo = Titulo.get(position);
        String descricao = Descricao.get(position);
        String data = "Link para fonte (Facebook Vigilância Restinga)";
        String infoAdd = InfoAdd.get(position);


        //.setText(Html.fromHtml(getResources().getString(R.string.string_with_links)));

        holder.titulo.setText(titulo);
        holder.descricao.setText(descricao);
        holder.infoAdd.setText(infoAdd);
        holder.data.setText(data);
        final String Link = "https://www.facebook.com/vigilancia.restinga.5";
        holder.data.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri uri = Uri.parse(Link);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                v.getContext().startActivity(intent);
            }
        });

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
        TextView data, titulo, descricao, infoAdd;

        ViewHolder(View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_covid);
            descricao = itemView.findViewById(R.id.descritivo_covid);
            data = itemView.findViewById(R.id.data_covid);
            infoAdd = itemView.findViewById(R.id.infoAdd);

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