package com.example.googlefitnessapi;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.googlefitnessapi.Adapter.DailyDataAdapter;
import com.example.googlefitnessapi.Adapter.MyAdapter;
import com.example.googlefitnessapi.model.DailyPojo;
import com.example.googlefitnessapi.model.StepPojo;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessActivities;
import com.google.android.gms.fitness.FitnessOptions;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Session;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SessionInsertRequest;
import com.google.android.gms.fitness.request.SessionReadRequest;
import com.google.android.gms.fitness.result.DataReadResponse;
import com.google.android.gms.fitness.result.SessionReadResponse;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.firestore.QuerySnapshot;
import com.razerdp.widget.animatedpieview.AnimatedPieView;
import com.razerdp.widget.animatedpieview.AnimatedPieViewConfig;
import com.razerdp.widget.animatedpieview.data.SimplePieInfo;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;


import static com.google.android.gms.common.util.CollectionUtils.listOf;
import static java.text.DateFormat.getDateInstance;
import static java.text.DateFormat.getTimeInstance;

public class MainActivity extends AppCompatActivity {

    private FirebaseAnalytics mFirebaseAnalytics;
    private FirebaseCrashlytics firebaseCrashlytics;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private static final int PHYISCAL_ACTIVITY = 0;
    private static final int REQUEST_OAUTH = 1;

    List<DailyPojo> dailyPojos ;

    TextView steps ;
    private String TAG;
    TextView txtcalories;

    int total;
    double rounfoffdistance = 0.0;
    double rounfoffCalories = 0.0 ;
    String weekDay;
    String date;
    String currentDate;

    private RecyclerView recyclerView;
    private  RecyclerView.Adapter adapter;



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dailyPojos = new ArrayList<>();


        recyclerView =findViewById(R.id.dailyrecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        Calendar timeOff9 = Calendar.getInstance();
        timeOff9.set(Calendar.HOUR_OF_DAY, 23);
        timeOff9.set(Calendar.MINUTE, 20);
        timeOff9.set(Calendar.SECOND, 0);

        AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        //creating a new intent specifying the broadcast receiver
        Intent i = new Intent(this, MyAlarm.class);

        //creating a pending intent using the intent
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        //setting the repeating alarm that will be fired every day

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            am.setInexactRepeating(AlarmManager.RTC_WAKEUP, timeOff9.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pi);

        }
        Toast.makeText(this, "Alarm is set" + timeOff9.getTimeInMillis(), Toast.LENGTH_SHORT).show();



        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);


        FirebaseFirestoreSettings settings = new FirebaseFirestoreSettings.Builder()
                .setPersistenceEnabled(true)
                .setCacheSizeBytes(FirebaseFirestoreSettings.CACHE_SIZE_UNLIMITED)
                .build();
        db.setFirestoreSettings(settings);


        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
         currentDate = sdf.format(new Date());

        Toast.makeText(this, "" +currentDate, Toast.LENGTH_SHORT).show();



        SimpleDateFormat dayFormat = new SimpleDateFormat("EEEE", Locale.US);
        Calendar calendar = Calendar.getInstance();
        weekDay = dayFormat.format(calendar.getTime());


        date = new SimpleDateFormat("dd-MMM", Locale.getDefault()).format(new Date());

        steps = findViewById(R.id.steps);
       // txtdistance = findViewById(R.id.Distance);
        txtcalories = findViewById(R.id.Calories);

        steps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this , ShowData.class);
                startActivity(intent);
                //getSteps();

            }
        });


        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        firebaseCrashlytics =FirebaseCrashlytics.getInstance();


        Bundle bundle = new Bundle();
        bundle.putString("key1" , FirebaseAnalytics.Event.APP_OPEN);
        bundle.putString(FirebaseAnalytics.Param.SCREEN_NAME , "Splash Screen" );
        bundle.putString("Key2" , FirebaseAnalytics.Event.SCREEN_VIEW);
        bundle.putString("key3" , FirebaseAnalytics.Event.SELECT_CONTENT);
        bundle.putString("key4" , FirebaseAnalytics.Event.SELECT_ITEM);
        bundle.putString("key5" , FirebaseAnalytics.Event.VIEW_ITEM);

        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);




        String[] PERMISSIONS = {
                Manifest.permission.ACTIVITY_RECOGNITION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.INTERNET
        };

        //Permission
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACTIVITY_RECOGNITION)
                    != PackageManager.PERMISSION_GRANTED) {

                ActivityCompat.requestPermissions(this,
                        PERMISSIONS, PHYISCAL_ACTIVITY
                        );
            }
        }
               googleSignin();


          }
          public void drawpiechart(){

              AnimatedPieView mAnimatedPieView = findViewById(R.id.piechart);
              AnimatedPieViewConfig config = new AnimatedPieViewConfig();
              config.startAngle(-90)// Starting angle offset

                      .addData(new SimplePieInfo(total, Color.parseColor("#194EA7"), "Steps"))//Data (bean that implements the IPieInfo interface)
                      .addData(new SimplePieInfo(10000, Color.parseColor("#E2E5F0"), ""))
                     // .guidePointRadius(2)
                      .strokeWidth(22)
                      .textSize(30.0f)
                      //.drawText(true)
                    .guideLineWidth(1)
                      .duration(2000);// draw pie animation duration

// The following two sentences can be replace directly 'mAnimatedPieView.start (config); '
              mAnimatedPieView.applyConfig(config);
              mAnimatedPieView.start();



          }

          public void drawpiechart2(){

        AnimatedPieView mAnimatedPieView = findViewById(R.id.piechart1);
        AnimatedPieViewConfig config = new AnimatedPieViewConfig();
        config.startAngle(-90)// Starting angle offset

                .addData(new SimplePieInfo(rounfoffdistance, Color.parseColor("#EF4931"), "Distance"))//Data (bean that implements the IPieInfo interface)
                .addData(new SimplePieInfo(10000, Color.parseColor("#E2E5F0"), ""))
                //.guidePointRadius(2)
                .strokeWidth(22)
                .textSize(30.0f)
                .drawText(true)
                //  .guideLineWidth(4)
                .duration(2000);// draw pie animation duration

// The following two sentences can be replace directly 'mAnimatedPieView.start (config); '
        mAnimatedPieView.applyConfig(config);
        mAnimatedPieView.start();



    }


          private void googleSignin() {
              FitnessOptions fitnessOptions = FitnessOptions.builder()
                      .addDataType(DataType.AGGREGATE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                      .addDataType(DataType.TYPE_CALORIES_EXPENDED ,FitnessOptions.ACCESS_READ)
                      .addDataType(DataType.TYPE_CALORIES_EXPENDED , FitnessOptions.ACCESS_WRITE)
                      .addDataType(DataType.AGGREGATE_CALORIES_EXPENDED ,FitnessOptions.ACCESS_READ)
                      .addDataType(DataType.TYPE_DISTANCE_DELTA, FitnessOptions.ACCESS_READ)
                      .addDataType(DataType.TYPE_STEP_COUNT_DELTA, FitnessOptions.ACCESS_READ)
                      .addDataType(DataType.TYPE_STEP_COUNT_CUMULATIVE ,FitnessOptions.ACCESS_READ )
                      .build();




              if (!GoogleSignIn.hasPermissions(GoogleSignIn.getLastSignedInAccount(this), fitnessOptions)) {
                  GoogleSignIn.requestPermissions(
                          this, // your activity
                          REQUEST_OAUTH,
                        GoogleSignIn.getLastSignedInAccount(this),

                          fitnessOptions);
              } else {

                  stepcount();
                  distcount();
                 readDataCalories();

              }
          }



          @Override
          protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
              super.onActivityResult(requestCode, resultCode, data);
              if (resultCode == Activity.RESULT_OK) {
                  if (REQUEST_OAUTH == requestCode) {
                      SuscribeC();
                      Suscribe();




                 //     getDistancehistory();

                      getStepCounthistory();
                  //    readdataSteps();
                   //   readDataDistance();
                   //   readDataCalories();


//                      insertSessionRequest();
//                      readSessioData();

                  }
                  if (PHYISCAL_ACTIVITY == requestCode) {

                  }
              } else {
                  GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

              }



          }

          private void Suscribe() {

              Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                    .subscribe(DataType.TYPE_STEP_COUNT_DELTA)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.i(TAG, "Successfully subscribed!");
                            Toast.makeText(MainActivity.this, "Suscribed", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.i(TAG, "There was a problem subscribing.");
                        }
                    });

          }

         private void SuscribeC() {

        Fitness.getRecordingClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .subscribe(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.i(TAG, "Successfully subscribed To Calories!");
                        Toast.makeText(MainActivity.this, "Suscribed to 'Calories", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG, "There was a problem subscribing.");
                    }
                });
    }


          private void readDataCalories() {
        // Invoke the History API to fetch the data with the query
        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readDailyTotal(DataType.TYPE_CALORIES_EXPENDED)
                .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                    @Override
                    public void onSuccess(DataSet dataSet) {
                        double CaloriesBurn;
                        rounfoffCalories = 0.0;
                        if(dataSet.isEmpty()){
                           CaloriesBurn = 0.0;
                        }
                        else {
                            CaloriesBurn = dataSet.getDataPoints().get(0).getValue(Field.FIELD_CALORIES).asFloat();
                            rounfoffCalories = Math.round(CaloriesBurn * 100.0) / 100.0;
                        }
                        Toast.makeText(MainActivity.this, "Calories  " + rounfoffCalories, Toast.LENGTH_SHORT).show();
                        txtcalories.setText(rounfoffCalories+" Cal");

                        SharedPreferences.Editor editor = getSharedPreferences("DataAll", MODE_PRIVATE).edit();
                        editor.putInt("Steps", total);
                        editor.putFloat("dist", (float) rounfoffdistance);
                        editor.putFloat("cal", (float) rounfoffCalories);
                        editor.apply();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(MainActivity.this, "Unabel to Calculate c", Toast.LENGTH_SHORT).show();
            }
        });



          }

          public void distcount(){

              Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                      .readDailyTotal(DataType.TYPE_DISTANCE_DELTA)
                      .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                          @Override
                          public void onSuccess(DataSet dataSet) {
                              double totaldistance;
                              rounfoffdistance = 0.0;
                              if(dataSet.isEmpty()){
                                  totaldistance = 0.0;
                              }
                              else {
                                  totaldistance = dataSet.getDataPoints().get(0).getValue(Field.FIELD_DISTANCE).asFloat();
                                  rounfoffdistance = Math.round(totaldistance * 100.0) / 100.0;

                              }
                              Toast.makeText(MainActivity.this, "distance  " + rounfoffdistance , Toast.LENGTH_SHORT).show();
                              //    txtdistance.setText(rounfoffdistance + " M");
                              drawpiechart2();

                          }
                      }).addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(MainActivity.this, "Unabel to Calculate Distance", Toast.LENGTH_SHORT).show();
                  }
              });

          }

          public void stepcount(){
              Fitness.getHistoryClient(this , GoogleSignIn.getLastSignedInAccount(this))
                      .readDailyTotal(DataType.TYPE_STEP_COUNT_DELTA)
                      .addOnSuccessListener(new OnSuccessListener<DataSet>() {
                          @Override
                          public void onSuccess(DataSet dataSet) {
                              // int total;
                              if(dataSet.isEmpty()){
                                  total = 0;

                              }else {
                                  total =   dataSet.getDataPoints().get(0).getValue(Field.FIELD_STEPS).asInt();
                              }
                              Toast.makeText(MainActivity.this, "Steps  "+total, Toast.LENGTH_SHORT).show();
                              steps.setText(String.valueOf(total));
                              drawpiechart();




                          }

                      }) .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Toast.makeText(MainActivity.this, "Faild to step count", Toast.LENGTH_SHORT).show();

                  }
              });


          }




          public void StoreSteps(){

              StepPojo stepPojo = new StepPojo(
                   date, rounfoffCalories ,rounfoffdistance , total
              );

              db.collection(String.valueOf(GoogleSignIn.getLastSignedInAccount(this))).document(weekDay)
                      .set(stepPojo)
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              Toast.makeText(MainActivity.this, "Data Added", Toast.LENGTH_SHORT).show();
                          }
                      })
                   .addOnFailureListener(new OnFailureListener() {
                  @Override
                  public void onFailure(@NonNull Exception e) {
                      Log.w(TAG, "Error adding document", e);
                  }
              });


          }



    private void getStepCounthistory() {

        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();


        DataReadRequest readRequest = new DataReadRequest.Builder()
                .read(DataType.TYPE_STEP_COUNT_DELTA)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .bucketByTime(365, TimeUnit.DAYS)
                .build();



        Fitness.getHistoryClient(this, GoogleSignIn.getLastSignedInAccount(this))
                .readData(readRequest)
                .addOnSuccessListener(new OnSuccessListener<DataReadResponse>() {
                    @Override
                    public void onSuccess(DataReadResponse dataReadResponse) {
                        Log.d(TAG, "onSuccess()");

                        Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();

                        for (Bucket bucket : dataReadResponse.getBuckets()) {
                            Log.e("History", "Data returned for Data type: " + bucket.getDataSets());

                            List<DataSet> dataSets = bucket.getDataSets();
                            for (DataSet dataSet : dataSets) {

                                for (DataPoint dp : dataSet.getDataPoints()) {


                                    DateFormat timeFormat = DateFormat.getTimeInstance();
                                    DateFormat dateFormat = DateFormat.getDateInstance();


                                    if(currentDate.equals(dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)))) {

                                        Log.e("History", "Data point:");
                                        Log.e("History", "\tType: " + dp.getDataType().getName());
                                        Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
                                        Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));



                                        dailyPojos.add(new DailyPojo(timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)), timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)), dp.getValue(dp.getDataType().getFields().get(0))));
                                    }

                                }
                            }
                            printData();
                            adapter = new DailyDataAdapter(dailyPojos , MainActivity.this);
                            adapter.notifyDataSetChanged();
                            recyclerView.setAdapter(adapter);
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e(TAG, "onFailure()", e);
                    }
                })
                .addOnCompleteListener(new OnCompleteListener<DataReadResponse>() {
                    @Override
                    public void onComplete(@NonNull Task<DataReadResponse> task) {
                        Log.d(TAG, "onComplete()");
                    }
                });
    }
    private void printData() {

        for(int i =0; i < dailyPojos.size() ; i++)
        {
            Toast.makeText(this, dailyPojos.get(i).getStartTime() +"" , Toast.LENGTH_SHORT).show();

        }

    }

    private void showDataSet(DataSet dataSet) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();


        for (DataPoint dp : dataSet.getDataPoints()) {

            Log.e("History", "Data point:");
            Log.e("History", "\tType: " + dp.getDataType().getName());
            Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)));
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());

            for (Field field : dp.getDataType().getFields()) {
                Value value = dp.getValue(field);


                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + value);
                // {"Data point":{ty}

            }
        }
    }

          public void getSteps(){
                    db.collection("Steps")
                            .get()
                            .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                                @Override
                                public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                                    if(!queryDocumentSnapshots.isEmpty()){

                                        List<DocumentSnapshot> list =queryDocumentSnapshots.getDocuments();

                                        for(DocumentSnapshot DS : list){



                                            DS.getData();
                                            Toast.makeText(MainActivity.this, ""+DS.getData(), Toast.LENGTH_SHORT).show();
                                        }
                                    }

                                }
                            });


          }

          public void StoreDistance(){
        Map<String , Object> Distnce = new HashMap<>();
        Distnce.put("Distance" , rounfoffdistance);

        db.collection("Distance").document(weekDay)
                .set(Distnce)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        Toast.makeText(MainActivity.this, "Success Fully added Distance", Toast.LENGTH_SHORT).show();

                    }
                })
             .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });


    }

          public void StoreCalories(){
        Map<String , Object> Calories = new HashMap<>();
        Calories.put("Calories" , rounfoffCalories);

        db.collection("Calories").document(weekDay)

                .set(Calories)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(MainActivity.this, "Add Calorie To firestore", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Log.w(TAG, "Error adding document", e);
            }
        });


    }











          public void insertSessionRequest(){

              Calendar cal = Calendar.getInstance();
              Date now = new Date();
              cal.setTime(now);
              long endTime = cal.getTimeInMillis();
              cal.add(Calendar.WEEK_OF_YEAR, -1);
              long startTime = cal.getTimeInMillis();


              // Create a data source
//              DataSource speedDataSource = new DataSource.Builder()
//                      .setAppPackageName(this.getPackageName())
//                      .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
//                      .setStreamName("$SAMPLE_SESSION_NAME-count")
//                      .setType(DataSource.TYPE_RAW)
//                      .build();
//
////              val runSpeedMps = 10f
//              int walkSpeedMps = (int) 3f;

//              DataPoint firstRunSpeed = DataPoint.builder(speedDataSource)
//                      .setTimeInterval(startTime, startWalkTime, TimeUnit.MILLISECONDS)
//                      .setField(Field.FIELD_STEPS , 1)
//                      .build();

//              DataPoint walkSpeed = DataPoint.builder(speedDataSource)
//                      .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
//                      .setField(Field.FIELD_STEPS,walkSpeedMps )
//                      .build();
//
////              DataPoint secondRunSpeed = DataPoint.builder(speedDataSource)
////                      .setTimeInterval(endWalkTime, endTime, TimeUnit.MILLISECONDS)
////                      .setField(Field.FIELD_STEPS, 1)
////                      .build();
//
//              // Create a data set of the run speeds to include in the session.
//              DataSet speedDataSet = DataSet.builder(speedDataSource)
//                      .add( walkSpeed)
//                      .build();
//              // Create a second DataSet of ActivitySegments to indicate the runner took a 10-minute walk
//// in the middle of the run.
//              DataSource activitySegmentDataSource = new DataSource.Builder()
//                      .setAppPackageName(this.getPackageName())
//                      .setDataType(DataType.TYPE_ACTIVITY_SEGMENT)
//                      .setType(DataSource.TYPE_RAW)
//                      .build();
//              DataSet activitySegments = DataSet.create(activitySegmentDataSource);


//              DataPoint walkingDp = activitySegments.createDataPoint()
//                      .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS);
//              walkingDp.getValue(Field.A).setActivity(FitnessActivities.WALKING);
//              activitySegments.add(walkingDp);




// Create a session with metadata about the activity.
              Session session = new Session.Builder()
                      .setName("Today")
                      .setDescription("Long run around Shoreline Park")
                      .setIdentifier("UniqueIdentifierHere")
                      .setActivity(FitnessActivities.RUNNING)
                      .setStartTime(startTime, TimeUnit.MILLISECONDS)
                      .setEndTime(endTime, TimeUnit.MILLISECONDS)
                      .build();

// Build a session insert request
              SessionInsertRequest insertRequest = new SessionInsertRequest.Builder()
                      .setSession(session)
//                      .addDataSet(speedDataSet)
//                      .addDataSet(activitySegments)
                      .build();


              Log.i(TAG, "Inserting the session in the Sessions API");
               Fitness.getSessionsClient(this, GoogleSignIn.getLastSignedInAccount(this))
                      .insertSession(insertRequest)
                      .addOnSuccessListener(new OnSuccessListener<Void>() {
                          @Override
                          public void onSuccess(Void aVoid) {
                              Toast.makeText(MainActivity.this, "Session insert was successful!", Toast.LENGTH_SHORT).show();
                              // At this point, the session has been inserted and can be read.
                              Log.i(TAG, "Session insert was successful!");
                          }
                      })
                      .addOnFailureListener(new OnFailureListener() {
                          @Override
                          public void onFailure(@NonNull Exception e) {
                              Toast.makeText(MainActivity.this, "There was a problem inserting the session: " + e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                              Log.i(TAG, "There was a problem inserting the session: " +
                                      e.getLocalizedMessage());
                          }
                      });

          }


          public void readSessioData() {


           Calendar cal = Calendar.getInstance();
           Date now = new Date();
           cal.setTime(now);
           long endTime = cal.getTimeInMillis();
           cal.add(Calendar.WEEK_OF_YEAR, -1);
           long startTime = cal.getTimeInMillis();


           SessionReadRequest sessionReadRequest = new SessionReadRequest.Builder()
                   .read(DataType.TYPE_STEP_COUNT_DELTA)
                   .setTimeInterval(startTime, endTime, TimeUnit.MILLISECONDS)
                   .setSessionName("Xyz")
                   .build();

           Fitness.getSessionsClient(this ,GoogleSignIn.getLastSignedInAccount(this) )
                   .readSession(sessionReadRequest)
                   .addOnSuccessListener(new OnSuccessListener<SessionReadResponse>() {
                       @Override
                       public void onSuccess(SessionReadResponse sessionReadResponse) {


                           List<Session> sessions = sessionReadResponse.getSessions();
                           Log.i(TAG, "Session read was successful. Number of returned sessions is: "
                                   + sessions.size());
                           Toast.makeText(MainActivity.this, "Seesion Success " +sessions.size(), Toast.LENGTH_SHORT).show();

                           for ( Session session  :  sessions) {

                               dumpSession(session);

                               List<DataSet> dataSets = sessionReadResponse.getDataSet(session);
                               for (DataSet dataSet : dataSets) {
                                   sessiondata(dataSet);


                               }


                           }
                       }
                   }).addOnFailureListener(new OnFailureListener() {
               @Override
               public void onFailure(@NonNull Exception e) {
                   Toast.makeText(MainActivity.this, "fail to seesion create", Toast.LENGTH_SHORT).show();
               }
           });

       }

          private void dumpSession(Session session) {
        Log.i(TAG, "Data returned for Session: " + session.getName()
                + "\n\tDescription: " + session.getDescription()
                + "\n\tStart: " + session.getStartTime(TimeUnit.MICROSECONDS)
                + "\n\tEnd: " + session.getEndTime(TimeUnit.MICROSECONDS));

    }

          private void sessiondata(DataSet dataSet) {
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {



            Log.e("Session", "Data point:");
            Log.e("Session", "\tType: " + dp.getDataType().getName());
            Log.e("Session", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("Session", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());


            for (Field field : dp.getDataType().getFields()) {
                Value value = dp.getValue(field);

                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));

           //     txtsteps.setText(value+"");
                Toast.makeText(this, "" + dp.getValue(field), Toast.LENGTH_SHORT).show();


            }






        }
    }
}


