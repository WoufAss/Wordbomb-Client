package impl.render;

abstract class Scene {
    public abstract void init();
    public abstract void stop();
    public abstract void render();
    public abstract String getName();
    public abstract void onMouseClicked(int mouseX, int mouseY, int mouseButton);
    public abstract void onMouseReleased();
    public abstract void onKeyPressed(int keyCode, char c);
}