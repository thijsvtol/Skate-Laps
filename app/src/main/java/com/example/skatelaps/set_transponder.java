package com.example.skatelaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class set_transponder extends WearableActivity {

    private Button btnSave;
    private EditText editTextTransponder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transponder);

        btnSave = (Button) findViewById(R.id.button_save);
        editTextTransponder = (EditText) findViewById(R.id.editTextTransponder);

        // Enables Always-on
        setAmbientEnabled();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transponderNumber = editTextTransponder.getText().toString();
                transponderNumber = transponderNumber.contains("-") ? transponderNumber :
                        transponderNumber.substring(0, 2) + "-" + transponderNumber.substring(2);
                System.out.println(transponderNumber);

                if (transponderNumber.length() == 8) {
                    Intent i = new Intent(set_transponder.this, MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("transponder", transponderNumber);
                    i.putExtras(bundle);
                    startActivity(i);
                }
                else {
                    editTextTransponder.setError("Invalid transponder number!");
                }
            }
        });
    }

}
