package com.safehome.disenosoft.safehome;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class AccionesHogarActivity extends AppCompatActivity {

    WebView camara;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acciones_hogar);
        Toolbar toolbar = (Toolbar) findViewById(R.id.accionesToolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        setTitle("Acciones del hogar");


        camara = (WebView)findViewById(R.id.camaraWebView);

        camara.loadUrl("http://190.235.213.122:5000");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       //Toast.makeText(this, "adios", Toast.LENGTH_SHORT).show();
        camara.loadUrl("");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
