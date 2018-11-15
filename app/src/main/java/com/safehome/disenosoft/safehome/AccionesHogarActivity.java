package com.safehome.disenosoft.safehome;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.safehome.disenosoft.safehome.Servicios.ConexionBD;

public class AccionesHogarActivity extends AppCompatActivity {

    WebView camara;

    Button abrirPuerta;

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

        final EditText ip = (EditText) findViewById(R.id.ipEditText);

        abrirPuerta = (Button) findViewById(R.id.abrirPuertaButton);

        camara.loadUrl("http://192.168.1.13:5000");
        //Toast.makeText(AccionesHogarActivity.this, "Cargando " + ip.getText().toString(), Toast.LENGTH_SHORT).show();

        abrirPuerta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //camara.loadUrl(ip.getText().toString());
                //Toast.makeText(AccionesHogarActivity.this, "Cargando " + ip.getText().toString(), Toast.LENGTH_SHORT).show();
                new AbrirPuertaTask().execute();
            }
        });


    }
    
    private void PuertaAbierta(){
        Toast.makeText(this, "La puerta se abrirá de inmediato", Toast.LENGTH_SHORT).show();
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

    public class AbrirPuertaTask extends AsyncTask<Void, Integer, Boolean>{

        @Override
        protected Boolean doInBackground(Void... voids) {
            ConexionBD.getInstancia().AbrirPuerta();
            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            PuertaAbierta();
        }
    }

}
