package com.joel.a0800restinga.ui.cadastros.telefones;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.TelefonesModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_Telefones;

import java.util.ArrayList;
import java.util.List;

public class CadastroTelefonesFragment extends Fragment implements RecyclerAdapter_Telefones.ItemClickListener, AdapterView.OnItemSelectedListener, View.OnClickListener {

    private RecyclerView recyclerView;

    DatabaseReference databaseReference;
    List<String> titulo, item, whatsapp;
    private String Email = "";
    TelefonesModel user;
    RecyclerAdapter_Telefones adapter;

    private View root;
    private Spinner spinnerCategorias;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private Button cadastrar;
    private Query myTopPostsQuery;

    private EditText Nome, Telefone;
    private Switch WhatsApp;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cad_telefones, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("telefones");

        spinnerCategorias = (Spinner) root.findViewById(R.id.categoria);
        cadastrar= (Button) root.findViewById(R.id.cadastrartelefone);
        Nome = (EditText)  root.findViewById(R.id.nome);
        Telefone = (EditText) root.findViewById(R.id.telefone);
        WhatsApp = (Switch)  root.findViewById(R.id.possuiWhatsapp);


        titulo = new ArrayList<String>();
        item = new ArrayList<String>();
        whatsapp = new ArrayList<String>();



        //imageButton = (ImageButton) root.findViewById(R.id.imageSearch);
        //editText = (EditText) root.findViewById(R.id.editSearch);

        spinnerCategorias.setOnItemSelectedListener(this);


        String[] categorias = getResources().getStringArray(R.array.categorias_telefone);
        spinnerCategorias.setAdapter(new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, categorias));

        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }else{

            firebaseAuth = FirebaseAuth.getInstance();
            currentUser =firebaseAuth.getCurrentUser();
            Email = firebaseAuth.getCurrentUser().getEmail();

            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("telefones")
                    .orderByChild("email")
                    .equalTo(Email);

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
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


                    }

                    recyclerView = root.findViewById(R.id.recicler_lastnews);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

                    adapter = new RecyclerAdapter_Telefones(null, getContext(), titulo, item, whatsapp, true);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        cadastrar.setOnClickListener(this);
        /*RecyclerAdapter_Telefones.ItemClickListener item = null;
        adapter.setOnItemClickListener(item);
        int position = 0;
        item.onItemClick(position);

         */
        return root;





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

            }

            recyclerView = root.findViewById(R.id.recicler_lastnews);
            recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));
            adapter = new RecyclerAdapter_Telefones(null, getContext(), titulo, item, whatsapp, true);
            recyclerView.setAdapter(adapter);
        }

        @Override
        public void onCancelled(@NonNull DatabaseError error) {

        }
    };

    public static CadastroTelefonesFragment newInstance() {
        return new CadastroTelefonesFragment();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }



    @Override
    public void onClick(View v) {
        if (v == cadastrar){

            if (Nome.getText().toString().isEmpty()){
                Nome.setError("Nome não informado");
            }else{
                if (Telefone.getText().toString().isEmpty()){
                    Telefone.setError("Telefone não informado");
                }else{



                    String Telefones = Telefone.getText().toString();
                    String Nome = this.Nome.getText().toString();
                    String Categoria = this.spinnerCategorias.getSelectedItem().toString();
                    String eMail = Email;
                    String WhatsApp = "N";

                    if (this.WhatsApp.isChecked()){
                        WhatsApp = "S";
                    }

                    TelefonesModel user = new TelefonesModel(Telefones, Nome, WhatsApp, eMail, Categoria);
                    String Id = Nome.replaceAll("\\s+","") + Telefones.replaceAll("\\s+","");
                    databaseReference.child(Id).setValue(user);
                    Toast.makeText(v.getContext(), "Dados inseridos com sucesso", Toast.LENGTH_LONG).show();
                    myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

                    this.Nome.setText("");
                    this.Telefone.setText("");

                    hideSoftKeyboard();
                }
            }
        }
    }
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }



    @Override
    public void onItemClick(int position) {
        Toast.makeText(root.getContext(), position, Toast.LENGTH_LONG).show();
    }
}