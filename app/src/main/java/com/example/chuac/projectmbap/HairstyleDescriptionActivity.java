package com.example.chuac.projectmbap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HairstyleDescriptionActivity extends AppCompatActivity {

    TextView hairtitle;
    TextView hairdescription;
    TextView hairinstruction;
    TextView hairreview;
    ImageView hairimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hairstyle_description);

        hairtitle = findViewById(R.id.HairTitle);
        hairdescription = findViewById(R.id.hairDescr);
        hairinstruction = findViewById(R.id.hairInstruction);
        hairreview = findViewById(R.id.hairReview);
        hairimg = findViewById(R.id.hairIMG);


        DatabaseReference wDatabase =
                FirebaseDatabase.getInstance().getReference().child("Hairstyle");
        wDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                    ArrayList<String> names = new ArrayList<String>();
                    ArrayList<String> password = new ArrayList<String>();
                    ArrayList<String> email = new ArrayList<String>();
                    ArrayList<String> review = new ArrayList<String>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String name = ds.child("name").getValue(String.class);
                        String Desc = ds.child("Desc").getValue(String.class);
                        String Instruction = ds.child("Instruction").getValue(String.class);
                        String Review = ds.child("review").getValue(String.class);

                        names.add(name);
                        password.add(Desc);
                        email.add(Instruction);
                        review.add(Review);
                    }

                    String[] userrnames = names.toArray(new String[names.size()]);
                    String[] passswords = password.toArray(new String[password.size()]);
                    String[] emmail = email.toArray(new String[email.size()]);
                    String[] revview = review.toArray(new String[review.size()]);

                    String comparator = getIntent().getStringExtra("description");
                    int imgname = getIntent().getExtras().getInt("imagelocation");

                    for (int i = 0 ; i < userrnames.length ; i++){
                        if (userrnames[i].matches(comparator)){
                            hairtitle.setText(userrnames[i].toString());
                            hairdescription.setText(passswords[i].toString());
                            hairinstruction.setText(emmail[i].toString());
                            hairreview.setText(revview[i].toString());
                            hairimg.setImageResource(imgname);
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

    public void goComments (View v){
        String usegetname = getIntent().getStringExtra("description");
        //Toast.makeText(getApplicationContext(), usegetname, Toast.LENGTH_LONG).show();
        Intent commentIntent = new Intent(this, CommentActivity.class);
        commentIntent.putExtra("CommentsUsename", usegetname);
        startActivity(commentIntent);
    }

    public void addToFavourites (final View v){

        final String usehairname = getIntent().getStringExtra("description"); //obtain hairstyle name


        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        String useUserName = prefs.getString("name", null); //obtain username
        if (useUserName != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        }



        if (usehairname.length() != 0 && useUserName.length() != 0)
        {



            DatabaseReference wDatabase =
                    FirebaseDatabase.getInstance().getReference().child("Favourites");
            wDatabase.addListenerForSingleValueEvent(new ValueEventListener() {

                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                        ArrayList<String> hairkeys = new ArrayList<String>();
                        ArrayList<String> userkeys = new ArrayList<String>();
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            Favourites favourites = ds.getValue(Favourites.class);
                            hairkeys.add(favourites.hairkey);
                            userkeys.add(favourites.userkey);
                        }

                        String[] passswords = hairkeys.toArray(new String[hairkeys.size()]);
                        String[] userrnames = userkeys.toArray(new String[userkeys.size()]);

                        String comparator = usehairname; //obtain hairstyle name


                        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
                        String comparator1 = prefs.getString("name", null); //obtain username
                        if (comparator1 != null) {
                            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
                        }



                        int count = 0;
                        for (int i = 0 ; i < userrnames.length; i++) {
                            if (comparator1.matches(userrnames[i]) && comparator.matches(passswords[i])) {
                                count = 1;
                                break;
                            }
                        }
                        Log.d("INT", "Value = " + count );


                        if (count == 1){
                            Toast.makeText(getApplicationContext(), "You have already added it into favourites", Toast.LENGTH_LONG).show();
                            return;

                        }
                        else if (count == 0){
                            // To write data to the Firebase
                            Log.d("username1:", comparator1);
                            Log.d("hairstylename1:", usehairname);
                            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                            Favourites favourites = new Favourites(usehairname, comparator1);
                            // To write to the database one item
                            String key = mDatabase.child("Favourites").push().getKey(); //define unique key for favourites

                            mDatabase.child("Favourites").child(key).setValue(favourites);

                            Toast.makeText(getApplicationContext(), "You have successfully added " + usehairname + " to favourites", Toast.LENGTH_LONG).show();
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
}
class Favourites {
    public String hairkey;
    public String userkey;
    public Favourites() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Favourites(String hairkey, String userkey) {
        this.hairkey = hairkey;
        this.userkey = userkey;
    }
}