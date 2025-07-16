package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCardLinkRq {

    @Expose
    @SerializedName("description")
    public String description;

    @Expose
    @SerializedName("url")
    public String url;
}
