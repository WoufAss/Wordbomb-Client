package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class RoomInfo {
    @SerializedName("roomId")
    private String roomId;
    @SerializedName("processId")
    private String processId;
}
