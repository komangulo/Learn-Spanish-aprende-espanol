package com.example.denys.juliaaprendeespaol;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;

public class PracticeActivity extends AppCompatActivity {

    //Global variables
    int correctAnswers;
    int wrongAnswers;

    //Va a contener el json en string
    String jsonString;
    //objeto que contendra el json
    JSONObject obj;
    JSONArray wordsArr;
    int current;
    ImageView picture;
    TextView german;
    JSONObject currentWord;
    Button verificar_btn;
    EditText escrito;
    ImageView correct;
    ImageView wrong;
    TextView correct_answer;
    int[] randomIndex;
    String categoria;
    int audioResource;

    int correct_audio;

    int incorrect_audio;

    //MediaPlayer global instance. Made global so that it could be referred inside a subclass.Handles playback
    MediaPlayer mediaPlayer;

    //OnCompletionListener. Its executed when the audio finishes playing
    MediaPlayer.OnCompletionListener mCompletionListener = new MediaPlayer.OnCompletionListener() {
        @Override
        public void onCompletion(MediaPlayer mp) {
            //Log.v("FORCE","Audio Finished");
            // Now that the sound file has finished playing, release the media player resources.
            releaseMediaPlayer();
        }
    };


    private void releaseMediaPlayer() {
        // If the media player is not null, then it may be currently playing a sound.
        if (mediaPlayer != null) {
            // Regardless of the current state of the media player, release its resources
            // because we no longer need it.
            mediaPlayer.release();

            // Set the media player back to null. For our code, we've decided that
            // setting the media player to null is an easy way to tell that the media player
            // is not configured to play an audio file at the moment.
            mediaPlayer = null;


            mAudioManager.abandonAudioFocus(afChangeListener);
        }
    }

    //AudioManager instance; handles audio focus
    AudioManager mAudioManager;

    AudioManager.OnAudioFocusChangeListener afChangeListener = //Se debe declarar el FocusChangeListener antes del hacer el focusRequest porque se le pasa como parametro al request
            afChangeListener = new AudioManager.OnAudioFocusChangeListener() {
                @Override
                public void onAudioFocusChange(int focusChange) {

                    switch (focusChange)
                    {
                        //audio focus  is lost for short amount of time; so we're pausing it, and since the audio files we're dealing with are short, we're turning the play to
                        //the begining
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT:
                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0);
                            break;
                        //AudioFocus has been lost
                        case AudioManager.AUDIOFOCUS_LOSS:
                            releaseMediaPlayer();
                            break;
                        //he AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK case means that our app is allowed to continue playing sound but at a lower volume.
                        //We should really turn down the volume of the playing, but since we're dealing with short audio files, we are pausing it and resetting it
                        case AudioManager.AUDIOFOCUS_LOSS_TRANSIENT_CAN_DUCK:
                            mediaPlayer.pause();
                            mediaPlayer.seekTo(0);
                            break;
                        //// The AUDIOFOCUS_GAIN case means we have regained focus and can resume playback. .start() actually resumes the playing
                        case  AudioManager.AUDIOFOCUS_GAIN:
                            mediaPlayer.start();
                            break;
                    }

                }
            };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_practice);



        //Inicializa las variables globales
        picture = findViewById(R.id.picture);
        german = findViewById(R.id.german);
        verificar_btn = (Button)findViewById(R.id.verificar);
        escrito = findViewById(R.id.escrito);
        correct = findViewById(R.id.correct);
        wrong = findViewById(R.id.wrong);
        correct_answer = findViewById(R.id.respuesta_correcta);
        correct_answer.setText("Respuesta correcta: ");
        correct_answer.setVisibility(View.GONE);
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        correct_audio = getResources().getIdentifier("correct_sound","raw", getPackageName());
        incorrect_audio = getResources().getIdentifier("incorrect_sound","raw", getPackageName());

        //objeto que intent usando para recibir los datos
        Intent intent = getIntent();

        //Recibe el string de categoria seleccionada
        categoria = intent.getStringExtra("categoria");
        current = 0;
        correctAnswers= 0;
        wrongAnswers = 0;

        //Cuando el boton verificar es clickeado, llama al metodo verificar
        verificar_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                verificar();
            }
        });

        //cuando el textView es clickeado para escuchar el audio
        german.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                playAudio(audioResource);
            }
        });

        //Se mete dentro de un try-catch porque getJSONArray y compania pueden lanzar una excepcion
        try
        {
            //llama al metodo implementado en esta misma clase que devuelve el json como un string
            jsonString = getJson();
            //crea un objecto json en base al string
            obj = new JSONObject(jsonString);
            //saca los numeros
            wordsArr = obj.getJSONArray(categoria);
            randomIndex = new int[wordsArr.length()];

            //llena el objeto randomIndex de numros
            for (int i = 0; i < wordsArr.length(); i++) {
                randomIndex[i] = i;
            }

            //El objeto shuffle pone el array de manera aleatoria
            Collections.shuffle(Arrays.asList(randomIndex));

        }


        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        // Llama al metodo de juego
        juego();

    }


    public void juego()
    {

        //Si el juego no ha terminado
        if(current < wordsArr.length())
        {
            try
            {
                //Consigue el objeto actual del Array obtenido del Json, y cambia la foto
                currentWord = wordsArr.getJSONObject(randomIndex[current]);
                int picResource = getResources().getIdentifier(currentWord.getString("picResource"),"drawable", getPackageName());
                audioResource = getResources().getIdentifier(currentWord.getString("audioResource"),"raw", getPackageName());
                picture.setImageResource(picResource);
                german.setText(currentWord.getString("german"));
                playAudio(audioResource);

            }

            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }
        //se acabo la ronda
        else
        {
            //Empieza la actividad de finilizada la practica
            Intent finishedIntent = new Intent(PracticeActivity.this, PracticeFinishedActivity.class);
            finishedIntent.putExtra("correctas",""+correctAnswers);
            finishedIntent.putExtra("incorrectas",""+wrongAnswers);
            startActivity(finishedIntent);

        }
    }

    public void playAudio(int audio)
    {
        //// Release the media player if it currently exists because we are about to play a different sound file
        releaseMediaPlayer();

        //Requests the audioFocus and stores it on a variable
        int result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

        //if the audioFocus has been granted
        if(result ==  AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
        {
            //Inicializa el mediaPlayer object, Asigna el resourceId dependiendo de la posicion  en el array
            mediaPlayer =  MediaPlayer.create(getApplicationContext(), audio);
            mediaPlayer.start();
            mediaPlayer.setOnCompletionListener(mCompletionListener);

        }

    }

    public void verificar()
    {
        try
        {
            //Log.v("ESCRITO= ", escrito.getText().toString());
            //Log.v("RESPUESTA= ", currentWord.getString("spanish"));

            //si la respuesta es correcta
            if(currentWord.getString("spanish").contains(escrito.getText().toString()))
            {

                playAudio(correct_audio);
                current++;
                correctAnswers++;
                //la imagen de checkMark correcta viene visible
                correct.setVisibility(View.VISIBLE);

                //objecto handler
                Handler handler = new Handler();

                //Post delayed recibe dos parametros: un objeto runnable y el tiempo de delay
                handler.postDelayed(new Runnable() {
                    //metodo Run es el que se ejecuta cuando ha pasado el tiempo delay
                    @Override
                    public void run() {
                        correct.setVisibility(View.GONE);
                        escrito.setText("");
                        juego();
                    }
                }, 1000);
            }
            //si la respuesta es incorrecta
            else
            {
                playAudio(incorrect_audio);
                current++;
                wrongAnswers++;
                //Pone la cruz de incorrecta visible y muestra la respuesta correcta
                String correct_answer_text = correct_answer.getText().toString();
                correct_answer_text += currentWord.getString("spanish");
                correct_answer.setText(correct_answer_text);

                correct_answer.setVisibility(View.VISIBLE);


                wrong.setVisibility(View.VISIBLE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        wrong.setVisibility(View.GONE);
                        correct_answer.setVisibility(View.GONE);
                        correct_answer.setText("Respuesta correcta: ");
                        escrito.setText("");
                        juego();
                    }
                }, 1000);

            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }



    public String getJson()
    {
        //output
        String json;
        try
        {
            //InputSream is used to read data from a stream such as a file, a webpage

            //openRawResource opens a data stream for reading a raw resource.
            InputStream inputStream = this.getResources().openRawResource(R.raw.words);

            //Returns an estimate of the number of bytes that can be read
            int size = inputStream.available();


            byte[] buffer = new byte[size];

            //Reads some number of bytes from the input stream and stores them into the buffer array b
            inputStream.read(buffer);

            inputStream.close();

            //Crea el string a partir del array buffer
            json = new String(buffer, "UTF-8");
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
            return null;
        }

        return json;
    }

    @Override
    public void onBackPressed() {
        Dialog dialog = new AlertDialog.Builder(this)
                .setMessage("¿Estás segura de que quieres salir?")
                .setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(PracticeActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                }).create();

        dialog.show();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
