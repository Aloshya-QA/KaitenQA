package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AddCardCoverRq {

    @Expose
    @SerializedName("card_cover")
    public boolean cover;
}
