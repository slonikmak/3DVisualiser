package model;

/**
 * Created by Oceanos on 02.12.2016.
 */
public class Record {
    private double pitch;
    private double roll;
    private double course;
    private double depth;

    public Record() {
    }

    public Record(double pitch, double roll, double course, double depth) {
        this.pitch = pitch;
        this.roll = roll;
        this.course = course;
        this.depth = depth;
    }


    public double getPitch() {
        return pitch;
    }

    public void setPitch(double pitch) {
        this.pitch = pitch;
    }

    public double getRoll() {
        return roll;
    }

    public void setRoll(double roll) {
        this.roll = roll;
    }

    public double getCourse() {
        return course;
    }

    public void setCourse(double course) {
        this.course = course;
    }

    public double getDepth() {
        return depth;
    }

    public void setDepth(double depth) {
        this.depth = depth;
    }
}
