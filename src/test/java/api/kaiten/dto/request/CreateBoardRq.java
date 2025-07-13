package api.kaiten.dto.request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class CreateBoardRq {

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("columns")
    public List<Column> columns;

    @Expose
    @SerializedName("lanes")
    public List<Lane> lanes;
}
