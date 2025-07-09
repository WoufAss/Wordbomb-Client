package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

public class SessionInfo {
    @SerializedName("room")
    @Getter
    private RoomInfo room;
    @SerializedName("sessionId")
    @Getter
    private String sessionId;
}
