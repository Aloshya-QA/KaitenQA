package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCardTagsRs {

    @Expose
    @SerializedName("name")
    public String name;
}
