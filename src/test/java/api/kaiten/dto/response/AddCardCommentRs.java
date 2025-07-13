package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class AddCardCommentRs {

    @Expose
    @SerializedName("text")
    public String text;
}
