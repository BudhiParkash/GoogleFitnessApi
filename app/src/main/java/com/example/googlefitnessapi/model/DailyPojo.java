package com.example.googlefitnessapi.model;

import com.google.android.gms.fitness.data.Value;

public class DailyPojo {

    private String StartTime;
    private String EndTime;
    private Value Steps;
   // private Value Distance;


    public String getStartTime() {
        return StartTime;
    }

    public void setStartTime(String startTime) {
        StartTime = startTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public Value getSteps() {
        return Steps;
    }

    public void setSteps(Value steps) {
        Steps = steps;
    }

//    public Value getDistance() {
//        return Distance;
//    }
//
//    public void setDistance(Value distance) {
//        Distance = distance;
//    }


    public DailyPojo(String startTime, String endTime, Value steps) {
        StartTime = startTime;
        EndTime = endTime;
        Steps = steps;
    }
}
