package present2glass.fonts;

import javafx.scene.text.Font;


public class Roboto {
    public Font font;
    public static final String THIN = "Roboto-Thin.ttf",
        THIN_ITALIC = "Roboto-ThinItalic.ttf",
        LIGHT = "Roboto-Light.ttf",
        LIGHT_ITALIC ="Roboto-LightItalic.ttf",
        REGULAR = "Roboto-Regular.ttf",
        ITALIC = "Roboto-Italic.ttf",
        MEDIUM = "Roboto-Medium.ttf",
        MEDIUM_ITALIC = "Roboto-MediumItalic.ttf",
        BOLD = "Roboto-Bold.ttf",
        BOLD_ITALIC = "Roboto-BoldItalic.ttf",
        BOLD_CONDENSED= "Roboto-BoldCondensed.ttf",
        BOLD_CONDENSED_ITALIC = "Roboto-BoldCondensedItalic.ttf",
        CONDENSED = "Roboto-Condensed.ttf",
        CONDENSED_ITALIC = "Roboto-CondensedItalic.ttf",
        BLACK = "Roboto-Black.ttf",
        BLACK_ITALIC = "Roboto-BlackItalic.ttf";

    public Roboto(String name, int size){
        try {
            font = Font.loadFont(getClass().getResourceAsStream("/fonts/"+name), size);
        } catch (Error e){
            throw new Error("Unable to load font");
        }
    }
}