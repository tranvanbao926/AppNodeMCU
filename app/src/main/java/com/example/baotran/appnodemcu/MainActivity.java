package com.example.baotran.appnodemcu;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    Button btnLed1, btnLed2, btnLed3, btnAll;
    TextView txtLdr;
    EditText txtResultAll;

    Handler handler = new Handler();

    boolean statusReceipt = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLed1 = (Button) findViewById(R.id.btnLed1);
        btnLed2 = (Button) findViewById(R.id.btnLed2);
        btnLed3 = (Button) findViewById(R.id.btnLed3);
        btnAll = (Button) findViewById(R.id.btnAll);

        txtLdr = (TextView) findViewById(R.id.txtLdr);
        txtResultAll = (EditText) findViewById(R.id.txtResultAll);

        handler.postDelayed(updateStatus, 0);

        btnLed1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request("led1");
            }
        });
        btnLed2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request("led2");
            }
        });
        btnLed3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request("led3");
            }
        });
        btnAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                request("All");
            }
        });
    }

    private Runnable updateStatus = new Runnable() {
        @Override
        public void run() {
            if(statusReceipt) {
                request("");
                handler.postDelayed(this, 2000);
                Log.d("Status", "Request");
            }else {
                handler.removeCallbacks(updateStatus);
                Log.d("Status", "Finished");
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        statusReceipt = false;
    }

    public void request(String command){
        ConnectivityManager CnMg = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo NwI = CnMg.getActiveNetworkInfo();

        if(NwI != null && NwI.isConnected()){
            String url = "https://192.168.1.150/";

            new RequestData().execute(url + command);
        }
        else {
            Toast.makeText(MainActivity.this, "No connections were detected", Toast.LENGTH_LONG).show();
        }

    }

    private class RequestData extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... url) {
            return Connect.getData(url[0]);
        }

        @Override
        protected void onPostExecute(String resultAll) {
            if(resultAll != null){
                txtResultAll.setText(resultAll);
                // L1of, L2of, L3of
                if(resultAll.contains("l1on")) {
                    //btnLed1.setText("Led 1 connected!");
                    btnLed1.setBackgroundResource(R.drawable.on_button);
                }else if(resultAll.contains("l1of")){
                    //btnLed1.setText("Led 1 disconnect!");
                    btnLed1.setBackgroundResource(R.drawable.off_button);
                }
                if(resultAll.contains("l2on")) {
                    //btnLed1.setText("Led 2 connected!");
                    btnLed2.setBackgroundResource(R.drawable.on_button);
                }else if(resultAll.contains("l2of")){
                    //btnLed1.setText("Led 2 disconnect!");
                    btnLed2.setBackgroundResource(R.drawable.off_button);
                }
                if(resultAll.contains("l3on")) {
                    //btnLed1.setText("Led 3 connected!");
                    btnLed3.setBackgroundResource(R.drawable.on_button);
                }else if(resultAll.contains("l3of")){
                    //btnLed1.setText("Led 3 disconnect!");
                    btnLed3.setBackgroundResource(R.drawable.off_button);
                }

                String[] dataReceive = resultAll.split(",");
                txtLdr.setText(dataReceive[3]);
            }
            else {
                Toast.makeText(MainActivity.this, "An error has occurred", Toast.LENGTH_LONG).show();
            }
        }
    }
}
