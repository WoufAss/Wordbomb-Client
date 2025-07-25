package impl.game;

import com.google.gson.Gson;
import com.microsoft.playwright.*;
import impl.network.SocketClient;
import impl.struct.Room;
import impl.struct.SessionInfo;
import lombok.Getter;
import okhttp3.*;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class Client {
    // Network
    private volatile CountDownLatch connectionLatch;
    private final String authToken;
    private final OkHttpClient httpClient;
    private SocketClient socketClient;

    //Game infos
    @Getter
    private String roomID;
    private String sessionID;
    private String processID;


    public Client(final String token){
        this.authToken = "Bearer " + token;
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
    }

    public final void awaitConnection() throws InterruptedException {
        if (connectionLatch != null) connectionLatch.await();
    }

    public final void signalConnectionConfirmed() {
        if (connectionLatch != null) connectionLatch.countDown();
    }

    public void connect() throws Exception {
        if (socketClient != null && socketClient.isOpen())
            socketClient.closeBlocking();

        this.connectionLatch = new CountDownLatch(1);

        final String url = String.format("wss://1266394578702041119.discordsays.com/.proxy/colyseus/server/%s/%s?sessionId=%s",
                processID, roomID, sessionID);

        final Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) discord/1.0.1148 Chrome/134.0.6998.205 Electron/35.3.0 Safari/537.36");

        socketClient = new SocketClient(new URI(url), headers, this);
        socketClient.connect();
    }

    public void startGame() {
        try {
            // Start packet
            final byte[] packet = {(byte) 0x0d, (byte) 0xa5, 0x73, 0x74, 0x61, 0x72, 0x74};

            if (socketClient == null || socketClient.isClosed())
                return;
            if (socketClient != null && socketClient.isOpen()) {
                socketClient.send(packet);
            }

        } catch (Exception e) {
            System.err.println("Failed to send start game command: " + e.getMessage());
        }
    }

    // a
    public void sendChat(String message) {
        final byte[] chatPrefix = {(byte) 0x0d, (byte) 0xa4, 0x63, 0x68, 0x61, 0x74, (byte) 0x81, (byte) 0xa7, 0x6d, 0x65, 0x73, 0x73, 0x61, 0x67, 0x65};
        final byte[] messageBytes = message.getBytes();
        final byte[] packet = new byte[chatPrefix.length + 1 + messageBytes.length];

        System.arraycopy(chatPrefix, 0, packet, 0, chatPrefix.length);
        packet[chatPrefix.length] = (byte) (0xA0 | messageBytes.length);
        System.arraycopy(messageBytes, 0, packet, chatPrefix.length + 1, messageBytes.length);

        socketClient.send(packet);
    }

    public final void createGame(){
        final String jsonPayload = "{\"locked\":true,\"maxp\":32}";
        final RequestBody body = RequestBody.create(jsonPayload, MediaType.get("application/json"));
        final Request request = new Request.Builder()
                .url("https://1266394578702041119.discordsays.com/.proxy/colyseus/server/matchmake/create/my_room")
                .post(body)
                .header("Authorization", authToken)
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) discord/1.0.1148 Chrome/134.0.6998.205 Electron/35.3.0 Safari/537.36")
                .build();

        try (Response response = httpClient.newCall(request).execute()) {
            final String responseBody = response.body().string();
            boolean parseFailed;

            if (!response.isSuccessful()) throw new IOException("Failed to create room. Server returned status " + response.code() + ". Response: " + responseBody);

            final SessionInfo sessionInfo = new Gson().fromJson(responseBody, SessionInfo.class);
            parseFailed = (sessionInfo == null || sessionInfo.getRoom() == null);
            if (parseFailed) throw new IOException("Failed to parse a valid 'room' object from the server's response.");


            this.roomID = sessionInfo.getRoom().getRoomId();
            this.sessionID = sessionInfo.getSessionId();
            this.processID = sessionInfo.getRoom().getProcessId();

            parseFailed = roomID == null
                    || sessionID == null
                    || processID == null;

            if (parseFailed) throw new IOException("Failed to parse required IDs from response: " + responseBody);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public final void stop() {
        /* */
    }

    public final CompletableFuture<List<Room>> getRoomList() {
        final CompletableFuture<List<Room>> roomAsync = new CompletableFuture<>();

        Executors.newCachedThreadPool().submit(() -> {
            final List<Room> rooms = new ArrayList<>();

            try (Playwright playwright = Playwright.create()) {
                final Browser browser = playwright.chromium().launch(new BrowserType.LaunchOptions().setHeadless(true));

                final Page page = browser.newPage();
                page.navigate("https://wordbomb.io");
                page.waitForSelector(".lobby-list-item");

                for (ElementHandle el : page.querySelectorAll(".lobby-list-item")) {
                    final Room room = new Room();

                    final boolean ranked = el.getAttribute("data-room-id").contains("ranked");
                    if (ranked) {
                        System.out.println("Ranked room, skipping...");
                        continue;
                    }

                    final String name = el.querySelector(".lobby-list-name").innerText().trim();
                    final String avatarUrl = el.querySelector(".lobby-list-avatar").getAttribute("src").trim();
                    final String roomId = el.getAttribute("data-room-id");

                    //roles
                    List<ElementHandle> roles = el.querySelectorAll(".lobby-list-role");

                    String playerCount = "";
                    String gameMode = "";
                    boolean started = false;
                    boolean multiLang = false;

                    for (ElementHandle role : roles) {
                        String roleText = role.innerText().trim();

                        if (roleText.matches("\\d+/\\d+")) {
                            playerCount = roleText;
                        } else if (roleText.equalsIgnoreCase("Started")) {
                            started = true;
                        } else if (roleText.contains("Multi Language")) {
                            multiLang = true;
                        }
                        else if (!roleText.isEmpty()) {
                            gameMode = roleText;
                        }
                    }

                    // Set metadata
                    room.setRoomId(roomId);
                    room.setRoomName(name);
                    room.setUserAvatar(avatarUrl);
                    room.setPlayerCount(playerCount);
                    room.setStarted(started);
                    room.setMultiLang(multiLang);
                    room.setGameMode(gameMode);

                    rooms.add(room);
                }
            }

            roomAsync.complete(rooms);
        });


        return roomAsync;
    }
}
