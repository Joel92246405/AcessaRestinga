package com.joel.a0800restinga.RecyclerAdapter;
import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Typeface;
import android.net.Uri;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Activities.ProdutosActivity;
import com.joel.a0800restinga.Activities.Telefones;
import com.joel.a0800restinga.Model.ContatosAgenda;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.Model.TelefonesModel;
import com.joel.a0800restinga.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_MinhaEmpresa extends RecyclerView.Adapter<RecyclerAdapter_MinhaEmpresa.ViewHolder>{

    private List<String> Nome, Telefone;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<EmpresaModel> empresaModel;
    private Context ctx;
    private AlertDialog alerta;
    private static ItemClickListener itemClickListener;

    private FirebaseAuth firebaseAuth;
    private Query myTopPostsQuery;
    private DatabaseReference databaseReference;

    public RecyclerAdapter_MinhaEmpresa(Context context, List<String> nome, List<String> telefone) {

        this.mInflater = LayoutInflater.from(context);
        this.Nome = nome;
        this.Telefone = telefone;
        this.ctx = context;
    }



    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_minhaempresa, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {


        holder.nome.setText(Nome.get(position));
        holder.telefone.setText(Telefone.get(position));

        holder.lixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
                //define o titulo
                builder.setTitle("ATENÇÃO!");
                //define a mensagem
                builder.setMessage(R.string.deseja_excluir);
                //define um botão como positivo
                builder.setPositiveButton("Excluir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        DatabaseReference databaseReference;
                        databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa");
                        String Id = Nome.get(position).replaceAll("\\s+", "") + Telefone.get(position).replaceAll("\\s+", "");
                        databaseReference.child(Id).removeValue();
                    }
                });
                //define um botão como negativo.
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        //Toast.makeText(view.getContext(), "negativo=" + arg1, Toast.LENGTH_SHORT).show();
                    }
                });
                alerta = builder.create();
                alerta.show();


            }
        });

        holder.addProduto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //adcioinar produto a essa empresa
                Intent intent = new Intent(v.getContext(), ProdutosActivity.class);
                String Id = Nome.get(position).replaceAll("\\s+", "") + Telefone.get(position).replaceAll("\\s+", "");
                intent.putExtra("ChaveDaEmpresa", Id);
                v.getContext().startActivity(intent);
            }
        });

        holder.editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Editar", Toast.LENGTH_SHORT).show();


                String Id = Nome.get(position).replaceAll("\\s+","") + Telefone.get(position).replaceAll("\\s+","");

                myTopPostsQuery = FirebaseDatabase.getInstance().getReference("minha_empresa")
                        .equalTo(Id);

                myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {


                        for (DataSnapshot d : snapshot.getChildren()) {
                            EmpresaModel empresaModel = d.getValue(EmpresaModel.class);
                            //NomeEmpresa.add(empresaModel.getNome());
                            //TelefoneEmpresa.add(empresaModel.getTelefone());
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
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
        TextView telefone;
        ImageView editar;
        ImageView lixeira;
        ImageView addProduto;

        ViewHolder(View itemView) {
            super(itemView);



            nome = itemView.findViewById(R.id.nome_empresa);
            telefone = itemView.findViewById(R.id.telefone_empresa);
            editar = itemView.findViewById(R.id.editar_empresa);
            lixeira = itemView.findViewById(R.id.excluir_empresa);
            addProduto = itemView.findViewById(R.id.add_produto_empresa);
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


