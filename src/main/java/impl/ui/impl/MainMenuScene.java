package impl.ui.impl;

import impl.ui.Scene;
import impl.util.font.Fonts;
import impl.util.image.ImageObject;
import impl.util.interfaces.Consts;

public class MainMenuScene extends Scene implements Consts {
    final ImageObject backgroundImage = new ImageObject("mainmenu/background.JPG");

    @Override
    public void init() {
        backgroundImage.load();
    }


    @Override
    public void render() {
        backgroundImage.drawImg(0,0, DISPLAY.getWidth(), DISPLAY.getHeight());
        Fonts.interBold.drawString("salut",10,10,10,-1);
    }

    @Override
    public void stop() {

    }

    @Override
    public String getName() {
        return "Texture Scene";
    }

    @Override
    public void onMouseClicked(int mouseX, int mouseY, int mouseButton) {

    }

    @Override
    public void onMouseReleased() {

    }

    @Override
    public void onKeyPressed(int keyCode, char c) {

    }
}