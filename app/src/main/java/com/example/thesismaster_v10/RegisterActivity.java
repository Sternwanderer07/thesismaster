package com.example.thesismaster_v10;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.thesismaster_v10.DBController.DBHelperUser;
import com.example.thesismaster_v10.Model.UserModel;
import com.google.android.material.textfield.TextInputEditText;

public class RegisterActivity extends AppCompatActivity {

    private TextInputEditText username, password, repassword;
    private Button btnsignup, btnlogin;
    private DBHelperUser db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username_edit_text);
        password = findViewById(R.id.password_edit_text);
        repassword = findViewById(R.id.repassword_edit_text);

        btnsignup = findViewById(R.id.btnsignup);
        btnlogin = findViewById(R.id.btnlogin);
        db = new DBHelperUser(this);
        db.openDatabase();

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();

                if (user.equals("")||pass.equals("")||repass.equals(""))
                    Toast.makeText(RegisterActivity.this, "Bitte alle Felder ausfüllen.", Toast.LENGTH_SHORT).show();
                else {
                    if (pass.equals(repass)){
                        Boolean checkuser = db.checkusername(user);
                        if (!checkuser){
                            UserModel users = new UserModel();
                            users.setUsername(user);
                            users.setPassword(pass);
                            Boolean insert = db.insertUserData(users);
                            if (insert) {
                                Toast.makeText(RegisterActivity.this, "Registrierung erfolgreich.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }
                            else{
                                Toast.makeText(RegisterActivity.this, "Registrierung fehlgeschlagen.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(RegisterActivity.this, "Benutzer existiert bereits. Bitte loggen sie sich ein.", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        Toast.makeText(RegisterActivity.this, "Das Passwort stimmt nicht überein.", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);

            }
        });
    }

}