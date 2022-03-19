package com.example.chuac.projectmbap;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavouritesActivity extends AppCompatActivity {

    ListView heimdall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourites);

        heimdall = findViewById(R.id.heimdall);

        final String odin = getIntent().getStringExtra("heartache");

        //database reference
        DatabaseReference wDatabase =
                FirebaseDatabase.getInstance().getReference().child("Favourites");
        wDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<List> listOfMixedTypes = new ArrayList<List>();

                    List<String> names = new ArrayList<String>();
                    List<String> user = new ArrayList<String>();
                    List<Integer> hairimglink = new ArrayList<Integer>();

                    listOfMixedTypes.add(names);
                    listOfMixedTypes.add(user);

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String name = ds.child("hairkey").getValue(String.class);
                        String userkey = ds.child("userkey").getValue(String.class);

                        if (odin.matches(userkey)){
                            names.add(name);
                            user.add(userkey);

                            int parselink = getResources().getIdentifier(name.replaceAll(" ", "_").toLowerCase(),
                                    "drawable", getPackageName());

                            hairimglink.add(parselink);
                            Log.d("TAGimg1", "value:" +  parselink);
                        }
                        else{
                            Log.d("TAG", name);
                        }


                    }


                    int size = hairimglink.size();
                    int[] result = new int[size];
                    Integer[] temp = hairimglink.toArray(new Integer[hairimglink.size()]);
                    for (int n = 0; n < size; ++n) {
                        result[n] = temp[n];
                    }


                    String[] hairnames = names.toArray(new String[names.size()]);
                    String[] usernames = user.toArray(new String[user.size()]);


                    heimdall.setAdapter(new CustomFavouritesAdapter(FavouritesActivity.this, hairnames, result, usernames));

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
