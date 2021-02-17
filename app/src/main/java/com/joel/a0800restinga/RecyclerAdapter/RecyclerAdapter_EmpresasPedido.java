package com.joel.a0800restinga.RecyclerAdapter;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.a0800restinga.Activities.FinalizaPedido;
import com.joel.a0800restinga.Activities.ProdutosActivity;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.R;

import java.util.List;

public class RecyclerAdapter_EmpresasPedido extends RecyclerView.Adapter<RecyclerAdapter_EmpresasPedido.ViewHolder>{

    private List<String> Nome, Telefone;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context ctx;
    private AlertDialog alerta;
    private static ItemClickListener itemClickListener;

    public RecyclerAdapter_EmpresasPedido(Context context, List<String> nome, List<String> telefone) {

        this.mInflater = LayoutInflater.from(context);
        this.Nome = nome;
        this.Telefone = telefone;
        this.ctx = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_empresas, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        holder.nome.setText(Nome.get(position));


        holder.addPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), FinalizaPedido.class);
                String Id = Nome.get(position).replaceAll("\\s+","") + Telefone.get(position).replaceAll("\\s+","");
                intent.putExtra("ChaveDaEmpresa", Id);
                v.getContext().startActivity(intent);

            }
        });
    }



    // total number of rows
    @Override
    public int getItemCount() {
        return Nome.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void onItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nome;
        ImageView addPedido;

        ViewHolder(View itemView) {
            super(itemView);



            nome = itemView.findViewById(R.id.nome_empresa);
            addPedido = itemView.findViewById(R.id.fazer_pedido);
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
        return Nome.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }






}