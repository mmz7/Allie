package com.zhangmegan.allie;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class LogActivity extends Fragment {
    ViewPager2 viewPager;
    ViewPager2Adapter vpAdapter;
    TextView test;
    HashMap<String, HashMap<String, Integer>> symptoms_map = new HashMap<String, HashMap<String, Integer>>();
    //    HashMap<String, ArrayList> ingredients_map = new HashMap<String, ArrayList>();
    HashMap<String, Integer> ing_scores = new HashMap<String, Integer>();
    ArrayList<Object> id = new ArrayList<Object>();
    int log_len = 0;
    int recent_index = 0;
    int current_vp_pos = 0;
    HashMap<Integer, Integer> day_start = new HashMap<Integer, Integer>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.log_container, container, false);
        return fragment;
    }
    @SuppressLint("NotifyDataSetChanged")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        DatabaseReference myRef2 = database.getReference("User1");

        myRef2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                id = (ArrayList<Object>) dataSnapshot.getValue();
                System.out.print(id);

                ArrayList<Object> current_slide = (ArrayList<Object>) ((Map<String, Object>)id.get(current_vp_pos)).get("entries");
                log_len = current_slide.size();

                // rearrange log by date/time
                Map<String, Object> recent = (Map<String, Object>) current_slide.get(log_len-1);
                Map<String, Object> comp;

                LocalTime recent_date = LocalTime.of((int)(long)recent.get("hour"), (int)(long)recent.get("minute"));
                recent_index = log_len-1;
                for(int i = log_len-2; i >= 0; i--) {
                    comp = (Map<String, Object>) current_slide.get(i);
                    LocalTime comp_date = LocalTime.of((int)(long)comp.get("hour"), (int)(long)comp.get("minute"));
                    if(recent_date.compareTo(comp_date) >= 0) {
                        if(i != log_len-2) {
                            System.out.println("for loop ends");
                            if(i > 0) {
                                current_slide.set(i + 1, recent);
                                recent_index = i+1;
//                                int comp_daycount = (int)(long)comp.get("day_count");
//                                if(comp_daycount != (int)(long)recent.get("day_count")) {
//                                    day_start.put((int)(long)recent.get("day_count"), i+1);
//                                }
                            } else {
                                current_slide.set(0, recent);
                                recent_index = 0;
                            }
                        }
                        break;
                    }
                    current_slide.set(i+1, comp);
                }
//                System.out.println(id);

//                System.out.println(day_start);
                ((Map<String, Object>)id.get(current_vp_pos)).put("entries", current_slide);
                myRef2.setValue(id);

                System.out.println("done");

                // find first entry of the same day
//                LocalDate last_entry = LocalDate.of((int)(long)recent.get("year"), (int)(long)recent.get("month"),
//                        (int)(long)recent.get("day"));
//                for(int i = recent_index-1; i >= 0; i--) {
//                    comp = (Map<String, Object>) id.get(i);
//                    LocalDate current_entry = LocalDate.of((int)(long)comp.get("year"), (int)(long)comp.get("month"),
//                            (int)(long)comp.get("day"));
//                    if(last_entry.compareTo(current_entry) == 0) {
//                        current_day_start = i;
//                    } else { break; }
//                }

                // update symptoms map and ingredients frequency count
                if(recent.get("type").equals("symptom")) {
                    ArrayList<String> symptoms = new ArrayList<String>(Arrays.asList(((String) Objects.requireNonNull(recent.get("entry"))).split(", ")));
                    for (int i = recent_index-1; i >= 0; i--) {
                        comp = (Map<String, Object>)current_slide.get(i);
//                        LocalTime comp_date = LocalTime.of((int)(long)comp.get("hour"), (int)(long)comp.get("minute"));
//                        if(Math.abs(comp_date.toEpochSecond(ZoneOffset.UTC) - recent_date.toEpochSecond(ZoneOffset.UTC)) > 172800) {
//                            break;
//                        }
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

        viewPager = view.findViewById(R.id.viewpager);
        vpAdapter = new ViewPager2Adapter(view.getContext(), id);
        viewPager.setAdapter(vpAdapter);
//        viewPager.setCurrentItem(2);

        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            // This method is triggered when there is any scrolling activity for the current page
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            // triggered when you select a new page
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                current_vp_pos = position;
            }

            // triggered when there is
            // scroll state will be changed
            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

    }

}


//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.log_container);
//
////        day_start.put(0,0);
////        day_start.put(1,3);
////        day_start.put(2,4);
////        day_start.put(3,7);
////        day_start.put(4,9);
//
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//        DatabaseReference myRef2 = database.getReference("User1");
//
//        viewPager = getActivity().findViewById(R.id.viewpager);
//
//        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
//            @Override
//            // This method is triggered when there is any scrolling activity for the current page
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
//            }
//
//            // triggered when you select a new page
//            @Override
//            public void onPageSelected(int position) {
//                super.onPageSelected(position);
//                current_vp_pos = position;
//            }
//
//            // triggered when there is
//            // scroll state will be changed
//            @Override
//            public void onPageScrollStateChanged(int state) {
//                super.onPageScrollStateChanged(state);
//            }
//        });
//
//        myRef2.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                // This method is called once with the initial value and again
//                // whenever data at this location is updated.
//                id = (ArrayList<Object>) dataSnapshot.getValue();
//                System.out.print(id);
//
//                ArrayList<Object> current_slide = (ArrayList<Object>) ((Map<String, Object>)id.get(current_vp_pos)).get("entries");
//                log_len = current_slide.size();
//
//                // rearrange log by date/time
//                Map<String, Object> recent = (Map<String, Object>) current_slide.get(log_len-1);
//                Map<String, Object> comp;
//
//                LocalTime recent_date = LocalTime.of((int)(long)recent.get("hour"), (int)(long)recent.get("minute"));
//                recent_index = log_len-1;
//                for(int i = log_len-2; i >= 0; i--) {
//                    comp = (Map<String, Object>) current_slide.get(i);
//                    LocalTime comp_date = LocalTime.of((int)(long)comp.get("hour"), (int)(long)comp.get("minute"));
//                    if(recent_date.compareTo(comp_date) >= 0) {
//                        if(i != log_len-2) {
//                            System.out.println("for loop ends");
//                            if(i > 0) {
//                                current_slide.set(i + 1, recent);
//                                recent_index = i+1;
////                                int comp_daycount = (int)(long)comp.get("day_count");
////                                if(comp_daycount != (int)(long)recent.get("day_count")) {
////                                    day_start.put((int)(long)recent.get("day_count"), i+1);
////                                }
//                            } else {
//                                current_slide.set(0, recent);
//                                recent_index = 0;
//                            }
//                        }
//                        break;
//                    }
//                    current_slide.set(i+1, comp);
//                }
////                System.out.println(id);
//
////                System.out.println(day_start);
//                ((Map<String, Object>)id.get(current_vp_pos)).put("entries", current_slide);
//                myRef2.setValue(id);
//
//                vpAdapter = new ViewPager2Adapter(context, id);
//
//                viewPager.setAdapter(vpAdapter);
//                viewPager.setCurrentItem(2);
//
//                // find first entry of the same day
////                LocalDate last_entry = LocalDate.of((int)(long)recent.get("year"), (int)(long)recent.get("month"),
////                        (int)(long)recent.get("day"));
////                for(int i = recent_index-1; i >= 0; i--) {
////                    comp = (Map<String, Object>) id.get(i);
////                    LocalDate current_entry = LocalDate.of((int)(long)comp.get("year"), (int)(long)comp.get("month"),
////                            (int)(long)comp.get("day"));
////                    if(last_entry.compareTo(current_entry) == 0) {
////                        current_day_start = i;
////                    } else { break; }
////                }
//
//                // update symptoms map and ingredients frequency count
//                if(recent.get("type").equals("symptom")) {
//                    ArrayList<String> symptoms = new ArrayList<String>(Arrays.asList(((String) Objects.requireNonNull(recent.get("entry"))).split(", ")));
//                    for (int i = recent_index-1; i >= 0; i--) {
//                        comp = (Map<String, Object>)current_slide.get(i);
////                        LocalTime comp_date = LocalTime.of((int)(long)comp.get("hour"), (int)(long)comp.get("minute"));
////                        if(Math.abs(comp_date.toEpochSecond(ZoneOffset.UTC) - recent_date.toEpochSecond(ZoneOffset.UTC)) > 172800) {
////                            break;
////                        }
//                        if(comp.get("type").equals("food")) {
//                            ArrayList<String> foods = new ArrayList<String>(Arrays.asList(((String) Objects.requireNonNull(comp.get("entry"))).split(", ")));
//                            for(String symp : symptoms) {
//                                for(String ing : foods) {
//                                    if(symptoms_map.containsKey(symp)) {
//                                        HashMap<String, Integer> temp_map = symptoms_map.get(symp);
//                                        if(temp_map.containsKey(ing)) {
//                                            temp_map.put(ing, temp_map.get(ing)+1);
//                                        } else {
//                                            temp_map.put(ing, 1);
//                                        }
//                                    } else {
//                                        HashMap<String, Integer> new_map = new HashMap<String, Integer>();
//                                        new_map.put(ing, 1);
//                                        symptoms_map.put(symp, new_map);
//                                    }
//                                }
//                            }
//                        }
//                        System.out.println("in symptoms if");
//                    }
//                }
//                System.out.println(symptoms_map);
//
//
//
////                for(int i = 0; i < len; i++) {
////                    Map<String, Object> entry = (Map<String, Object>) id.get(1);
////                    System.out.println(entry.get("symptom"));
////                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                // Failed to read value
//                System.out.println("Failed to read value."+ databaseError.toException());
//            }
//        });
//
//
//    }
