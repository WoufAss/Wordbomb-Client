package impl.util.interfaces;

import impl.game.Client;
import impl.render.Display;
import impl.util.shader.ShaderManager;
import main.Main;

public interface Consts {
    String TITLE = "WordBomb Client";
    Display DISPLAY = Main.getDisplay();
    ShaderManager SHADER_MANAGER = Main.getShaderManager();
    Client CLIENT = Main.getClient();
}
