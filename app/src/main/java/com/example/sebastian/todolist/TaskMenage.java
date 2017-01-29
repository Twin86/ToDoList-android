package com.example.sebastian.todolist;

import android.app.Activity;
import android.content.Intent;
import android.database.DataSetObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
/**
 * Created by sebastian on 27.01.17.
 */


@SuppressWarnings("serial")
public class TaskMenage extends AppCompatActivity {

    Spinner statusy, wagi;
    Task tempTask;
    Button btSave;
    boolean edit = false;

    Intent intent;

    EditText nazwa, opis, data;

    InputStream zapisz;
    ArrayAdapter<String> listaStatusow;
    ArrayAdapter<String> listaWag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.task_main);

        tempTask = new Task(0, "", "", 0, 0, "0000-00-00");

        statusy = (Spinner) findViewById(R.id.spinnerStatusy);
        wagi = (Spinner) findViewById(R.id.spinnerStan);
        btSave = (Button) findViewById(R.id.buttonSaveTask);

        nazwa = (EditText) findViewById(R.id.editTaskTitle);
        opis = (EditText) findViewById(R.id.editTaskDescription);
        data = (EditText) findViewById(R.id.editTaskData);

        intent  = getIntent();



        listaStatusow = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempTask.getSlownikStatusow());
        listaWag = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, tempTask.getSlownikWag());

        statusy.setAdapter(listaStatusow);
        wagi.setAdapter(listaWag);

        if(intent.getSerializableExtra("sampleObject") != null){
            tempTask = (Task) intent.getSerializableExtra("sampleObject");

            edit = true;
            updateForm();
        }

        View.OnClickListener l1 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit) {
                    //robie dodawanie nowego zadania
                    if (sprawdzForm()) {

                        tempTask.setNazwa(nazwa.getText().toString());
                        tempTask.setOpis(opis.getText().toString());
                        tempTask.setTermin(data.getText().toString());
                        tempTask.setWaga(wagi.getSelectedItemPosition());
                        tempTask.setStatus(statusy.getSelectedItemPosition());

                        addZadanie(tempTask);
                        setResult(RESULT_OK);
                        finish();

                    } else {

                    }

                } else {
                    //robie edycje
                    if (sprawdzForm()) {
                        tempTask.setNazwa(nazwa.getText().toString());
                        tempTask.setOpis(opis.getText().toString());
                        tempTask.setTermin(data.getText().toString());
                        tempTask.setWaga(wagi.getSelectedItemPosition());
                        tempTask.setStatus(statusy.getSelectedItemPosition());


                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("RETURN_EDIT_TASK",tempTask);
                        setResult(Activity.RESULT_OK, resultIntent);

                        finish();
                    }

                }
            }
        };

        btSave.setOnClickListener(l1);


    }

    protected boolean sprawdzForm() {

        if (!nazwa.getText().toString().isEmpty() && !data.getText().toString().isEmpty()) {
            return true;
        } else {
            Toast.makeText(this, "Uzupełnij pole nazwa i data!", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    protected void addZadanie(Task zadanie) {
        String tresc = "";
        try {
            OutputStreamWriter out = new OutputStreamWriter(
            openFileOutput("zadania.txt", MODE_APPEND));
            out.append(zadanie.getTask());
            out.append("\r\n");
            out.close();
            Toast.makeText(this.getApplicationContext(), "Zapisano zadanie!!!", Toast.LENGTH_SHORT).show();

        } catch (Throwable t) {
            Toast.makeText(this.getApplicationContext(), "Błąd przy zapisie, " + t.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }

    private void updateForm(){
        nazwa.setText(tempTask.getNazwa());
        opis.setText(tempTask.getOpis());
        data.setText(tempTask.getTermin());
        statusy.setSelection(tempTask.getStatus(),true);
        wagi.setSelection(tempTask.getWaga(),true);
    }
}
