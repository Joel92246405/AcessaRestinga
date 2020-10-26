package com.joel.a0800restinga.ui.lastnews;

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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.joel.a0800restinga.Model.LastNewsModel;
import com.joel.a0800restinga.MyRecyclerViewAdapter;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_LastNews;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LastNewsFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;

    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item, link;

    ArrayAdapter<String> arrayAdapter;
    LastNewsModel lastNewsModel;
    RecyclerAdapter_LastNews adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        link = new ArrayList<String>();



        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("last_news");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_lastnews, R.id.titulo_lastnews, titulo);



        final View root = inflater.inflate(R.layout.activity_lastnews, container, false);

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
                    for (DataSnapshot d : snapshot.getChildren()) {
                        lastNewsModel = d.getValue(LastNewsModel.class);
                        String texto = lastNewsModel.getTitulo();
                        titulo.add(String.valueOf(texto));
                        item.add(lastNewsModel.getDetalhe());
                        link.add(lastNewsModel.getLink());

                    }

                    recyclerView = root.findViewById(R.id.recicler_lastnews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_LastNews(getContext(), titulo, item, link);
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