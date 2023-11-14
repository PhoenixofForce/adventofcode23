package dev.phoenixofforce.adventofcode.common;

import org.springframework.util.ResourceUtils;

import java.io.*;
import java.util.Optional;

public class FileUtils {

    public static String getFileName(int day, int year) {
        return "y" + year + "/day" + day;
    }

    public static void createInputFile(int day, int year) {
        File f = getFile(day, year).get();
        if(!f.exists()) {
            f.getParentFile().mkdirs();
            try {
                f.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static boolean doesFileExist(int day, int year) {
        try {
            File f = ResourceUtils.getFile(getFileName(day, year));
            return f.exists();
        } catch (FileNotFoundException ignored) {}
        return false;
    }

    public static Optional<File> getFile(int day, int year) {
        try {
            File f = ResourceUtils.getFile(getFileName(day, year));
            return Optional.of(f);
        } catch (FileNotFoundException ignored) {}
        return Optional.empty();
    }

    public static void write2file(File f, String s) {
        f.getParentFile().mkdirs();
        try {
            BufferedWriter w = new BufferedWriter(new FileWriter(f));
            w.write(s);
            w.flush();
            w.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
