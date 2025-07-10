package main;

import com.google.gson.Gson;
import impl.config.Config;
import impl.game.Client;
import org.lwjgl.Version;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class Main {
    private final static Client client;

    // Files (config, words)
    public static Config config;

    static {
        //Config
        final Gson gson = new Gson();
        final InputStream configStream = Main.class.getClassLoader().getResourceAsStream("config.json");
        config = gson.fromJson(new InputStreamReader(configStream), Config.class);

        // Init game client
        client = new Client(config.token);;
    }


    public static void main(String[] args) throws Exception {
        System.out.println("LWJGL version : " + Version.getVersion());

        if (true) return;
        client.createGame();

        final String roomID = client.getRoomID();
        System.out.println("Created room with ID : " + roomID);

        client.connect();
        client.awaitConnection();

        client.startGame();
    }
}
