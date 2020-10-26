package com.joel.a0800restinga.ui.leis;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.barteksc.pdfviewer.PDFView;
import com.joel.a0800restinga.Model.LeisModel;

import com.joel.a0800restinga.MyRecyclerViewAdapter;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Leis;
import com.google.firebase.database.DatabaseReference;


import java.util.ArrayList;
import java.util.List;

public class LeisFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener {


    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;
    private Button download;
    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item;
    ArrayAdapter<String> arrayAdapter;
    LeisModel leisModel;
    RecyclerAdapter_Leis adapter;
    private String TAG;
    PDFView mPDFView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        //databaseReference = FirebaseDatabase.getInstance().getReference("organica");
        //arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_leis, R.id.titulo_leis, titulo);


        final View root = inflater.inflate(R.layout.activity_leis, container, false);


        mPDFView = (PDFView)  root.findViewById(R.id.pdfView);
        mPDFView.fromAsset("lei.pdf")
                .enableDoubletap(true).load();

        ConnectivityManager cm =
                (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        /*
        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }

            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    titulo.clear();
                    item.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        leisModel = d.getValue(LeisModel.class);
                        String texto = leisModel.getTitulo();
                        titulo.add(String.valueOf(texto));
                        item.add(leisModel.getDetalhe());
                    }

                    recyclerView = root.findViewById(R.id.recicler_leis);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
                    adapter = new RecyclerAdapter_Leis(getContext(), titulo, item);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

    */
        return root;
    }

    @Override
    public void onItemClick(int position) {

    }




}