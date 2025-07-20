package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCardChecklistRq {

    @Expose
    @SerializedName("name")
    public String name;
}
