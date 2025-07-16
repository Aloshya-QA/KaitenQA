package api.tempMail.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Message {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("subject")
    @Expose
    public String subject;

    @SerializedName("updatedAt")
    @Expose
    public String updatedAt;
}
