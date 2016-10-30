package com.tcg.recordtime.utils;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by JoseR on 10/30/2016.
 */
public class Profile implements Serializable {

    private String name;
    private ArrayList<Long> times;

    public static Profile currentProfile;

    public Profile(String name) {
        this.name = name;
        times = new ArrayList<>();
    }

    public Profile(Profile profile) {
        this.name = profile.getName();
        times = new ArrayList<>(profile.getTimes());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Long> getTimes() {
        return times;
    }

    public void setTimes(ArrayList<Long> times) {
        this.times = times;
    }

    public void addTime(long time) {
        times.add(time);
    }

    public static Profile load(File file) throws IOException, ClassNotFoundException {
        Profile profile = null;
        FileInputStream fileInputStream = new FileInputStream(file);
        ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
        byte[] bytes = (byte[]) objectInputStream.readObject();
        profile = (Profile) Profile.deserialize(bytes);
        objectInputStream.close();
        fileInputStream.close();
        return profile;
    }

    public static void save(Profile profile, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(Profile.serialize(profile));
        fileOutputStream.close();
        objectOutputStream.close();
    }

    public static byte[] serialize(Profile profile) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(b);
        out.writeObject(profile);
        return b.toByteArray();
    }

    public static Object deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream in = new ObjectInputStream(b);
        return in.readObject();
    }

}
