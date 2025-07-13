package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class AddCardTagsRq {

    @Expose
    @SerializedName("name")
    public String name;
}
