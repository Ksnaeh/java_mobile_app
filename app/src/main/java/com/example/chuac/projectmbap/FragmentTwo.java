package com.example.chuac.projectmbap;

import android.app.ActionBar;
import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FragmentTwo extends Fragment {

    ListView secondHolder;

    public FragmentTwo() {
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
        return inflater.inflate(R.layout.fragment_two, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        secondHolder = getView().findViewById(R.id.heimdall);

        DatabaseReference wDatabase =
                FirebaseDatabase.getInstance().getReference().child("Hairstyle");
        wDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<String> names = new ArrayList<String>();
                    List<Integer> hairimglink = new ArrayList<Integer>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String name = ds.child("name").getValue(String.class);
                        Long year = ds.child("Year").getValue(Long.class);

                        if (year == 2018) {
                            names.add(name);

                            int parselink = getResources().getIdentifier(name.replaceAll(" ", "_").toLowerCase(),
                                    "drawable", getActivity().getPackageName());

                            hairimglink.add(parselink);
                            Log.d("TAG", name);
                        }
                        else{
                            Log.d("TAG", name);
                        }
                    }
                    String[] hairnames = names.toArray(new String[names.size()]);

                    int size = hairimglink.size();
                    int[] result = new int[size];
                    Integer[] temp = hairimglink.toArray(new Integer[hairimglink.size()]);
                    for (int n = 0; n < size; ++n) {
                        result[n] = temp[n];
                    }

                    secondHolder.setAdapter(new CustomAdapter(getContext(), hairnames,result));
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }
}

