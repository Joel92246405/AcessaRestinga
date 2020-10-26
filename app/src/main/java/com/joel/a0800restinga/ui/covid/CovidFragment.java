package com.joel.a0800restinga.ui.covid;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.CovidModel;
import com.joel.a0800restinga.MyRecyclerViewAdapter;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Covid;

import java.util.ArrayList;
import java.util.List;

public class CovidFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    List<String> titulo, item, data;
    ArrayAdapter<String> arrayAdapter;
    CovidModel covidModel;
    RecyclerAdapter_Covid adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        data = new ArrayList<String>();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("covid");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_covid, R.id.titulo_covid, titulo);



        final View root = inflater.inflate(R.layout.activity_covid, container, false);

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

                    for (DataSnapshot d : snapshot.getChildren()) {
                        covidModel = d.getValue(CovidModel.class);
                        String texto = covidModel.getTitulo();
                        titulo.add(String.valueOf(texto));
                        item.add(covidModel.getDetalhe());
                        data.add(covidModel.getData());

                    }

                    recyclerView = root.findViewById(R.id.recicler_covid);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_Covid(getContext(), titulo, item, data);
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