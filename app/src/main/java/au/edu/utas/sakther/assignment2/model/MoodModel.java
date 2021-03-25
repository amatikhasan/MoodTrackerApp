package au.edu.utas.sakther.assignment2.model;

public class MoodModel {

    private int id;
    private String mood;
    private String date;
    private String time;
    private int moodValue;


    public MoodModel(int id, String mood, int moodValue, String date, String time) {

        this.id = id;
        this.mood = mood;
        this.time=time;
        this.date = date;
        this.moodValue=moodValue;

    }

    public MoodModel(String mood, int moodValue, String date, String time) {
        this.mood = mood;
        this.time=time;
        this.date = date;
        this.moodValue=moodValue;

    }

    public MoodModel(String mood, int moodValue) {
        this.mood = mood;
        this.moodValue=moodValue;
    }
    public MoodModel(String mood) {
        this.mood = mood;
    }

    public int getMoodValue() {
        return moodValue;
    }

    public void setMoodValue(int moodValue) {
        this.moodValue = moodValue;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }


}
