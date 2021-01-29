package com.joel.a0800restinga.RecyclerAdapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.a0800restinga.R;

import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import static com.joel.a0800restinga.Activities.Inicial.REQUEST_PERMISSIONS_CODE;

public class RecyclerAdapter_EuAlugo extends RecyclerView.Adapter<RecyclerAdapter_EuAlugo.ViewHolder>{

    private List<String> Nome, Telefone, TipodeContrato, Valor, WhatsApp, Titulo, OcultarValor, Endereco, Categoria;
    private boolean esconderLixeira;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerAdapter_EuAlugo(Context context, List<String> nome, List<String> telefone,  List<String> valor, List<String> whatsApp, List<String> titulo, List<String> ocultarValor, List<String> endereco, List<String> Categoria, boolean esconderLixeira) {
        this.mInflater = LayoutInflater.from(context);
        this.Nome = nome;
        this.Telefone = telefone;
        this.Valor = valor;
        this.WhatsApp = whatsApp;
        this.Titulo = titulo;
        this.OcultarValor = ocultarValor;
        this.Endereco = endereco;
        this.Categoria = Categoria;
        this.esconderLixeira = esconderLixeira;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_eu_alugo, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String Nome, Telefone, TipoDecontrato, Valor, WhatsApp, Titulo, OcultarValor, Endereco, Categoria;

        final String nome = this.Nome.get(position), telefone = this.Telefone.get(position);

        Nome = this.Nome.get(position);
        Telefone = this.Telefone.get(position);

        Valor = this.Valor.get(position);
        WhatsApp = this.WhatsApp.get(position);
        Titulo = this.Titulo.get(position);
        OcultarValor = this.OcultarValor.get(position);
        Endereco = this.Endereco.get(position);


        if (esconderLixeira){
            holder.lixeira.setVisibility(View.INVISIBLE);
        }

        final String maisinfo = "Nome: " + Nome + "\nEndereço: " + Endereco +" \nEntre em contato pela tela anterior";

        holder.Titulo.setText(Titulo);
        holder.Nome.setText(Nome);
        holder.Endereco.setText(Endereco);
        holder.Telefone.setText(Telefone);
        if (OcultarValor.equals("S")){
            holder.Valor.setText("Valor não informado");
        }else
        {
            holder.Valor.setText(Valor);
        }


        holder.lixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference("eu_alugo");
                String Id = nome.replaceAll("\\s+","") + telefone.replaceAll("\\s+","");
                databaseReference.child(Id).removeValue();



            }
        });

        holder.zap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (WhatsApp.equals('N'))
                {
                    Toast.makeText(v.getContext(), "O contato não disponibilizou WhatsApp", Toast.LENGTH_LONG).show();
                }else{
                    String telefone = WhatsApp;
                    try {
                            Intent sendIntent = new Intent("android.intent.action.MAIN");
                            sendIntent.putExtra("jid", "55" + telefone + "@s.whatsapp.net");
                            sendIntent.putExtra(Intent.EXTRA_TEXT, "Olá!");
                            sendIntent.setAction(Intent.ACTION_SEND);
                            sendIntent.setPackage("com.whatsapp");
                            sendIntent.setType("text/plain");
                            v.getContext().startActivity(sendIntent);

                    }catch (Exception ex){
                        Log.d("ZapAluguel", ex.toString());
                        Toast.makeText(v.getContext(), "Não foi possível entrar em contato com esse número", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        holder.ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String telefone = Telefone;
                    Uri uri = Uri.parse("tel:" + telefone);
                    Intent intent = new Intent(Intent.ACTION_DIAL, uri);
                    v.getContext().startActivity(intent);
                }catch (Exception ex) {
                    Log.d("PhoneAluguel", ex.toString());
                    //Toast.makeText(v.getContext(), "Clicou", Toast.LENGTH_LONG).show();
                }
            }
        });

        holder.MaisInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callDialog(
                        v.getContext(),
                        maisinfo);
                        }
            });
    }
    private MaterialDialog mMaterialDialog;
    private void callDialog(Context ctx,
                            String message
                            ){

        mMaterialDialog = new MaterialDialog( ctx )
                .setTitle( "Mais informações" )
                .setMessage( message )
                .setPositiveButton( "Ok", new View.OnClickListener() {

                    @Override
                    public void onClick( View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }
    // total number of rows
    @Override
    public int getItemCount() {
        return Nome.size();
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

        TextView Nome, Telefone, TipoDecontrato, Valor, WhatsApp, Titulo, MaisInfo, Endereco;
        ImageView ligar, zap;
        ImageView lixeira;

        ViewHolder(View itemView) {
            super(itemView);
            Nome = itemView.findViewById(R.id.eualugo_nome);
            Telefone = itemView.findViewById(R.id.eualugo_telefone);
            Valor = itemView.findViewById(R.id.eualugo_valor);
            Titulo = itemView.findViewById(R.id.eualugo_titulo);
            Endereco = itemView.findViewById(R.id.eualugo_endereco);
            ligar = itemView.findViewById(R.id.phone_eualugo);
            zap =  itemView.findViewById(R.id.zap_eualugo);
            MaisInfo = itemView.findViewById(R.id.eualugo_maisinfo);
            WhatsApp = itemView.findViewById(R.id.whatsapp_eualugo);
            lixeira = itemView.findViewById(R.id.excluir_eualugo);


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