package de.blu.profilesystem.util;

public final class Random {

    public static int randomRange(int min, int max){
        return (int) (min + Math.round(Math.random() * (max - min)));
    }
}
