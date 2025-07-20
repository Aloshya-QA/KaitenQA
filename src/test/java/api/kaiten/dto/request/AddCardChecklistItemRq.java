package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCardChecklistItemRq {

    @Expose
    @SerializedName("text")
    public String text;

    @Expose
    @SerializedName("checked")
    public boolean checked;
}
