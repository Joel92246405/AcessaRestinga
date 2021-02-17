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
import java.util.Arrays;
import java.util.List;

public class FinalizaPedidoConferenciaEnvio extends AppCompatActivity{

    /*Recursos de Tela*/
    private TextView txtconferencisa, txtValorFinal;
    private Button btnFinaliza;
    private String ValorTotal, ListaCarrinho, Telefone;

    /*Recursos para Banco*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finaliza_pedido_conferir);

        /*Instanciando recursos de tela*/
        txtconferencisa = (TextView) findViewById(R.id.txtConferencia);
        txtValorFinal = (TextView) findViewById(R.id.Valor);
        Toolbar tlb = (Toolbar) findViewById(R.id.toolbarFinalizaPedidoConferir);



        //Configurando Toolbar
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setTitle("Finalizar");


        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ValorTotal = extras.getString("ValorDoPedido");
            ListaCarrinho = extras.getString("ListaDeProdutos");
            Telefone = extras.getString("TelefoneEmpresa");
        }else {
            Toast.makeText(this, "Empresa não localizada", Toast.LENGTH_LONG).show();
            finish();
        }

        String textoFormatado = "";
        //List<String> lista = Arrays.asList(ListaCarrinho.split(";"));
        List<String> lista = new ArrayList<String>(Arrays.asList(ListaCarrinho.split("Ñ")));
        for (String item: lista){
            textoFormatado += item + "\n";
        }
        txtconferencisa.setText(textoFormatado);

        FloatingActionButton fab = findViewById(R.id.confirmerPedido);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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