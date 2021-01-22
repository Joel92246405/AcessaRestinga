package com.joel.a0800restinga.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Menu;

import com.joel.a0800restinga.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

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

public class Inicial extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public static final int REQUEST_PERMISSIONS_CODE = 128;

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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        /*

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_informativo, R.id.nav_covid, R.id.nav_leis, R.id.nav_eventos, R.id.nav_news)
                .setDrawerLayout(drawer)
                .build();
*/
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow, R.id.nav_informativo, R.id.nav_covid, R.id.nav_eualugo, R.id.nav_leis, R.id.navsaude)
                .setDrawerLayout(drawer)
                .build();

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
        //getMenuInflater().inflate(R.menu.inicial, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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
/*
    public ArrayList<String> pegarContatos(){

        ArrayList<String> listaDeContatosArray = new ArrayList<String>();

        Uri agenda = ContactsContract.Contacts.CONTENT_URI;
        Cursor cursor = getContentResolver().query(agenda,null,null,null, null);

        while(cursor.moveToNext()){

            String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));



            String contato =nome;

            listaDeContatosArray.add(contato);
        }

        cursor.close();

        return listaDeContatosArray;
    }
    */

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
        //}
        //else{

        //    List<String> contacts = getContactNames(ctx);
        }



        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contacts);
        //lstNames.setAdapter(adapter);
    }


    private MaterialDialog mMaterialDialog;
    private void callDialog(final Activity activity, Context ctx,
                            String message,
                            final String[] permissions ){

        mMaterialDialog = new MaterialDialog( ctx )
                .setTitle( "Permission" )
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
                .setNegativeButton( "Cancel", new View.OnClickListener() {

                    @Override
                    public void onClick( View v ) {

                        mMaterialDialog.dismiss();
                    }
                });

        mMaterialDialog.show();
    }

    /*
    private List<String> getContactNames(Context ctx) {
        List<String> contacts = new ArrayList<String>();
        // Get the ContentResolver
        ContentResolver cr =  ctx.getContentResolver();


        Cursor phones = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null, null);

        while (phones.moveToNext()) {
            String number = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            String nome = phones.getString(phones.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));

        }
        phones.close();

        return contacts;
    }
     */

}