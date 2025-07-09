package impl.network;

import impl.game.Client;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.nio.ByteBuffer;
import java.util.Map;

public class SocketClient extends WebSocketClient {
    private final Client client;

    public SocketClient(URI serverUri, Map<String, String> httpHeaders, Client client) {
        super(serverUri, httpHeaders);
        this.client = client;
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("WebSocket connection opened.");
    }

    @Override
    public void onMessage(String message) {
        // To debug
        // TODO : remove
        System.out.println("Received string message: " + message);
    }

    @Override
    public void onMessage(ByteBuffer bytes) {
        /* */
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        /* */
    }

    @Override
    public void onError(Exception e) {
        /* */
    }


}
