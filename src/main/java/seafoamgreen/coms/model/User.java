package seafoamgreen.coms.model;

import java.util.ArrayList;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class User {

    @Id
    private String id;
    private String username;
    private String password;

    private ArrayList<String> messagesSentIds = new ArrayList<String>();
    private ArrayList<String> messagesReceivedIds= new ArrayList<String>();

    private boolean isAdmin;

    public User() {
    }

    /**
     * @return the isAdmin
     */
    public boolean isAdmin() {
        return isAdmin;
    }

    /**
     * @param isAdmin the isAdmin to set
     */
    public void setAdmin(boolean isAdmin) {
        this.isAdmin = isAdmin;
    }

    /**
     * @return the messagesRecieved
     */
    public ArrayList<String> getMessagesReceived() {
        return messagesReceivedIds;
    }

    /**
     * @param messagesRecieved the messagesRecieved to set
     */
    public void setMessagesRecieved(ArrayList<String> messagesRecieved) {
        this.messagesReceivedIds = messagesRecieved;
    }



    /**
     * @return the messages
     */
    public ArrayList<String> getMessagesSent() {
        return messagesSentIds;
    }

    /**
     * @param messages the messages to set
     */
    public void setMessagesSentIds(ArrayList<String> messages) {
        this.messagesSentIds = messages;
    }

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, firstName='%s', lastName='%s']",
                id, username, password);
    }
}