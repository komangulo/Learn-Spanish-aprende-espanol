package com.example.denys.juliaaprendeespaol;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class WordAdapter extends ArrayAdapter<Word> {
    boolean hasImage;

    public WordAdapter(Activity context, ArrayList<Word> words, boolean image) {
        //Llama al constructor de la clase madre y le pasa los parametros. El segundo parametro(el id del archivo layout
        // que contiene la vista que se va a usar para entrarlas al ListView. Como ese archivo es propio nuestro y lo conocemos, le pasamos 0
        super(context, 0, words);
        this.hasImage = image;

    }

    @NonNull
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent)
    {
        //listItemView is the view being recycled. All its elements are being replaced by the ones at the current position
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_element, parent, false);
        }

        //Retrieves the word object from the array at the current position
        Word currentWord = getItem(position);


        //Retrieves the TextView object corresponding to the miwok word on the view being recycled
        TextView spanishWord = (TextView)listItemView.findViewById(R.id.spanish);
        //Replaces the text from the view being recycled with the at the current position
        spanishWord.setText(currentWord.getSpanish_translation());


        //Retrieves the TextView object corresponding to the english word on the view being recycled
        TextView germanWord = (TextView) listItemView.findViewById(R.id.german_view);
        //Replaces the text from the view being recycled with the one at the current position
        germanWord.setText(currentWord.getGerman_translation());

        //se seagura que el color de fondo sea el default antes de verificar si lo cambia o no
        listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.default_list_background));
        //se asegura que el textView de aleman sea visible(ya que con las letras se hace no visible)
        germanWord.setVisibility(View.VISIBLE);



        //object of the image view
        ImageView picture = (ImageView) listItemView.findViewById(R.id.image_view);
        if(this.hasImage)
        {

            //sets the image
            picture.setImageResource(currentWord.getPicture_resource_id());

        }
        else
        {

            picture.setVisibility(View.GONE);
            /*
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
            params.addRule(RelativeLayout.LEFT_OF, RelativeLayout.TRUE);
            germanWord.setLayoutParams(params);
            */

        }

        if(currentWord.getIsLetter())
        {
            germanWord.setVisibility(View.GONE);
            listItemView.setBackgroundColor(getContext().getResources().getColor(R.color.letter_list_background));
        }

        return listItemView;

    }
}

