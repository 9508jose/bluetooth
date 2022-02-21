package de.kai_morich.simple_bluetooth_terminal;

import android.Manifest;
import android.annotation.SuppressLint;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.ui.AppBarConfiguration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;


public class Principal extends AppCompatActivity  implements FragmentManager.OnBackStackChangedListener  {
    String Valor;
    Button envia;
    TextView valorDato;
    private int year;
    private int month;
    private int day;
    ConnectionClass connectionClass;
    private BluetoothAdapter mAdapter;
    private BluetoothDevice mDevice;
    private static final int ACCESS_REQUEST_CODE=100;
    private static final int REQUEST_ENABLE_CODE=1;
    private AudioRecord mRecord;
    private TextView mBluetoothStatusText;
    private String myString = "hello";
    private AppBarConfiguration appBarConfiguration;
    NavigationView navigationView;

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_principal);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }
        if (mAdapter!=null && mAdapter.isEnabled()){

        }
        else {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
         //  startActivityForResult(enableIntent, REQUEST_ENABLE_CODE);
        }

        setUpToolbar();

        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId())
                {
                    case  R.id.Home_main:{


                        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                            getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                        }

                        if (mBluetoothAdapter == null) {
                            Toast.makeText(Principal.this, "El Bluetooth no esta activado", Toast.LENGTH_LONG).show();

                        } else if (!mBluetoothAdapter.isEnabled()) {
                              Toast.makeText(Principal.this, "El Bluetooth no esta activado", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(Principal.this, "El Bluetooth esta habilitado", Toast.LENGTH_LONG).show();
                             for (Fragment fragment : getSupportFragmentManager().getFragments()) {
                               getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                            }
                            getSupportFragmentManager().beginTransaction().add(R.id.fragment, new DevicesFragment(), "devices").commit();
                            DrawerLayout mDrawerLayout;
                             mDrawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
                            mDrawerLayout.closeDrawers();
                        }





                       // startActivity(intent);
                       // setSupportActionBar(toolbar);
                       // getSupportFragmentManager().addOnBackStackChangedListener(this);
                        //if (savedInstanceState == null){}



                    }

                    break;


                    case  R.id.Vehiculos:{

                        Intent intent = new Intent(Principal.this,configuraciones.class);



                        startActivity(intent);

                    }
                    break;


                }
                return false;
            }
        });

    }



    public void setUpToolbar() {
        drawerLayout = findViewById(R.id.drawerLayout);
        Toolbar toolbar = findViewById(R.id.toolbar1);
        // setSupportActionBar(toolbar);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
       actionBarDrawerToggle.syncState();


    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_ENABLE_CODE){
            if (resultCode==RESULT_OK){
                Toast.makeText(Principal.this, "El Bluetooth esta activado", Toast.LENGTH_LONG).show();
            }
            else if (resultCode==RESULT_CANCELED){
                Toast.makeText(Principal.this, "El Bluetooth no esta activado", Toast.LENGTH_LONG).show();
               // finish();
            }
        }}


    public void onBackStackChanged() {
        getSupportActionBar().setDisplayHomeAsUpEnabled(getSupportFragmentManager().getBackStackEntryCount()>0);
    }

    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public class DoLogin extends AsyncTask<String, String, String> {
        String z = "";
        Boolean isSuccess = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onPostExecute(String r) {


        }

        @SuppressLint("WrongThread")
        @Override
        protected String doInBackground(String... params) {

            try {


                Connection con1 = new ConnectionClass().CONN();

                Statement stmt1 = con1.createStatement();
                String query1 = "select Email,Contrasena from User_email where Email='" + "Correo" + "'";
                ResultSet rs1 = stmt1.executeQuery(query1);
                if (rs1.next()) {


                } else {

                    isSuccess = false;
                }


            } catch (Exception ex) {
                isSuccess = false;
                z = "Exceptions";
            }

          return z;
        }


    }
    }

