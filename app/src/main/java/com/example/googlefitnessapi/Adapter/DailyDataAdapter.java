package com.example.googlefitnessapi.Adapter;

import android.content.Context;
import android.renderscript.Sampler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlefitnessapi.R;
import com.example.googlefitnessapi.model.DailyPojo;
import com.google.android.gms.fitness.data.Value;

import java.util.List;

public class DailyDataAdapter extends RecyclerView.Adapter<DailyDataAdapter.ViewHolder> {

    private List<DailyPojo> dailyPojos;
    private Context context;

    public DailyDataAdapter(List<DailyPojo> dailyPojos, Context context) {
        this.dailyPojos = dailyPojos;
        this.context = context;
    }

    @NonNull
    @Override
    public DailyDataAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.dailydataview , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull DailyDataAdapter.ViewHolder holder, int position) {

        DailyPojo data = dailyPojos.get(position);

        String Stime = data.getStartTime();
        String Etime = data.getEndTime();
        Value Steps = data.getSteps();


        holder.strttime.setText(Stime);
        holder.endtime.setText(Etime);
        holder.step.setText(Steps+"");
    }

    @Override
    public int getItemCount() {
        return dailyPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView strttime ,endtime , step;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            strttime = itemView.findViewById(R.id.Starttime);
            endtime = itemView.findViewById(R.id.endtime);
            step = itemView.findViewById(R.id.dailystep);

        }
    }
}
