package com.example.myapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeScreenActivity extends AppCompatActivity {

    Button[] buttons = new Button[8];
    ListView listView;
    ArrayList<String> subscriptions = new ArrayList<>() ;
    DatabaseReference mRootRef = FirebaseDatabase.getInstance().getReference().child("Subs");
    DatabaseReference mMaintainRef = mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        listView = (ListView) findViewById(R.id.subsList);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.listview_item, subscriptions);

        listView.setAdapter(arrayAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                Intent intent = new Intent(HomeScreenActivity.this, AddSubActivity.class);
                startActivity(intent);
            }
        });

        mMaintainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //total amount of sbscriptions
                //int amount = Integer.valueOf(dataSnapshot.child("Amount").getValue().toString());

                //System.out.println(amount);
                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String name = childSnapshot.child("name").getValue(String.class);
                    //String price = childSnapshot.child("price").getValue(String.class);
                    subscriptions.add(name);
                }
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //Toast.makeText(HomeScreenActivity.this, String.valueOf(position + id), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(HomeScreenActivity.this, SubInfoActivity.class);
                intent.putExtra("service_id", subscriptions.get(position));
                startActivity(intent);
            }
        });

    }

   /* @Override
    protected void onStart(){
        super.onStart();
        mMaintainRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (int i=0; i<8; i++){
                    String text = dataSnapshot.child("Subs").child(String.valueOf(i+1)).child("name").getValue(String.class);
                    String price =  dataSnapshot.child("Subs").child(String.valueOf(i+1)).child("price").getValue(String.class);
                    if (text != null){
                        buttons[i].setText((i+1) + ". " + text + ", £" + price);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.test_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item1:
                Toast.makeText(this, "Item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item2:
                Toast.makeText(this, "Item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem1:
                Toast.makeText(this, "Sub item 1 selected", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.subitem2:
                Toast.makeText(this, "Sub item 2 selected", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Gets called when a subscription button is pressed from the home screen
    public void onClick(View v) {
        Button clickedButton = (Button) v;
        if (clickedButton.getText() != ""){
            String id = Character.toString(clickedButton.getText().charAt(0));
            Intent intent = new Intent(HomeScreenActivity.this, SubInfoActivity.class);
            intent.putExtra("service_id", id);
            startActivity(intent);
        }
        else{
            Toast.makeText(this, "There is not a subscription there yet", Toast.LENGTH_SHORT).show();
        }

    }
}