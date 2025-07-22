package impl.util.render;

import org.lwjgl.opengl.GL11;

import java.util.ArrayList;
import java.util.List;

public class SimpleRenderer {

    private final List<float[]> vertices = new ArrayList<>();

    public void begin() {
        vertices.clear();
    }

    public void posTexColor(float x, float y, float z, float u, float v, int r, int g, int b, int a) {
        vertices.add(new float[] {
                x, y, z,
                u, v,
                r / 255f, g / 255f, b / 255f, a / 255f
        });
    }

    public void draw() {
        GL11.glBegin(GL11.GL_QUADS);
        for (float[] v : vertices) {
            GL11.glColor4f(v[5], v[6], v[7], v[8]);
            GL11.glTexCoord2f(v[3], v[4]);
            GL11.glVertex3f(v[0], v[1], v[2]);
        }
        GL11.glEnd();
    }
}