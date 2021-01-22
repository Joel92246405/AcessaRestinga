package com.joel.a0800restinga.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.Carrossel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.Uteis.BancoController;
import com.onesignal.OneSignal;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public List<Carrossel> mSliderItems = new ArrayList<Carrossel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);



        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference telefones = FirebaseDatabase.getInstance().getReference("telefones");
            DatabaseReference eu_alugo = FirebaseDatabase.getInstance().getReference("eu_alugo");
            DatabaseReference eventos = FirebaseDatabase.getInstance().getReference("eventos");
            DatabaseReference informativos = FirebaseDatabase.getInstance().getReference("informativos");
            DatabaseReference last_news = FirebaseDatabase.getInstance().getReference("last_news");
            DatabaseReference organica = FirebaseDatabase.getInstance().getReference("organica");
            DatabaseReference vcsabia = FirebaseDatabase.getInstance().getReference("vcsabia");
            DatabaseReference carrossel = FirebaseDatabase.getInstance().getReference("carrossel");
            DatabaseReference saudepublica = FirebaseDatabase.getInstance().getReference("saudepublica");

            telefones.keepSynced(true);
            carrossel.keepSynced(true);
            eu_alugo.keepSynced(true);
            eventos.keepSynced(true);
            informativos.keepSynced(true);
            last_news.keepSynced(true);
            organica.keepSynced(true);
            vcsabia.keepSynced(true);
            saudepublica.keepSynced(true);


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
                Intent i = new Intent(SplashScreen.this,
                        Inicial.class);
                startActivity(i);

                // Fecha esta activity
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}