package com.example.chuac.projectmbap;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ChangeEmailActivity extends AppCompatActivity {

    EditText changeemail;
    EditText oldemaill;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        changeemail = findViewById(R.id.swapEmail);
        oldemaill = findViewById(R.id.oldEmail);
    }


    public void changeEmail (final View view) {

        if (changeemail.getText().toString().length() != 0 &&
                oldemaill.getText().toString().length() != 0) {
            final DatabaseReference wDatabase =
                    FirebaseDatabase.getInstance().getReference().child("users");
            wDatabase.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        ArrayList<String> names = new ArrayList<String>();
                        ArrayList<String> emails = new ArrayList<String>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            User user = ds.getValue(User.class);
                            names.add(user.username);
                            emails.add(user.email);
                        }

                        String[] userrnames = names.toArray(new String[names.size()]);
                        String[] emmails = emails.toArray(new String[emails.size()]);

                        String senor = getIntent().getStringExtra("toilet"); //retrieved stored user name
                        String senora = getIntent().getStringExtra("toiletbowl"); //retrieve stored email

                        for (int i = 0; i < userrnames.length; i++) {
                            if (userrnames[i].matches(senor)) {
                                for (int x = 0; x < emmails.length; x++) {
                                    if (userrnames[i].matches(senor) && emmails[i].matches(senora)) {
                                        //Toast.makeText(getApplicationContext(), "Password successfully", Toast.LENGTH_LONG).show();
                                        String swappingmags = changeemail.getText().toString(); //new email
                                        String reloading = oldemaill.getText().toString(); //old email
                                        // To write to the database one item
                                        if (senora.matches(reloading)) {
                                            wDatabase.child(userrnames[i]).child("email").setValue(swappingmags);
                                            Intent tilbaks = new Intent(view.getContext(), ProfilePage.class);
                                            tilbaks.putExtra("storedname", senor);
                                            startActivity(tilbaks);
                                            Toast.makeText(getApplicationContext(), "Email has been changed", Toast.LENGTH_LONG).show();
                                            return;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Old email is invalid", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                }
                            } else if ((i == userrnames.length - 1) && (senor != userrnames[i].toString())) {
                                Toast.makeText(getApplicationContext(), "User does not fucking exist?", Toast.LENGTH_LONG).show();

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
    }


    public void goBacktoProfile (View view){
        finish();
    }
}
