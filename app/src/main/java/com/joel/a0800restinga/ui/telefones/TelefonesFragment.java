package com.joel.a0800restinga.ui.telefones;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.Query;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Telefones;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.Model.TelefonesModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TelefonesFragment extends Fragment implements RecyclerAdapter_Telefones.ItemClickListener, AdapterView.OnItemSelectedListener{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



    //private EditText editText;
    //private ImageButton imageButton;
    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item, whatsapp;
    ArrayAdapter<String> arrayAdapter;
    TelefonesModel user;
    RecyclerAdapter_Telefones adapter;
    Spinner spinnerCategorias;
    private View root;
    Button QueroAnunciar;

    public static TelefonesFragment newInstance(int num) {
        TelefonesFragment f = new TelefonesFragment();

        // Supply num input as an argument.
        Bundle args = new Bundle();
        args.putInt("num", num);
        f.setArguments(args);

        return f;
    }


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        whatsapp = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.item_telefones, R.id.txtnomeeualugo, titulo);

        root = inflater.inflate(R.layout.activity_main, container, false);

        spinnerCategorias = (Spinner) root.findViewById(R.id.spinner);
        //imageButton = (ImageButton) root.findViewById(R.id.imageSearch);
        //editText = (EditText) root.findViewById(R.id.editSearch);

        spinnerCategorias.setOnItemSelectedListener(this);


        String[] categorias = getResources().getStringArray(R.array.categorias_telefone);
        spinnerCategorias.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, categorias));



        /*imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                search();

            }
        });*/



        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }

        QueroAnunciar = root.findViewById(R.id.COLOCARMEUFONE);
        QueroAnunciar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://forms.gle/fGLLwaqu3uLVEWjW9";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });
        return root;
    }

    private void search() {
        String categoria = (String) spinnerCategorias.getSelectedItem();

        try{
            if (categoria.equals("Categorias")) {

                    Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("telefones")
                            .orderByChild("End");

                    myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);


            }
            else{

                    Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("telefones")
                            .orderByChild("Cat")
                            .equalTo(categoria);

                    myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.options_menu, menu);
    }

    ValueEventListener valueEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(@NonNull DataSnapshot snapshot) {
            titulo.clear();
            item.clear();
            whatsapp.clear();

            for (DataSnapshot d : snapshot.getChildren()) {
                user = d.getValue(TelefonesModel.class);
                String texto = user.getTel();
                titulo.add(String.valueOf(texto));
                item.add(user.getEnd());
                whatsapp.add(user.getWhatsapp());
                TelefonesModel model = new TelefonesModel(texto, user.getEnd(), user.getWhatsapp(), "", "");

            }

            recyclerView = root.findViewById(R.id.recicler);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));


            adapter = new RecyclerAdapter_Telefones(null, getContext(), titulo, item, whatsapp, false);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };



    public void onItemSelected(AdapterView<?> parent, final View view,
                               int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
        search();

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public void onItemClick(int position) {
        Toast.makeText(root.getContext(), position, Toast.LENGTH_LONG).show();
    }
}