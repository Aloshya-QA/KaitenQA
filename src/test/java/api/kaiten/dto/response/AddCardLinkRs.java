package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCardLinkRs {

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("url")
    public String url;
}
