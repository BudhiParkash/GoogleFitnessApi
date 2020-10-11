package com.example.googlefitnessapi.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.googlefitnessapi.R;
import com.example.googlefitnessapi.model.PrizePojo;
import com.squareup.picasso.Picasso;

import java.util.List;


public class PrizeAdapter extends RecyclerView.Adapter<PrizeAdapter.PrizeViewHolder> {

    private List<PrizePojo> prizePojos;
    private Context context;

    public PrizeAdapter(List<PrizePojo> prizePojos, Context context) {
        this.prizePojos = prizePojos;
        this.context = context;
    }

    @NonNull
    @Override
    public PrizeAdapter.PrizeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.prizeitem_view , parent , false);
        return new PrizeViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull PrizeAdapter.PrizeViewHolder holder, int position) {
        PrizePojo data = prizePojos.get(position);

        double DocCoin = data.getDocCoin();
        String PrizeName = data.getPrizeTitle();
        String PrizeDescription = data.getPrizeDiscription();


        holder.txtDocCoin.setText(String.valueOf(DocCoin));
        holder.txtPrizeName.setText(PrizeName);
        holder.txtDiscription.setText(PrizeDescription);

        holder.prizeImage.setVisibility(View.VISIBLE);

        Picasso.get().load(data.getPrizeImage()).into(holder.prizeImage);



    }

    @Override
    public int getItemCount() {
        return prizePojos.size();
    }

    public class PrizeViewHolder extends RecyclerView.ViewHolder {

        TextView txtDocCoin , txtPrizeName , txtDiscription;
        ImageView prizeImage;
        public PrizeViewHolder(@NonNull View itemView) {
            super(itemView);

            txtDocCoin = itemView.findViewById(R.id.DocCoin);
            txtPrizeName = itemView.findViewById(R.id.PrizeName);
            txtDiscription = itemView.findViewById(R.id.PrizeDescription);

            prizeImage = itemView.findViewById(R.id.PrizeImage);



        }
    }

}
