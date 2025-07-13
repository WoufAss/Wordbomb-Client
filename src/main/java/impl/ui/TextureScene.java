package impl.ui;

import impl.util.image.ImageObject;
import impl.util.interfaces.Consts;
import impl.util.shader.ShaderProgram;

import static org.lwjgl.opengl.GL20.*;

public class TextureScene extends Scene implements Consts {
    private int textureId;
    private ShaderProgram shader;

    @Override
    public void init() {
        shader = new ShaderProgram("texture.glsl");
        createTexture();
    }

    private void createTexture() {
        final ImageObject img = new ImageObject("perla.png");
        img.load();
        textureId = img.getTextureID();
    }


    @Override
    public void render() {
        glClearColor(0.2f, 0.3f, 0.3f, 1.0f);

        shader.bind();
        glBindTexture(GL_TEXTURE_2D, textureId);
        shader.setUniform("iChannel0",0);
        shader.drawQuads(DISPLAY.getWidth()/2f - 150,DISPLAY.getHeight()/2f - 150,300,300);
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