package org.me.gcu.earthquakecw.Data;

import android.os.Parcel;
import android.os.Parcelable;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * This is a class EarthQuake class, this is used to store the data about the earthquake
 * in one object. Array of earthquake objects is used in the application to display the info about
 * multiple earthquakes
 */

public class EarthQuake implements Parcelable {

    private String title;
    private String location;
    private int depth;
    private float magnitude;
    private String link;
    private LocalDateTime date;
    private String category;
    private float glat;
    private float glong;

    public EarthQuake() {
        this.title = "test";
        this.location = "test";
        this.depth = 0;
        this.magnitude = 0;
        this.link = "test";
        this.date = LocalDateTime.of(2001, 1, 1,0,0);
        this.category = "test";
        this.glat = 0;
        this.glong = 0;
    }

    public EarthQuake(String title, String location, int depth, float magnitude, String link, LocalDateTime date, String category, float glat, float glong) {
        this.title = title;
        this.location = location;
        this.depth = depth;
        this.magnitude = magnitude;
        this.link = link;
        this.date = date;
        this.category = category;
        this.glat = glat;
        this.glong = glong;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public int getDepth() {
        return depth;
    }

    public void setDepth(int depth) {
        this.depth = depth;
    }

    public float getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(float magnitude) {
        this.magnitude = magnitude;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public float getGlat() {
        return glat;
    }

    public void setGlat(float glat) {
        this.glat = glat;
    }

    public float getGlong() {
        return glong;
    }

    public void setGlong(float glong) {
        this.glong = glong;
    }

    @Override
    public String toString() {
        return "EarthQuake{" +
                "title='" + title + '\'' +
                ",\n location='" + location + '\'' +
                ",\n depth='" + depth + '\'' +
                ",\n magnitude=" + magnitude +
                ",\n link='" + link + '\'' +
                ",\n date=" + date +
                ",\n category='" + category + '\'' +
                ",\n glat=" + glat +
                ",\n glong=" + glong +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeString(title);
        dest.writeString(location);
        dest.writeInt(depth);
        dest.writeFloat(magnitude);
        dest.writeString(link);
        dest.writeString(date.toString());
        dest.writeString(category);
        dest.writeFloat(glat);
        dest.writeFloat(glong);
    }

    private EarthQuake(Parcel in) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        title = in.readString();
        location = in.readString();
        depth = in.readInt();
        magnitude = in.readFloat();
        link = in.readString();
        date = LocalDateTime.parse(in.readString(), formatter);
        category = in.readString();
        glat = in.readFloat();
        glong = in.readFloat();
    }

    public static final Creator<EarthQuake> CREATOR
            = new Creator<EarthQuake>() {
        public EarthQuake createFromParcel(Parcel in) {
            return new EarthQuake(in);
        }

        public EarthQuake[] newArray(int size) {
            return new EarthQuake[size];
        }
    };
}