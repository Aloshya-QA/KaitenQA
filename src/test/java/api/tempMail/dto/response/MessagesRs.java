package api.tempMail.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MessagesRs {

    @SerializedName("id")
    @Expose
    public String id;

    @SerializedName("subject")
    @Expose
    public String subject;

    @SerializedName("createdAt")
    @Expose
    public String createdAt;

}
