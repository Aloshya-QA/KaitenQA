package api.tempMail.dto.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetMessagesRs {

    @SerializedName("hydra:member")
    @Expose
    public List<Message> messages;

}
