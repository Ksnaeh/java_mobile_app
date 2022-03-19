package com.example.chuac.projectmbap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUPactivity extends AppCompatActivity {

    EditText username;
    EditText passwort;
    EditText email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_upactivity);
        username = findViewById(R.id.username);
        passwort = findViewById(R.id.password);
        email = findViewById(R.id.newemail);

    }

    public void addNewUser(View v) {
        // check that both the fields are not empty
        if (username.getText().toString().length() != 0 &&
                email.getText().toString().length() != 0)
        {
            // To write data to the Firebase
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            User user = new User(username.getText().toString(), passwort.getText().toString(), email.getText().toString());
            // To write to the database one item

            mDatabase.child("users").child(username.getText().toString()).setValue(user);
            Toast.makeText(getApplicationContext(), "Details added: " +
                    username.getText().toString() + ", "
                    + email.getText().toString(), Toast.LENGTH_LONG).show();
            Intent backIntent = new Intent(this, SignInActivity.class);
            startActivity(backIntent);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "ERROR: Name and email cannot be empty", Toast.LENGTH_LONG).show();
        }
    }
}
class User {
    public String username;
    public String passwort;
    public String email;
    public User() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public User(String username, String passwort, String email) {
        this.username = username;
        this.passwort = passwort;
        this.email = email;
    }
}