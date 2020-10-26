package com.joel.a0800restinga.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.joel.a0800restinga.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.joel.a0800restinga.ui.telefones.GalleryFragment;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        try{
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
            DatabaseReference telefones = FirebaseDatabase.getInstance().getReference("telefones");
            DatabaseReference eventos = FirebaseDatabase.getInstance().getReference("eventos");
            DatabaseReference informativos = FirebaseDatabase.getInstance().getReference("informativos");
            DatabaseReference last_news = FirebaseDatabase.getInstance().getReference("last_news");
            DatabaseReference organica = FirebaseDatabase.getInstance().getReference("organica");
            DatabaseReference vcsabia = FirebaseDatabase.getInstance().getReference("vcsabia");

            telefones.keepSynced(true);
            eventos.keepSynced(true);
            informativos.keepSynced(true);
            last_news.keepSynced(true);
            organica.keepSynced(true);
            vcsabia.keepSynced(true);

        }catch (Exception e){

            e.printStackTrace();
        }


        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);



        final TextView textView = root.findViewById(R.id.text_home);
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                String Texto = "Olá. Seja bem-vindo ao App de Utilidade Pública desenvolvido para você, cidadão Restinguense.\n\n"
                        +"Nele você vai encontrar:\n\n" +
                        "*Uma lista telefônica com vários telefones uteis;\n" +
                        "*Informativo sobre o Covid-19 Municipal e informativos gerais\n" +
                        "*Curiosidades;\n" +
                        "*Lei Orgânica Municipal\n" +
                        "*Eventos;\n" +
                        "E muito mais!\n\n\n" +
                        "Espero estar sendo útil!";
                textView.setText(Texto);
            }
        });
        return root;
    }
}