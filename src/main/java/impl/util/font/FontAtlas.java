package impl.util.font;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import impl.util.font.glyph.Glyph;
import impl.util.image.ImageObject;
import impl.util.interfaces.Shaders;
import impl.util.render.SimpleRenderer;
import lombok.Setter;
import main.Main;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Objects;

/*
How to use the gen?

- msdf-atlas-gen.exe -type msdf -font verdana.ttf -charset charset.txt -imageout verdana.png -size 128 -pxrange 8 -json verdana.json
- charset.txt: [30, 255]
*/
public class FontAtlas implements Shaders {

    private final static String FORMATTING_PALETTE = "0123456789abcdefklmnor";
    private final static int[][] FORMATTING_COLOR_PALETTE = new int[32][3];

    private final int[] textColor = new int[3];
    private volatile float textX;

    private final int width;
    private final int height;
    private final float unitRangeX, unitRangeY;

    @Setter
    private float size = 9;

    private final Glyph[] glyphs = new Glyph[256];
    private final FontMetrics fontMetrics;

    private final ImageObject tex;

    static {
        for (int i = 0; i < 32; ++i) {
            int j = (i >> 3 & 1) * 85;
            int k = (i >> 2 & 1) * 170 + j;
            int l = (i >> 1 & 1) * 170 + j;
            int i1 = (i & 1) * 170 + j;

            if (i == 6) {
                k += 85;
            }


            if (i >= 16) {
                k /= 4;
                l /= 4;
                i1 /= 4;
            }

            FORMATTING_COLOR_PALETTE[i][0] = k;
            FORMATTING_COLOR_PALETTE[i][1] = l;
            FORMATTING_COLOR_PALETTE[i][2] = i1;
        }
    }

    public FontAtlas(final String name) {
        this(
                new InputStreamReader(Objects.requireNonNull(Main.class.getResourceAsStream("/fonts/" + name + '/' + name + ".json"))),
                Main.class.getResourceAsStream("/fonts/" + name + "/" + name + ".png")
        );
    }

    public FontAtlas(final Reader meta, final InputStream texture) {
        this.tex = new ImageObject(texture);

        final JsonObject atlasJson = JsonParser.parseReader(meta).getAsJsonObject();

        if ("msdf".equals(atlasJson.getAsJsonObject("atlas").get("width").getAsString())) {
            throw new RuntimeException("Unsupported atlas-type");
        }

        this.width = atlasJson.getAsJsonObject("atlas").get("width").getAsInt();
        this.height = atlasJson.getAsJsonObject("atlas").get("height").getAsInt();
        this.fontMetrics = FontMetrics.parse(atlasJson.getAsJsonObject("metrics"));

        final int distanceRange = atlasJson.getAsJsonObject("atlas").get("distanceRange").getAsInt();
        this.unitRangeX = distanceRange / (float) width;
        this.unitRangeY = distanceRange / (float) height;

        for (final JsonElement glyphElement : atlasJson.getAsJsonArray("glyphs")) {
            final JsonObject glyphObject = glyphElement.getAsJsonObject();
            final Glyph glyph = Glyph.parse(glyphObject);

            this.glyphs[glyph.getUnicode()] = glyph;
        }
    }

    public void loadTexture() {
        tex.load();
    }

    public void drawCenteredString(final String text, final float x, final float y, final float size, final int color) {
        drawString(text, x - getWidth(text, size) / 2, y, size, color);
    }

    public void drawStringWithShadow(final String text, final float x, final float y, final float size, final int color) {
        final int alpha = (color >> 24) & 255;
        drawString(text, x - 0.5f, y - 0.5f, size, new Color(0, 0, 0, alpha).getRGB());
        drawString(text, x, y, size, color);
    }

    public void drawString(final String text, final float x, final float y, final float size, final int color) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        this.textX = x;

        int alpha = (color >> 24) & 255;
        int red = (color >> 16) & 255;
        int green = (color >> 8) & 255;
        int blue = color & 255;

        GL30.glActiveTexture(GL30.GL_TEXTURE0);
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex.getTextureID());

        msdfShader.bind();
        msdfShader.setUniform("Sampler0", 0);
        msdfShader.setUniform("ColorModulator", red / 255f, green / 255f, blue / 255f, alpha / 255f);
        msdfShader.setUniform("unitRange",unitRangeX, unitRangeY);

        this.textColor[0] = red;
        this.textColor[1] = green;
        this.textColor[2] = blue;

        final SimpleRenderer renderer = new SimpleRenderer();
        renderer.begin();


        for (int i = 0; i < text.length(); i++) {
            int unicode = text.codePointAt(i);

            if (unicode == '§' && i + 1 < text.length()) {
                final int colorIndex = FORMATTING_PALETTE.indexOf(Character.toLowerCase(text.charAt(i + 1)));
                if (colorIndex < 16) {
                    System.arraycopy(FORMATTING_COLOR_PALETTE[colorIndex], 0, textColor, 0, 3);
                } else if (colorIndex == 21) {
                    textColor[0] = red;
                    textColor[1] = green;
                    textColor[2] = blue;
                }
                i++;
            } else {
                if (unicode < 0 || unicode >= glyphs.length)
                    continue;

                final Glyph glyph = this.glyphs[unicode];
//
//                if (glyph == null)
//                    continue;

                textX += visit(renderer, glyph, textX, y, size, alpha);
            }
        }

        renderer.draw();
        msdfShader.unbind();

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
        GL11.glBindTexture(GL11.GL_TEXTURE_2D,0);
    }

    private float visit(final SimpleRenderer renderer, final Glyph glyph, final float x, final float y, final float size, final int alpha) {
        if (glyph.getPlaneRight() - glyph.getPlaneLeft() != 0) {
            float x0 = x + glyph.getPlaneLeft() * size;
            float x1 = x + glyph.getPlaneRight() * size;
            float y0 = y + fontMetrics.getAscender() * size - glyph.getPlaneTop() * size;
            float y1 = y + fontMetrics.getAscender() * size - glyph.getPlaneBottom() * size;
            float u0 = glyph.getAtlasLeft() / width;
            float u1 = glyph.getAtlasRight() / width;
            float v0 = glyph.getAtlasTop() / height;
            float v1 = glyph.getAtlasBottom() / height;

            renderer.posTexColor(x0, y0, 0, u0, 1 - v0, textColor[0], textColor[1], textColor[2], alpha);
            renderer.posTexColor(x0, y1, 0, u0, 1 - v1, textColor[0], textColor[1], textColor[2], alpha);
            renderer.posTexColor(x1, y1, 0, u1, 1 - v1, textColor[0], textColor[1], textColor[2], alpha);
            renderer.posTexColor(x1, y0, 0, u1, 1 - v0, textColor[0], textColor[1], textColor[2], alpha);
        }
        return size * glyph.getAdvance();
    }

    public final float getSize() {
        return this.size;
    }

    public final float getWidth(final String text) {
        return this.getWidth(text, size);
    }

    public float getWidth(String text, float size) {
        float sum = 0;
        for (int i = 0; i < text.length(); i++) {
            final int unicode = text.codePointAt(i);

            if (unicode == '§' && i + 1 < text.length()) {
                i++;
            } else {
                final Glyph glyph = glyphs[unicode];

                if (glyph == null)
                    continue;

                sum += size * glyph.getAdvance();
            }
        }

        return sum;
    }

    public final float getLineHeight() {
        return this.getLineHeight(size);
    }

    public final float getLineHeight(final float size) {
        return this.fontMetrics.getLineHeight() * size;
    }
}
