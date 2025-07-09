package main;

import com.google.gson.Gson;
import impl.config.Config;
import impl.game.Client;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Main {
    private final static Client client;

    // Files (config, words)
    private static ArrayList<String> words = new ArrayList<>();
    public static Config config = new Config();

    static {
        try {
            //Config
            final Gson gson = new Gson();
            final InputStream configStream = Main.class.getClassLoader().getResourceAsStream("config.json");
            config = gson.fromJson(new InputStreamReader(configStream), Config.class);

            // Authorized chars
            final Pattern wordPattern = Pattern.compile("^[a-zA-Z]+$");
            final InputStream wordStream = Main.class.getClassLoader().getResourceAsStream("words.txt");
            final String content = new String(wordStream.readAllBytes(), StandardCharsets.UTF_8);
            words = (ArrayList<String>) Stream.of(content.replace("\r", "").split("\n"))
                    .filter(word -> wordPattern.matcher(word).matches())
                    .collect(Collectors.toList());

            // Init game client
            client = new Client(config.token);


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static void main(String[] args) throws Exception {
        client.createGame();
    }
}
