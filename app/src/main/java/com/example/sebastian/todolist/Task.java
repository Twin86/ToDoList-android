package com.example.sebastian.todolist;

import android.app.Activity;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.text.StaticLayout;

import java.security.PublicKey;
import java.util.Date;
import java.io.Serializable;
/**
 * Created by sebastian on 26.01.17.
 */
@SuppressWarnings("serial")
public class Task extends Activity implements Serializable{

    String nazwa, opis, termin;
    int status,waga,id;

    //SimpleDateFormat termin;

    String[] slWag = {
            "NORMALNE",
            "WAŻNE",
            "NIEWAŻNE"
    };

    String[] slStatus = {
            "TRWA",
            "WYKONANE",
            "ANULOWANE"
    };


    public Task(int i, String n, String o, int w, int s, String t) {
        nazwa = n;
        opis = o;
        status = s;
        waga = w;
        termin = t;
        id = i;
    }

    public Task(String n, String o, int w, int s, String t) {
        nazwa = n;
        opis = o;
        status = s;
        waga = w;
        termin = t;

    }

    public String getTask(){
        return getNazwa() + ";" + getOpis() + ";" + getWaga()+";"+getStatus()+";"+getTermin();
    }

    public String getTaskString(){
        return getId()+";"+getNazwa() + ";" + getOpis() + ";" + getWagaZslownik(getWaga())+";"+getStatusZslownik(getStatus())+";"+getTermin();
    }

    public String getWagaZslownik(int a) {
        switch (a) {
            case 1: {

                return slWag[1];

            }
            case 2: {
                return slWag[2];
            }
            default: {
                return slWag[0];
            }
        }
    }

    public String getStatusZslownik(int a) {
        switch (a) {

            case 1: {
                return slStatus[1];

            }
            case 2: {
                return slStatus[2];
            }
            default: {
                return slStatus[0];
            }
        }
    }

    public void aktualizuj(Task dane) {
        nazwa = dane.getNazwa();
        opis = dane.getOpis();
        waga = dane.getWaga();
        status = dane.getStatus();
        termin = dane.getTermin();
    }

    public void setNazwa(String a) {
        nazwa = a;
    }

    public void setOpis(String a) {
        opis = a;
    }

    public void setTermin(String a) {
        termin = a;
    }

    public void setWaga(int a) {
        waga = a;
    }

    public void setStatus(int a) {
        status = a;
    }

    public void setId(int a) {
        id = a;
    }

    public String getNazwa() {
        return nazwa;
    }

    public String getOpis() {
        return opis;
    }

    public int getWaga() {
        return waga;
    }

    public int getStatus() {
        return status;
    }

    public String getTermin() {
        return termin;
    }

    public int getId() {
        return id;
    }

    public String[] getSlownikWag() {
        return slWag;
    }

    public String[] getSlownikStatusow() {
        return slStatus;
    }



}
