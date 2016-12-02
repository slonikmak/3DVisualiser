package model;

/**
 * Created by Oceanos on 02.12.2016.
 */
public class Session {
    private long id;
    private String name;
    private String type;
    private int time;

    public Session(long id, String name, String type, int time) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.time = time;
    }

    public Session() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", time=" + time +
                '}';
    }
}
