package com.joel.a0800restinga.ui.telefones;

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

import com.joel.a0800restinga.MyRecyclerViewAdapter;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.Users;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class GalleryFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;


    private ImageButton back;

    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item;
    ArrayAdapter<String> arrayAdapter;
    Users user;
    MyRecyclerViewAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();

        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("telefones");
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list, R.id.txtnome, titulo);



        final View root = inflater.inflate(R.layout.activity_main, container, false);


        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }

        //if (isConnected) {
        try {
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    titulo.clear();
                    item.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        user = d.getValue(Users.class);
                        String texto = user.getTel();
                        titulo.add(String.valueOf(texto));
                        item.add(user.getEnd());
                    }

                    recyclerView = root.findViewById(R.id.recicler);
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));


                    adapter = new MyRecyclerViewAdapter(getContext(), titulo, item);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }catch (Exception e){

            e.printStackTrace();
        }

        //}

        return root;
    }

    @Override
    public void onItemClick(int position) {

    }
}