package dev.phoenixofforce.adventofcode.meta;

import java.awt.*;
import java.awt.datatransfer.StringSelection;

public class Clipboard {

    public static void save(String theString) {
        StringSelection selection = new StringSelection(theString);
        java.awt.datatransfer.Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);
    }

}
