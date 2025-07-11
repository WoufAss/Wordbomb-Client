package impl.render;

import impl.ui.Scene;
import impl.util.interfaces.Consts;
import lombok.Getter;
import main.Main;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;
import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

// https://www.lwjgl.org/guide
public class Display implements Consts {
    private long window;
    @Getter
    private int width, height;
    private boolean vsync = true;
    private final Scene initScene;

    public Display(final int width, final int height, final Scene initScene) {
        this.width = width;
        this.height = height;
        this.initScene = initScene;
    }

    public void run() {
        init();
        loop();
        cleanup();
    }

    private void init() {
        GLFWErrorCallback.createPrint(System.err).set();

        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW");
        }

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE);

        window = glfwCreateWindow(width, height, TITLE, NULL, NULL);
        if (window == NULL)
            throw new RuntimeException("Failed to create the GLFW window");


        //events - will do a proper event system later
        glfwSetKeyCallback(window, (window, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(window, true);
            }

            // key event
            if (action == GLFW_PRESS) {
                final char keyChar = (char) key;
                Main.sceneManager.onKeyPressed(key, keyChar);
            }


        });


        glfwSetMouseButtonCallback(window, (window, button, action, mods) -> {
            if (action == GLFW_PRESS) {
                double[] xpos = new double[1];
                double[] ypos = new double[1];
                glfwGetCursorPos(window, xpos, ypos);

                int mouseX = (int) xpos[0];
                int mouseY = (int) ypos[0];

                Main.sceneManager.onMouseClicked(mouseX, mouseY, button);
            }
            if (action == GLFW_RELEASE) {
                Main.sceneManager.onMouseReleased();
            }
        });

        // resize event
        glfwSetWindowSizeCallback(window, (window, width, height) -> {
            this.width = width;
            this.height = height;
        });

        try (MemoryStack stack = stackPush()) {
            final IntBuffer pWidth = stack.mallocInt(1);
            final IntBuffer pHeight = stack.mallocInt(1);
            glfwGetWindowSize(window, pWidth, pHeight);

            final GLFWVidMode vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            if (vidMode == null) throw new RuntimeException("Video mode failed to initialize.");
            glfwSetWindowPos(window,
                    (vidMode.width() - pWidth.get(0)) / 2,
                    (vidMode.height() - pHeight.get(0)) / 2);
        }

        glfwMakeContextCurrent(window);
        glfwSwapInterval(vsync ? 1 : 0);
        glfwShowWindow(window);

        GL.createCapabilities();
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
    }

    private void loop() {
        Main.sceneManager.setInitScene(initScene);
        while (!glfwWindowShouldClose(window)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            Main.sceneManager.render();

            glfwSwapBuffers(window);
            glfwPollEvents();
        }
    }

    private void cleanup() {
        glfwFreeCallbacks(window);
        glfwDestroyWindow(window);
        glfwTerminate();
        glfwSetErrorCallback(null).free();
    }

    private void toggleVSync() {
        vsync = !vsync;
        glfwSwapInterval(vsync ? 1 : 0);
        System.out.println("VSync: " + (vsync ? "ON" : "OFF"));
    }
}
