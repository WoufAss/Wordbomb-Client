package impl.util.image;

import impl.util.interfaces.Shaders;
import lombok.Getter;
import main.Main;
import org.lwjgl.opengl.EXTTextureFilterAnisotropic;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL30;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;

public class ImageObject implements Shaders {

    private BufferedImage img = null;
    @Getter
    private int textureID = 0;
    private int width, height = 0;
    public boolean loading = false;
    public boolean isLoaded = false;
    private final InputStream inputStream;

    public ImageObject(final String imageName) {
        this.inputStream = Main.class.getResourceAsStream("/images/" + imageName);
    }

    public ImageObject(final InputStream imageStream) {
        this.inputStream = imageStream;
    }

    public final void load() {
        try {
            System.out.println("loading image");
            loading = true;
            loadImageFromDisk();
            width = img.getWidth();
            height = img.getHeight();
            int[] pixels = new int[width * height];
            img.getRGB(0, 0, width, height, pixels, 0, width);

            ByteBuffer buffer = ByteBuffer.allocateDirect(4 * width * height);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int pixel = pixels[y * width + x];
                    buffer.put((byte) ((pixel >> 16) & 0xFF)); // Red
                    buffer.put((byte) ((pixel >> 8) & 0xFF));  // Green
                    buffer.put((byte) (pixel & 0xFF));         // Blue
                    buffer.put((byte) ((pixel >> 24) & 0xFF)); // Alpha
                }
            }

            buffer.flip();

            textureID = GL11.glGenTextures();
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, textureID);

            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL12.GL_CLAMP_TO_EDGE);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
            GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR_MIPMAP_LINEAR);

            GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA8, width, height, 0, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, buffer);

            GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D);

            if (Objects.requireNonNull(GL11.glGetString(GL11.GL_EXTENSIONS)).contains("GL_EXT_texture_filter_anisotropic")) {
                float maxAnisotropy = GL11.glGetFloat(EXTTextureFilterAnisotropic.GL_MAX_TEXTURE_MAX_ANISOTROPY_EXT);
                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, EXTTextureFilterAnisotropic.GL_TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy);
            }
            isLoaded = true;


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void loadImageFromDisk() throws IOException {
        img = ImageIO.read(inputStream);
    }


    public final void unload() {
        if (!isLoaded)
            return;

        GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
        GL11.glDeleteTextures(textureID);
    }

    public final void drawImg(float x, float y, float width, float height) {
        if(!isLoaded)
            return;

        drawImg(x,y,width,height, new Color(255,255,255));
    }

    public final void drawImg(float x, float y) {
        if(!isLoaded)
            return;

        drawImg(x,y,width,height, new Color(255,255,255));
    }

    public void drawImg(float x, float y, float width, float height, Color color) {
        if(!isLoaded)
            return;


        GL11.glColor4f(color.getRed() / 255f, color.getGreen() / 255f, color.getBlue() / 255f, color.getAlpha() / 255f);
        textureShader.bind();
        glBindTexture(GL_TEXTURE_2D, textureID);
        textureShader.setUniform("iChannel0",0);
        textureShader.drawQuads(x,y,width,height);
        textureShader.unbind();

    }
}