package com.example.chuac.projectmbap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfilePage extends AppCompatActivity {

    TextView profileusername;
    TextView profilepassword;
    TextView profileemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        profileusername = findViewById(R.id.profileUsername);
        profilepassword = findViewById(R.id.textView8);
        profileemail = findViewById(R.id.profileEmail);



            DatabaseReference wDatabase =
                FirebaseDatabase.getInstance().getReference().child("users");
        wDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> password = new ArrayList<String>();
                    ArrayList<String> email = new ArrayList<String>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        User user = ds.getValue(User.class);
                        names.add(user.username);
                        password.add(user.passwort);
                        email.add(user.email);
                    }
                    String[] userrnames = names.toArray(new String[names.size()]);
                    String[] passswords = password.toArray(new String[password.size()]);
                    String[] emmail = email.toArray(new String[email.size()]);
                    String comparator = getIntent().getStringExtra("storedname");

                    for (int i = 0 ; i < userrnames.length ; i++){
                        if (userrnames[i].matches(comparator)){
                            profileusername.setText(userrnames[i].toString());
                            profilepassword.setText(passswords[i].toString());
                            profileemail.setText(emmail[i].toString());
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

    public void goChangePassword (View view){
        String brukernavn = profileusername.getText().toString();
        String ordet = profilepassword.getText().toString();
        Intent passwordintent = new Intent(this, ChangePasswordActivity.class);
        passwordintent.putExtra("brukers", brukernavn);
        passwordintent.putExtra("passordet", ordet);
        startActivity(passwordintent);
    }

    public void goChangeEmail (View view){
        String usename = profileusername.getText().toString();
        String emailet = profileemail.getText().toString();
        Intent emailintent = new Intent(this, ChangeEmailActivity.class);
        emailintent.putExtra("toilet", usename);
        emailintent.putExtra("toiletbowl", emailet);
        startActivity(emailintent);
    }

    public void logOut (View view){
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public void goFavourites (View view){

        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        String corazon = prefs.getString("name", null); //obtain username
        if (corazon != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        }


        Intent favouritesIntent = new Intent(this, FavouritesActivity.class);
        favouritesIntent.putExtra("heartache", corazon);
        startActivity(favouritesIntent);
    }
}
