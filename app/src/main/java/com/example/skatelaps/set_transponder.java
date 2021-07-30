package com.example.skatelaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class set_transponder extends WearableActivity {

    private Button btnSave;

    private TextView transponderLetter1;
    private TextView transponderLetter2;
    private TextView transponderNumber1;
    private TextView transponderNumber2;
    private TextView transponderNumber3;
    private TextView transponderNumber4;
    private TextView transponderNumber5;

    private Button btnTransponderLetter1Up;
    private Button btnTransponderLetter1Down;
    private Button btnTransponderLetter2Up;
    private Button btnTransponderLetter2Down;
    private Button btnTransponderNumber1Down;
    private Button btnTransponderNumber1Up;
    private Button btnTransponderNumber2Down;
    private Button btnTransponderNumber2Up;
    private Button btnTransponderNumber3Down;
    private Button btnTransponderNumber3Up;
    private Button btnTransponderNumber4Down;
    private Button btnTransponderNumber4Up;
    private Button btnTransponderNumber5Down;
    private Button btnTransponderNumber5Up;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transponder);

        btnSave = (Button)findViewById(R.id.button_save);

        btnTransponderLetter1Down = (Button) findViewById(R.id.button_l1b);
        btnTransponderLetter1Up = (Button) findViewById(R.id.button_l1);
        btnTransponderLetter2Down = (Button) findViewById(R.id.button_l2b);
        btnTransponderLetter2Up = (Button) findViewById(R.id.button_l2);
        btnTransponderNumber1Down = (Button) findViewById(R.id.button_c1b);
        btnTransponderNumber1Up = (Button) findViewById(R.id.button_c1);
        btnTransponderNumber2Down = (Button) findViewById(R.id.button_c2b);
        btnTransponderNumber2Up = (Button) findViewById(R.id.button_c2);
        btnTransponderNumber3Down = (Button) findViewById(R.id.button_c3b);
        btnTransponderNumber3Up = (Button) findViewById(R.id.button_c3);
        btnTransponderNumber4Down = (Button) findViewById(R.id.button_c4b);
        btnTransponderNumber4Up = (Button) findViewById(R.id.button_c4);
        btnTransponderNumber5Down = (Button) findViewById(R.id.button_c5b);
        btnTransponderNumber5Up = (Button) findViewById(R.id.button_c5);

        transponderLetter1 = (TextView) findViewById(R.id.text_l1);
        transponderLetter2 = (TextView) findViewById(R.id.text_l2);
        transponderNumber1 = (TextView) findViewById(R.id.text_c1);
        transponderNumber2 = (TextView) findViewById(R.id.text_c2);
        transponderNumber3 = (TextView) findViewById(R.id.text_c3);
        transponderNumber4 = (TextView) findViewById(R.id.text_c4);
        transponderNumber5 = (TextView) findViewById(R.id.text_c5);

        // Enables Always-on
        setAmbientEnabled();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transponderNumber =
                    transponderLetter1.getText().toString() +
                    transponderLetter2.getText().toString() + "-" +
                    transponderNumber1.getText().toString() +
                    transponderNumber2.getText().toString() +
                    transponderNumber3.getText().toString() +
                    transponderNumber4.getText().toString() +
                    transponderNumber5.getText().toString()
                ;
                System.out.println(transponderNumber);
                Intent i = new Intent(set_transponder.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("transponder", transponderNumber);
                i.putExtras(bundle);
                startActivity(i);
            }
        });

        btnTransponderLetter1Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderLetter1.setText(nextLetterInAlphabet(transponderLetter1));
            }
        });

        btnTransponderLetter1Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderLetter1.setText(previousLetterInAlphabet(transponderLetter1));
            }
        });

        btnTransponderLetter2Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderLetter2.setText(nextLetterInAlphabet(transponderLetter2));
            }
        });

        btnTransponderLetter2Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderLetter2.setText(previousLetterInAlphabet(transponderLetter2));
            }
        });

        btnTransponderNumber1Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber1.setText(upNumber(transponderNumber1));
            }
        });

        btnTransponderNumber1Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber1.setText(lowerNumber(transponderNumber1));
            }
        });

        btnTransponderNumber2Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber2.setText(upNumber(transponderNumber2));
            }
        });

        btnTransponderNumber2Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber2.setText(lowerNumber(transponderNumber2));
            }
        });

        btnTransponderNumber3Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber3.setText(upNumber(transponderNumber3));
            }
        });

        btnTransponderNumber3Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber3.setText(lowerNumber(transponderNumber3));
            }
        });

        btnTransponderNumber4Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber4.setText(upNumber(transponderNumber4));
            }
        });

        btnTransponderNumber4Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber4.setText(lowerNumber(transponderNumber4));
            }
        });

        btnTransponderNumber5Up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber5.setText(upNumber(transponderNumber5));
            }
        });

        btnTransponderNumber5Down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                transponderNumber5.setText(lowerNumber(transponderNumber5));
            }
        });
    }

    private String nextLetterInAlphabet(TextView textView) {
        int ascii = textView.getText().charAt(0) + 1;
        ascii = Character.isAlphabetic((char) ascii) ? ascii : ascii - 1;
        return String.valueOf((char) (ascii));
    }

    private String previousLetterInAlphabet(TextView textView) {
        int ascii = textView.getText().charAt(0) - 1;
        ascii = Character.isAlphabetic((char) ascii) ? ascii : ascii + 1;
        return String.valueOf((char) (ascii));
    }
    
    private String upNumber(TextView textView) {
        return (Integer.valueOf(textView.getText().toString()) + 1) < 10 && (Integer.valueOf(textView.getText().toString()) + 1) > -1 ? String.valueOf(Integer.valueOf(textView.getText().toString()) + 1) : String.valueOf(Integer.valueOf(textView.getText().toString()));
    }

    private String lowerNumber(TextView textView) {
        return (Integer.valueOf(textView.getText().toString()) - 1) < 10 && (Integer.valueOf(textView.getText().toString()) - 1) > -1 ? String.valueOf(Integer.valueOf(textView.getText().toString()) - 1) : String.valueOf(Integer.valueOf(textView.getText().toString()));
    }

}
