package com.safehome.disenosoft.safehome;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.media.Image;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.drive.Drive;
import com.safehome.disenosoft.safehome.Servicios.ConexionBD;
import com.safehome.disenosoft.safehome.Servicios.Habitante;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class RegistrarHabitanteActivity extends AppCompatActivity {

    LinearLayout rootLayout;

    TextInputLayout nombres;
    TextInputLayout apellidos;
    TextInputLayout correo;

    Button registrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_habitante);

        rootLayout = (LinearLayout) findViewById(R.id.registrarHabitanteLayout);

        nombres  = (TextInputLayout) findViewById(R.id.nombresRegistroInputLayout);
        apellidos  = (TextInputLayout) findViewById(R.id.apellidosRegistroInputLayout);
        correo = (TextInputLayout) findViewById(R.id.correoRegistroInputLayout);

        registrar = (Button) findViewById(R.id.registrarRegistroButton);


        nombres.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                nombres.setError(null);
            }
        });

        apellidos.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                apellidos.setError(null);
            }
        });

        correo.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                correo.setError(null);
            }
        });

        registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean esPosibleRegistrar = true;
                if(nombres.getEditText().getText().length() == 0){
                    nombres.setError("Debe llenar este campo");
                    nombres.setErrorEnabled(true);
                    esPosibleRegistrar = false;
                }
                if(apellidos.getEditText().getText().length() == 0){
                    apellidos.setError("Debe llenar este campo");
                    apellidos.setErrorEnabled(true);
                    esPosibleRegistrar = false;
                }

                if(correo.getEditText().getText().length() == 0){
                    correo.setError("Debe llenar este campo");
                    correo.setErrorEnabled(true);
                    esPosibleRegistrar = false;
                }

                if(esPosibleRegistrar)
                    empezarRegistro();
            }
        });


    }

    private void empezarRegistro(){
        nombres.setEnabled(false);
        apellidos.setEnabled(false);
        correo.setEnabled(false);
        registrar.setEnabled(false);

        Habitante nuevoHabitante = new Habitante(correo.getEditText().getText().toString(),nombres.getEditText().getText().toString(),apellidos.getEditText().getText().toString(),0,true,"","");

        new PruebaTask().execute(nuevoHabitante);

        //Toast.makeText(this, "Registrado nuevo habitante...", Toast.LENGTH_SHORT).show();

        Snackbar.make(rootLayout,"Registrando nuevo habitante...",Snackbar.LENGTH_SHORT).show();
    }

    private void terminarRegistro(Habitante habitante){
        //Toast.makeText(this, "Nuevo habitante registrado", Toast.LENGTH_SHORT).show();

        Intent retorno = new Intent();

        retorno.putExtra("habitante_nuevo",habitante);

        setResult(RESULT_OK,retorno);

        finish();
    }

    private void correoExistente(){
        nombres.setEnabled(true);
        apellidos.setEnabled(true);
        correo.setEnabled(true);
        registrar.setEnabled(true);

        correo.setError("Correo ya existente");
        correo.setErrorEnabled(true);
    }


    public class PruebaTask extends AsyncTask<Habitante,Integer,Habitante>{

        @Override
        protected Habitante doInBackground(Habitante... habitante) {
            return ConexionBD.getInstancia().CrearHabitante(habitante[0]);
        }

        @Override
        protected void onPostExecute(Habitante habitante) {
            if(habitante == null){
                correoExistente();
            }else{
                terminarRegistro(habitante);
            }
        }
    }
}
