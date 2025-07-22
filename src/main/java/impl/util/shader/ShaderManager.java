package impl.util.shader;

import java.util.HashMap;

public class ShaderManager {
    public static HashMap<String, ShaderProgram> shaders = new HashMap<>(){{
        put("texture", new ShaderProgram("texture.glsl"));
        put("msdf", new ShaderProgram("msdf.fsh"));
    }};

    public void compileShaders() {
        for(ShaderProgram shader : shaders.values()){
            shader.compile();
        }
    }
}
