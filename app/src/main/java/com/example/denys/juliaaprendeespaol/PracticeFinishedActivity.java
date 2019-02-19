package com.example.denys.juliaaprendeespaol;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class PracticeFinishedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice_finished);

        //objeto que intent usando para recibir los datos (la cantidad de respuestas correctas e incorrectas)
        Intent i = getIntent();

        //Pone en texto de respuestas correctas la cnatidad de correctas recibidas por el intent desde la actividad de practica
        TextView correctas = findViewById(R.id.correctas);
        String correctasText = correctas.getText().toString();
        correctasText += i.getStringExtra("correctas");
        correctas.setText(correctasText);

        //Pone en texto de respuestas correctas la cnatidad de incorrectas recibidas por el intent desde la actividad de practica
        TextView incorrectas = findViewById(R.id.incorrectas);
        String incorrectasText = incorrectas.getText().toString();
        incorrectasText += i.getStringExtra("incorrectas");
        incorrectas.setText(incorrectasText);

        int total = Integer.parseInt(i.getStringExtra("correctas")) + Integer.parseInt(i.getStringExtra("incorrectas"));
        TextView totalView = findViewById(R.id.total);
        totalView.setText("Total = " + String.valueOf(total));


        Button repeat = findViewById(R.id.repeat_practice);

        //Click listener para el boton de repetir, comienza la actividad del menu
        repeat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent repeatIntent = new Intent(PracticeFinishedActivity.this, PraticeMenuActivity.class);
                startActivity(repeatIntent);
            }
        });

        Button exit = findViewById(R.id.exit);

        //Lleva al menu principal al clickear
        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent exitIntent = new Intent(PracticeFinishedActivity.this, MainActivity.class);
                startActivity(exitIntent);

            }
        });
    }
}
