package studio.mon.callhistoryanalyzer.model;

import java.util.Date;

/**
 * Created by admin on 6/4/2016.
 */
public class CallAnalyzer {
    private int id;
    private String name;
    private String number;
    private String time;
    private String type;
    private String duration;

    public CallAnalyzer() {
    }

    public CallAnalyzer(int id, String name, String number, String time, String type, String duration) {
        this.id = id;
        this.name = name;
        this.number = number;
        this.time = time;
        this.type = type;
        this.duration = duration;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }
}
