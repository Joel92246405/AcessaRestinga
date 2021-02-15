package com.joel.a0800restinga.ui.cadastros.meusprodutos;

import android.Manifest;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.R;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

import static android.app.Activity.RESULT_OK;

public class MinhaEmpresaFragment extends Fragment implements View.OnClickListener/*, AdapterView.OnItemSelectedListener, RecyclerAdapter_EuAlugo.ItemClickListener*/ {

    /*Instâncias de banco*/
    private FirebaseAuth firebaseAuth;
    private Query myTopPostsQuery;
    private DatabaseReference databaseReference;
    private String Email = "";

    /*Recursos para o Recycler*/
    private List<String> NomeEmpresa;
    private RecyclerView recyclerView;
    //RecyclerAdapter_EuAlugo adapter;


    private boolean PERMISSION_GRANTED = false;
    public static final int REQUEST_PERMISSIONS_CODE = 128;

    /*Campos do cadastro*/
    private View root;
    private EmpresaModel empresaModel;
    private EditText Nome, Telefone, Descricao;
    private Button Cadastrar, SelecionarImagem;
    private ImageView ImagemDaEmpresa;
    private int RESULT_GALERIA = 222;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cad_empresa, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa");


        /*Inicialização dos campos de tela*/
        Nome = (EditText) root.findViewById(R.id.nome_empresa);
        Telefone = (EditText) root.findViewById(R.id.telefone_empresa);
        Descricao = (EditText) root.findViewById(R.id.descritivo_empresa);
        Cadastrar = (Button) root.findViewById(R.id.cadastrarempresa);
        SelecionarImagem = (Button) root.findViewById(R.id.btnSelecionarIumagemEmpresa);
        ImagemDaEmpresa = (ImageView) root.findViewById(R.id.imagemEmpresa);

        Cadastrar.setOnClickListener(this);
        SelecionarImagem.setOnClickListener(this);

        /*Recursos do Recycler*/
        NomeEmpresa = new ArrayList<String>();

        /*Recursos de Conectividade*/
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }else{

            firebaseAuth = FirebaseAuth.getInstance();
            Email = firebaseAuth.getCurrentUser().getEmail();

            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("minha_empresa")
                    .orderByChild("email")
                    .equalTo(Email);

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    NomeEmpresa.clear();// = new ArrayList<String>();

                    for (DataSnapshot d : snapshot.getChildren()) {
                        empresaModel = d.getValue(EmpresaModel.class);
                        NomeEmpresa.add(empresaModel.getNome());
                    }

                    recyclerView = root.findViewById(R.id.recicler_empresas);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

                    //adapter = new RecyclerAdapter_EuAlugo(root.getContext(), nome, telefone, valor, whatsapp, titulo, ocultarvalor, endereco, categoria, false);
                    //recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }



        return root;
    }

    public static MinhaEmpresaFragment newInstance() {
        return new MinhaEmpresaFragment();
    }

    private int REQUEST_CODE_READ = 0;
    private int REQUEST_CODE_WRITE = 0;
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



        requestPermissions(new String[]
                {Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_READ);

        requestPermissions(new String[]
                {Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE);

    }


    // Sobrescrever método para receber resultado das permissões
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


    }



    @Override
    public void onClick(View v) {
        if (v == Cadastrar) {
/*
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
            */
        }

        if (v == SelecionarImagem) {

            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ
                );
            } else if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE
                );
            } else {
                Intent intent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, RESULT_GALERIA);
            }
        }
    }







    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_GALERIA && resultCode == RESULT_OK){

            Uri imagemURI = data.getData();
            String[] colunaArquivo = {MediaStore.Images.Media.DATA};

            if (Build.VERSION.SDK_INT >= 29) {
                final Cursor cursor_29 = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        colunaArquivo, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
                if (cursor_29.moveToFirst()) {

                    // You can replace '0' by 'cursor.getColumnIndex(MediaStore.Images.ImageColumns._ID)'
                    // Note that now, you read the column '_ID' and not the column 'DATA'
                    //Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, cursor_29.getInt(0));

                    // now that you have the media URI, you can decode it to a bitmap
                    try  {
                            int ColumnIndex =cursor_29.getColumnIndex(colunaArquivo[0]);
                            String caminhoDaFoto = cursor_29.getString(ColumnIndex);

                        File imgFile = new  File(caminhoDaFoto);

                        if(imgFile.exists()){

                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            //Bitmap bitmap = BitmapFactory.decodeFile(caminhoDaFoto);
                            ImagemDaEmpresa.setImageBitmap(myBitmap);
                        }


                    } catch (Exception ex) {
                        Log.d("IMG_UP", ex.toString());
                    }



                }
            }
             else {

                Cursor cursor = getContext().getContentResolver().query(imagemURI, colunaArquivo, null, null, null);
                cursor.moveToFirst();
                int ColumnIndex =cursor.getColumnIndex(colunaArquivo[0]);
                String caminhoDaFoto = cursor.getString(ColumnIndex);
                Bitmap foto = BitmapFactory.decodeFile(caminhoDaFoto);
                if (foto != null) {
                    ImagemDaEmpresa.setImageBitmap(foto);
                }
            }

        }
    }




    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}