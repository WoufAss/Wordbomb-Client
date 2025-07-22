package impl.util.font;


import impl.util.shader.ShaderProgram;

import java.io.IOException;

public class Fonts {
    //Shader
    public static final ShaderProgram msdfShader = new ShaderProgram("msdf.fsh");

    //Fonts
    public static FontAtlas volteSemiBold;
    public static FontAtlas interRegular;
    public static FontAtlas interBold;
    public static FontAtlas openSansRegular;
    public static FontAtlas openSansMedium;
    public static FontAtlas openSansBold;
    public static FontAtlas sanFrancisco;
    public static FontAtlas robotoRegular;
    public static FontAtlas robotoMedium;
    public static FontAtlas robotoBold;
    public static FontAtlas minecraft;
    public static FontAtlas mojang;
    public static FontAtlas genshin;
    public static FontAtlas nunito;
    public static FontAtlas greycliff;
    public static FontAtlas tahoma;
    public static FontAtlas kalingab;

    public static void init() throws IOException {
        volteSemiBold = new FontAtlas("volte-semibold");
        interBold = new FontAtlas("inter-bold");
        interRegular = new FontAtlas("inter-regular");
        openSansRegular = new FontAtlas("opensans-regular");
        openSansMedium = new FontAtlas("opensans-medium");
        openSansBold = new FontAtlas("opensans-bold");
        sanFrancisco = new FontAtlas("sansfransisco");
        robotoRegular = new FontAtlas("roboto-regular");
        robotoMedium = new FontAtlas("roboto-medium");
        robotoBold = new FontAtlas("roboto-bold");
        minecraft = new FontAtlas("minecraft-regular");
        mojang = new FontAtlas("mojang-regular");
        genshin = new FontAtlas("genshin");
        nunito = new FontAtlas("nunito");
        greycliff = new FontAtlas("greycliff");
        tahoma = new FontAtlas("tahoma");
        kalingab = new FontAtlas("kalingab");
    }
}