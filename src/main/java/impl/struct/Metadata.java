package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Metadata {
    // Host
    @SerializedName("auth_id")
    private String authId;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("type")
    private int type;

    // Room misc
    @SerializedName("started")
    private boolean started;
    @SerializedName("ranked")
    private boolean ranked;
    @SerializedName("diff")
    private int diff;
    @SerializedName("mode")
    private int mode;
    @SerializedName("sc")
    private int sc;
    @SerializedName("language")
    private String language;
    @SerializedName("mlang")
    private boolean mlang;
    @SerializedName("wpp")
    private int wpp;

    // Ranked-specific player info
    @SerializedName("player1")
    private PlayerInfo player1;
    @SerializedName("player2")
    private PlayerInfo player2;

}