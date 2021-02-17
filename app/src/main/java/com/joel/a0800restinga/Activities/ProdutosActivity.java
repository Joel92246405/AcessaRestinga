package com.joel.a0800restinga.Activities;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.Nullable;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.joel.a0800restinga.Model.MeusProdutosModel;
import com.joel.a0800restinga.Model.Usuario;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.RecyclerAdapter.RecyclerAdapter_MinhaEmpresaProdutos;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import me.drakeet.materialdialog.MaterialDialog;

import static android.Manifest.permission.READ_CONTACTS;
import static java.security.AccessController.getContext;

public class ProdutosActivity extends AppCompatActivity implements View.OnClickListener {

    private FirebaseAuth mAuth;

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    // UI references.
    private RatingBar relevanciaProduto;
    private EditText NomeProduto, DescricaoProduto, ValorProduto;
    private ImageView AddFotoProduto;
    private Button Confirmar;


    /*Componentes Recycler*/
    private List<String> NomeProdutos, ValorProdutos;
    RecyclerAdapter_MinhaEmpresaProdutos adapter;
    private RecyclerView recyclerView;

    /*Instâncias de banco*/
    private FirebaseAuth firebaseAuth;
    private Query myTopPostsQuery;
    private DatabaseReference databaseReference;
    private String Email = "";
    FirebaseStorage storage;
    StorageReference storageReference;

    /*controle*/
    private int RESULT_GALERIA = 222;
    private String CaminhoDaFoto;
    private Uri CaminhoDaFotoUri;
    private String ChaveDaEmpresa = "";
    private String relevanciaDoProduto = "2.0";



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cad_empresa_produto);
        databaseReference = FirebaseDatabase.getInstance().getReference("minha_empresa_produtos");
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            ChaveDaEmpresa = extras.getString("ChaveDaEmpresa");
        }else {
            Toast.makeText(this, "Empresa não localizada", Toast.LENGTH_LONG).show();
            finish();
        }


        /*Recursos de Conectividade*/
        ConnectivityManager cm =
                (ConnectivityManager)this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (! isConnected) {
            Toast.makeText(this, "Você está desconectado", Toast.LENGTH_LONG).show();
        }else{

            storage = FirebaseStorage.getInstance();
            storageReference = storage.getReference();

            firebaseAuth = FirebaseAuth.getInstance();
            Email = firebaseAuth.getCurrentUser().getEmail();

            myTopPostsQuery = FirebaseDatabase.getInstance().getReference("minha_empresa_produtos")
                    .orderByChild("codigoDaEmpresa")
                    .equalTo(ChaveDaEmpresa);

            NomeProdutos= new ArrayList<String>();
            ValorProdutos = new ArrayList<String>();

            myTopPostsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {

                    NomeProdutos.clear();
                    ValorProdutos.clear();
                    MeusProdutosModel produto;
                    for (DataSnapshot d : snapshot.getChildren()) {
                        produto = d.getValue(MeusProdutosModel.class);
                        NomeProdutos.add(produto.getNome());
                        ValorProdutos.add(produto.getValor());
                    }

                    recyclerView = findViewById(R.id.recicler_produtos);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                    adapter = new RecyclerAdapter_MinhaEmpresaProdutos(getApplicationContext(), NomeProdutos, ValorProdutos, ChaveDaEmpresa);
                    recyclerView.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        //Instanciando XML
        NomeProduto = (EditText) findViewById(R.id.nome_produto);
        DescricaoProduto = (EditText) findViewById(R.id.descritivo_produto);
        ValorProduto = (EditText) findViewById(R.id.valor_produto);
        AddFotoProduto = (ImageView) findViewById(R.id.imagemProduto);
        Confirmar = (Button) findViewById(R.id.cadastrarproduto);
        recyclerView = (RecyclerView) findViewById(R.id.recicler_produtos);
        relevanciaProduto = (RatingBar) findViewById(R.id.relevancia_produto);
        Toolbar tlb = (Toolbar) findViewById(R.id.toolbarProduto);


        //Configurando Toolbar
        setSupportActionBar(tlb);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setTitle("Produto");


        //configurando eventos de tela
        relevanciaProduto.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                relevanciaDoProduto = String.valueOf(rating);
                //Toast.makeText(getApplicationContext(), String.valueOf(rating), Toast.LENGTH_LONG).show();
            }
        });

        Confirmar.setOnClickListener(this);
        AddFotoProduto.setOnClickListener(this);
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
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:

                finish(); // Finaliza a Activity atual

                break;

            default:
                break;
        }

        return true;
    }

    @Override
    public void onBackPressed(){

        finish(); // Finaliza a Activity atual

        return;
    }

    private int REQUEST_CODE_READ = 0;
    private int REQUEST_CODE_WRITE = 0;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onClick(View v) {
        if (v == Confirmar){
            if (NomeProduto.getText().toString().equals("")){
                NomeProduto.setError("Nome do produto não foi informado");
            }else if (DescricaoProduto.getText().toString().equals("")){
                DescricaoProduto.setError("Não foi informada nenhuma descrição para o produto");
            }else if (Float.valueOf(ValorProduto.getText().toString()) <= 0){
                ValorProduto.setError("Valor do produto não foi informado");
            }else{
                uploadFoto();
            }



        }

        if (v == AddFotoProduto){
            if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_READ
                );
            } else if (ActivityCompat.checkSelfPermission(v.getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
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

    private void uploadFoto() {
        if (CaminhoDaFotoUri != null) {
            CaminhoDaFoto = NomeProduto.getText().toString().replaceAll("\\s+", "") + ChaveDaEmpresa;
            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Salvando os dados...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageReference
                    .child(
                            "imagesProdutos/"
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

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(getApplicationContext(),
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                    insertProduto();
                                }
                            })


                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(getApplicationContext(),
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

    private void insertProduto() {

        String nome = NomeProduto.getText().toString();
        String descricao = DescricaoProduto.getText().toString();
        String valor = ValorProduto.getText().toString();
        String CodigoDaEmpresa = ChaveDaEmpresa;
        String CodigoDoProduto = CaminhoDaFoto;
        String RelevanciaDoProduto = relevanciaDoProduto;
        String ImagemDoProduto = CaminhoDaFoto;


        //minha_empresa_produtos
        MeusProdutosModel meusProdutosModel = new MeusProdutosModel(CodigoDaEmpresa, CodigoDoProduto, RelevanciaDoProduto, nome, descricao, valor, ImagemDoProduto);
        databaseReference.child(CodigoDoProduto).setValue(meusProdutosModel);
        Toast.makeText(this, "Produto inserido com sucesso", Toast.LENGTH_LONG).show();
        //myTopPostsQuery.addListenerForSingleValueEvent(valueEventListener);

        this.NomeProduto.setText("");
        this.DescricaoProduto.setText("");
        this.ValorProduto.setText("");
        AddFotoProduto.setImageBitmap(null);
        hideSoftKeyboard();


    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) getApplicationContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow( getWindow().getDecorView().getRootView().getWindowToken() , InputMethodManager.HIDE_NOT_ALWAYS);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_GALERIA && resultCode == RESULT_OK){


            CaminhoDaFotoUri = data.getData();
            String[] colunaArquivo = {MediaStore.Images.Media.DATA};

            if (Build.VERSION.SDK_INT >= 29) {
                final Cursor cursor_29 = this.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        colunaArquivo, null, null, MediaStore.Images.ImageColumns.DATE_TAKEN + " DESC");
                if (cursor_29.moveToFirst()) {


                    try  {
                        int ColumnIndex =cursor_29.getColumnIndex(colunaArquivo[0]);
                        String caminhoDaFoto = cursor_29.getString(ColumnIndex);
                        File imgFile = new  File(caminhoDaFoto);
                        if(imgFile.exists()){
                            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                            AddFotoProduto.setImageBitmap(myBitmap);
                        }

                    } catch (Exception ex) {
                        Toast.makeText(this, "Não foi possivel carregar sua imagem " + ex.toString(), Toast.LENGTH_LONG).show();
                        Log.d("IMG_UP", ex.toString());
                    }



                }
            }
            else {

                Cursor cursor = this.getContentResolver().query(CaminhoDaFotoUri, colunaArquivo, null, null, null);
                cursor.moveToFirst();
                int ColumnIndex =cursor.getColumnIndex(colunaArquivo[0]);
                String caminhoDaFoto = cursor.getString(ColumnIndex);
                Bitmap foto = BitmapFactory.decodeFile(caminhoDaFoto);
                if (foto != null) {
                    AddFotoProduto.setImageBitmap(foto);
                }
            }

        }
    }
}
