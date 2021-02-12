package com.joel.a0800restinga.Activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.joel.a0800restinga.R;

public class PoliticaPrivacidadeActivity  extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_politica_privacidade);

        Toolbar tlb = (Toolbar) findViewById(R.id.toolbarPp);

        setSupportActionBar(tlb);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setElevation(1);
        getSupportActionBar().setTitle("Politica de Privacidade");
        WebView webView = (WebView) findViewById(R.id.wv);
        webView.loadUrl("file:///android_asset/politica_privacidade.html");
    }

    @Override
    public void onBackPressed(){


        finish(); // Finaliza a Activity atual

        return;
    }
}