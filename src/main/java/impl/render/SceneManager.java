package impl.render;

public class SceneManager {
    private Scene currentScene;

    public void render() {
        if (currentScene != null) currentScene.render();
    }

    public void setCurrentScene(final Scene scene) {
        currentScene.stop();
        scene.init();
        currentScene = scene;
        System.out.println("Switched to scene: " + scene.getName());
    }

    public void setInitScene(final Scene scene) {
        scene.init();
        currentScene = scene;
        System.out.println("Initialized to scene: " + scene.getName());
    }

    public void onMouseClicked(final int mouseX, final int mouseY, final int mouseButton){
        currentScene.onMouseClicked(mouseX, mouseY, mouseButton);
    }


    public void onMouseReleased() {
        currentScene.onMouseReleased();
    }

    public void onKeyPressed(int keyCode, char c) {
        currentScene.onKeyPressed(keyCode, c);
    }
}
