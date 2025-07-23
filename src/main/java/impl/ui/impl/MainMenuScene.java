package impl.ui.impl;

import impl.ui.Scene;
import impl.util.animation.Animation;
import impl.util.animation.Easing;
import impl.util.font.Fonts;
import impl.util.image.ImageObject;
import impl.util.interfaces.Consts;

public class MainMenuScene extends Scene implements Consts {
    private final ImageObject backgroundImage = new ImageObject("mainmenu/background.JPG");
    private final Animation animation = new Animation(Easing.EASE_OUT_BACK, 750);

    @Override
    public void init() {
        backgroundImage.load();
    }


    @Override
    public void render() {
        animation.loop(0.0, 1.0);
        backgroundImage.drawImg(0,0, DISPLAY.getWidth(), DISPLAY.getHeight());
        Fonts.genshin.drawString("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890?!.,$", 10F, 10F,(float) (20 + 5*animation.getValue()),-1);
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