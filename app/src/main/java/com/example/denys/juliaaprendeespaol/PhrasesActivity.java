package com.example.denys.juliaaprendeespaol;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

public class PhrasesActivity extends AppCompatActivity {


    ArrayList<Word> words;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.words);

        //Va a contener el json en string
        String jsonString;
        //objeto que contendra el json
        JSONObject obj;
        //array para el adapter
        words = new ArrayList<Word>();

        //Se mete dentro de un try-catch porque getJSONArray y compania pueden lanzar una excepcion
        try
        {
            //llama al metodo implementado en esta misma clase que devuelve el json como un string
            jsonString = getJson();
            //crea un objecto json en base al string
            obj = new JSONObject(jsonString);
            //saca los numeros
            JSONArray wordsArr = obj.getJSONArray("phrases");

            //relena el array con objetos de la clase Word en base a la info del json n
            for(int i = 0; i < wordsArr.length(); i++)
            {
                JSONObject word = wordsArr.getJSONObject(i);
                //int picResource = getResources().getIdentifier(word.getString("picResource"),"drawable", getPackageName() );
                int audioResource = getResources().getIdentifier(word.getString("audioResource"),"raw", getPackageName());
                //Log.v("JSON=",word.getString("spanish")+", " + word.getString("german") +"," + word.get("picResource") + "= "+picResource);
                words.add(new Word(word.getString("spanish"), word.getString("german"), audioResource));

            }
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

        }


        WordAdapter adapter = new WordAdapter(this, words, false);

        //List view object. Parameter is the listView id
        ListView listView = (ListView) findViewById(R.id.word_list);

        listView.setAdapter(adapter);


        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //// Release the media player if it currently exists because we are about to play a different sound file
                releaseMediaPlayer();

                //Requests the audioFocus and stores it on a variable
                int result = mAudioManager.requestAudioFocus(afChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);

                //if the audioFocus has been granted
                if(result ==  AudioManager.AUDIOFOCUS_REQUEST_GRANTED)
                {
                    //Inicializa el mediaPlayer object, Asigna el resourceId dependiendo de la posicion  en el array
                    mediaPlayer =  MediaPlayer.create(getApplicationContext(), words.get(position).getAudio_resource_id());
                    mediaPlayer.start();
                    mediaPlayer.setOnCompletionListener(mCompletionListener);

                }
            }
        });
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
}
