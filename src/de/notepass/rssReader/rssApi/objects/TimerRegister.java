package de.notepass.rssReader.rssApi.objects;

import java.util.ArrayList;
import java.util.Timer;

public class TimerRegister {
    private static ArrayList<Timer> timers = new ArrayList<Timer>();

    public static void add(Timer t) {
        timers.add(t);
    }

    public static void stopAllTimer() {
        for (Timer t:timers) {
            t.cancel();
        }
    }
}
