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

    List<Bitmap> fotosBitmap = null;
    ImageView[] fotos;

    LinearLayout rootLayout;

    TextInputLayout nombres;
    TextInputLayout apellidos;
    TextInputLayout correo;

    Button registrar;
    Button subirFotos;
    Button rotarFotos;

    int anguloRotacion = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_habitante);

        rootLayout = (LinearLayout) findViewById(R.id.registrarHabitanteLayout);

        nombres  = (TextInputLayout) findViewById(R.id.nombresRegistroInputLayout);
        apellidos  = (TextInputLayout) findViewById(R.id.apellidosRegistroInputLayout);
        correo = (TextInputLayout) findViewById(R.id.correoRegistroInputLayout);

        subirFotos = (Button) findViewById(R.id.subirFotografiasRegistroButton);
        registrar = (Button) findViewById(R.id.registrarRegistroButton);
        rotarFotos = (Button) findViewById(R.id.rotarFotografiasRegistroButton2) ;

        registrar.setEnabled(false);
        rotarFotos.setEnabled(false);

        fotos = new ImageView[10];

        fotos[0] = (ImageView) findViewById(R.id.fotoRegistro1);
        fotos[1] = (ImageView) findViewById(R.id.fotoRegistro2);
        fotos[2] = (ImageView) findViewById(R.id.fotoRegistro3);
        fotos[3] = (ImageView) findViewById(R.id.fotoRegistro4);
        fotos[4] = (ImageView) findViewById(R.id.fotoRegistro5);
        fotos[5] = (ImageView) findViewById(R.id.fotoRegistro6);
        fotos[6] = (ImageView) findViewById(R.id.fotoRegistro7);
        fotos[7] = (ImageView) findViewById(R.id.fotoRegistro8);
        fotos[8] = (ImageView) findViewById(R.id.fotoRegistro9);
        fotos[9] = (ImageView) findViewById(R.id.fotoRegistro10);

        subirFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Selecciona tus fotos"), 1);
            }
        });

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

                if(fotosBitmap == null){
                    //Toast.makeText(RegistrarHabitanteActivity.this, "Asegúrate de subir las fotos correctamente", Toast.LENGTH_SHORT).show();
                    Snackbar.make(rootLayout,"Asegúrate de subir las fotos correctamente", Toast.LENGTH_LONG).show();

                    esPosibleRegistrar = false;
                }


                if(esPosibleRegistrar)
                    empezarRegistro();
            }
        });

        rotarFotos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                anguloRotacion += 90;
                if(anguloRotacion == 360)
                    anguloRotacion = 0;
                for(int i=0; i<10; i++){
                    Bitmap aux = fotosBitmap.get(i);
                    aux = rotateImage(aux, anguloRotacion);
                    aux = Bitmap.createScaledBitmap(aux, 300, 400, true);
                    fotosBitmap.set(i,aux);
                    fotos[i].setImageBitmap(aux);
                }
            }
        });
    }

    private void empezarRegistro(){
        nombres.setEnabled(false);
        apellidos.setEnabled(false);
        correo.setEnabled(false);
        subirFotos.setEnabled(false);
        registrar.setEnabled(false);
        rotarFotos.setEnabled(false);

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
        subirFotos.setEnabled(true);
        registrar.setEnabled(true);

        correo.setError("Correo ya existente");
        correo.setErrorEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data != null && data.getClipData() != null){
            if(data.getClipData().getItemCount() >= 10){
                try {
                    fotosBitmap = new ArrayList<Bitmap>();
                    for(int i=0; i<data.getClipData().getItemCount(); i++){
                        Uri imagenUri = data.getClipData().getItemAt(i).getUri();

                        InputStream imageStream = getContentResolver().openInputStream(imagenUri);
                        Bitmap foto = BitmapFactory.decodeStream(imageStream);

                        try {
                            foto = rotateImageIfRequired(foto, imagenUri);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        foto = Bitmap.createScaledBitmap(foto, 450, 600, true);

                        fotos[i].setImageBitmap(foto);

                        fotosBitmap.add(foto);
                    }
                   // new PruebaTask().execute(fotos.toArray(new Bitmap[fotos.size()]));
                    registrar.setEnabled(true);
                    rotarFotos.setEnabled(true);

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }else{
                Toast.makeText(this, "Debes cargar al menos 10 fotografías", Toast.LENGTH_SHORT).show();

            }
        }else{
            Toast.makeText(this, "Debes cargar al menos 10 fotografías", Toast.LENGTH_SHORT).show();
        }
    }

    private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        InputStream input = getContentResolver().openInputStream(selectedImage);
        ExifInterface ei;
        if (Build.VERSION.SDK_INT > 23)
            ei = new ExifInterface(input);
        else
            ei = new ExifInterface(selectedImage.getPath());

        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                anguloRotacion = 90;
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                anguloRotacion = 180;
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                anguloRotacion = 270;
                return rotateImage(img, 270);
            default:
                return img;
        }
    }


    private Bitmap rotateImage(Bitmap img, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        Bitmap rotatedImg = Bitmap.createBitmap(img, 0, 0, img.getWidth(), img.getHeight(), matrix, true);
        img.recycle();
        return rotatedImg;
    }


    public class PruebaTask extends AsyncTask<Habitante,Integer,Habitante>{

        @Override
        protected Habitante doInBackground(Habitante... habitante) {
            return ConexionBD.getInstancia().CrearHabitante(habitante[0],fotosBitmap);
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
