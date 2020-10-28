package com.joel.a0800restinga.ui.telefones;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.Query;
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

public class GalleryFragment extends Fragment implements MyRecyclerViewAdapter.ItemClickListener, AdapterView.OnItemSelectedListener{



    private RecyclerView recyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;



    //private EditText editText;
    //private ImageButton imageButton;
    ListView listView;
    DatabaseReference databaseReference;
    List<String> titulo, item;
    ArrayAdapter<String> arrayAdapter;
    Users user;
    MyRecyclerViewAdapter adapter;
    Spinner spinnerCategorias;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.list, R.id.txtnome, titulo);

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

        return root;
    }

    private void search() {
        String categoria = (String) spinnerCategorias.getSelectedItem();
        //String texto = editText.getText().toString();

        try{
            if (categoria.equals("Categorias")) {

                /*if(texto.length() > 0){
                    Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("telefones")
                            .orderByChild("End")
                            .startAt("\uf8ff"+texto+"\uf8ff")
                            .endAt("\uf8ff"+texto+"\uf8ff");
                    myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);
                }else {*/
                    Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("telefones")
                            .orderByChild("End");

                    myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);


            }
            else{

                /*if(texto.length() > 0){
                    Query myTopPostsQuery = FirebaseDatabase.getInstance().getReference("telefones")
                            .orderByChild("Cat")
                            .equalTo(categoria)
                            .startAt("\uf8ff"+texto+"\uf8ff")
                            .endAt("\uf8ff"+texto+"\uf8ff");

                    myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);
                }else {*/
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

    }
}