package au.edu.utas.sakther.assignment2.model;

/**
 * Created by User on 3/21/2018.
 */

public class JournalModel {
    private String title;
    private byte[] image;
    private int id;
    private String body;
    private String location;
    private String date;
    private String time;
    private String mood;
    private String tag;



    public JournalModel(int id, String title, String body, String mood, byte[] image, String date, String time, String location, String tag) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.mood = mood;
        this.image = image;
        this.date = date;
        this.time=time;
        this.location = location;
        this.tag=tag;
    }

    public JournalModel(String title, String body, String mood, byte[] image, String date, String time, String location, String tag) {
        this.title = title;
        this.body = body;
        this.mood = mood;
        this.image = image;
        this.date = date;
        this.time=time;
        this.location = location;
        this.tag=tag;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



}
