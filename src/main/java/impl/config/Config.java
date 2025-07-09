package impl.config;

import com.google.gson.annotations.SerializedName;

public class Config {
    @SerializedName("token")
    public String token;

    @SerializedName("rounds")
    public int rounds;

    @SerializedName("games")
    public int games;

    @SerializedName("wpm")
    public int wpm;

    @SerializedName("typo")
    public int typo;
}