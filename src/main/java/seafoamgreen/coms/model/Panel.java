package seafoamgreen.coms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class Panel {

    //Mongo generated ID. Should not have a setter only getter
    @Id
    private String id;
    //User that owns this comic
    private String userID;
    //Comic in which panel belongs to
    private String comicID;
    //JSON data of the canvas
    private String snapshotJSON;

    public Panel(String userID, String comicID, String snapshotJSON) {
        this.userID = userID;
        this.comicID = comicID;
        this.snapshotJSON = snapshotJSON;
    }

    public String getId() {
        return id;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getComicID() {
        return comicID;
    }

    public void setComicID(String comicID) {
        this.comicID = comicID;
    }

    public String getSnapshotJSON() {
        return snapshotJSON;
    }

    public void setSnapshotJSON(String snapshotJSON) {
        this.snapshotJSON = snapshotJSON;
    }

    @Override
    public String toString() {
        return "Panel{" +
                "id='" + id + '\'' +
                ", userID='" + userID + '\'' +
                ", comicID='" + comicID + '\'' +
                ", snapshotJSON='" + snapshotJSON + '\'' +
                '}';
    }
}