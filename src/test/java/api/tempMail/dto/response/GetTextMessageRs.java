package api.tempMail.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetTextMessageRs {

    @SerializedName("text")
    @Expose
    public String text;
}
