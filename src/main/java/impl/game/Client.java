package impl.game;

import com.google.gson.Gson;
import impl.network.SocketClient;
import impl.struct.SessionInfo;
import okhttp3.*;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class Client {
    private final String authToken;
    private final OkHttpClient httpClient;
    //private final SocketClient socketClient;

    //Game infos
    private String roomID;
    private String sessionID;
    private String processID;


    public Client(final String token){
        this.authToken = token;
        //this.socketClient = new SocketClient(this);
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
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
}
