package com.joel.a0800restinga.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.joel.a0800restinga.R;
import com.joel.a0800restinga.ui.cadastros.estoudoando.CadastroEstouDoandoFragment;
import com.joel.a0800restinga.ui.cadastros.eualugo.CadastroEuAlugoFragment;
import com.joel.a0800restinga.ui.cadastros.telefones.CadastroTelefonesFragment;

public class MeusCadastros extends AppCompatActivity  implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigationView;
    private TextView textoinicial;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meuscadastros);

        Toolbar tlb = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(tlb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(1);
        textoinicial = (TextView) findViewById(R.id.telainicial);


        navigationView = (BottomNavigationView) findViewById(R.id.navigationView);
        navigationView.setOnNavigationItemSelectedListener(this);

    }

    @Override
    public void onBackPressed(){


        finish(); // Finaliza a Activity atual

        return;
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
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser =firebaseAuth.getCurrentUser();
        if (currentUser == null){
            Toast.makeText(getApplicationContext(), "Você deve estar logado no aplicativo", Toast.LENGTH_LONG).show();
        }else {
            String Email = firebaseAuth.getCurrentUser().getEmail();
            textoinicial.setText("");
            switch (item.getItemId()) {
                case R.id.navigation_telefones: {
                    getSupportActionBar().setTitle("Telefones");
                    Fragment fragment = CadastroTelefonesFragment.newInstance();
                    openFragment(fragment);
                    break;
                }
                case R.id.navigation_eualugo: {
                    getSupportActionBar().setTitle("Eu Alugo");
                    Fragment fragment = CadastroEuAlugoFragment.newInstance();
                    openFragment(fragment);
                    break;
                }
                case R.id.navigation_estoudoando: {
                    getSupportActionBar().setTitle("Estou Doando");
                    Fragment fragment = CadastroEstouDoandoFragment.newInstance();
                    openFragment(fragment);
                    break;
                }
            }
        }
        return true;
    }
    private void openFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}