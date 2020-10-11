package com.example.googlefitnessapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlefitnessapi.R;
import com.example.googlefitnessapi.model.StepPojo;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<StepPojo> stepPojos;
    private Context context;

    public MyAdapter(List<StepPojo> stepPojos, Context context) {
        this.stepPojos = stepPojos;
        this.context = context;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview , parent , false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolder holder, int position) {

        StepPojo data = stepPojos.get(position);
        String day = data.getWeeokofday();
                double caloris = data.getCalories();
                double distance = data.getDistance();
        int step = data.getSteps();



        holder.day.setText(day+"");
        holder.calories.setText(Integer.toString((int) caloris) +" Cal" );
        holder.distance.setText(Integer.toString((int) distance) + " M");
      holder.step.setText(Integer.toString(step) +"Step");




    }

    @Override
    public int getItemCount() {
        return stepPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        TextView  day , step ,distance ,calories;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            day  = itemView.findViewById(R.id.day);

            calories = itemView.findViewById(R.id.TotalCalories);
            distance = itemView.findViewById(R.id.TotalDistance);

            step = itemView.findViewById(R.id.Totalsteps);


        }
    }
}
