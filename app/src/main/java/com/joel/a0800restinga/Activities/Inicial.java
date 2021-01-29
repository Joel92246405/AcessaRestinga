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

import android.widget.ExpandableListView;
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
import com.google.firebase.FirebaseOptions;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.drakeet.materialdialog.MaterialDialog;

//import static com.joel.a0800restinga.MyRecyclerViewAdapter.REQUEST_PERMISSIONS_CODE;

public class Inicial extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener
        /*implements NavigationView.OnNavigationItemSelectedListener*/{

    private AppBarConfiguration mAppBarConfiguration;
    public static final int REQUEST_PERMISSIONS_CODE = 128;
    private View headerView;
    private TextView textTitulo, textDescricao;


    private GoogleSignInClient mGoogleSignInClient;
    private String TAG = "LOGIN";

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;

    private ImageView SignOut, SignIn;
    private TextView Entrar, Sair;
    private int RC_SIGN_IN = 1;
    private Uri Photo;
    private ImageView imageView;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicial);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Envie sua sugestão / solicitação para o meu email", Snackbar.LENGTH_LONG)
                        .setAction("Enviar", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.setData(Uri.parse("mailto"));
                                email.setType("message/rfc822");
                                email.putExtra(Intent.EXTRA_EMAIL,
                                        new String[]{"joelmnascimento88@gmail.com"});
                                email.putExtra(Intent.EXTRA_SUBJECT,
                                        "Sugestão: ");
                                email.putExtra(Intent.EXTRA_TEXT, "Olá " + "");
                                startActivity(Intent.createChooser(email, "ENVIAR E-MAIL"));
                            }
                        }).show();
            }
        });

        /*
        expandableListView = findViewById(R.id.expandableListView);

        prepareMenuData();
        populateExpandableList();
*/

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_meus_cadastros, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_informativo, R.id.nav_estoudoando, R.id.nav_eualugo, R.id.nav_leis, R.id.navsaude)
                .setDrawerLayout(drawer)
                .build();




        headerView = navigationView.getHeaderView(0);



        textTitulo = (TextView) headerView.findViewById(R.id.texttitulo);
        textDescricao = (TextView) headerView.findViewById(R.id.textDescricao);

        SignIn = headerView.findViewById(R.id.efetuarLogin);
        SignOut = headerView.findViewById(R.id.desconectar);
        Entrar = headerView.findViewById(R.id.txtLogin);
        Sair = headerView.findViewById(R.id.txtdesconectar);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.TOKENGOOGLE))
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        if(currentUser == null)
        {
            //Usuário não está logado
            controlaBotoes(1, "", "");
        }
        else
        {
            String Nome = currentUser.getDisplayName();// firebaseAuth.getCurrentUser().getDisplayName();
            String Email = firebaseAuth.getCurrentUser().getEmail();
            //Usuário está logado
            controlaBotoes(0, Nome, Email);


        }

        SignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        SignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mGoogleSignInClient.signOut();
                Toast.makeText(Inicial.this,"Você está desconectado",Toast.LENGTH_SHORT).show();
                controlaBotoes(1, "", "");
            }
        });



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

        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        */

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



    private void signIn(){
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_SIGN_IN){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){
        try{

            GoogleSignInAccount acc = completedTask.getResult(ApiException.class);
            Toast.makeText(Inicial.this,"Login efetuado com sucesso!",Toast.LENGTH_SHORT).show();
            FirebaseGoogleAuth(acc);
        }
        catch (ApiException e){
            Toast.makeText(Inicial.this,"Você está desconectado",Toast.LENGTH_SHORT).show();
            controlaBotoes(1, "", "");
            FirebaseGoogleAuth(null);
        }
    }

    private void FirebaseGoogleAuth(GoogleSignInAccount acct) {
        //check if the account is null
        if (acct != null) {
            AuthCredential authCredential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
            firebaseAuth.signInWithCredential(authCredential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        FirebaseUser user = firebaseAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        updateUI(null);
                    }
                }
            });
        }
        else{
            Toast.makeText(Inicial.this, "Erro", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateUI(FirebaseUser fUser){
        //controlaBotoes(0);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if(account !=  null){
            String personName = account.getDisplayName();
            String personGivenName = account.getGivenName();
            String personFamilyName = account.getFamilyName();
            String personEmail = account.getEmail();
            String personId = account.getId();
            Uri personPhoto = account.getPhotoUrl();
            //Photo = account.getPhotoUrl();
            controlaBotoes(0, personName, personEmail);
            //imageView.setImageURI(Photo);
            //Toast.makeText(Inicial.this,personName + personEmail ,Toast.LENGTH_SHORT).show();
        }else {
            controlaBotoes(1, "", "");
        }

    }

    private void controlaBotoes(int flag, String nome, String descricao){
        if (flag != 1){//conectado
            SignOut.setVisibility(View.VISIBLE);
            Sair.setVisibility(View.VISIBLE);
            SignIn.setVisibility(View.INVISIBLE);
            Entrar.setVisibility(View.INVISIBLE);
            textTitulo.setText(nome);
            textDescricao.setText(descricao);
        }else{//desconectado
            SignOut.setVisibility(View.INVISIBLE);
            Sair.setVisibility(View.INVISIBLE);
            SignIn.setVisibility(View.VISIBLE);
            Entrar.setVisibility(View.VISIBLE);
            textTitulo.setText("App de Utilidade Pública");
            textDescricao.setText("Desenvolvido por Joel Nascimento");
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        return true;


    }


    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


/*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.


        return true;
    }


    private void prepareMenuData() {

        MenuModel menuModel;
        List<MenuModel> childModelsList = new ArrayList<MenuModel>();
        MenuModel childModel;

        menuModel = new MenuModel("Home", true, true, "Home"); //Menu of Java Tutorials
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

        menuModel = new MenuModel("Meus Cadastros", true, true, ""); //Menu of Java Tutorials
        headerList.add(menuModel);
        childModelsList = new ArrayList<MenuModel>();
        childModel = new MenuModel("Telefones", false, false, "CadTelefones");
        childModelsList.add(childModel);

        childModel = new MenuModel("Alugueis", false, false, "CadAlugueis");
        childModelsList.add(childModel);

        childModel = new MenuModel("Doação", false, false, "CadDoação");
        childModelsList.add(childModel);


        if (menuModel.hasChildren) {
            childList.put(menuModel, childModelsList);
        }

        menuModel = new MenuModel("Telefones", true, true, "Telefones"); //Menu of Java Tutorials
        headerList.add(menuModel);
        if (!menuModel.hasChildren) {
            childList.put(menuModel, null);
        }

    }
    */
    /*
    private GalleryFragment galleryFragment = null;
    private void populateExpandableList() {

        expandableListAdapter = new ExpandableListAdapter(this, headerList, childList);
        expandableListView.setAdapter(expandableListAdapter);

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                if (headerList.get(groupPosition).isGroup) {
                    if (!headerList.get(groupPosition).hasChildren) {
                        //WebView webView = findViewById(R.id.webView);
                        //webView.loadUrl(headerList.get(groupPosition).url);
                        onBackPressed();
                    }
                }

                return false;
            }
        });

        expandableListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                if (headerList.get(groupPosition).url.equals("Telefones")){
                    Toast.makeText(v.getContext(), "Teste", Toast.LENGTH_LONG).show();
                }
                return false;
            }
        });



        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {

                if (childList.get(headerList.get(groupPosition)) != null) {
                    MenuModel model = childList.get(headerList.get(groupPosition)).get(childPosition);
                    if (model.url.length() > 0) {
                        //WebView webView = findViewById(R.id.webView);
                        //webView.loadUrl(model.url);
                        //ação

                        String url =model.url;
                        if ("Telefones".equals(url)) {//ação


                        } else if ("Outros".equals(url)) {
                        }


                        onBackPressed();
                    }
                }

                return false;
            }
        });
    }
    */

}