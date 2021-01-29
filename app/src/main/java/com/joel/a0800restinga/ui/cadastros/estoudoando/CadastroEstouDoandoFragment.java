package com.joel.a0800restinga.ui.cadastros.estoudoando;

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
import android.widget.Button;
import android.widget.EditText;
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
import com.joel.a0800restinga.Activities.EstouDoando;
import com.joel.a0800restinga.Model.EstouDoandoModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EstouDoando;

import java.util.ArrayList;
import java.util.List;

public class CadastroEstouDoandoFragment extends Fragment implements AdapterView.OnItemSelectedListener, RecyclerAdapter_EstouDoando.ItemClickListener, View.OnClickListener{

    private RecyclerView recyclerView;

    DatabaseReference databaseReference;
    List<String> descricao, nome, telefone, whatsapp, email;
    private String Email = "";
    EstouDoandoModel estouDoandoModel;
    RecyclerAdapter_EstouDoando adapter;

    private View root;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private Query myTopPostsQuery;

    /*Campos do cadastro*/
    private EditText Descricao, Nome, Telefone;
    private Switch WhatsApp;
    private Button cadastrar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cad_estoudoando, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("estoudoando");


        cadastrar = (Button) root.findViewById(R.id.cadastrarestoudoando);

        Descricao = (EditText)  root.findViewById(R.id.descritivo_doacao);
        Nome = (EditText)  root.findViewById(R.id.nome_doacao);
        Telefone = (EditText)  root.findViewById(R.id.telefone_doacao);

        WhatsApp = (Switch)  root.findViewById(R.id.possuiWhatsapp_doacao);


        descricao = new ArrayList<String>();
        nome = new ArrayList<String>();
        whatsapp = new ArrayList<String>();
        telefone = new ArrayList<String>();
        email = new ArrayList<String>();

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

            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("estoudoando")
                    .orderByChild("email")
                    .equalTo(Email);

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    descricao.clear();// = new ArrayList<String>();
                    nome.clear();// = new ArrayList<String>();
                    whatsapp.clear();// = new ArrayList<String>();
                    telefone.clear();// = new ArrayList<String>();
                    email.clear();// = new ArrayList<String>();

                    for (DataSnapshot d : snapshot.getChildren()) {
                        estouDoandoModel = d.getValue(EstouDoandoModel.class);

                        nome.add(estouDoandoModel.getNome());
                        whatsapp.add(estouDoandoModel.getWhatsapp());
                        telefone.add(estouDoandoModel.getTelefone());
                        descricao.add(estouDoandoModel.getConteudo());
                        email.add(estouDoandoModel.getEmail());

                    }

                    recyclerView = root.findViewById(R.id.recicler_estoudoando);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

                    adapter = new RecyclerAdapter_EstouDoando(root.getContext(), nome, descricao, telefone, whatsapp, true);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }


        return root;

    }

    public static CadastroEstouDoandoFragment newInstance() {
        return new CadastroEstouDoandoFragment();
    }

    @Override
    public void onClick(View v) {
        if (v == cadastrar){

            if (Descricao.getText().toString().isEmpty()){
                Descricao.setError("Descritivo não informado");
            }else{
                if (Nome.getText().toString().isEmpty()){
                    Nome.setError("Nome não informado");
                }else{
                    if (Telefone.getText().toString().isEmpty()) {
                        Telefone.setError("Telefone não informado");
                    }else {


                        String conteudo = Descricao.getText().toString();
                        String nome = Nome.getText().toString();
                        String telefone = Telefone.getText().toString();
                        String whatsApp = "N";
                        if (WhatsApp.isChecked())
                            whatsApp = "S";

                        EstouDoandoModel user = new EstouDoandoModel(nome, telefone, whatsApp, Email, conteudo);
                        String Id = nome.replaceAll("\\s+", "") + telefone.replaceAll("\\s+", "") + conteudo.substring(1,5).replaceAll("\\s+","");
                        databaseReference.child(Id).setValue(user);
                        Toast.makeText(v.getContext(), "Dados inseridos com sucesso", Toast.LENGTH_LONG).show();
                        //myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

                        this.Nome.setText("");
                        this.Telefone.setText("");
                        this.Descricao.setText("");
                        this.WhatsApp.setChecked(false);
                        hideSoftKeyboard();
                    }
                }
            }
        }
    }
    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {

    }
}