package com.joel.a0800restinga.ui.cadastros.meusprodutos;

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
import com.joel.a0800restinga.Model.EuAlugoModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_EuAlugo;

import java.util.ArrayList;
import java.util.List;

public class MeusProdutosFragment extends Fragment implements AdapterView.OnItemSelectedListener, RecyclerAdapter_EuAlugo.ItemClickListener, View.OnClickListener {

    private RecyclerView recyclerView;
    DatabaseReference databaseReference;

    List<String> titulo, nome, telefone, endereco, valor, whatsapp, ocultarvalor, categoria, email;
    private String Email = "";
    EuAlugoModel euAlugoModel;
    RecyclerAdapter_EuAlugo adapter;

    private View root;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private Query myTopPostsQuery;

    /*Campos do cadastro*/
    private EditText Titulo, Nome, Telefone, Endereco, Valor;
    private Switch WhatsApp, OcultarValor;
    private Button cadastrar;
    private Spinner spinnerCategorias;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cad_eualugo, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("eu_alugo");


        cadastrar = (Button) root.findViewById(R.id.cadastrartelefone);

        spinnerCategorias = (Spinner) root.findViewById(R.id.categoria_eualugo);

        Titulo = (EditText)  root.findViewById(R.id.titulo_eualugo);
        Nome = (EditText)  root.findViewById(R.id.nome_eualugo);
        Telefone = (EditText)  root.findViewById(R.id.telefone_eualugo);
        Endereco = (EditText)  root.findViewById(R.id.endereco_eualugo);
        Valor = (EditText)  root.findViewById(R.id.valor_eualugo);


        WhatsApp = (Switch)  root.findViewById(R.id.possuiWhatsapp_eualugo);
        OcultarValor = (Switch)  root.findViewById(R.id.ocultarvalor_eualugo);

        titulo = new ArrayList<String>();
        nome = new ArrayList<String>();
        whatsapp = new ArrayList<String>();
        telefone = new ArrayList<String>();
        endereco = new ArrayList<String>();
        valor  = new ArrayList<String>();
        ocultarvalor = new ArrayList<String>();
        categoria = new ArrayList<String>();
        email = new ArrayList<String>();


        spinnerCategorias.setOnItemSelectedListener(this);


        String[] categorias = getResources().getStringArray(R.array.categorias_aluguel);
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

            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("eu_alugo")
                    .orderByChild("email")
                    .equalTo(Email);

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {


                    titulo.clear();// = new ArrayList<String>();
                    nome.clear();// = new ArrayList<String>();
                    whatsapp.clear();// = new ArrayList<String>();
                    telefone.clear();// = new ArrayList<String>();
                    endereco.clear();// = new ArrayList<String>();
                    valor.clear();//  = new ArrayList<String>();
                    ocultarvalor.clear();// = new ArrayList<String>();
                    categoria.clear();// = new ArrayList<String>();
                    email.clear();// = new ArrayList<String>();

                    for (DataSnapshot d : snapshot.getChildren()) {
                        euAlugoModel = d.getValue(EuAlugoModel.class);

                        titulo.add(euAlugoModel.getTitulo());
                        nome.add(euAlugoModel.getNome());
                        whatsapp.add(euAlugoModel.getWhatsApp());
                        telefone.add(euAlugoModel.getTelefone());
                        endereco.add(euAlugoModel.getEndereco());
                        valor.add(euAlugoModel.getValor());
                        ocultarvalor.add(euAlugoModel.getOcultarValor());
                        categoria.add(euAlugoModel.getCategoria());
                        email.add(euAlugoModel.getEmail());

                    }

                    recyclerView = root.findViewById(R.id.recicler_eualugo);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

                    adapter = new RecyclerAdapter_EuAlugo(root.getContext(), nome, telefone, valor, whatsapp, titulo, ocultarvalor, endereco, categoria, false);
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

    public static MeusProdutosFragment newInstance() {
        return new MeusProdutosFragment();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onItemClick(int position) {
        //click no recycler
    }

    @Override
    public void onClick(View v) {
        if (v == cadastrar){

            if (Titulo.getText().toString().isEmpty()){
                Titulo.setError("Titulo não informado");
            }else{
                if (Nome.getText().toString().isEmpty()){
                    Nome.setError("Nome não informado");
                }else{
                    if (Telefone.getText().toString().isEmpty()) {
                        Telefone.setError("Telefone não informado");
                    }else {


                        String titulo = Titulo.getText().toString();
                        String nome = Nome.getText().toString();
                        String telefone = Telefone.getText().toString();
                        String endereco = Endereco.getText().toString();
                        String valor = Valor.getText().toString();
                        String categoria = this.spinnerCategorias.getSelectedItem().toString();
                        String whatsApp = "N";
                        if (WhatsApp.isChecked())
                            whatsApp = "S";

                        String ocultarValor = "N";
                        if (OcultarValor.isChecked())
                            ocultarValor = "S";


                        EuAlugoModel user = new EuAlugoModel(nome, telefone, valor, whatsApp, titulo, ocultarValor, endereco, categoria, Email);
                        String Id = nome.replaceAll("\\s+", "") + telefone.replaceAll("\\s+", "");
                        databaseReference.child(Id).setValue(user);
                        Toast.makeText(v.getContext(), "Dados inseridos com sucesso", Toast.LENGTH_LONG).show();
                        //myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

                        this.Nome.setText("");
                        this.Telefone.setText("");
                        this.Titulo.setText("");
                        this.Valor.setText("");
                        this.Endereco.setText("");
                        this.Telefone.setText("");
                        this.WhatsApp.setChecked(false);
                        this.OcultarValor.setChecked(false);
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
}