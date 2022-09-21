package com.lucidiovacas.backtostock.model;

import lombok.Data;
import static com.lucidiovacas.backtostock.constants.Constants.*;

@Data
public class User{
    private String name;
    private boolean premium;
    private int age;

    public User(String name, boolean premium, int age) {
        this.name = name;
        this.premium = premium;
        this.age = age;
    }

    public boolean isElderly(){
        return this.age > ELDERLY_STARTING_AGE;
    }

    public boolean isPremiumOrElderly(){
        return isPremium() || isElderly();
    }

    public int getPriorityForMedicalProduct() {
        return isPremiumOrElderly() ? HIGH_PRIORITY : LOW_PRIORITY;
    }

    public int getPriorityForNonMedicalProduct() {
        if      (isPremium())   return HIGH_PRIORITY;
        else if (isElderly())   return MEDIUM_PRIORITY;
        else                    return LOW_PRIORITY;
    }
}
