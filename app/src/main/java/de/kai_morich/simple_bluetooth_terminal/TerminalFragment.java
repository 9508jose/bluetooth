package de.kai_morich.simple_bluetooth_terminal;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.SimpleDateFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TerminalFragment extends Fragment implements ServiceConnection, SerialListener,View.OnClickListener,DatePickerDialog.OnDateSetListener,TimePickerDialog.OnTimeSetListener {

    int validador=0,validador2=0,validador3=0;


    @Override
    public void onClick(View view) {
        View send_Calcero = view.findViewById(R.id.send_CalCero);

        View sendcalganan = view.findViewById(R.id.send_CalGan);
        View sendTpMuestra = view.findViewById(R.id.send_TpMuestra);

        switch (view.getId()) {
            case R.id.etPlannedDate:



                      showDatePickerDialog();
                      //Intent intent = new Intent(getActivity(), txt_terminal.class);
                      //intent.putExtra("dato", receiveText.getText().toString());


              //  startActivity(intent);
                break;
            case R.id.send_txt:
                Intent intent = new Intent(getActivity(), txt_terminal.class);
                intent.putExtra("dato", receiveText.getText().toString());


                  startActivity(intent);
                break;

            case R.id.ettime:

               showTimePickerDialog();
                break;
            case R.id.send_hora:
                ediTime.setText(selectedhour);
                if(selectedhour=="") {Toast.makeText(getActivity(), "no ha seleccionado una hora", Toast.LENGTH_SHORT).show();   ediTime.setText("00-00-00");}
                else {
                    View sendhora = view.findViewById(R.id.send_hora);
                    sendhora.setOnClickListener(v -> send(
                            "$$HRS "+HoraEnvia+"\\r"
                    ));
                }
                break;
            case R.id.send_fecha:
                etPlannedDate.setText(selectedDate);

                if(selectedDate=="") {Toast.makeText(getActivity(), "no ha seleccionado una fecha", Toast.LENGTH_SHORT).show();   etPlannedDate.setText("00-00-00");}
                else {
                    View senfecha = view.findViewById(R.id.send_fecha);
                    senfecha.setOnClickListener(v -> send(
                            "$$DAT "+ FechaEnvia.toString() + "\\r"));
                }
                break;

            case R.id.actualiza:
                mBluetoothAdapter1.disable();
                SystemClock.sleep(1000);
                Fragment nuevoFragmento = new DevicesFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, nuevoFragmento);

                 mBluetoothAdapter1.enable();
                transaction.addToBackStack(null);
                SystemClock.sleep(1000);
                // Commit a la transacción
                transaction.commit();
                break;

            case R.id.send_CalCero:
                new CountDownTimer(3000, 1000){
                    public void onTick(long millisUntilFinished){
                        //textView.setText(String.valueOf(counter));
                        counter++;

                    }
                    public  void onFinish(){
                        // textView.setText("FINISH!!");
                        validador=0;
                    }
                }.start();
                validador=validador+1;

                if(validador>3)
                {
                validador=0;
                  }
               else  if(validador==3){
                    send_Calcero.setOnClickListener(this);

                 send(
                            "$$ZER\\r");

                }
               else  if(validador>=3)
                {
                    validador=0;
                }

                else{}

                break;
            case R.id.send_TpMuestra:
                new CountDownTimer(3000, 1000){
                    public void onTick(long millisUntilFinished){
                        //textView.setText(String.valueOf(counter));
                        counter++;

                    }
                    public  void onFinish(){
                        // textView.setText("FINISH!!");
                        validador2=0;
                    }
                }.start();
                validador2=validador2+1;
                if(validador2>3) {
                    validador2 = 0;
                }
                else if(validador2==3){
                sendTpMuestra.setOnClickListener(this);

           send(
                        "$$TMU "+cck);}
                else if(validador2>=3){ validador2=0;}
                else{}
                break;


            case R.id.send_CalGan:

                new CountDownTimer(3000, 1000){
                    public void onTick(long millisUntilFinished){
                        //textView.setText(String.valueOf(counter));
                        counter++;

                    }
                    public  void onFinish(){
                       // textView.setText("FINISH!!");
                        validador3=0;
                    }
                }.start();
                validador3=validador3+1;
                if(validador3>3){
                    validador3=0;}
                else if (validador3==3){
                    sendcalganan.setOnClickListener(this);
                    send(
                            "$$GAN\\r");

}
                else  if(validador3>=3)
                {
                    validador3=0;
                }
                break;


        }
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }


    private void showDatePickerDialog() {


        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                String dia="";
                String mes="";
                if(month<=9 ){
                    mes="0"+String.valueOf(month+1);

                }
                else {
                    mes=String.valueOf(month+1);
                }

                if(day<=9 ){
                    dia="0"+String.valueOf(day);

                }
                else {
                    dia=String.valueOf(day);
                }
                selectedDate = dia + " / " + mes + " / " + year;
                String ano = Integer.toString(year);

                FechaEnvia= ano.substring(2,4)+mes+dia;
                etPlannedDate.setText(selectedDate);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }


    private void showTimePickerDialog() {


        TimePickerFragment newFragment = TimePickerFragment.newInstance(new TimePickerDialog.OnTimeSetListener() {
            SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm a");

            public void onTimeSet(TimePicker timePicker, int hora, int minuto) {
                if(hora == 0 ){ hora=1;}
                if(minuto == 0 ){ minuto=1;}
           String min="";
           String hor="";
                if(minuto<=9 ){
                 min="0"+String.valueOf(minuto);

                }
                else{
                    min=String.valueOf(minuto);
                }
                if(hora<=9 ){
                    hor="0"+String.valueOf(hora);
                }
                else{
                    hor=String.valueOf(hora);
                }
                selectedhour = hor+":"+min;
                HoraEnvia=hor+min;
                ediTime.setText(selectedhour);

            }


        });

        newFragment.show(getActivity().getSupportFragmentManager(), "timePicker");
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {

    }


    private enum Connected { False, Pending, True }
    Spinner spinner;
    String selectedDate="";
    String selectedhour="";
    String HoraEnvia;
    String FechaEnvia;
    public TextView mBluetoothStatusText1;
    public BluetoothAdapter mBluetoothAdapter1;
    Button send_button,send_buttonlv;
    EditText etPlannedDate,ediTime;
    TextView Dispositivo;
    private String deviceAddress,devicename;
    private SerialService service;
    public int counter;

    String[] country = { "01", "02", "03", "04", "05","06","07","08","09",
            "10"
    };


    private TextView receiveText,Hora,Fecha;

    private TextView sendText,send_hora,send_fecha,send_dis_on,send_baja_datos,send_txt,send_delete,send_cal_cero;
    private ImageView Refresh;
    private TextUtil.HexWatcher hexWatcher;
     Button btnShowTimePicker,send_cal,send_Tp_Muestra,send_cal_ganancia;
    private Connected connected = Connected.False;
    private boolean initialStart = true;
    private boolean hexEnabled = false;
    private boolean pendingNewline = false;
    private String newline = TextUtil.newline_crlf;
    private String Dato="",cck;
    private SpinnerAdapter oAdapter;
    Timer timer = new Timer();

    Handler handler = new Handler(); // En esta zona creamos el objeto Handler


    /*
     * Lifecycle
     */


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setRetainInstance(true);
        deviceAddress = getArguments().getString("device");
        devicename = getArguments().getString("name");
        mBluetoothAdapter1 = BluetoothAdapter.getDefaultAdapter();
        Timer timer = new Timer();
        //Que actue cada 3000 milisegundos
        //Empezando des de el segundo 0
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //La función que queremos ejecutar
                FuncionParaEsteHilo();
            }
        }, 0, 1000);
  }


    @Override
    public void onDestroy() {
        if (connected != Connected.False)
            disconnect();
        getActivity().stopService(new Intent(getActivity(), SerialService.class));
        super.onDestroy();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(service != null)
            service.attach(this);
        else
            getActivity().startService(new Intent(getActivity(), SerialService.class)); // prevents service destroy on unbind from recreated activity caused by orientation change
        Dispositivo.setText("Disp conec: "+devicename);
    }

    @Override
    public void onStop() {
        if(service != null && !getActivity().isChangingConfigurations())
            service.detach();
        super.onStop();
    }

    @SuppressWarnings("deprecation") // onAttach(context) was added with API 23. onAttach(activity) works for all API versions
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        getActivity().bindService(new Intent(getActivity(), SerialService.class), this, Context.BIND_AUTO_CREATE);

    }

    @Override
    public void onDetach() {
        try { getActivity().unbindService(this); } catch(Exception ignored) {}
        super.onDetach(); }

    @Override
    public void onResume() {
        super.onResume();
        if(initialStart && service != null) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder binder) {
        service = ((SerialService.SerialBinder) binder).getService();
        service.attach(this);
        if(initialStart && isResumed()) {
            initialStart = false;
            getActivity().runOnUiThread(this::connect);
        }
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        service = null;
    }
    public void checkbluetooth(){
        if(mBluetoothAdapter1 == null)
            Dispositivo.setText("Bluetooth Desconectado");

        else if(!mBluetoothAdapter1.isEnabled()){}

        else{

        }

    }

    private final int TIEMPO = 100;

    public void ejecutarTarea() {
        handler.postDelayed(new Runnable() {
            public void run() {

                // función a ejecutar
                checkbluetooth(); // función para refrescar la ubicación del conductor, creada en otra línea de código

                handler.postDelayed(this, TIEMPO);
            }

        }, TIEMPO);

    }

    /*
     * UI
     */
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_terminal, container, false);
       // mBluetoothAdapter1.enable();
        receiveText = view.findViewById(R.id.receive_text);
        receiveText.setTextColor(getResources().getColor(R.color.colorRecieveText)); // set as default color to reduce number of spans
        receiveText.setMovementMethod(ScrollingMovementMethod.getInstance());

        spinner  = (Spinner) view.findViewById(R.id.spinner1);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),android.R.layout.simple_list_item_1,country);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(arrayAdapter);

        Hora = view.findViewById(R.id.ettime);
        Fecha = view.findViewById(R.id.etPlannedDate);

        ejecutarTarea();
        etPlannedDate = (EditText) view.findViewById(R.id.etPlannedDate);
        Dispositivo =(TextView) view.findViewById(R.id.dispositivo);
        Dispositivo.setOnClickListener(this);
        etPlannedDate.setOnClickListener(this);
        Refresh=(ImageView) view.findViewById(R.id.actualiza);
        Refresh.setOnClickListener(this);
        ediTime = (EditText) view.findViewById(R.id.ettime);
        ediTime.setOnClickListener(this);
        send_button = (Button) view.findViewById(R.id.send_hora);
        send_button.setOnClickListener(this);
        send_cal = (Button) view.findViewById(R.id.send_CalCero);
        send_cal.setOnClickListener(this);
        send_cal_ganancia = (Button) view.findViewById(R.id.send_CalGan);
        send_cal_ganancia.setOnClickListener(this);
        send_Tp_Muestra = (Button) view.findViewById(R.id.send_TpMuestra);
        send_Tp_Muestra.setOnClickListener(this);
        send_buttonlv = (Button) view.findViewById(R.id.send_fecha);
        send_buttonlv.setOnClickListener(this);
        send_txt = (Button) view.findViewById(R.id.send_txt);
        send_txt.setOnClickListener(this);
//////////////////////////////////////////////////////////////////////////

        Timer timer = new Timer();
        //Que actue cada 3000 milisegundos
        //Empezando des de el segundo 0
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                //La función que queremos ejecutar
                FuncionParaEsteHilo();
            }
        }, 0, 1000);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cck =  parent.getItemAtPosition(position).toString();

                System.out.println("Value was --->" + cck );

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });






////////////////////////////////////////////////////////////////////////////////////////////////////
        View send_dis_on = view.findViewById(R.id.send_dis_on);
        send_dis_on.setOnClickListener(this);
       // Dato="DSP\\r";
        send_dis_on.setOnClickListener(v -> send(
                "$$DSP\\r"));
////////////////////////////////////////////////////////////////////////////////////////////////////
        View send_baja_dato = view.findViewById(R.id.send_bajadatos);
        send_baja_dato.setOnClickListener(this);
     //   Dato="$$DOW\\r";
        send_baja_dato.setOnClickListener(v -> send(
                "$$DOW\\r"));

////////////////////////////////////////////////////////////////////////////////////////////////////
        View senddelete = view.findViewById(R.id.send_datos_delete);
        senddelete.setOnClickListener(this);
      //  Dato="$$DEL\\r";
        senddelete.setOnClickListener(v -> send(
                "$$DEL\\r"));
////////////////////////////////////////////////////////////////////////////////////////////////////

      //  Dato="$$ZER\\r";

      //  Dato="$$ZER\\r";

////////////////////////////////////////////////////////////////////////////////////////////////////
     //   Dato="$$GAN\\r";





        return view;

    }
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position, long id) {
        Toast.makeText(getContext(),country[position] , Toast.LENGTH_LONG).show();
    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void FuncionParaEsteHilo()
    {
        //Esta función es llamada des de dentro del Timer
        //Para no provocar errores ejecutamos el Accion
        //Dentro del mismo Hilo
        //en este caso mostrar un mensaje.
        checkbluetooth();

    }


    private Runnable Accion = new Runnable() {
        public void run() {
            //Aquí iría lo que queramos que haga,
            //en este caso mostrar un mensaje.
            checkbluetooth();

        }};

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_terminal, menu);
        menu.findItem(R.id.hex).setChecked(hexEnabled);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.clear) {
            receiveText.setText("");
            return true;
        } else if (id == R.id.newline) {
            String[] newlineNames = getResources().getStringArray(R.array.newline_names);
            String[] newlineValues = getResources().getStringArray(R.array.newline_values);
            int pos = java.util.Arrays.asList(newlineValues).indexOf(newline);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Newline");
            builder.setSingleChoiceItems(newlineNames, pos, (dialog, item1) -> {
                newline = newlineValues[item1];
                dialog.dismiss();
            });
            builder.create().show();
            return true;
        } else if (id == R.id.hex) {
            hexEnabled = !hexEnabled;

            hexWatcher.enable(hexEnabled);

            item.setChecked(hexEnabled);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    /*
     * Serial + UI
     */
    private void connect() {
        try {
            BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            BluetoothDevice device = bluetoothAdapter.getRemoteDevice(deviceAddress);
            status("connecting...");
            connected = Connected.Pending;
            SerialSocket socket = new SerialSocket(getActivity().getApplicationContext(), device);
            service.connect(socket);
        } catch (Exception e) {
            onSerialConnectError(e);
        }
    }


    private void disconnect() {
        connected = Connected.False;
        service.disconnect();
        Dispositivo.setText("Bluetooth Desconectado");
    }

    private void send(String str) {
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();


        if(connected != Connected.True) {
            Toast.makeText(getActivity(), "not connected", Toast.LENGTH_SHORT).show();

            return;
        }
        try {
            String msg;
            byte[] data;
            if(hexEnabled) {
                StringBuilder sb = new StringBuilder();
                TextUtil.toHexString(sb, TextUtil.fromHexString(str));
                TextUtil.toHexString(sb, newline.getBytes());
                msg = sb.toString();
                data = TextUtil.fromHexString(msg);
            } else {
                msg = str;
                data = (str + newline).getBytes();
            }
            SpannableStringBuilder spn = new SpannableStringBuilder(msg + '\n');
          //  Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
            spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            receiveText.append(spn);
            service.write(data);
        } catch (Exception e) {
            onSerialIoError(e);
        }
       // FileOutputStream fos = service.getApplicationContext().openFileOutput("", getActivity().MODE_PRIVATE);


}





    private void receive(byte[] data) {
        if(hexEnabled) {
            receiveText.append(TextUtil.toHexString(data) + '\n');
        } else {
            String msg = new String(data);
            if(newline.equals(TextUtil.newline_crlf) && msg.length() > 0) {
                // don't show CR as ^M if directly before LF
                msg = msg.replace(TextUtil.newline_crlf, TextUtil.newline_lf);
                // special handling if CR and LF come in separate fragments
                if (pendingNewline && msg.charAt(0) == '\n') {
                    Editable edt = receiveText.getEditableText();
                    if (edt != null && edt.length() > 1)
                        edt.replace(edt.length() - 2, edt.length(), "");
                }
                pendingNewline = msg.charAt(msg.length() - 1) == '\r';
            }
            receiveText.append(TextUtil.toCaretString(msg, newline.length() != 0));
        }
    }

    private void status(String str) {
        SpannableStringBuilder spn = new SpannableStringBuilder(str + '\n');
        spn.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)), 0, spn.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        receiveText.append(spn);

    }


    @Override
    public void onSerialConnect() {
        status("connected");
        connected = Connected.True;
    }

    @Override
    public void onSerialConnectError(Exception e) {
        status("connection failed: " + e.getMessage());
        disconnect(); }

    @Override
    public void onSerialRead(byte[] data) {
        receive(data);
    }

    @Override
    public void onSerialIoError(Exception e) {
        status("connection lost: " + e.getMessage());
        disconnect();
    }

    private void bluetoothNotAvailable() {
        // mBluetoothStatusBtn.setImageResource(R.drawable.grey_button);
        mBluetoothStatusText1.setText("Bluetooth is not available");

    }

    private void bluetoothEnabled() {
        // mBluetoothStatusBtn.setImageResource(R.drawable.green_button);
        mBluetoothStatusText1.setText("Bluetooth is ON");
    }

    private void bluetoothDisabled() {
        // mBluetoothStatusBtn.setImageResource(R.drawable.red_button);
        mBluetoothStatusText1.setText("Bluetooth is OFF");
    }



}
