package com.zhangmegan.allie;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogActivity extends AppCompatActivity {
    HashMap<String, HashMap<String, Integer>> symptoms_map = new HashMap<String, HashMap<String, Integer>>();
    //    HashMap<String, ArrayList> ingredients_map = new HashMap<String, ArrayList>();
    HashMap<String, Integer> ing_scores = new HashMap<String, Integer>();
    Button button;
    EditText date_et, time_et, type_et, log_entry_et;
    int log_len = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef2 = database.getReference("User1");

        date_et = findViewById(R.id.date);
        time_et = findViewById(R.id.time);
        type_et = findViewById(R.id.type);
        log_entry_et = findViewById(R.id.entry);

        button = findViewById(R.id.submit);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] date_arr = date_et.getText().toString().split("/");
                String[] time_arr = time_et.getText().toString().split(":");
                DataEntry entry = new DataEntry(Integer.parseInt(date_arr[0]),Integer.parseInt(date_arr[1]),
                        Integer.parseInt(date_arr[2]), Integer.parseInt(time_arr[0]), Integer.parseInt(time_arr[1]),
                        type_et.getText().toString(), log_entry_et.getText().toString());
//                DataEntry entry = new DataEntry(1,7,2023,13,9,"symptom", "rash");
                myRef2.child(String.valueOf(log_len++)).setValue(entry);
//                log_len++;
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
                System.out.print(id);
                log_len = id.size();

                // rearrange log by date/time
                Map<String, Object> recent = (Map<String, Object>) id.get(log_len-1);
                Map<String, Object> comp;
                LocalDateTime recent_date = LocalDateTime.of((int)(long)recent.get("year"), (int)(long)recent.get("month"),
                        (int)(long)recent.get("day"), (int)(long)recent.get("hour"), (int)(long)recent.get("minute"));

                int index = log_len-1;
                for(int i = log_len-2; i >= 0; i--) {
                    comp = (Map<String, Object>) id.get(i);
                    LocalDateTime comp_date = LocalDateTime.of((int)(long)comp.get("year"), (int)(long)comp.get("month"),
                            (int)(long)comp.get("day"), (int)(long)comp.get("hour"), (int)(long)comp.get("minute"));
                    if(recent_date.compareTo(comp_date) >= 0) {
                        if(i != log_len-2) {
                            System.out.println("for loop ends");
                            if(i > 0) {
                                id.set(i + 1, recent);
                                index = i+1;
                            } else {
                                id.set(0, recent);
                                index = 0;
                            }
                        }
                        break;
                    }
                    id.set(i+1, comp);
                }
                System.out.println(id);

                // update symptoms map and ingredients frequency count
                if(recent.get("type").equals("symptom")) {
                    ArrayList<String> symptoms = new ArrayList<String>(Arrays.asList(((String) Objects.requireNonNull(recent.get("entry"))).split(", ")));
                    for (int i = index-1; i >= 0; i--) {
                        comp = (Map<String, Object>)id.get(i);
                        LocalDateTime comp_date = LocalDateTime.of((int)(long)comp.get("year"), (int)(long)comp.get("month"),
                                (int)(long)comp.get("day"), (int)(long)comp.get("hour"), (int)(long)comp.get("minute"));
                        if(Math.abs(comp_date.toEpochSecond(ZoneOffset.UTC) - recent_date.toEpochSecond(ZoneOffset.UTC)) > 172800) {
                            break;
                        }
                        if(comp.get("type").equals("food")) {
                            ArrayList<String> foods = new ArrayList<String>(Arrays.asList(((String) Objects.requireNonNull(comp.get("entry"))).split(", ")));
                            for(String symp : symptoms) {
                                for(String ing : foods) {
                                    if(symptoms_map.containsKey(symp)) {
                                        HashMap<String, Integer> temp_map = symptoms_map.get(symp);
                                        if(temp_map.containsKey(ing)) {
                                            temp_map.put(ing, temp_map.get(ing)+1);
                                        } else {
                                            temp_map.put(ing, 1);
                                        }
                                    } else {
                                        HashMap<String, Integer> new_map = new HashMap<String, Integer>();
                                        new_map.put(ing, 1);
                                        symptoms_map.put(symp, new_map);
                                    }
                                }
                            }
                        }
                        System.out.println("in symptoms if");
                    }
                }
                System.out.println(symptoms_map);
                myRef2.setValue(id);



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
