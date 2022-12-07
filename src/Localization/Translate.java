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
        //Locale french = new Locale("fr", "FR");
        //ResourceBundle rb = ResourceBundle.getBundle("Localization/lang", french);
        ArrayList tokens = new ArrayList<String>();
        StringTokenizer tokenizer = new StringTokenizer(string);
        for (int i = 1; tokenizer.hasMoreTokens(); i++) {
            String currentToken = tokenizer.nextToken();
            tokens.add(rb.getString(currentToken));
        }
        return String.join(" ", tokens);
    }
}
