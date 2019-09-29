package com.example.myapplication26;

import java.util.ArrayList;

public interface TriggerEvent {
    public ArrayList<Float> list = new ArrayList<>();
    public void trigger(float value);
}
