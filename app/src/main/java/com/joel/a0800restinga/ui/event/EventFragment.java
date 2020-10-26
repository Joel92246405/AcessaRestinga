package com.joel.a0800restinga.ui.event;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.Model.EventosModel;
import com.joel.a0800restinga.MyRecyclerViewAdapter;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Eventos;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class EventFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    List<String> titulo, item, data, link;
    ArrayAdapter<String> arrayAdapter;
    EventosModel eventosModel;
    RecyclerAdapter_Eventos adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        data = new ArrayList<String>();
        link = new ArrayList<String>();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("eventos");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_eventos, R.id.titulo_eventos, titulo);



        final View root = inflater.inflate(R.layout.activity_eventos, container, false);

        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    titulo.clear();
                    item.clear();
                    data.clear();
                    link.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        eventosModel = d.getValue(EventosModel.class);
                        String texto = eventosModel.getTitulo();
                        titulo.add(String.valueOf(texto));
                        item.add(eventosModel.getDetalhe());
                        data.add(eventosModel.getData());
                        link.add(eventosModel.getLink());

                    }

                    recyclerView = root.findViewById(R.id.recicler_covid);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_Eventos(getContext(), titulo, item, data, link);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        return root;
    }

    @Override
    public void onItemClick(int position) {

    }
}