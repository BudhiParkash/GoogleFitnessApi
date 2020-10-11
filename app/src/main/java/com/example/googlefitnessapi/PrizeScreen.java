package com.example.googlefitnessapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.googlefitnessapi.Adapter.MyAdapter;
import com.example.googlefitnessapi.Adapter.PrizeAdapter;
import com.example.googlefitnessapi.model.PrizePojo;
import com.example.googlefitnessapi.model.StepPojo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class PrizeScreen extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;


    private List<PrizePojo> prizePojosData;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    public PrizeScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prize_screen);


        prizePojosData = new ArrayList<>();

        recyclerView =findViewById(R.id.prizerecyleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        db.collection("Prize")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot DS : list){

                                PrizePojo data = DS.toObject(PrizePojo.class);
                                prizePojosData.add(data);



                            }
                            adapter = new PrizeAdapter(prizePojosData , PrizeScreen.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });


    }
}