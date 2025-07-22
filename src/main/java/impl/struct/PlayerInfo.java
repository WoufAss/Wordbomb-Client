package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class PlayerInfo {
    @SerializedName("auth_id")
    private String authId;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("type")
    private int type;
    @SerializedName("wp")
    private int wp;
}