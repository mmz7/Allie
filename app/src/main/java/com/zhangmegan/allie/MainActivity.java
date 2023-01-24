package com.zhangmegan.allie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    HashMap<String, HashMap> symptoms = new HashMap<String, HashMap>();
    HashMap<String, ArrayList> ingredients = new HashMap<String, ArrayList>();
    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef2 = database.getReference("User1");

        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataEntry entry = new DataEntry(1,17,2023, 1, 3, "food", "honeydew, egg, milk");
                myRef2.child("4").setValue(entry);
            }
        });

        // write to database
//        DatabaseReference myRef = database.getReference("message");
//        myRef.setValue("Hello, World!");

        // read from database
        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                ArrayList<Object> id = (ArrayList<Object>) dataSnapshot.getValue();
                int len = id.size();

                Map<String, Object> recent = (Map<String, Object>) id.get(len-1);

//                for(int i = 0; i < len; i++) {
//                    Map<String, Object> entry = (Map<String, Object>) id.get(1);
//                    System.out.println(entry.get("symptom"));
//                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Failed to read value
                System.out.println("Failed to read value."+ databaseError.toException());
            }
        });
    }
}

