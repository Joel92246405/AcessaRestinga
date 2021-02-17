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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.a0800restinga.Activities.ProdutosActivity;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.R;

import java.util.List;

public class RecyclerAdapter_FinalizaPedido extends RecyclerView.Adapter<RecyclerAdapter_FinalizaPedido.ViewHolder>{

    private List<String> Produtos;
    private List<String> Quantidades;
    private List<String> Valor;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private Context ctx;
    private AlertDialog alerta;
    private static ItemClickListener itemClickListener;

    public RecyclerAdapter_FinalizaPedido(Context context, List<String> produtos, List<String> qtd, List<String> valorunit) {

        this.mInflater = LayoutInflater.from(context);
        this.Produtos = produtos;
        this.Valor = valorunit;
        this.Quantidades = qtd;

        this.ctx = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_produtos_pedido, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        holder.Prod.setText(Produtos.get(position));
        holder.valor.setText(Valor.get(position));

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Qtd = Integer.parseInt(Quantidades.get(position));
                Qtd ++;
                Quantidades.set(position, String.valueOf(Qtd));
                holder.Qtd.setText(Quantidades.get(position));
            }
        });

        holder.decr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int Qtd = Integer.parseInt(Quantidades.get(position));

                if (! (Qtd <= 0)){
                    Qtd --;
                    Quantidades.set(position, String.valueOf(Qtd));
                    holder.Qtd.setText(Quantidades.get(position));
                }
            }
        });

        holder.verFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Ver Foto", Toast.LENGTH_LONG).show();
            }
        });

    }

    public String finaliza(){
        String Texto = "";
        int position = 0;
        for(String produtos : Quantidades)
        {
            int Quantidade = Integer.parseInt(produtos);
            if (Quantidade > 0){
                int Qtd = Integer.parseInt(produtos);
                float Valor = Float.parseFloat(this.Valor.get(position));
                float total = Qtd * Valor;
                Texto += Produtos.get(position) + " " + Quantidades.get(position) + " - " + total +"Ñ";
            }

            position ++;
        }


        return Texto;
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return Produtos.size();
    }

    public void setOnItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }

    public interface ItemClickListener {

        void onItemClick(int position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView Prod;
        TextView Qtd;
        ImageView plus;
        ImageView decr;
        ImageView verFoto;
        TextView valor;

        ViewHolder(View itemView) {
            super(itemView);



            Prod = itemView.findViewById(R.id.nome_prod);
            Qtd = itemView.findViewById(R.id.qtdItem);
            plus = itemView.findViewById(R.id.incrementaItem);
            decr = itemView.findViewById(R.id.decrementaItem);
            verFoto = itemView.findViewById(R.id.verProd);
            valor = itemView.findViewById(R.id.valor_prod);
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
        return Produtos.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }






}