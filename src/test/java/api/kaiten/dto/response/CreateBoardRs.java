package api.kaiten.dto.response;

import api.kaiten.dto.request.Lane;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CreateBoardRs {

    @Expose
    @SerializedName("created")
    public String created;

    @Expose
    @SerializedName("title")
    public String title;

    @Expose
    @SerializedName("id")
    public String id;

    @Expose
    @SerializedName("columns")
    public List<Column> columns;

    @Expose
    @SerializedName("lanes")
    public List<Lane> lanes;
}
