package Localization;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;

public class Translate {
    public static String str(String string) {
        // Resource Bundle is a bundle of translations.
        // We detect the language based on timezone, locality etc.
        // setting in the OS and then grab the appropriate bundle
        ResourceBundle rb = ResourceBundle.getBundle("Localization/lang", Locale.getDefault());
        //ResourceBundle rb = ResourceBundle.getBundle("Localization/lang", french);
        ArrayList tokens = new ArrayList<String>();
        StringTokenizer tok = new StringTokenizer(string);
        for (int i = 1; tok.hasMoreTokens(); i++) {
            tokens.add(rb.getString(tok.nextToken()));
        }
        return String.join(" ", tokens);
    }
}
