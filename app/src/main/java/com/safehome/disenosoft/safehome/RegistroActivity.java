package com.safehome.disenosoft.safehome;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.safehome.disenosoft.safehome.CustomViews.NonSwipeableViewPager;
import com.safehome.disenosoft.safehome.Servicios.ConexionBD;
import com.safehome.disenosoft.safehome.Servicios.Habitante;


public class RegistroActivity extends AppCompatActivity {

    private SectionsPagerAdapter mSectionsPagerAdapter;

    private static NonSwipeableViewPager mViewPager;

    private Habitante miHabitante;

    public static Activity registroActivity = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        registroActivity = this;

        miHabitante = (Habitante) getIntent().getSerializableExtra("habitante");

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        mViewPager = (NonSwipeableViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";


        public PlaceholderFragment() {
        }

        public static PlaceholderFragment newInstance(int sectionNumber, Habitante miHabitante) {


            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putSerializable("habitante", miHabitante);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            int pagina = getArguments().getInt(ARG_SECTION_NUMBER);

            View rootView = null;
            switch (pagina){
                case 1:{
                    rootView = inflater.inflate(R.layout.fragment_registro_1, container, false);

                    Button empezarButton = (Button) rootView.findViewById(R.id.empezarRegistroButton);

                    empezarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mViewPager.setCurrentItem(1);
                        }
                    });
                    break;
                }
                case 2:{
                    rootView = inflater.inflate(R.layout.fragment_registro_2, container, false);

                    Button siguienteButton = (Button) rootView.findViewById(R.id.primerSiguienteRegistroButton);

                    siguienteButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mViewPager.setCurrentItem(2);
                        }
                    });

                    break;
                }
                case 3:{
                    rootView = inflater.inflate(R.layout.fragment_registro_3, container, false);

                    final Button terminarButton = (Button) rootView.findViewById(R.id.terminarRegistroButton);
                    
                    final EditText pin1 = rootView.findViewById(R.id.primerDigitoPinRegistroEditText);
                    final EditText pin2 = rootView.findViewById(R.id.segundoDigitoPinRegistroEditText);
                    final EditText pin3 = rootView.findViewById(R.id.tercerDigitoPinRegistroEditText);
                    final EditText pin4 = rootView.findViewById(R.id.cuartoDigitoPinRegistroEditText);
                    final LinearLayout pinRegistroLayout = rootView.findViewById(R.id.pinRegistroLayout);
                    final ProgressBar registroProgressBar = rootView.findViewById(R.id.registroProgressBar);

                    pin1.requestFocus();

                    pin1.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            if(i != KeyEvent.KEYCODE_DEL && i != KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_UP)
                                pin2.requestFocus();
                            return false;
                        }
                    });

                    pin2.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            if(keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                if(i != KeyEvent.KEYCODE_DEL && i != KeyEvent.KEYCODE_BACK  && keyEvent.getAction() == KeyEvent.ACTION_UP){
                                    pin3.requestFocus();
                                }
                            }
                            return false;
                        }
                    });

                    pin3.setOnKeyListener(new View.OnKeyListener() {
                        @Override
                        public boolean onKey(View view, int i, KeyEvent keyEvent) {
                            if(keyEvent.getAction() == KeyEvent.ACTION_UP) {
                                if(i != KeyEvent.KEYCODE_DEL && i != KeyEvent.KEYCODE_BACK  && keyEvent.getAction() == KeyEvent.ACTION_UP){
                                    pin4.requestFocus();
                                }
                            }
                            return false;
                        }
                    });
                    
                    terminarButton.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if(pin1.getText().length() > 0 && pin2.getText().length() > 0 && pin3.getText().length() > 0 && pin4.getText().length() > 0){
                                String pinFinal = pin1.getText().toString()+pin2.getText().toString()+pin3.getText().toString()+pin4.getText().toString();
                                final Habitante miHabitante = (Habitante) getArguments().getSerializable("habitante");

                                miHabitante.setPrimeraVez(false);
                                miHabitante.setPin(pinFinal);

                                pin1.setEnabled(false);
                                pin2.setEnabled(false);
                                pin3.setEnabled(false);
                                pin4.setEnabled(false);
                                terminarButton.setEnabled(false);

                                pinRegistroLayout.setVisibility(View.GONE);
                                registroProgressBar.setVisibility(View.VISIBLE);

                                new AsyncTask<Habitante,Integer,Habitante>(){

                                    @Override
                                    protected Habitante doInBackground(Habitante... habitante) {
                                        ConexionBD.getInstancia().ModificarHabitante(habitante[0]);
                                        return habitante[0];
                                    }

                                    @Override
                                    protected void onPostExecute(Habitante habitante) {
                                        super.onPostExecute(habitante);
                                        registroProgressBar.setVisibility(View.GONE);
                                        pinRegistroLayout.setVisibility(View.VISIBLE);
                                        //Toast.makeText(getContext(), "¡Registro exitoso!", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getContext(),MainActivity.class);
                                        intent.putExtra("habitante",habitante);

                                        getContext().startActivity(intent);

                                        registroActivity.finish();

                                    }
                                }.execute(miHabitante);

                            }else{
                                
                            }
                        }
                    });

                    break;
                }
            }

            return rootView;
        }

    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1, miHabitante);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }
    }

}
