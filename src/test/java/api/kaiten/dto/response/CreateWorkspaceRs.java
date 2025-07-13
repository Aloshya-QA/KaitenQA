package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CreateWorkspaceRs {

    @SerializedName("created")
    @Expose
    public String created;

    @SerializedName("id")
    @Expose
    public int id;

    @SerializedName("title")
    @Expose
    public String title;
}
