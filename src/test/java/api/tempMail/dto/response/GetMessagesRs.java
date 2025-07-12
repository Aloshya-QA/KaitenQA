package api.tempMail.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

@JsonIgnoreProperties(ignoreUnknown = true)
public class GetMessagesRs {

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
