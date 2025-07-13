package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UpdateCardRq {

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
