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
    private Instant createdAt;

    public Room(String roomId) {
        this.roomId = roomId;
    }
}