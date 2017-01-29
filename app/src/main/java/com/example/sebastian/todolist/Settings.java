package com.example.sebastian.todolist;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by sebastian on 25.01.17.
 */

public class Settings extends Activity{

    String imie,nazwisko,gr,album;
    Context parent;
    InputStream czytaj;

    public Settings(Context c){

        parent = c;

        String linia = "";
        czytaj = parent.getResources().openRawResource(R.raw.ustawienia);
        BufferedReader buffor = new BufferedReader(new InputStreamReader(czytaj));

        if(czytaj != null){

            try {
                int i = 0;
                while ((linia = buffor.readLine()) != null){
                    switch (i){
                        case 0:{
                            imie = linia;
                            break;
                        }
                        case 1: {
                            nazwisko = linia;
                            break;
                        }
                        case 2:{
                            gr = linia;
                            break;
                        }
                        case 3:{
                            album = linia;
                            break;
                        }
                    }

                    i++;
                }
                czytaj.close();

            } catch (IOException e) {

                Toast.makeText(parent,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(parent,"Plik ustawień zawiera błędne dane!!!",Toast.LENGTH_SHORT).show();
        }
    }

    public String getImie(){
        return imie;
    }

    public String getNazwisko(){
        return nazwisko;
    }
    public String getGrupa(){
        return gr;
    }
    public String getAlbum(){
        return album;
    }

    public String getAll(){
        czytaj = parent.getResources().openRawResource(R.raw.ustawienia);

        String linia = "";
        StringBuffer txt = new StringBuffer();
        BufferedReader buffor = new BufferedReader(new InputStreamReader(czytaj));

        if(czytaj != null){

            try {

                while ((linia = buffor.readLine()) != null){
                    txt.append(linia+"\n");
                }
                czytaj.close();
            } catch (IOException e) {

                Toast.makeText(parent,e.getMessage(),Toast.LENGTH_SHORT).show();
            }

        }else{
            Toast.makeText(parent,"Plik ustawień zawiera błędne dane!!!",Toast.LENGTH_SHORT).show();
        }
        return txt.toString();
    }
}
