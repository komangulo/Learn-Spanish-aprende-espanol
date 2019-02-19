package com.example.denys.juliaaprendeespaol;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

public class PraticeMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pratice_menu);


        //El boton para comenzar a practicar
        Button start_btn = (Button)findViewById(R.id.star_btn);

        //Textview para el count down a iniciar el bboton
        final TextView counter = (TextView)findViewById(R.id.counter_view);


        //Cuando se clicklee el boton
        start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Retrieve the options
                RadioGroup opciones = findViewById(R.id.opciones);

                //find the one selected
                int seleccionadaId = opciones.getCheckedRadioButtonId();

                //View instance of the selected one
                RadioButton seleccionada = findViewById(seleccionadaId);

                //opcion to send to the intent
                final String opcion;

                //si la opcion seleccionada es numeros
                if(seleccionada.getText().toString().equals("Números"))
                    opcion = "numbers";
                //si la opcion es familia
                else
                    opcion = "family";

                //CountDownTimer instance.
                CountDownTimer timer = new CountDownTimer(4000, 800) {

                    //On tick, changes the text of the of the counter TextView
                    public void onTick(long millisUntilFinished) {
                        counter.setText(String.valueOf((int)millisUntilFinished/1000));

                    }

                    //when the counter finishes
                    public void onFinish() {
                        //Starts the Practice acitivity
                        Intent intent = new Intent(PraticeMenuActivity.this, PracticeActivity.class);
                        intent.putExtra("categoria", opcion);
                        startActivity(intent);
                    }

                }.start();
            }
        });
    }
}
