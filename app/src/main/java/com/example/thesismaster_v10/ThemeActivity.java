package com.example.thesismaster_v10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thesismaster_v10.Model.QuestionModel;
import com.google.android.material.navigation.NavigationView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ThemeActivity extends AppCompatActivity{

    private TextView themeText1, themeText2;
    private Button btntheme1, btntheme2, btntheme3;
    private String thesisTitle = "Abschlussarbeit";
    private String dueDate = "Viel Erfolg";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theme);

        Toolbar toolbar = findViewById(R.id.toolbartheme);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btntheme1 = findViewById(R.id.btntheme1);
        btntheme2 = findViewById(R.id.btntheme2);
        btntheme3 = findViewById(R.id.btntheme3);


        themeText1 = findViewById(R.id.themeText1);
        themeText2 = findViewById(R.id.themeText2);


        Bundle extrasget = getIntent().getExtras();
        if (extrasget != null) {
            thesisTitle = extrasget.getString("thesis");
            dueDate = extrasget.getString("dueDate");
        }
        themeText1.setText(thesisTitle);


        try {
            String FinalDate = dueDate;

            LocalDate now = LocalDate.now();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
            String CurrentDate = now.format(formatter);

            Date date1;
            Date date2;
            SimpleDateFormat dates = new SimpleDateFormat("dd.MM.yyyy");
            date1 = dates.parse(CurrentDate);
            date2 = dates.parse(FinalDate);
            long difference = Math.abs(date1.getTime() - date2.getTime());
            long differenceDates = difference / (24 * 60 * 60 * 1000);
            String dayDifference = Long.toString(differenceDates);
            themeText2.setText(dayDifference + " Tage bis zur Abgabe");
        } catch (Exception exception) {
            Toast.makeText(this, "Unable to find difference", Toast.LENGTH_SHORT).show();
        }

        btntheme1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Toast.makeText(ThemeActivity.this, "Clicked Struktur der Arbeit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("thesis", themeText1.getText());
                intent.putExtra("theme", btntheme1.getText());
                startActivity(intent);

            }
        });
        btntheme2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ThemeActivity.this, "Clicked Methodisches Vorgehen der Arbeit", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("thesis", themeText1.getText());
                intent.putExtra("theme", btntheme2.getText());
                startActivity(intent);


            }
        });
        btntheme3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(ThemeActivity.this, "Clicked Schlussfolgerung und Mehrwert", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.putExtra("thesis", themeText1.getText());
                intent.putExtra("theme", btntheme3.getText());
                startActivity(intent);

            }
        });


    }



}