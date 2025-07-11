package main;

import com.google.gson.Gson;
import impl.config.Config;
import impl.game.Client;
import impl.render.Display;
import impl.render.SceneManager;
import impl.render.ShaderScene;
import impl.util.Consts;

import java.io.InputStream;
import java.io.InputStreamReader;

public class Main implements Consts {
    private final static Client client;

    // Files (config, words)
    public static Config config;
    public static SceneManager sceneManager = new SceneManager();
    private static Display display;

    static {
        //Config
        final Gson gson = new Gson();
        final InputStream configStream = Main.class.getClassLoader().getResourceAsStream("config.json");
        config = gson.fromJson(new InputStreamReader(configStream), Config.class);

        // Init game client
        client = new Client(config.token);
        display = new Display(800,600,new ShaderScene());
    }


    public static void main(String[] args) throws Exception {
        display.run();

        if (true) return;
        client.createGame();

        final String roomID = client.getRoomID();
        System.out.println("Created room with ID : " + roomID);

        client.connect();
        client.awaitConnection();

        client.startGame();
    }
}
