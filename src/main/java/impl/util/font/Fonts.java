package impl.util.font;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Fonts {
    //List
    private static final List<FontAtlas> fonts = new ArrayList<>();

    //Fonts
    public static final FontAtlas volteSemiBold = register(new FontAtlas("volte-semibold"));
    public static final FontAtlas interRegular = register(new FontAtlas("inter-regular"));
    public static final FontAtlas interBold = register(new FontAtlas("inter-bold"));
    public static final FontAtlas openSansRegular = register(new FontAtlas("opensans-regular"));
    public static final FontAtlas openSansMedium = register(new FontAtlas("opensans-medium"));
    public static final FontAtlas openSansBold = register(new FontAtlas("opensans-bold"));
    public static final FontAtlas sanFrancisco = register(new FontAtlas("sansfransisco"));
    public static final FontAtlas robotoRegular = register(new FontAtlas("roboto-regular"));
    public static final FontAtlas robotoMedium = register(new FontAtlas("roboto-medium"));
    public static final FontAtlas robotoBold = register(new FontAtlas("roboto-bold"));
    public static final FontAtlas minecraft = register(new FontAtlas("minecraft-regular"));
    public static final FontAtlas mojang = register(new FontAtlas("mojang-regular"));
    public static final FontAtlas genshin = register(new FontAtlas("genshin"));
    public static final FontAtlas nunito = register(new FontAtlas("nunito"));
    public static final FontAtlas greycliff = register(new FontAtlas("greycliff"));
    public static final FontAtlas tahoma = register(new FontAtlas("tahoma"));
    public static final FontAtlas kalingab = register(new FontAtlas("kalingab"));

    private static FontAtlas register(FontAtlas font) {
        fonts.add(font);
        return font;
    }

    public static void init() throws IOException {
        for (FontAtlas font : fonts)
            font.loadTexture();
    }
}