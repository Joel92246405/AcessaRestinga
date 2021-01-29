package com.joel.a0800restinga.ui.saudepublica;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.SaudePublicaModel;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Telefones;
import com.joel.a0800restinga.R;

import java.util.ArrayList;
import java.util.List;

public class SaudePublicaFragment extends Fragment{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    DatabaseReference databaseReference;
    List<String> titulo, horarios;
    SaudePublicaModel saudePublicaModel;
    TextView cabecalho, horario;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("saudepublica");

        titulo = new ArrayList<String>();
        horarios = new ArrayList<String>();

        final View root = inflater.inflate(R.layout.activity_saudepublica, container, false);

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


                    for (DataSnapshot d : snapshot.getChildren()) {
                        saudePublicaModel = d.getValue(SaudePublicaModel.class);

                        titulo.add(saudePublicaModel.getTitulo());
                        horarios.add(saudePublicaModel.getHorarios());
                        break;
                    }

                    cabecalho = root.findViewById(R.id.apresentacao_saude);
                    horario = root.findViewById(R.id.horarios);

                    if (titulo.size() > 0)
                    {
                        cabecalho.setText(titulo.get(0).toString());
                        horario.setText(horarios.get(0).toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });


        return root;
    }
/*
    @Override
    public void onItemClick(int position) {

    }

 */
}