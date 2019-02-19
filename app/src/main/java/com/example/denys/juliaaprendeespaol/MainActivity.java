package com.example.denys.juliaaprendeespaol;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Retrieves the view
        LinearLayout numeros_view = (LinearLayout)findViewById(R.id.numeros_view);

        //Crea un click listener por cada vista de categoria, y comienza la actividad relacionada a esa vista
        numeros_view.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent numbersIntent = new Intent(MainActivity.this, NumbersActivity.class);

                // Start the new activity
                startActivity(numbersIntent);

            }
        });

        LinearLayout abc_view = (LinearLayout)findViewById(R.id.abc_view);

        abc_view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){;
                Intent abcIntent = new Intent(MainActivity.this, AbcActivity.class);
                startActivity(abcIntent);
            }
        });

        LinearLayout phrases_view = (LinearLayout)findViewById(R.id.frases_view);

        phrases_view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){;
                Intent phrasesIntent = new Intent(MainActivity.this, PhrasesActivity.class);
                startActivity(phrasesIntent);
            }
        });


        LinearLayout family_view = (LinearLayout)findViewById(R.id.familia_view);

        family_view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){;
                Intent familyIntent = new Intent(MainActivity.this, FamilyActivity.class);
                startActivity(familyIntent);
            }
        });


        LinearLayout pratice_view = (LinearLayout)findViewById(R.id.practice_view);

        pratice_view.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){;
                Intent practiceIntent = new Intent(MainActivity.this, PraticeMenuActivity.class);
                startActivity(practiceIntent);
            }
        });
    }
}
