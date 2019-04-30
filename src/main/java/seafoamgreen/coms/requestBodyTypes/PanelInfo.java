package seafoamgreen.coms.requestBodyTypes;

public class PanelInfo {
    private String userID;
    private String comicID;
    private String snapshotJSON;

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
}
