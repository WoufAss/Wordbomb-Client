package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

import java.time.Instant;

@Getter
public class Room {
    @SerializedName("roomId")
    private String roomId;
    @SerializedName("metadata")
    private Metadata metadata;
    @SerializedName("createdAt")
    private String createdAt;
    @SerializedName("clients")
    private int clients;
    @SerializedName("locked")
    private boolean locked;
    @SerializedName("private")
    private boolean isPrivate;

    public Room(String roomId) {
        this.roomId = roomId;
    }

    public Room() {}
}