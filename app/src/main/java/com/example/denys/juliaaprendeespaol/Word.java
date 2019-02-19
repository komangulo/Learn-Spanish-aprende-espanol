package com.example.denys.juliaaprendeespaol;

public class Word
{

    //Atributos
    private String spanish_translation;
    private String german_translation;
    private int picture_resource_id;
    private int audio_resource_id;
    private boolean isLetter;

    //Constructor para palabras que llevan una imagen
    public  Word(String spanish, String german, int pictureResource, int audioResource)
    {
        this.spanish_translation = spanish;
        this.german_translation = german;
        this.picture_resource_id = pictureResource;
        this.audio_resource_id = audioResource;
        this.isLetter = false;
    }

    //constructor para letras
    public  Word(String spanish, String german, int pictureResource, int audioResource, boolean isLetter)
    {
        this.spanish_translation = spanish;
        this.german_translation = german;
        this.picture_resource_id = pictureResource;
        this.audio_resource_id = audioResource;
        this.isLetter = true;

    }

    //Constructor para palabras que NO  llevan una imagen
    public  Word(String spanish, String german, int audioResource)
    {
        this.spanish_translation = spanish;
        this.german_translation = german;
        this.picture_resource_id = 0;
        this.audio_resource_id = audioResource;
        this.isLetter = false;
    }

    //getters
    public int getPicture_resource_id() {
        return picture_resource_id;
    }

    public String getSpanish_translation()
    {
        return spanish_translation;
    }

    public String getGerman_translation()
    {
        return german_translation;
    }

    public int getAudio_resource_id(){return  audio_resource_id;}

    public boolean getIsLetter(){return isLetter;}
}
