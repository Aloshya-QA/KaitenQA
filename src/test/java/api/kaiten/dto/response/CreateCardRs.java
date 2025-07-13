package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateCardRs {

    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("title")
    public String title;
}
