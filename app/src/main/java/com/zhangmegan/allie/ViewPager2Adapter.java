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
import java.util.Map;

class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

    private Context context;
    private ArrayList list;
    private final int current_index;

    ViewPager2Adapter(Context context, ArrayList list, int current_index) {
        this.context = context;
        this.list = list;
        this.current_index = current_index;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.log_content, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(current_index+position > 0) {
            Map<String, Object> recent = (Map<String, Object>) list.get(current_index + position);
            String date = recent.get("month") + "/" + recent.get("day");
            holder.text.setText(date);

//            int current_day = (int) recent.get("day_count");
//            for(int i = current_index+position; i < list.size(); i++) {
//                Map<String, Object> current = (Map<String, Object>) list.get(i);
//                if((int)current.get("day_count") != current_day) { break; }
//                ConstraintLayout new_entry =
//            }
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView text;
        public ConstraintLayout layout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            text = itemView.findViewById(R.id.date_display);
            layout = (ConstraintLayout) itemView;
        }
    }
}
