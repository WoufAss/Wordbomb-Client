package impl.util;

import org.lwjgl.opengl.GL11;

public final class RenderUtil {


    public static void render(final int mode, final Runnable render) {
        GL11.glBegin(mode);
        render.run();
        GL11.glEnd();
    }

}
