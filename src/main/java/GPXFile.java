package main.java;

import java.time.Instant;

public class GPXFile {

    private Instant time;

    public Instant dateTime() {
        return time;
    }

    public void setTime(Instant time) {
        this.time = time;
    }
}
