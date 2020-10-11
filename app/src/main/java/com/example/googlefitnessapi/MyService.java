package com.example.googlefitnessapi;

import android.app.Service;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.googlefitnessapi.model.StepPojo;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MyService extends Service {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String weekDay ,date;
    int totalstep;
    double dis,cal;



    private static final String TAG ="" ;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());

        date = new SimpleDateFormat("dd-MMM", Locale.getDefault()).format(new Date());

        SharedPreferences prefs = getSharedPreferences("DataAll", MODE_PRIVATE);
        totalstep = prefs.getInt("Steps", 0);//"No name defined" is the default value.
        dis = prefs.getFloat("dist", 0);
        cal = prefs.getFloat("cal" , 0);

        StoreSteps();

        return Service.START_STICKY;
    }

    public void StoreSteps() {

        StepPojo stepPojo = new StepPojo(date ,cal,dis,totalstep);

        db.collection(String.valueOf(GoogleSignIn.getLastSignedInAccount(this))).document(weekDay)
                .set(stepPojo)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MyService.this, "Data Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error adding document", e);
                    }
                });
    }

        @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
