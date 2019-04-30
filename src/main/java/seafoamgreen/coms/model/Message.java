package seafoamgreen.coms.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;


@Document
public class Message {

    //Mongo generated ID. Should not have a setter only getter
    @Id
    private String id;
    //Owner of comic
    @Field
    private String messageTitle;
    // Name of comic
    @Field
    private String messageBody;
    // Series that comic belongs to
    @Field
    private String fromUsername;
    // List of Panels within the comic
    @Field
    private String toUsername;

    @Field
    private boolean read;

    public Message(String messageTitle, String messageBody, String fromUsername, String toUsername, boolean read) {
        this.messageTitle = messageTitle;
        this.messageBody = messageBody;
        this.fromUsername = fromUsername;
        this.toUsername = toUsername;
        this.read = read;
    }

    /**
     * @return the read
     */
    public boolean isRead() {
        return read;
    }

    /**
     * @param read the read to set
     */
    public void setRead(boolean read) {
        this.read = read;
    }

    /**
     * @return the toUser
     */
    public String getToUsername() {
        return toUsername;
    }

    /**
     * @param toUser the toUser to set
     */
    public void setToUsername(String toUser) {
        this.toUsername = toUser;
    }

    /**
     * @return the fromUser
     */
    public String getFromUsername() {
        return fromUsername;
    }

    /**
     * @param fromUser the fromUser to set
     */
    public void setFromUsername(String fromUser) {
        this.fromUsername = fromUser;
    }

    /**
     * @return the messageBody
     */
    public String getMessageBody() {
        return messageBody;
    }

    /**
     * @param messageBody the messageBody to set
     */
    public void setMessageBody(String messageBody) {
        this.messageBody = messageBody;
    }

    /**
     * @return the messageTitle
     */
    public String getMessageTitle() {
        return messageTitle;
    }

    /**
     * @param messageTitle the messageTitle to set
     */
    public void setMessageTitle(String messageTitle) {
        this.messageTitle = messageTitle;
    }

    public String getId(){
        return id;
    }

    public void setID(String id){
        this.id = id;
    }




}