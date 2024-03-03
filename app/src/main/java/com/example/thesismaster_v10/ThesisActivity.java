package com.example.thesismaster_v10;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thesismaster_v10.Adapter.ThesisAdapter;
import com.example.thesismaster_v10.DBController.DatabaseHandler;
import com.example.thesismaster_v10.DBController.DatabaseHelper;
import com.example.thesismaster_v10.Model.QuestionModel;
import com.example.thesismaster_v10.Model.ThesisModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ThesisActivity extends AppCompatActivity implements DialogCloseListener, NavigationView.OnNavigationItemSelectedListener{

    private DrawerLayout drawerLayout;

    private DatabaseHandler MyDB;

    public RecyclerView thesisRecyclerView;
    private ThesisAdapter.RecyclerViewClickListener recListener;
    private ThesisAdapter thesisAdapter;
    public FloatingActionButton thesisfab;
    private List<ThesisModel> thesisList;
    private ImageView infoImage;

    SharedPreferences sharedPreferences;
    TextView hello;

    public static final String login = "login";
    public static final String UName = "username";

    private static String shareduser;

    public String getShareduser(){
        return shareduser;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thesis);

        Toolbar toolbar = findViewById(R.id.toolbarstart);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //Navigation
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        hello = findViewById(R.id.thesisList1);
        sharedPreferences = getSharedPreferences(login, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(UName))
            hello.setText("Willkommen "+ sharedPreferences.getString(UName, ""));
        shareduser = sharedPreferences.getString(UName, "");

        MyDB = new DatabaseHandler(this);
        MyDB.openDatabase();


        // Abschlussarbeiten in Liste einfügen
        thesisRecyclerView = findViewById(R.id.thesisRecyclerView);
        thesisRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        setOnClickListener();

        thesisAdapter = new ThesisAdapter(MyDB,ThesisActivity.this, recListener);
        thesisRecyclerView.setAdapter(thesisAdapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new ThRecyclerItemTouchHelper(thesisAdapter));
        itemTouchHelper.attachToRecyclerView(thesisRecyclerView);

        thesisfab = findViewById(R.id.thesisfab);

        thesisList = MyDB.getAllUserThesis(sharedPreferences.getString(UName, ""));
        Collections.reverse(thesisList);

        thesisAdapter.setThesis(thesisList);

        thesisfab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewThesis.newInstance().show(getSupportFragmentManager(), AddNewThesis.TAG);

            }
        });

        infoImage = findViewById(R.id.infoImage);

        infoImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setTitle("Anleitung zur Nutzung");
                builder.setMessage("- Bearbeiten: Arbeit nach rechts wischen \n- Löschen: Arbeit nach links wischen \n- Reflexion starten: Anklicken der Arbeit");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Schließen des Dialogs
                        dialog.dismiss();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }

    private void setOnClickListener() {
        recListener = new ThesisAdapter.RecyclerViewClickListener(){
            @Override
            public void onClick (View v,int position){
                Intent intent = new Intent(getApplicationContext(), ThemeActivity.class);
                intent.putExtra("thesis", thesisList.get(position).getThesis());
                intent.putExtra("dueDate", thesisList.get(position).getDuedate());
                startActivity(intent);
            }
        };

    }


    @Override
    public void handleDialogClose(DialogInterface dialog){
        sharedPreferences = getSharedPreferences(login, Context.MODE_PRIVATE);
        thesisList = MyDB.getAllUserThesis(sharedPreferences.getString(UName, ""));
        thesisAdapter.setThesis(thesisList);
        thesisAdapter.notifyDataSetChanged();
    }

    @Override
    public void onResume() {
        super.onResume();
        thesisList = MyDB.getAllUserThesis(sharedPreferences.getString(UName, ""));
        Collections.reverse(thesisList);
        thesisAdapter.setThesis(thesisList);
        thesisAdapter.notifyDataSetChanged();
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int id = item.getItemId();


            if (id == R.id.settings) {
                Toast.makeText(ThesisActivity.this, "Einstellungen in Arbeit", Toast.LENGTH_SHORT).show();
            } else if (id == R.id.logout) {
                SharedPreferences preferences = getSharedPreferences(login, Context.MODE_PRIVATE);
                preferences.edit().clear().apply();
                Toast.makeText(this, "Logout erfolgreich.", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(this,LoginActivity.class);
                startActivity(intent2);
            }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}