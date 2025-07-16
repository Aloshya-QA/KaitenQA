package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateCardRs {

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("asap")
    public boolean asap;

    @Expose
    @SerializedName("description")
    public String description;
}
