package com.example.chuac.projectmbap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SignInActivity extends AppCompatActivity {

    EditText loginusername;
    EditText loginpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);
        loginusername = findViewById(R.id.username);
        loginpassword = findViewById(R.id.password);

    }


    public void goSignUp (View view){
        Intent signupIntent = new Intent(this, SignUPactivity.class);
        startActivity(signupIntent);
    }

    public void validateUser(final View v) {
        // check that both the fields are not empty
        if (loginusername.getText().toString().length() != 0 &&
                loginpassword.getText().toString().length() != 0) {


            DatabaseReference wDatabase =
                    FirebaseDatabase.getInstance().getReference().child("users");
                    wDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        ArrayList<String> names = new ArrayList<String>();
                        ArrayList<String> password = new ArrayList<String>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            User user = ds.getValue(User.class);
                            names.add(user.username);
                            password.add(user.passwort);
                        }

                        String[] userrnames = names.toArray(new String[names.size()]);
                        String[] passswords = password.toArray(new String[password.size()]);
                        for (int i = 0 ; i < userrnames.length; i++) {
                            if (userrnames[i].matches(loginusername.getText().toString())) {

                                for (int x = 0 ; x < passswords.length; x++){
                                    if (userrnames[i].matches(loginusername.getText().toString()) && passswords[i].matches(loginpassword.getText().toString())){
                                        //Toast.makeText(getApplicationContext(), "Password successfully", Toast.LENGTH_LONG).show();
                                        Intent homeIntent = new Intent(v.getContext(), HomeActivity.class);
                                        homeIntent.putExtra("STRING_I_NEED", userrnames[i]);


                                        SharedPreferences.Editor editor = getApplicationContext().getSharedPreferences("mypref", MODE_PRIVATE).edit();
                                        editor.putString("name", loginusername.getText().toString());
                                        editor.commit();

                                        startActivity(homeIntent);
                                        //Toast.makeText(getApplicationContext(), userrnames[i], Toast.LENGTH_LONG).show();
                                        finish();
                                        return;
                                    }
                                    else if ((x == passswords.length-1) && (loginusername.getText().toString() != userrnames[i].toString()) && (loginpassword.getText().toString() != passswords[i].toString())){
                                        Toast.makeText(getApplicationContext(), "Wrong username or password!", Toast.LENGTH_LONG).show();
                                    }
                                }
                            }
                            else if ((i == userrnames.length-1) && (loginusername.getText().toString() != userrnames[i].toString())){
                                Toast.makeText(getApplicationContext(), "User does not exist!", Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                }
            });
        }
        else
        {
            Toast.makeText(getApplicationContext(), "Username and password cannot be empty", Toast.LENGTH_LONG).show();
        }
    }

}
