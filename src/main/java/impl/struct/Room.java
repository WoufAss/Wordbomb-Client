package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class Room {
    @SerializedName("roomName")
    private String roomName;
    @SerializedName("roomId")
    private String roomId;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("clients")
    private int clients;
    @SerializedName("playerCount")
    private String playerCount;
    @SerializedName("avatar")
    private String userAvatar;
    @SerializedName("started")
    private boolean started;
    @SerializedName("multiLang")
    private boolean multiLang;
    @SerializedName("gamemode")
    private String gameMode;

    public Room(String roomId) {
        this.roomId = roomId;
    }

    public Room() {}
}