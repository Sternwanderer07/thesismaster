package com.example.thesismaster_v10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thesismaster_v10.DBController.DBHelperUser;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {
    TextInputEditText username, password;
    Button btnlogin, btnsignup;
    DBHelperUser db;

    public static SharedPreferences sharedPreferences;
    public static final String login = "login";
    public static final String UName = "username";
    public static final String Pw = "password";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        username = findViewById(R.id.username_edit_text_lg);
        password = findViewById(R.id.password_edit_text_lg);

        btnlogin = findViewById(R.id.btnloginlg);
        btnsignup = findViewById(R.id.btnsignuplg);

        sharedPreferences = getSharedPreferences(login, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(UName)){
            Intent i = new Intent(LoginActivity.this, ThesisActivity.class);
            startActivity(i);
        }


        db = new DBHelperUser(this);
        db.openDatabase();

        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();



                if (user.equals("")||pass.equals(""))
                    Toast.makeText(LoginActivity.this, "Bitte alle Felder ausfüllen.", Toast.LENGTH_SHORT).show();
                else {
                    Boolean checkuserpass = db.checkusernamepassword(user, pass);
                    if (checkuserpass==true){
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(UName, user);
                        editor.putString(Pw, pass);
                        editor.commit();

                        Toast.makeText(LoginActivity.this, "Login erfolgreich.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(),ThesisActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(LoginActivity.this, "Login fehlgeschlagen. Zugangsdaten sind nicht korrekt.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);

            }
        });
    }



}