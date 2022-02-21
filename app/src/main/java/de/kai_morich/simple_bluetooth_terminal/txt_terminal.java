package de.kai_morich.simple_bluetooth_terminal;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class txt_terminal extends AppCompatActivity {
String dato;
TextView Ruta;
EditText direccion, file_name;
    ImageView Regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_txt_terminal);

        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
                        PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,},
                    1000);
        }

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
        != PackageManager.PERMISSION_GRANTED){
            requestPermissions(new  String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1000);
        }
        dato =getIntent().getStringExtra("dato");
        Button guarda = (Button) findViewById(R.id.guardar);
        TextView valorDato = (TextView) findViewById(R.id.datoo);
       Ruta = (TextView) findViewById(R.id.textView7);
        direccion = (EditText) findViewById(R.id.txt_carpeta);
        Regresar = (ImageView) findViewById(R.id.imageView3);
        file_name = (EditText) findViewById(R.id.txt_name);
        String palabra2 = file_name.getText().toString();

        valorDato.setText(dato);


        direccion.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                Ruta.setText("/Documents/"+direccion.getText().toString());


            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });

        Regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(txt_terminal.this,Principal.class);



                startActivity(intent);

            }

        });





        guarda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String filedate = dato.toString();
                if(file_name.getText().toString().trim().equals("")) {
                    Toast.makeText(txt_terminal.this, "Se necesita ingresar  el Nombre!", Toast.LENGTH_LONG).show();

                    return;
                }
              else{
                    Toast.makeText(txt_terminal.this, "Se guardara en "+Ruta.getText().toString(), Toast.LENGTH_LONG).show();
               savetxtfile(file_name.getText().toString(),dato,direccion.getText().toString());}
               // savetest();
            }
        });


    }

public void savetxtfile(String filename,String datos,String Directory){
    try {


        File path = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Documents/"+Directory);
        if (!path.exists()) {
            path.mkdirs();
        }
        File myFile = new File(path, filename+".txt");
        FileOutputStream fOut = new FileOutputStream(myFile,true);
        OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
        myOutWriter.append(datos);
        myOutWriter.close();
        fOut.close();

        Toast.makeText(this,"Text file Saved !",Toast.LENGTH_LONG).show();
    }

    catch (java.io.IOException e) {

        //do something if an IOException occurs.
        Toast.makeText(this,"ERROR - Text could't be added",Toast.LENGTH_LONG).show();
    }


}


public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        switch (requestCode){
            case 1000:
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(this,"Permisos concedidos",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Permisos denegados",Toast.LENGTH_SHORT).show();
                finish();
            }
        }
}
}