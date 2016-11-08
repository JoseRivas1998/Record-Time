package com.tcg.recordtime.managers;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.CopyOption;
import java.nio.file.Files;

/**
 * Created by JoseR on 10/29/2016.
 */
public class FileManager {

    public static void createApplicationFolder() {
        if(!applicationFolderExists()) {
            File folder = new File(localAppDataFolder("Record Time"));
            folder.mkdirs();
        }
    }

    public static boolean applicationFolderExists() {
        File applicationFolder = new File(localAppDataFolder("Record Time"));
        return applicationFolder.exists();
    }

    public static void saveProfilesJSON(JSONObject profiles) {
        BufferedWriter bufferedWriter = null;
        FileWriter fileWriter = null;
        try {
            File profilesJSON = new File(localAppDataFolder("Record Time") + File.separator + "profiles.json");
            if(!profilesJSON.exists()) createProfilesJSON();
            fileWriter = new FileWriter(profilesJSON);
            bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write(profiles.toString());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedWriter.close();
                fileWriter.close();
            } catch (Exception e) {}
        }
    }

    public static JSONObject loadProfilesJSON(boolean createNotExist) {
        BufferedReader bufferedReader = null;
        FileReader  fileReader = null;
        JSONObject profiles = null;

        try {
            File profilesJSON = new File(localAppDataFolder("Record Time") + File.separator + "profiles.json");
            if(profilesJSON.exists()) {
                fileReader = new FileReader(profilesJSON);
                bufferedReader = new BufferedReader(fileReader);
                profiles = new JSONObject(bufferedReader.readLine());
            } else if(createNotExist) {
                createProfilesJSON();
                return loadProfilesJSON(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                bufferedReader.close();
                fileReader.close();
            } catch (Exception e) {}
        }

        if(profiles != null) {
            return profiles;
        } else {
            return new JSONObject();
        }
    }

    public static void createProfilesJSON() {
        if(!profilesJSONExists()) {
            BufferedWriter bufferedWriter = null;
            FileWriter fileWriter = null;
            try {
                File profileJSON = new File(localAppDataFolder("Record Time") + File.separator + "profiles.json");
                fileWriter = new FileWriter(profileJSON);
                bufferedWriter = new BufferedWriter(fileWriter);
                JSONObject profiles = new JSONObject();
                JSONArray profilesArr = new JSONArray();
                profiles.put("profiles", profilesArr);
                bufferedWriter.write(profiles.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    bufferedWriter.close();
                    fileWriter.close();
                } catch (Exception e) {}
            }
        }
    }

    public static boolean profilesJSONExists() {
        File profiles = new File(localAppDataFolder("Record Time") + File.separator + "profiles.json");
        return profiles.exists();
    }

    public static String localAppDataFolder(String subfolder) {
        return System.getProperty("user.home") + File.separator + "AppData" + File.separator + "Local" + File.separator + "Tiny Country Games" + File.separator + subfolder;
    }

    public static boolean downloadToFile(String url, File target, CopyOption... options) {
        boolean copied = false;
        try {
            URL webFile = new URL(url);
            InputStream in = webFile.openStream();
            Files.copy(in, target.toPath(), options);
            copied = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copied;
    }

}
