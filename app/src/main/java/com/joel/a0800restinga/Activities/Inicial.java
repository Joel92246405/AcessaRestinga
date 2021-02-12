package com.joel.a0800restinga.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.annotations.Nullable;
import com.joel.a0800restinga.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import me.drakeet.materialdialog.MaterialDialog;

//import static com.joel.a0800restinga.MyRecyclerViewAdapter.REQUEST_PERMISSIONS_CODE;

public class Inicial extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        /*implements NavigationView.OnNavigationItemSelectedListener*/{

    public static final String SP_KEY_EMAIL = "";
    public static final String SP_NAME = "";
    private AppBarConfiguration mAppBarConfiguration;
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private View headerView;



    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "LOGIN";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private ImageView imagemLogin;
    private TextView tSair, textNome;
    private int RC_SIGN_IN = 1;
    private Uri Photo;
    private ImageView imageView;
    private boolean cadastrado;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);





        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_meus_cadastros, R.id.nav_telefones, R.id.nav_vocesabia, R.id.nav_transporte, R.id.nav_estoudoando, R.id.nav_eualugo, R.id.nav_leis, R.id.navsaude)
                .setDrawerLayout(drawer)
                .build();




        headerView = navigationView.getHeaderView(0);



        tSair = headerView.findViewById(R.id.textSair);
        textNome = headerView.findViewById(R.id.textDescricao);
        FirebaseApp.initializeApp(this);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        tSair.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                controlaBotoes(1, "", "");
            }
        });

        if(currentUser == null)
        {
            controlaBotoes(1, "", "");
        }
        else
        {
            String Nome = currentUser.getEmail();
            controlaBotoes(0, Nome, "");
        }





        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);



        try {
            listaDeContatos(this, this.getApplicationContext());
        }catch (Exception ex){
            Log.d("INICIALIZACAO", ex.toString());
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();



    }

    private int  contador = 0;
    @Override
    public void onBackPressed() {

        contador += 1;
        if (contador == 1) {
            Toast.makeText(this, "Pressione novamente para sair", Toast.LENGTH_LONG).show();
        }else {
            Intent setIntent = new Intent(Intent.ACTION_MAIN);
            setIntent.addCategory(Intent.CATEGORY_HOME);
            setIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(setIntent);
        }


    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String[] permissions,
            int[] grantResults ){



        switch( requestCode ){
            case REQUEST_PERMISSIONS_CODE:
                for( int i = 0; i < permissions.length; i++ ){

                    if( permissions[i].equalsIgnoreCase( Manifest.permission.READ_CONTACTS )
                            && grantResults[i] == PackageManager.PERMISSION_GRANTED ){

                        //pegarContatos();
                    }
                }
        }

        super.onRequestPermissionsResult(
                requestCode,
                permissions,
                grantResults
        );
    }

    private void listaDeContatos(Activity activity, Context ctx) {

        if( ContextCompat.checkSelfPermission( ctx, Manifest.permission.READ_CONTACTS ) != PackageManager.PERMISSION_GRANTED ){

            if( ActivityCompat.shouldShowRequestPermissionRationale( activity, Manifest.permission.READ_CONTACTS ) ){

                callDialog(this,
                        this,
                        "É preciso a permissão de acesso aos contatos para utilizar algumas funções do App",
                        new String[]{
                                Manifest.permission.READ_CONTACTS
                        }
                );
            }
            else{

                ActivityCompat.requestPermissions(
                        this,
                        new String[]{
                                Manifest.permission.READ_CONTACTS
                        },
                        REQUEST_PERMISSIONS_CODE
                );
            }
        }

    }


    private MaterialDialog mMaterialDialog;
    private void callDialog(final Activity activity, Context ctx,
                            String message,
                            final String[] permissions ){

        mMaterialDialog = new MaterialDialog( ctx )
                .setTitle( "Premissão" )
                .setMessage( message )
                .setPositiveButton( "Ok", new View.OnClickListener() {

                    @Override
                    public void onClick( View v) {

                        ActivityCompat.requestPermissions(
                                activity,
                                permissions,
                                REQUEST_PERMISSIONS_CODE
                        );

                        mMaterialDialog.dismiss();
                    }
                })
                .setNegativeButton( "Cancelar", new View.OnClickListener() {

                    @Override
                    public void onClick( View v ) {

                        mMaterialDialog.dismiss();
                    }
                });

        mMaterialDialog.show();
    }





    private void controlaBotoes(int flag, String nome, String descricao){

        if (nome.length() <= 0){
            tSair.setVisibility(View.GONE);
            textNome.setText("Acessa Restinga");
        }else{
            textNome.setText(nome);
            tSair.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}