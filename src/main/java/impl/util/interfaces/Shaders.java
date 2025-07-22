package impl.util.interfaces;

import impl.util.shader.ShaderManager;
import impl.util.shader.ShaderProgram;
import main.Main;

public interface Shaders {
    ShaderProgram textureShader = ShaderManager.shaders.get("texture");
    ShaderProgram msdfShader = ShaderManager.shaders.get("msdf");

}
