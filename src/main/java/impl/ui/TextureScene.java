package impl.ui;

import impl.util.image.ImageObject;
import impl.util.interfaces.Consts;
import impl.util.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.*;

public class TextureScene extends Scene implements Consts {
    final ImageObject img = new ImageObject("perla.png");

    @Override
    public void init() {
        createTexture();
    }

    private void createTexture() {
        img.load();
    }


    @Override
    public void render() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);
        img.drawImg(0,0,50,50);
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