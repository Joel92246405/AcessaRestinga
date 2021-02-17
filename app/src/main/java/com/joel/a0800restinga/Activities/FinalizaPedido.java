package com.joel.a0800restinga.Activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.joel.a0800restinga.Model.MeusProdutosModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_FinalizaPedido;

import java.util.ArrayList;
import java.util.List;

public class FinalizaPedido extends AppCompatActivity{

    /*Recursos de Tela*/
    private TextView txtValor;
    private Button btnFinaliza;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    /*Recursos para Banco*/
    DatabaseReference databaseReference;
    private String Email = "";
    FirebaseStorage storage;
    StorageReference storageReference;
    private Query myTopPostsQuery;
    List<String> Produtos, Vazio, ValorProd;

    ArrayAdapter<String> arrayAdapter;
    MeusProdutosModel meusProdutosModel;
    RecyclerAdapter_FinalizaPedido adapter;

    /*Variaveis de controle*/
    private String ChaveDaEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_pedido);

        /*Instanciando recursos de tela*/
        Toolbar tlb = (Toolbar) findViewById(R.id.toolbarFinalizaPedido);


        //Configurando Toolbar
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setTitle("Produtos");

        /*Inicializando variaveis*/
        Produtos = new ArrayList<String>();
        Vazio = new ArrayList<String>();
        ValorProd = new ArrayList<String>();

        /*Instanciando Banco de Dados*/
        databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa_produtos");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ChaveDaEmpresa = extras.getString("ChaveDaEmpresa");
        }else {
            Toast.makeText(this, "Empresa não localizada", Toast.LENGTH_LONG).show();
            finish();
        }


        /*Recursos de Conectividade*/
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(this, "Você está desconectado", Toast.LENGTH_LONG).show();
        }else{

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();


            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("minha_empresa_produtos")
                    .orderByChild("codigoDaEmpresa")
                    .equalTo(ChaveDaEmpresa);

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    Produtos.clear();
                    Vazio.clear();
                    ValorProd.clear();

                    MeusProdutosModel produto;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        produto = d.getValue(MeusProdutosModel.class);
                        Produtos.add(produto.getNome());
                        Vazio.add("0");
                        ValorProd.add(produto.getValor());
                    }

                    recyclerView = findViewById(R.id.recicler_produtos_carrinho);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    adapter = new RecyclerAdapter_FinalizaPedido(getApplicationContext(), Produtos, Vazio, ValorProd);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        FloatingActionButton fab = findViewById(R.id.finalizaPedido);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String texto = adapter.finaliza();
                Intent intent = new Intent(getApplicationContext(), FinalizaPedidoConferenciaEnvio.class);
                //String Id = Nome.get(position).replaceAll("\\s+","") + Telefone.get(position).replaceAll("\\s+","");
                intent.putExtra("ListaDeProdutos", texto);
                startActivity(intent);
                //Toast.makeText(getApplicationContext(), texto, Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onBackPressed(){


        finish(); // Finaliza a Activity atual

        return;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish(); // Finaliza a Activity atual

                break;

            default:
                break;
        }

        return true;
    }
    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}