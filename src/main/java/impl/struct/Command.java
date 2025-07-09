package impl.struct;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;

@Getter
public class Command {
    @SerializedName("command")
    private int command;

    @SerializedName("message")
    private String message;

    @Override
    public String toString() {
        return "Command{" + "command=" + command + ", message='" + message + '\'' + '}';
    }
}