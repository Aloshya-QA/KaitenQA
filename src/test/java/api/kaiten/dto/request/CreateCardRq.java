package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateCardRq {

    @Expose
    @SerializedName("board_id")
    public int boardId;

    @Expose
    @SerializedName("title")
    public String title;
}
