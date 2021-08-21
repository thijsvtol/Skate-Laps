package com.wearos.skatelaps;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.wearable.activity.WearableActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetTransponder extends WearableActivity {

    public static final String myPref = "preferenceName";

    private Button btnSave;
    private EditText editTextTransponder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_transponder);

        btnSave = (Button) findViewById(R.id.button_save);
        editTextTransponder = (EditText) findViewById(R.id.editTextTransponder);
        if (!getPreferenceValue().equals("TheDefaultValueIfNoValueFoundOfThisKey")) {
            editTextTransponder.setText(getPreferenceValue());
        }

        // Enables Always-on
        setAmbientEnabled();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String transponderNumber = editTextTransponder.getText().toString();
                transponderNumber = transponderNumber.contains("-") ? transponderNumber :
                        transponderNumber.substring(0, 2) + "-" + transponderNumber.substring(2);
                System.out.println(transponderNumber);
                transponderNumber.toUpperCase();

                if (transponderNumber.length() == 8) {
                    writeToPreference(transponderNumber);

                    Intent i = new Intent(SetTransponder.this, MainActivity.class);
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
    public String getPreferenceValue()
    {
        SharedPreferences sp = getSharedPreferences(myPref,0);
        String str = sp.getString("transponder","TheDefaultValueIfNoValueFoundOfThisKey");
        return str;
    }

    public void writeToPreference(String thePreference)
    {
        SharedPreferences.Editor editor = getSharedPreferences(myPref,0).edit();
        editor.putString("transponder", thePreference);
        editor.commit();
    }


}
