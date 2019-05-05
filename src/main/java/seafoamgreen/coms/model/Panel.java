package seafoamgreen.coms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Panel {

    //Mongo generated ID. Should not have a setter only getter
    @Id
    private String id;
    //User that owns this comic
    private String username;
    //Comic in which panel belongs to
    private String comicID;
    //JSON data of the canvas
    private String fabricJSON;
    //BLOB
    private String AWSURL;

    public Panel(String username, String comicID, String fabricJSON) {
        this.username = username;
        this.comicID = comicID;
        this.fabricJSON = fabricJSON;
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

    public String getComicID() {
        return comicID;
    }

    public void setComicID(String comicID) {
        this.comicID = comicID;
    }

    public String getFabricJSON() {
        return fabricJSON;
    }

    public void setFabricJSON(String fabricJSON) {
        this.fabricJSON = fabricJSON;
    }

    public String getAWSURL() {
        return AWSURL;
    }

    public void setAWSURL(String AWSURL) {
        this.AWSURL = AWSURL;
    }



    @Override
    public String toString() {
        return "Panel{" +
                "id='" + id + '\'' +
                ", username='" + username + '\'' +
                ", comicID='" + comicID + '\'' +
                ", fabricJSON='" + fabricJSON + '\'' +
                ", AWSURL='" + AWSURL + '\'' +
                '}';
    }
}

