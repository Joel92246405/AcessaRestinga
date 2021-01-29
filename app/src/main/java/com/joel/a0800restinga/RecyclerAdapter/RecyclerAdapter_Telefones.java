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

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.a0800restinga.Model.ContatosAgenda;
import com.joel.a0800restinga.Model.TelefonesModel;
import com.joel.a0800restinga.R;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter_Telefones extends RecyclerView.Adapter<RecyclerAdapter_Telefones.ViewHolder>{

    private List<String> Telefone, Whatsapp;
    private List<String> Nome;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private List<TelefonesModel> telefonesModel;
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private static final int PERMISSIONS_REQUEST_READ_CONTACTS = 100;
    private Context ctx;
    private boolean mostrarLixeira;
    public ArrayList<ContatosAgenda> contacts = new ArrayList<ContatosAgenda>();
    private AlertDialog alerta;
    private static ItemClickListener itemClickListener;

    public RecyclerAdapter_Telefones(Activity activity, Context context, List<String> telefone, List<String> nome, List<String> whatsapp, boolean MostrarLixeira) {

        this.mInflater = LayoutInflater.from(context);
        this.Telefone = telefone;
        this.Nome = nome;
        this.Whatsapp = whatsapp;
        this.ctx = context;
        this.mostrarLixeira = MostrarLixeira;


        if ( !(ContextCompat.checkSelfPermission( context, Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ) ){
            getContactNames(context);
        }else{
            Toast.makeText(context.getApplicationContext(), "Dê permissão de acesso a agenda nas configurações do aparelho para ter uma nova experiência de comunicar via WhatsApp!", Toast.LENGTH_LONG).show();
        }

    }

    private void getContactNames(Context ctx) {

        // Get the ContentResolver
        ContentResolver cr =  ctx.getContentResolver();


        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)).replaceAll("[^0-9]", "");;
            String nome = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

            ContatosAgenda contatosAgenda = new ContatosAgenda(nome, number);
            contacts.add(contatosAgenda);
        }
        phones.close();
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_telefones, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        String telefoneb = Telefone.get(position);
        String nome = Nome.get(position);

        holder.telefone.setText(telefoneb);
        holder.nome.setText(nome);

        holder.telefone.setTag(position);
        String What = Whatsapp.get(position);

        if (! mostrarLixeira){
            holder.lixeira.setVisibility(View.INVISIBLE);
        }

        if ( (ContextCompat.checkSelfPermission( ctx, Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ) ){
            holder.zapzap.setVisibility(View.INVISIBLE);
        }else {
            if ((What.equals("S"))) {
                holder.zapzap.setVisibility(View.VISIBLE);
            } else {
                holder.zapzap.setVisibility(View.INVISIBLE);
            }
        }

        holder.lixeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference databaseReference;
                databaseReference = FirebaseDatabase.getInstance().getReference("telefones");
                String Id = holder.nome.getText().toString().replaceAll("\\s+","") + holder.telefone.getText().toString().replaceAll("\\s+","");
                databaseReference.child(Id).removeValue();



            }
        });

        holder.compartilhar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String texto = "Olá. Segue o telefone de " + holder.nome.getText() + " - "+ holder.telefone.getText() +
                        " Aproveite e baixe já o app. Acesse: https://play.google.com/store/apps/details?id=com.joel.a0800restinga";;
                sendIntent.putExtra(Intent.EXTRA_TEXT, texto);
                sendIntent.setType("text/plain");
                v.getContext().startActivity(sendIntent);
            }
        });
        holder.ligar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String telefone = holder.telefone.getText().toString();
                Uri uri = Uri.parse("tel:"+telefone);
                Intent intent = new Intent(Intent.ACTION_DIAL,uri);
                view.getContext().startActivity(intent);
                //startActivity(intent);

            }
        });

        holder.zapzap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String telefone = holder.telefone.getText().toString();
                String telefoneCompara = telefone.substring((telefone.length()-8)).replaceAll("[^0-9]", "");;

                try {

                    Boolean existeNaAgenda = false;

                    ArrayList<ContentValues> data = new ArrayList<ContentValues>();

                    for (ContatosAgenda contatosAgenda : contacts) {

                        String telefoneArray = contatosAgenda.getTelefone().toString();
                        telefoneArray = telefoneArray.substring(telefoneArray.length() - 8).replaceAll("[^0-9]", "");
                        ;
                        if (telefoneArray.equals(telefoneCompara)) {
                            existeNaAgenda = true;

                            break;
                        }
                    }

                    if (existeNaAgenda) {
                        Intent sendIntent = new Intent("android.intent.action.MAIN");
                        sendIntent.putExtra("jid", "55" + telefone + "@s.whatsapp.net");
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Olá!");
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.setPackage("com.whatsapp");
                        sendIntent.setType("text/plain");
                        view.getContext().startActivity(sendIntent);
                    } else {
                        if (ContextCompat.checkSelfPermission(view.getContext(), Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(view.getContext(), "Sem permissão para salvar/consultar o contato na agenda. /nAdcione este contato para conversar com ele!", Toast.LENGTH_SHORT).show();
                        } else {
                            String phone = holder.telefone.getText().toString();
                            String phoneName = holder.nome.getText().toString();
                            verificaSeDesejaCadastrar(view.getContext(), phone, phoneName);
                        }
                    }
                }catch (Exception ex){
                    Log.d("ZapRecView", ex.toString());
                    Toast.makeText(view.getContext(), "Não foi possível entrar em contato com esse número", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void verificaSeDesejaCadastrar(final Context ctx, final String telefone, final String nome) {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        //define o titulo
        builder.setTitle("Adcionar Contato");
        //define a mensagem
        builder.setMessage("Para utilizar o WhatsApp desse contado tenha ele na sua agenda");
        //define um botão como positivo
        builder.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                //Toast.makeText(view.getContext(), "positivo=" + arg1, Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(Intent.ACTION_INSERT, ContactsContract.Contacts.CONTENT_URI);
                intent.putExtra(ContactsContract.Intents.Insert.PHONE, telefone);
                intent.putExtra(ContactsContract.Intents.Insert.NAME, nome);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                ctx.getApplicationContext().startActivity(intent);
                Toast.makeText(ctx.getApplicationContext(), "Não existe na agenda", Toast.LENGTH_SHORT).show();
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

    // total number of rows
    @Override
    public int getItemCount() {
        return Telefone.size();
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
        TextView whatsapp_telefone;
        ImageView ligar;
        ImageView zapzap;
        ImageView compartilhar;
        ImageView lixeira;

        ViewHolder(View itemView) {
            super(itemView);

            Typeface tpNome = Typeface.createFromAsset(itemView.getContext().getAssets(), "LibreBaskerville-Regular.ttf");

            nome = itemView.findViewById(R.id.txtnomeeualugo);
            nome.setTypeface(tpNome);
            telefone = itemView.findViewById(R.id.txttelefoneeualugo);
            compartilhar = itemView.findViewById(R.id.share);
            ligar = itemView.findViewById(R.id.ligar);
            zapzap = itemView.findViewById(R.id.zapzap);
            whatsapp_telefone = itemView.findViewById(R.id.whatsapp_telefone);
            lixeira = itemView.findViewById(R.id.excluir);

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
        return Telefone.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = (ItemClickListener) itemClickListener;
    }






}