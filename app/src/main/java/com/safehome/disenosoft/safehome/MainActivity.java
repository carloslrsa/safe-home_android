package com.safehome.disenosoft.safehome;

import android.app.Activity;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.safehome.disenosoft.safehome.Servicios.Habitante;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final int MODIFICACION_CODIGORESPUESTA = 0;

    ImageView foto;
    TextView nombre;
    TextView correo;

    TextView cerrarSesion;

    Habitante miHabitante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Bienvenido a Safe Home");

        miHabitante = (Habitante) getIntent().getSerializableExtra("habitante");

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        if(miHabitante.getTipoCuenta() == 0){
            navigationView.getMenu().findItem(R.id.seccionAdministradorMenu).setVisible(false);
        }

        //Set

        View header = navigationView.getHeaderView(0);

        foto = (ImageView) header.findViewById(R.id.fotoMainImageView);
        nombre = (TextView) header.findViewById(R.id.nombreMainTextView);
        correo = (TextView) header.findViewById(R.id.correoMainTextView);

        cerrarSesion = (TextView) findViewById(R.id.cerrarSesionTextView);

        cerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sp = getSharedPreferences("local_vars", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putInt("no_cerrar_sesion",0);
                editor.putString("correo","");
                editor.commit();

                Intent intent = new Intent(getApplication(),LoginActivity.class);

                startActivity(intent);


                finish();
            }
        });

        inicializar();

        //stopService();
        //startService();
    }

    public void startService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService() {
        Intent serviceIntent = new Intent(this, NotificationService.class);
        stopService(serviceIntent);
    }

    private void inicializar(){
        foto.setImageBitmap(miHabitante.getFoto());
        nombre.setText(miHabitante.getNombres() + " " + miHabitante.getApellidos());
        correo.setText(miHabitante.getId());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainLayout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == MODIFICACION_CODIGORESPUESTA && resultCode == RESULT_OK){

            Snackbar.make((DrawerLayout)findViewById(R.id.mainLayout),"Cuenta modificada",Snackbar.LENGTH_LONG).show();

            miHabitante = (Habitante) data.getSerializableExtra("habitante_modificado");
            inicializar();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.accionesHogarItem) {
            Intent intent = new Intent(getApplication(),AccionesHogarActivity.class);
            intent.putExtra("habitante",miHabitante);
            startActivity(intent);
        } else if (id == R.id.configurarCuentaItem) {
            Intent intent = new Intent(getApplication(),ConfigurarCuentaActivity.class);
            intent.putExtra("habitante",miHabitante);
            startActivityForResult(intent,MODIFICACION_CODIGORESPUESTA);
        } else if (id == R.id.gestionarHabitantesItem) {
            Intent intent = new Intent(getApplication(),GestorHabitantesActivity.class);
            intent.putExtra("habitante",miHabitante);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.mainLayout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
