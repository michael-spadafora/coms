package seafoamgreen.coms.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class ComicReport {

    @Id
    private String id;

    private String reportedComicId;

    private String reportMessage;

    private String reporterUsername;

    /**
     * @return the reportMessage
     */
    public String getReportMessage() {
        return reportMessage;
    }

    /**
     * @return the reportedComicId
     */
    public String getReportedComicId() {
        return reportedComicId;
    }

    /**
     * @param reportedComicId the reportedComicId to set
     */
    public void setReportedComicId(String reportedComicId) {
        this.reportedComicId = reportedComicId;
    }

    /**
     * @param reportMessage the reportMessage to set
     */
    public void setReportMessage(String reportMessage) {
        this.reportMessage = reportMessage;
    }

    /**
     * @return the reporterUsername
     */
    public String getReporterUsername() {
        return reporterUsername;
    }

    /**
     * @param reporterUsername the reporterUsername to set
     */
    public void setReporterUsername(String reporterUsername) {
        this.reporterUsername = reporterUsername;
    }



}
