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
        final byte[] msg = bytes.array();
        final int packetId = msg[0] & 0xFF;

        try {
            switch (packetId) {
                case 10 -> {
                    System.out.println("[JOIN_ROOM] Received join confirmation.");
                    client.sendChat("Joined game ! ");

                    client.signalConnectionConfirmed();
                    send(new byte[]{10});
                }
                case 13 -> {
                    // Room data
                    handleRoomData(msg);
                }
                case 14 -> {
                    // Room state
                }
                case 15 -> {
                    // Room state (2)
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleRoomData(byte[] msg) {
        final MsgPackParser parser = new MsgPackParser();
        final Object event = parser.parse(msg, 1);

        Object payload = null;
        if (parser.getBufferPosition() < msg.length) {
            payload = parser.parse(msg, parser.getBufferPosition());
        }

        if (!(event instanceof String))
            return;

        // Events
        final String eventMessage = (String) event;
        switch (eventMessage) {
            case "eliminated" -> {
                /* */
            }
            case "start" -> {
                // Called : after countdown event
                // Payload :
            }
            case "wrong" -> {

            }
            case "correct" -> {

            }
            case "turn" -> {

            }
            case "winner" -> {

            }
            case "chat" -> {

            }
            case "hurt" -> {

            }
            case "user-letter-healths" -> {
                //
            }
            case "countdown" -> {
                // Called : When starting the game
                // Payload : time until game starts
            }
            case "dc" -> {
                // disconnection
            }
            default -> {
                // For debugging and finding useful events
                System.out.printf("[ROOM_DATA] Unknown Event: %s, Payload: %s\n", event, payload);
            }
        }

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
