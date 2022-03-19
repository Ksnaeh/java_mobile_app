package com.example.chuac.projectmbap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends AppCompatActivity {

    ListView loki;
    EditText addcomments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        String restoredText = prefs.getString("name", null);
        if (restoredText != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        }

        final String restoredTitle = getIntent().getStringExtra("CommentsUsename"); //hairstyle name


        loki = findViewById(R.id.heimdall);
        addcomments = findViewById(R.id.AddComments);



        //database reference
        DatabaseReference wDatabase =
                FirebaseDatabase.getInstance().getReference().child("Comments");
        wDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {
                    List<String> names = new ArrayList<String>();
                    List<String> hairimglink = new ArrayList<String>();
                    List<String> user = new ArrayList<String>();

                    for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        String name = ds.child("hairstyle").getValue(String.class);
                        String comments = ds.child("comments").getValue(String.class);
                        String userkey = ds.child("userkey").getValue(String.class);

                        if (restoredTitle.matches(name)){
                            names.add(name);
                            hairimglink.add(comments);
                            user.add(userkey);
                        }
                        else{
                            Log.d("TAG", name);
                        }


                    }


                    String[] hairnames = names.toArray(new String[names.size()]);
                    String[] cOmment = hairimglink.toArray(new String[hairimglink.size()]);
                    String[] uEsr = user.toArray(new String[user.size()]);
                    //ArrayAdapter adapter = new ArrayAdapter(CommentActivity.this, android.R.layout.simple_list_item_1, hairimglink);
                    //loki.setAdapter(adapter);
                    loki.setAdapter(new CustomCommentAdapter(CommentActivity.this, uEsr, cOmment));


                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
            }
        });
    }

    public void addComments (View v){

        SharedPreferences prefs = getSharedPreferences("mypref", MODE_PRIVATE);
        String restoredText1 = prefs.getString("name", null);
        if (restoredText1 != null) {
            String name = prefs.getString("name", "No name defined");//"No name defined" is the default value.
        }
        String restoredTitle1 = getIntent().getStringExtra("CommentsUsename");



        if (addcomments.getText().toString().length() != 0)
        {
            // To write data to the Firebase
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
            Comments com_ments = new Comments(restoredText1, addcomments.getText().toString(), restoredTitle1);
            // To write to the database one item
            String key = mDatabase.child("Comments").push().getKey();

            mDatabase.child("Comments").child(key).setValue(com_ments);
            Toast.makeText(getApplicationContext(), "Comment added!", Toast.LENGTH_LONG).show();
            addcomments.setText(null);
        }
        else
        {
            Toast.makeText(getApplicationContext(), "ERROR: Please enter something in the comments!", Toast.LENGTH_LONG).show();
        }
    }
}
class Comments {
    public String userkey;
    public String comments;
    public String hairstyle;
    public Comments() {
        // Default constructor required for calls to DataSnapshot.getValue(User.class)
    }
    public Comments(String userkey, String comments, String hairstyle) {
        this.userkey = userkey;
        this.comments = comments;
        this.hairstyle = hairstyle;
    }
}