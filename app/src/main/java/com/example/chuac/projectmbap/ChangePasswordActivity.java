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

public class ChangePasswordActivity extends AppCompatActivity {

    EditText newpassword;
    EditText oldpassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        newpassword = findViewById(R.id.newPassword);
        oldpassword = findViewById(R.id.oldPassword);
    }

    public void changePassword (final View view) {

        if (newpassword.getText().toString().length() != 0 &&
                oldpassword.getText().toString().length() != 0) {
            final DatabaseReference wDatabase =
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

                        String acompanio = getIntent().getStringExtra("brukers");
                        String acompania = getIntent().getStringExtra("passordet");

                        for (int i = 0; i < userrnames.length; i++) {
                            if (userrnames[i].matches(acompanio)) {
                                for (int x = 0; x < passswords.length; x++) {
                                    if (userrnames[i].matches(acompanio) && passswords[i].matches(acompania)) {
                                        //Toast.makeText(getApplicationContext(), "Password successfully", Toast.LENGTH_LONG).show();
                                        String updatepassword = newpassword.getText().toString(); //new password
                                        String priorpassword = oldpassword.getText().toString(); //old password
                                        // To write to the database one item
                                        if (acompania.matches(priorpassword)) {
                                            wDatabase.child(userrnames[i]).child("passwort").setValue(updatepassword);
                                            Intent tilbaks = new Intent(view.getContext(), ProfilePage.class);
                                            tilbaks.putExtra("storedname", acompanio);
                                            startActivity(tilbaks);
                                            Toast.makeText(getApplicationContext(), "password has been changed", Toast.LENGTH_LONG).show();
                                            return;
                                        } else {
                                            Toast.makeText(getApplicationContext(), "Old password is invalid", Toast.LENGTH_LONG).show();
                                            return;
                                        }
                                    }
                                }
                            } else if ((i == userrnames.length - 1) && (acompanio != userrnames[i].toString())) {
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

    public void goBackProfile (View view){
        finish();
    }
}
