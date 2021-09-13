package com.example.epsilonslotcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_TOTAL = "com.example.Total";
    public static final String EXTRA_1ST = "com.example.1st" ;
    public static final String EXTRA_2ND = "com.example.2nd" ;
    public static final String EXTRA_3RD = "com.example.3rd" ;
    public static final String EXTRA_4TH = "com.example.4th" ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void sendMessage(View view) {

        EditText editTotal = (EditText) findViewById(R.id.editTextTextPersonName) ;
        EditText edit1st = (EditText) findViewById(R.id.editTextTextPersonName2) ;
        EditText edit2nd = (EditText) findViewById(R.id.editTextTextPersonName3) ;
        EditText edit3rd = (EditText) findViewById(R.id.editTextTextPersonName4) ;
        EditText edit4th = (EditText) findViewById(R.id.editTextTextPersonName5) ;

        String total = editTotal.getText().toString() ;
        String s1 = edit1st.getText().toString() ;
        String s2 = edit2nd.getText().toString() ;
        String s3 = edit3rd.getText().toString() ;
        String s4 = edit4th.getText().toString() ;

        if (total.isEmpty()) {
            total = "0" ;
        }

        if (s1.isEmpty()) {
            s1 = "0" ;
        }

        if (s2.isEmpty()) {
            s2 = "0" ;
        }

        if (s3.isEmpty()) {
            s3 = "0" ;
        }

        if (s4.isEmpty()) {
            s4 = "0" ;
        }

        Intent intent = new Intent(this, DisplayMessageActivity.class);

        intent.putExtra(EXTRA_TOTAL, total);
        intent.putExtra(EXTRA_1ST, s1) ;
        intent.putExtra(EXTRA_2ND, s2) ;
        intent.putExtra(EXTRA_3RD, s3) ;
        intent.putExtra(EXTRA_4TH, s4) ;

        startActivity(intent);
    }
}