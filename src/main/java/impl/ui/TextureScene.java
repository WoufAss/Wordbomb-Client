package impl.ui;

import impl.util.animation.Animation;
import impl.util.animation.Easing;
import impl.util.image.ImageObject;
import impl.util.interfaces.Consts;

import static org.lwjgl.glfw.GLFW.glfwGetTime;

public class TextureScene extends Scene implements Consts {
    final ImageObject imgPerla = new ImageObject("perla.png");
    final ImageObject imgCaca = new ImageObject("pdp.JPG");
    private final Animation perlaAnim = new Animation(Easing.EASE_OUT_QUAD, 500);
    private final Animation cacaAnim = new Animation(Easing.EASE_OUT_BACK, 1000);

    @Override
    public void init() {
        imgPerla.load();
        imgCaca.load();
    }


    @Override
    public void render() {
        perlaAnim.loop(0.0, 1.0);
        cacaAnim.loop(0.0, 1.0);

        imgPerla.drawImg((float) (300*perlaAnim.getValue()),0,300,300);
        imgCaca.drawImg((float) (300*cacaAnim.getValue()),300,300,300);
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