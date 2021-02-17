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
import com.joel.a0800restinga.Activities.ProdutosActivity;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.Model.MeusProdutosModel;
import com.joel.a0800restinga.R;

import java.util.List;

public class RecyclerAdapter_MinhaEmpresaProdutos extends RecyclerView.Adapter<RecyclerAdapter_MinhaEmpresaProdutos.ViewHolder>{

    private List<String> Nome, Valor;
    private String ChaveEmpresa;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<MeusProdutosModel> meusProdutosModel;
    private Context ctx;
    private AlertDialog alerta;
    private static ItemClickListener itemClickListener;

    public RecyclerAdapter_MinhaEmpresaProdutos(Context context, List<String> nome, List<String> valor, String chaveEmpresa) {

        this.mInflater = LayoutInflater.from(context);
        this.Nome = nome;
        this.Valor = valor;
        this.ctx = context;
        this.ChaveEmpresa = chaveEmpresa;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_minhaempresa_produtos, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {



        holder.nome.setText(Nome.get(position));
        holder.valor.setText(Valor.get(position));

        holder.lixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa_produtos");
                String Id = Nome.get(position).replaceAll("\\s+","") + ChaveEmpresa;
                databaseReference.child(Id).removeValue();
                Toast.makeText(v.getContext(), "Item Excluido", Toast.LENGTH_SHORT).show();
            }
        });

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Editar", Toast.LENGTH_SHORT).show();
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
        TextView valor;
        ImageView editar;
        ImageView lixeira;

        ViewHolder(View itemView) {
            super(itemView);



            nome = itemView.findViewById(R.id.nome_produto);
            valor = itemView.findViewById(R.id.valor_produto);
            editar = itemView.findViewById(R.id.editar_produto);
            lixeira = itemView.findViewById(R.id.excluir_produto);
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