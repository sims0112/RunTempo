package com.example.epsilonslotcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;

public class DisplayMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_message);

        Intent intent = getIntent();
        String total = intent.getStringExtra(MainActivity.EXTRA_TOTAL);
        String s1 = intent.getStringExtra(MainActivity.EXTRA_1ST) ;
        String s2 = intent.getStringExtra(MainActivity.EXTRA_2ND) ;
        String s3 = intent.getStringExtra(MainActivity.EXTRA_3RD) ;
        String s4 = intent.getStringExtra(MainActivity.EXTRA_4TH) ;

        Integer num = Integer.parseInt(total);
        Integer num1 = Integer.parseInt(s1);
        Integer num2 = Integer.parseInt(s2);
        Integer num3 = Integer.parseInt(s3);
        Integer num4 = Integer.parseInt(s4);

        String message = "" ;
        // +=4 just to iterate faster, not every solution will be printed whereas +=1 will print every solution. +=4 comes from the fact that at lvl 2 warlock, short rest gives 4 sorcery points. Having num_i be arbitrary may give no solutions, but I think what I have is good enough.
        for (int a = num1 ; 2*a < num + 1 ; a += 4) {
            for (int b = num2 ; 3*b < num + 1 - 2*a ; b += 4) {
                for (int c = num3 ; 5*c < num + 1 - 2*a - 3*b ; c += 4) {
                    for (int d = num4 ; 6*d < num + 1 - 2*a - 3*b - 5*c ; d += 4) {
                        if (num == 2*a + 3*b + 5*c + 6*d) {
                            message += Integer.toString(a) + "      " + Integer.toString(b) + "      " + Integer.toString(c) + "      " + Integer.toString(d) + "\n\n" ;
                        }
                    }
                }
            }
        }

        TextView textView = findViewById(R.id.textView);
        textView.setMovementMethod(new ScrollingMovementMethod());
        textView.setText(message) ;
    }
}