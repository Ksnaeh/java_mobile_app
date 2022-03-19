package com.example.chuac.projectmbap;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class CustomFavouritesAdapter extends BaseAdapter{
    String [] result;
    Context context;
    int [] imageId;
    String[] array1;

    private static LayoutInflater inflater=null;
    public CustomFavouritesAdapter(Context mainActivity, String[] prgmNameList, int[] prgmImages, String[] bodo) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        array1 = bodo;
        inflater = ( LayoutInflater )context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        TextView monde;
        ImageView img;
        ImageButton btn;

    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView = inflater.inflate(R.layout.custom_favouritesadapter, null);

        //holder.listings = array1.get(position);
        Log.d("Tagheuer", "value" + array1[position]);
        Log.d("Tagposition", "value" + result[position]);

        holder.tv=(TextView) rowView.findViewById(R.id.NameView);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);
        holder.btn= rowView.findViewById(R.id.imageButton1);
        holder.monde =(TextView) rowView.findViewById(R.id.invisview);
        holder.monde.setText(result[position]);
        holder.tv.setText(array1[position]);
        holder.tv.setVisibility(View.GONE);
        holder.img.setImageResource(imageId[position]);
        holder.btn.setTag(array1[position]);
        holder.btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {
                //do something
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                Query deleteQuery = ref.child("Favourites").orderByChild("hairkey").equalTo(result[position]);

                deleteQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists() && dataSnapshot.getChildrenCount() > 0) {

                            ArrayList<String> uruserid = new ArrayList<String>();
                            ArrayList<String> uruniquekey = new ArrayList<String>();

                            for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                String getuserkey = dataSnapshot1.child("userkey").getValue(String.class);
                                uruserid.add(getuserkey);

                                String uniquekey = dataSnapshot1.getKey();
                                uruniquekey.add(uniquekey);
                            }

                            String[] convertedusers = uruserid.toArray(new String[uruserid.size()]);
                            String[] convertedids = uruniquekey.toArray(new String[uruniquekey.size()]);

                            int u = 0;
                            String unkey = "";
                            for (int x = 0; x < convertedusers.length; x++){
                                if (convertedusers[x].matches(array1[position])){
                                    u = 1;
                                    unkey = convertedids[x];
                                    break;
                                }
                            }
                            Log.d("INTurmomgay", "Value = " + u );
                            Log.d("INTuruniquekey", "Value = " + unkey );

                            if (u == 1) {
                                Log.d("username:", array1[position]);
                                Log.d("hairstylename:", result[position]);
                                Log.d("uniquekey", "Value = " + unkey );
                                //Log.d("favouritekey:", keys);

                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();

                                 mDatabase.child("Favourites").child(unkey).removeValue();

                                Toast.makeText(v.getContext(), "You have successfully removed " + result[position] + " from favourites", Toast.LENGTH_LONG).show();
                                return;
                            }
                            else{
                                return;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });
        rowView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                //Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
                Intent descriptionIntent = new Intent(v.getContext(), HairstyleDescriptionActivity.class);
                descriptionIntent.putExtra("description", result[position]);
                descriptionIntent.putExtra("imagelocation", imageId[position]);
                v.getContext().startActivity(descriptionIntent);

            }
        });
        return rowView;
    }
}

