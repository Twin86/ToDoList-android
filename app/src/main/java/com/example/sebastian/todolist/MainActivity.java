package com.example.sebastian.todolist;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.io.Serializable;
import static android.view.View.*;
@SuppressWarnings("serial")
public class MainActivity extends Activity {

    private static final int SECOND_ACTIVITY_RESULT_CODE = 0;
    private static final int SECOND_ACTIVITY_RESULT_CODE_EDIT = 1;

    Settings ustawienia;
    TextView tekstWidok;
    TextView trescZadanie;
    ListView listaZadan;
    Context context;
    int selectedItem;

    ArrayList<Task> zadania;
    Button btnZadanie;
    Button btnEdytujZadanie;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ustawienia = new Settings(this);
        selectedItem = -1;

        tekstWidok = (TextView) findViewById(R.id.textView);
        trescZadanie = (TextView) findViewById(R.id.textAboutTask);

        tekstWidok.setText("Lista zadań : " + ustawienia.getImie() + " " + ustawienia.getNazwisko());

        listaZadan = (ListView) findViewById(R.id.tasksList);
        zadania = getZadaniaInternalStorage();
        aktualizujListeZadan(zadania);

        OnClickListener l1 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                context = getApplicationContext();
                Intent intent = new Intent(context, TaskMenage.class);
                startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE);

            }
        };


        OnClickListener l4 = new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedItem == -1) {
                    Toast.makeText(getBaseContext(), "Wybierz zadanie!", Toast.LENGTH_LONG).show();
                }else{
                    context = getApplicationContext();
                    Intent intent = new Intent(context, TaskMenage.class);
                    intent.putExtra("sampleObject", zadania.get(selectedItem));
                    startActivityForResult(intent, SECOND_ACTIVITY_RESULT_CODE_EDIT);
                }
            }
        };

        AdapterView.OnItemClickListener l2 = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                trescZadanie.setText("Wybór:" + position + getTaskView(zadania.get(position)));
                selectedItem = position;
            }
        };

        AdapterView.OnItemLongClickListener l3 = new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                selectedItem = position;
                showMyDelDialog();
                return false;
            }
        };

        listaZadan.setOnItemClickListener(l2);
        listaZadan.setOnItemLongClickListener(l3);

        btnZadanie = (Button) findViewById(R.id.buttonAddTask);
        btnEdytujZadanie = (Button) findViewById(R.id.buttonEdytujTask);
        btnZadanie.setOnClickListener(l1);
        btnEdytujZadanie.setOnClickListener(l4);

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            switch (requestCode) {
                case (SECOND_ACTIVITY_RESULT_CODE): {
                    if (resultCode == RESULT_OK) {
                        zadania = getZadaniaInternalStorage();
                        aktualizujListeZadan(zadania);
                    }
                    break;
                }

                case (SECOND_ACTIVITY_RESULT_CODE_EDIT): {
                    if (resultCode == RESULT_OK) {
                        Task temp = (Task) getIntent().getSerializableExtra("RETURN_EDIT_TASK");
                        // tekstWidok.setText(temp.getTaskString());
                       zadania.add(temp);

                       aktualizujListeZadan(zadania);
                    }
                    break;
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, "Błąd przy przechwytywaniu rezultatu z ActivityResult!"+e.getMessage(), Toast.LENGTH_LONG).show();
            tekstWidok.setText(e.getMessage());
        }
    }

    private void aktualizujListeZadan(ArrayList<Task> zadania) {
        if (zadania != null) {
            ArrayList<String> temp = new ArrayList<String>();

            for (int i = 0; i < zadania.size(); i++) {
                zadania.get(i).setId(i);
                temp.add(zadania.get(i).getTaskString());
            }

            adapter = new ArrayAdapter<String>(this, R.layout.task_list_item, R.id.task_list_item, temp);
            listaZadan.setAdapter(adapter);
        }
    }

    protected String getTaskView(Task z) {
        String string = "";

        string = "\nNazwa:" + z.getNazwa();
        string += "\nOpis:" + z.getOpis();
        string += "\nWaga:" + z.getWagaZslownik(z.getWaga());
        string += "\nStatus:" + z.getStatusZslownik(z.getStatus());;
        string += "\nTermin:" + z.getTermin();

        return string;
    }

    private void showMyDelDialog() {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("czy usunąć zadanie?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        adapter.remove(adapter.getItem(selectedItem));
                        zadania.remove(selectedItem);
                        zapiszZadania();

                        trescZadanie.setText("Wybierz zadanie");
                        selectedItem = -1;
                        Toast.makeText(getBaseContext(), "Usuwanie!", Toast.LENGTH_LONG).show();

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog okno = builder1.create();
        okno.show();
    }

    private ArrayList<Task> getZadania() {

        ArrayList<Task> temp = new ArrayList<Task>();
        String linia = "";
        InputStream czytaj = this.getResources().openRawResource(R.raw.zadania);
        BufferedReader buffor = new BufferedReader(new InputStreamReader(czytaj));

        if (czytaj != null) {

            try {
                int i = 0;
                while ((linia = buffor.readLine()) != null) {

                    String[] row = linia.split(";");
                    temp.add(new Task(i, row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[4]));
                    i++;
                }

                czytaj.close();
                return temp;

            } catch (IOException e) {

                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

        } else {
            Toast.makeText(this, "Brak zadań do wyśiwtlenia!", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    private ArrayList<Task> getZadaniaInternalStorage() {

        ArrayList<Task> temp = new ArrayList<Task>();

        InputStream czytaj = null;
        try {
            File file = new File(this.getFilesDir(), "zadania.txt");
            if (!file.exists()) {
                file.createNewFile();
                Toast.makeText(this, "Utworzono plik", Toast.LENGTH_LONG).show();

            }

            czytaj = openFileInput("zadania.txt");
            String linia = "";
            if (czytaj != null) {
                InputStreamReader tekst = new InputStreamReader(czytaj);
                BufferedReader buffor = new BufferedReader(tekst);
                int i = 0;
                while ((linia = buffor.readLine()) != null) {

                    String[] row = linia.split(";");
                    temp.add(new Task(i, row[0], row[1], Integer.parseInt(row[2]), Integer.parseInt(row[3]), row[4]));
                    i++;
                }
                czytaj.close();
                return temp;
            } else {
                Toast.makeText(this, "Brak zadań do wyświetlenia!", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return null;
    }

    protected void zapiszZadania() {
        String tresc = "";
        try {
            OutputStreamWriter out = new OutputStreamWriter(

                    openFileOutput("zadania.txt", MODE_PRIVATE));
            openFileOutput("zadania.txt", MODE_APPEND);

            for (int i = 0; i < zadania.size(); i++) {
                out.append(zadania.get(i).getTask());
                out.append("\r\n");
            }
            Toast.makeText(this.getApplicationContext(), "Aktualizacja bazy zadań!", Toast.LENGTH_SHORT).show();
            out.close();

        } catch (Throwable t) {
            Toast.makeText(this.getApplicationContext(), "Błąd przy zapisie, " + t.getMessage(), Toast.LENGTH_SHORT).show();

        }
    }
}
