package de.kai_morich.simple_bluetooth_terminal;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class configuraciones extends AppCompatActivity {

    private BluetoothAdapter mBluetoothAdapter;
    Button Bluetooth_on,Bluetooth_off;
    private ImageButton mBluetoothStatusBtn;
    ImageView Regresar;
    private TextView mBluetoothStatusText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuraciones);
         Bluetooth_off =(Button) findViewById(R.id.bluetooth_Off);
        Bluetooth_on =(Button) findViewById(R.id.bluetooth_On);
        Regresar = (ImageView) findViewById(R.id.back1);

        mBluetoothStatusText = (TextView) findViewById(R.id.bluetooth_status);
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            bluetoothNotAvailable();
        } else {
            if (mBluetoothAdapter.isEnabled()) {
                bluetoothEnabled();
            } else {
                bluetoothDisabled();
            }

            Bluetooth_off.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {


                        mBluetoothAdapter.disable();
                        bluetoothDisabled();
                        Toast.makeText(configuraciones.this, "Bluetooth disabled", Toast.LENGTH_LONG).show();

                }
            });
            Bluetooth_on.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                        Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivityForResult(enableBtIntent, 1);

                }
            });

            Regresar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(configuraciones.this,Principal.class);



                    startActivity(intent);

                }

            });
        }
    }

    private void bluetoothNotAvailable() {
       // mBluetoothStatusBtn.setImageResource(R.drawable.grey_button);
        mBluetoothStatusText.setText("Bluetooth is not available");
        mBluetoothStatusBtn.setEnabled(false);
    }

    private void bluetoothEnabled() {
       // mBluetoothStatusBtn.setImageResource(R.drawable.green_button);
        mBluetoothStatusText.setText("Bluetooth is ON");
    }

    private void bluetoothDisabled() {
       // mBluetoothStatusBtn.setImageResource(R.drawable.red_button);
        mBluetoothStatusText.setText("Bluetooth is OFF");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                Toast.makeText(this, "Bluetooth Habilitado", Toast.LENGTH_LONG).show();
                bluetoothEnabled();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "User canceled", Toast.LENGTH_LONG).show();
            }
        }
    }

}
