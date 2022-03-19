package com.example.chuac.projectmbap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DetailsActivity extends AppCompatActivity {

    TextView titlen;
    TextView beskrivelsen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        titlen = findViewById(R.id.Titels);
        beskrivelsen = findViewById(R.id.D_esc);

        DatabaseReference wDatabase = FirebaseDatabase.getInstance().getReference().child("BarberTerms");
        wDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    ArrayList<String> titlene = new ArrayList<String>();
                    ArrayList<String> bskvlsn = new ArrayList<String>();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        String name = ds.child("Title").getValue(String.class);
                        String problematicsia = ds.child("Description").getValue(String.class);
                        titlene.add(name);
                        bskvlsn.add(problematicsia);
                    }

                    String[] t_itle = titlene.toArray(new String[titlene.size()]);
                    String[] d_esc = bskvlsn.toArray(new String[bskvlsn.size()]);
                    String comparator1 = getIntent().getStringExtra("barberterms");
                    for (int i = 0 ; i < t_itle.length ; i++){
                        if (t_itle[i].matches(comparator1)){
                            titlen.setText(t_itle[i].toString());
                            beskrivelsen.setText(d_esc[i].toString());
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
