package com.example.emotionmonitor;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void goToRaiva(View view) {
        Intent intent = new Intent(this, RaivaMonitor.class);
        startActivity(intent);
    }
    public void goToSolidao(View view) {
        Intent intent = new Intent(this, SolidaoMonitor.class);
        startActivity(intent);
    }
    public void goToMedo(View view) {
        Intent intent = new Intent(this, MedoMonitor.class);
        startActivity(intent);
    }
    public void goToAnsiedade(View view) {
        Intent intent = new Intent(this, AnsiedadeMonitor.class);
        startActivity(intent);
    }
    public void goToTristeza(View view) {
        Intent intent = new Intent(this, TristezaMonitor.class);
        startActivity(intent);
    }
    public void goToEstresse(View view) {
        Intent intent = new Intent(this, EstresseMonitor.class);
        startActivity(intent);
    }
}
