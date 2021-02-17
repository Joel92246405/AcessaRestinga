package com.joel.a0800restinga.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.Carrossel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.Uteis.BancoController;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public List<Carrossel> mSliderItems = new ArrayList<Carrossel>();
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

/*
        FirebaseOptions options = new FirebaseOptions.Builder()
                .setApplicationId("telefones-c6ce7")
                .setApiKey("AIzaSyDmLNH8ce7hgXgZypIHHm2zHlPJ4IMQxfY")
                .setDatabaseUrl("https://telefones-c6ce7.firebaseio.com")
                .setStorageBucket("telefones-c6ce7.appspot.com")
                .setGcmSenderId("683025115177").build();
        FirebaseApp.initializeApp(this, options);*/
        FirebaseApp.initializeApp(this);

        try{

            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference usuarios = FirebaseDatabase.getInstance().getReference("usuarios");
            DatabaseReference telefones = FirebaseDatabase.getInstance().getReference("telefones");

            DatabaseReference eu_alugo = FirebaseDatabase.getInstance().getReference("eu_alugo");

            DatabaseReference eventos = FirebaseDatabase.getInstance().getReference("eventos");

            DatabaseReference informativos = FirebaseDatabase.getInstance().getReference("informativos");

            DatabaseReference last_news = FirebaseDatabase.getInstance().getReference("last_news");

            DatabaseReference organica = FirebaseDatabase.getInstance().getReference("organica");

            DatabaseReference vcsabia = FirebaseDatabase.getInstance().getReference("vcsabia");

            DatabaseReference carrossel = FirebaseDatabase.getInstance().getReference("carrossel");

            DatabaseReference saudepublica = FirebaseDatabase.getInstance().getReference("saudepublica");

            DatabaseReference estoudoando = FirebaseDatabase.getInstance().getReference("estoudoando");

            DatabaseReference minhaempresa = FirebaseDatabase.getInstance().getReference("minha_empresa");

            DatabaseReference minhaempresaprodutos = FirebaseDatabase.getInstance().getReference("minha_empresa_produtos");

            telefones.keepSynced(true);
            estoudoando.keepSynced(true);
            carrossel.keepSynced(true);
            eu_alugo.keepSynced(true);
            eventos.keepSynced(true);
            informativos.keepSynced(true);
            last_news.keepSynced(true);
            organica.keepSynced(true);
            vcsabia.keepSynced(true);
            saudepublica.keepSynced(true);
            usuarios.keepSynced(true);
            minhaempresa.keepSynced(true);
            minhaempresaprodutos.keepSynced(true);

        }catch (Exception e){

            e.printStackTrace();
        }

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("carrossel");


        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                BancoController crud2 = new BancoController(getBaseContext());
                String resultado2 = crud2.deletaDados();
                    for (DataSnapshot d : snapshot.getChildren()) {

                        Carrossel carrossel = new Carrossel();
                        carrossel = d.getValue(Carrossel.class);
                        mSliderItems.add(carrossel);


                        BancoController crud = new BancoController(getBaseContext());

                        String descricao = carrossel.getDescricao();
                        String link = carrossel.getUrl();
                        String telefone = carrossel.getTelefone();
                        String whatsapp = carrossel.getWhatsapp();
                        String resultado;

                        resultado = crud.insereDado(descricao,link,telefone, whatsapp);

                    }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




        new Handler().postDelayed(new Runnable() {
            /*
             * Exibindo splash com um timer.
             */

            @Override
            public void run() {



                // Esse método será executado sempre que o timer acabar
                // E inicia a activity principal

                mAuth = FirebaseAuth.getInstance();
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    Toast.makeText(getApplicationContext(), "Bem vindo de volta " + user.getEmail() + "!", Toast.LENGTH_LONG).show();
                    /*
                    Intent i = new Intent(SplashScreen.this,
                            Inicial.class);
                    startActivity(i);

                    // Fecha esta activity
                    finish();

                     */
                }
                Intent i = new Intent(SplashScreen.this,
                        Inicial.class);
                startActivity(i);

                // Fecha esta activity
                finish();


            }
        }, SPLASH_TIME_OUT);
    }
}