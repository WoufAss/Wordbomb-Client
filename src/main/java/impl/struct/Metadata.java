package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Metadata {
    @SerializedName("auth_id")
    private String authId;
    @SerializedName("name")
    private String name;

}