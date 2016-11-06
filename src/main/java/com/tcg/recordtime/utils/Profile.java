package com.tcg.recordtime.utils;

import com.tcg.recordtime.managers.FileManager;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Created by JoseR on 10/30/2016.
 */
public class Profile implements Serializable {

    private static final long serialVersionUID = 237205711378210840L;

    private String name;
    private ArrayList<Long> times;
    private ArrayList<Calendar> dates;

    public static Profile currentProfile;

    public Profile(String name) {
        this.name = name;
        times = new ArrayList<>();
        dates = new ArrayList<>();
    }

    public Profile(Profile profile) {
        this.name = profile.getName();
        times = new ArrayList<>(profile.getTimes());
        dates = new ArrayList<>(profile.getDates());
    }

    public void sortByDate(boolean asc) {
        Map<Calendar, Long> map = new HashMap<>();
        for(int i = 0; i < dates.size(); i++) {
            map.put(dates.get(i), times.get(i));
        }
        Collections.sort(dates, new Comparator<Calendar>() {
            @Override
            public int compare(Calendar o1, Calendar o2) {
                if(asc) {
                    return o1.compareTo(o2);
                } else {
                    return o2.compareTo(o1);
                }
            }
        });
        for(int i = 0; i < times.size(); i++) {
            times.set(i, map.get(dates.get(i)));
        }
    }

    public void sortByTime(boolean asc) {
        Map<Long, Calendar> map = new HashMap<>();
        for(int i = 0; i < times.size(); i++) {
            map.put(times.get(i), dates.get(i));
        }
        Collections.sort(times, new Comparator<Long>() {
            @Override
            public int compare(Long o1, Long o2) {
                if(asc) {
                    return (int) (o1 - o2);
                } else {
                    return (int) (o2 - o1);
                }
            }
        });
        for(int i = 0; i < dates.size(); i++) {
            dates.set(i, map.get(times.get(i)));
        }
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

    public ArrayList<Calendar> getDates() {
        return dates;
    }

    public void setDates(ArrayList<Calendar> dates) {
        this.dates = dates;
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

    @Override
    public String toString() {
        return "Profile{" +
                "name='" + name + '\'' +
                ", times=" + times +
                '}';
    }

    public static List<Profile> loadProfiles() {
        JSONObject profiles = new JSONObject(FileManager.loadProfilesJSON(true).toString());
        JSONArray profilesArr = profiles.getJSONArray("profiles");
        List<Profile> profileList = new ArrayList<>();
        for(int i = 0; i < profilesArr.length(); i++) {
            String fileName = profilesArr.getJSONObject(i).getString("file");
            String profileName = profilesArr.getJSONObject(i).getString("name");
            File profileFile = new File(FileManager.localAppDataFolder("Record Time") + File.separator + fileName);
            try {
                profileList.add(Profile.load(profileFile));
            } catch (IOException | ClassNotFoundException e) {
                try {
                    Profile.save(new Profile(profileName), profileFile);
                } catch (IOException e1) {
                    e.printStackTrace();
                    e1.printStackTrace();
                }
            }
        }
        return profileList;
    }

}
