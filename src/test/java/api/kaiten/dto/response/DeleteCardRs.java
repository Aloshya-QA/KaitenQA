package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeleteCardRs {

    @Expose
    @SerializedName("condition")
    public int condition;
}
