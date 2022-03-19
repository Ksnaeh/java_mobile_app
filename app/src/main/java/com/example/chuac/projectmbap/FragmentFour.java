package com.example.chuac.projectmbap;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentFour extends Fragment {

    ListView urmather;

    public FragmentFour() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_four, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        urmather = getView().findViewById(R.id.propterms);

        DatabaseReference wDatabase =
                FirebaseDatabase.getInstance().getReference().child("BarberTerms");
        wDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    ArrayList<String> titles = new ArrayList<String>();
                    //List<Integer> descripsions = new ArrayList<Integer>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {


                        String name = ds.child("Title").getValue(String.class);
                        //String descripsion = ds.child("Description").getValue(String.class);

                        titles.add(name);
                        //descripsions.add(descripsions);

                    }
                    final String[] hairnames = titles.toArray(new String[titles.size()]);


                    urmather.setAdapter(new ArrayAdapter(view.getContext(), R.layout.custom_textview_fragmentfour, titles));
                    urmather.setClickable(true);
                    urmather.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                            Intent destails = new Intent(view.getContext(), DetailsActivity.class);
                            destails.putExtra("barberterms", hairnames[position]);
                            startActivity(destails);
                        }
                    });
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}
