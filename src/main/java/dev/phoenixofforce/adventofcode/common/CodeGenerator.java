package dev.phoenixofforce.adventofcode.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

import java.io.*;

@Component
public class CodeGenerator {

    @Value("${aoc.basePath}")
    private String basePath;

    public void generateDay(int day, int year) {
        try {
            String dayStr = (day < 10? "0": "") + day;

            File template = ResourceUtils.getFile("classpath:Template.java");
            File writeTo = new File(basePath + getPackageSuffixForDay(day, year).replace(".", "/") + "/Day" + dayStr + "_" + year + ".java");

            writeTo.getParentFile().mkdirs();
            writeTo.createNewFile();

            BufferedReader reader = new BufferedReader(new FileReader(template));
            BufferedWriter writer = new BufferedWriter(new FileWriter(writeTo));

            String line = reader.readLine();

            while(line != null) {
                writer.write(
                        line.replace("{{package}}", getPackageSuffixForDay(day, year))
                                .replace("{{day}}", dayStr + "_" + year) + "\r\n"
                );
                line = reader.readLine();
            }

            reader.close();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String getPackageSuffixForDay(int day, int year) {
        String dayString = (day < 10? "0": "") + day;
        return String.format("y%s.day%s", year, dayString);
    }
}
