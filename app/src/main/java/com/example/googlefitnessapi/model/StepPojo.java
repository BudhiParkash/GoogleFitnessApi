package com.example.googlefitnessapi.model;

public class StepPojo {

    private String Weeokofday;
    private double Calories;
    private  double Distance;
    private int Steps;



    public String getWeeokofday() {
        return Weeokofday;
    }

    public void setWeeokofday(String weeokofday) {
        Weeokofday = weeokofday;
    }

    public double getCalories() {
        return Calories;
    }

    public void setCalories(double calories) {
        Calories = calories;
    }

    public double getDistance() {
        return Distance;
    }

    public void setDistance(double distance) {
        Distance = distance;
    }

    public int getSteps() {
        return Steps;
    }

    public void setSteps(int steps) {
        Steps = steps;
    }


    public StepPojo(String weeokofday, double calories, double distance, int steps) {
        Weeokofday = weeokofday;
        Calories = calories;
        Distance = distance;
        Steps = steps;
    }
}
