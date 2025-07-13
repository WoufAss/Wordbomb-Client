package impl.util.mask;

import impl.render.Display;
import org.lwjgl.opengl.GL11;

public class ScissorUtil {
    public static void enable() {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
    }

    public static void disable() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void scissor(Display display, double x, double y, double width, double height) {
        GL11.glScissor((int) Math.round(x), (int) Math.round((display.getHeight() - (y + height))), (int) Math.round(width), (int) Math.round(height));
    }
}