package com.joel.a0800restinga.ui.cadastros.meusprodutos;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
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

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joel.a0800restinga.Activities.Telefones;
import com.joel.a0800restinga.Model.EmpresaModel;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_MinhaEmpresa;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;

import static android.app.Activity.RESULT_OK;

public class MinhaEmpresaFragment extends Fragment implements View.OnClickListener{

    /*Instâncias de banco*/
    private FirebaseAuth firebaseAuth;
    private Query myTopPostsQuery;
    private DatabaseReference databaseReference;
    private String Email = "";
    FirebaseStorage storage;
    StorageReference storageReference;

    /*Recursos para o Recycler*/
    private List<String> NomeEmpresa, TelefoneEmpresa;
    private RecyclerView recyclerView;
    RecyclerAdapter_MinhaEmpresa adapter;


    private boolean PERMISSION_GRANTED = false;
    public static final int REQUEST_PERMISSIONS_CODE = 128;

    /*Campos do cadastro*/
    private View root;
    private String CaminhoDaFoto;
    private Uri CaminhoDaFotoUri;
    private EmpresaModel empresaModel;
    private EditText Nome, Telefone, Descricao;
    private Button Cadastrar, SelecionarImagem;
    private ImageView ImagemDaEmpresa;
    private int RESULT_GALERIA = 222;
    private Boolean FotoSalva = false;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.fragment_cad_empresa, container, false);
        databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa");


        /*Inicialização dos campos de tela*/
        Nome = (EditText) root.findViewById(R.id.nome_empresa);
        Telefone = (EditText) root.findViewById(R.id.telefone_empresa);
        Descricao = (EditText) root.findViewById(R.id.descritivo_empresa);
        Cadastrar = (Button) root.findViewById(R.id.cadastrarempresa);

        ImagemDaEmpresa = (ImageView) root.findViewById(R.id.imagemEmpresa);

        Cadastrar.setOnClickListener(this);
        ImagemDaEmpresa.setOnClickListener(this);

        /*Recursos do Recycler*/
        NomeEmpresa = new ArrayList<String>();
        TelefoneEmpresa = new ArrayList<String>();

        /*Recursos de Conectividade*/
        ConnectivityManager cm =
                (ConnectivityManager)getContext().getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(getContext(), "Você está desconectado", Toast.LENGTH_LONG).show();
        }else{

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            firebaseAuth = FirebaseAuth.getInstance();
            Email = firebaseAuth.getCurrentUser().getEmail();

            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("minha_empresa")
                    .orderByChild("eMailControle")
                    .equalTo(Email);

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    NomeEmpresa.clear();// = new ArrayList<String>();
                    TelefoneEmpresa.clear();
                    for (DataSnapshot d : snapshot.getChildren()) {
                        empresaModel = d.getValue(EmpresaModel.class);
                        NomeEmpresa.add(empresaModel.getNome());
                        TelefoneEmpresa.add(empresaModel.getTelefone());
                    }

                    recyclerView = root.findViewById(R.id.recicler_empresas);
                    recyclerView.setLayoutManager(new LinearLayoutManager(root.getContext()));

                    adapter = new RecyclerAdapter_MinhaEmpresa(root.getContext(), NomeEmpresa, TelefoneEmpresa);
                    recyclerView.setAdapter(adapter);
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

            if (Nome.getText().toString().isEmpty()){
                Nome.setError("Você deve informar o nome da sua empresa!");
            }else{
                if (Telefone.getText().toString().isEmpty()){
                    Telefone.setError("Você deve informar o telefone de WhatsApp da sua empresa!");
                }else{
                    if (Descricao.getText().toString().isEmpty()) {
                        Descricao.setError("Descreva a sua empresa!");
                    }else {

                        uploadImage();
                    }
                }
            }

        }

        if (v == ImagemDaEmpresa) {

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


            CaminhoDaFotoUri = data.getData();
            String[] colunaArquivo = {MediaStore.Images.Media.DATA};

            if (Build.VERSION.SDK_INT >= 29) {
                final Cursor cursor_29 = getContext().getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        colunaArquivo, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
                if (cursor_29.moveToFirst()) {


                    try  {
                        int ColumnIndex =cursor_29.getColumnIndex(colunaArquivo[0]);
                        String caminhoDaFoto = cursor_29.getString(ColumnIndex);
                        File imgFile = new  File(caminhoDaFoto);
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            ImagemDaEmpresa.setImageBitmap(myBitmap);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(getContext(), "Não foi possivel carregar sua imagem " + ex.toString(), Toast.LENGTH_LONG).show();
                        Log.d("IMG_UP", ex.toString());
                    }



                }
            }
             else {

                Cursor cursor = getContext().getContentResolver().query(CaminhoDaFotoUri, colunaArquivo, null, null, null);
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


    private void uploadImage(){
        if (CaminhoDaFotoUri != null) {
            CaminhoDaFoto = UUID.randomUUID().toString();
            CaminhoDaFoto = Nome.getText().toString().replaceAll("\\s+", "") + Telefone.getText().toString().replaceAll("\\s+", "");
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Salvando os dados...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference
                    .child(
                            "images/"
                                    + CaminhoDaFoto);

            // adding listeners on upload
            // or failure of image
            ref.putFile(CaminhoDaFotoUri)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    FotoSalva = true;
                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getActivity(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    insertEmpresa();
                                }
                            })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {
                            FotoSalva = false;
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getActivity(),
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }

    private void insertEmpresa(){


                String nome = Nome.getText().toString();
                String telefone = Telefone.getText().toString();
                String descricao = Descricao.getText().toString();

                String codigoDaEmpresa = nome.replaceAll("\\s+", "") + telefone.replaceAll("\\s+", "");
                EmpresaModel empresaModel = new EmpresaModel(codigoDaEmpresa, nome, telefone, descricao, Email, CaminhoDaFoto, "P");


                databaseReference.child(codigoDaEmpresa).setValue(empresaModel);
                Toast.makeText(getContext(), "Empresa inserida com sucesso", Toast.LENGTH_LONG).show();
                //myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

                this.Nome.setText("");
                this.Telefone.setText("");
                this.Descricao.setText("");
                ImagemDaEmpresa.setImageBitmap(null);
                hideSoftKeyboard();


    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }
}