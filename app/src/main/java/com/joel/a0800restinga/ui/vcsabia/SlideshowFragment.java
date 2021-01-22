package com.joel.a0800restinga.ui.vcsabia;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_VcSabia;
import com.joel.a0800restinga.Model.VoceSabiaModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;

    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, descricao;
    ArrayAdapter<String> arrayAdapter;
    VoceSabiaModel vcsabia;
    RecyclerAdapter_VcSabia adapter;
    Toolbar mTopToolbar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        descricao = new ArrayList<String>();

        final View root = inflater.inflate(R.layout.activity_vcsabia, container, false);
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("vcsabia");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_vcsabia, R.id.titulo_covid, titulo);


        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }
        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    titulo.clear();
                    descricao.clear();
                    for(DataSnapshot d : snapshot.getChildren()){
                        vcsabia = d.getValue(VoceSabiaModel.class);
                        String texto =vcsabia.getTitulo();
                        titulo.add(String.valueOf(texto));
                        descricao.add(vcsabia.getDetalhe());
                    }

                    RecyclerView recyclerView = root.findViewById(R.id.recicler_vcsabia);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    adapter = new RecyclerAdapter_VcSabia(getContext(), titulo, descricao);
                    recyclerView.setAdapter(adapter);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){

            e.printStackTrace();
        }







        final TextView textView = root.findViewById(R.id.text_slideshow);

        return root;
    }
}