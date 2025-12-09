package dev.phoenixofforce.adventofcode.meta;

public class TimeUtil {

    public static String parseTime(double timeInMillis) {
        double millis = timeInMillis;
        int seconds = (int) Math.floor(timeInMillis / 1000.0);
        int minutes = (int) Math.floor(seconds / 60.0);
        int hours = (int) Math.floor(minutes / 60.0);

        millis -= seconds * 1000;
        seconds -= minutes * 60;
        minutes -= hours * 60;

        String out = "";
        if(hours > 0) out += hours + "h   ";
        if(hours > 0 || minutes > 0) out += minutes + "min   ";
        if(hours > 0 || minutes > 0 || seconds > 0) out += seconds + "s   ";

        String millisAsString = millis + "";
        if(millisAsString.contains(".") && millisAsString.startsWith("0")) millisAsString = millisAsString.substring(0, Math.min(millisAsString.length(), millisAsString.indexOf(".") + 4));
        out += millisAsString + "ms";

        return out;
    }
}
