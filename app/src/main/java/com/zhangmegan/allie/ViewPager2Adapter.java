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

    ViewPager2Adapter(Context context, ArrayList list, HashMap<Integer, Integer> day_start) {
        this.context = context;
        this.list = list;
        this.day_start = day_start;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.log_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        if(current_index+position > 0) {
        String date = "";
        Map<String, Object> recent = (Map<String, Object>) list.get(day_start.get(position));
        int current_day = (int) (long) recent.get("day_count");
        for(int i = day_start.get(position)+1; i < list.size(); i++) {
            date += recent.get("month") + "/" + recent.get("day")+"\n";
            recent = (Map<String, Object>) list.get(i);
            if((int)(long)recent.get("day_count") != current_day) {
                break;
            }
//            current_day = i;

        }
        holder.text.setText(date);

//            int current_day = (int) recent.get("day_count");
//            for(int i = current_index+position; i < list.size(); i++) {
//                Map<String, Object> current = (Map<String, Object>) list.get(i);
//                if((int)current.get("day_count") != current_day) { break; }
//                ConstraintLayout new_entry =
//            }
//        }
//        holder.text.setText(test_arr[position]);
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
