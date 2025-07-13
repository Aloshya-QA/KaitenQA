package api.kaiten.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Column {

    @Expose
    @SerializedName("title")
    public String title;
}
