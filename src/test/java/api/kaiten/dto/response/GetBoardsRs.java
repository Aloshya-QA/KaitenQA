package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetBoardsRs {

    @Expose
    @SerializedName("id")
    public int id;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("columns")
    public List<Column> columns;

}
