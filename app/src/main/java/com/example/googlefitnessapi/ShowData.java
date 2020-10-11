package com.example.googlefitnessapi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.example.googlefitnessapi.Adapter.MyAdapter;
import com.example.googlefitnessapi.model.StepPojo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;


public class ShowData extends AppCompatActivity {


    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;
    private List<StepPojo> listitems;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_bar_graph);

        recyclerView =findViewById(R.id.recyleview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


      //  String sessionId = getIntent().getStringExtra("useid");

        listitems = new ArrayList<>();



        db.collection("com.google.android.gms.auth.api.signin.GoogleSignInAccount@8b709d99")
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        if(!queryDocumentSnapshots.isEmpty()){

                            List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();

                            for(DocumentSnapshot DS : list){

                                StepPojo data = DS.toObject(StepPojo.class);
                                listitems.add(data);



                            }
                            adapter = new MyAdapter(listitems , ShowData.this);
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }

                    }
                });





    }
}