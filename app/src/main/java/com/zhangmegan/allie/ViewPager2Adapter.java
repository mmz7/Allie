package com.zhangmegan.allie;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

    private Context context;
    private ArrayList list;
    private HashMap<Integer, Integer> day_start;
    private String[] test = {"hello", "my", "name", "is"};

    ViewPager2Adapter(Context context, ArrayList list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.log_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if(current_index+position > 0) {
        String time = "";
        Map<String, Object> recent = (Map<String, Object>) list.get(position);
        ArrayList<Map<String, Object>> entries = (ArrayList<Map<String, Object>>) recent.get("entries");
//        int current_day = (int) (long) recent.get("day_count");
        Map<String, Object> current;
        for (int i = 0; i < entries.size(); i++) {
            current = entries.get(i);
            time += current.get("hour") + ":" + current.get("minute") + "\n";
//            if((int)(long)recent.get("day_count") != current_day) {
//                break;
//            }
//            current_day = i;

        }
        holder.text.setText(time);
//        holder.text.setText((String) test[position]);

//            int current_day = (int) recent.get("day_count");
//            for(int i = current_index+position; i < list.size(); i++) {
//                Map<String, Object> current = (Map<String, Object>) list.get(i);
//                if((int)current.get("day_count") != current_day) { break; }
//                ConstraintLayout new_entry =
//            }
//        }
//        holder.text.setText(test_arr[position]);
//        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
//        public ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.date_display);
//            layout = (ConstraintLayout) itemView;
        }
    }
}
