package seafoamgreen.coms.model;



import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
public class UserReport {

    @Id
    private String id;

    private String reportedUsername;

    private String reportMessage;

    private String reporterUsername;

    /**
     * @return the reportedUsername
     */
    public String getReportedUsername() {
        return reportedUsername;
    }

    /**
     * @return the reportMessage
     */
    public String getReportMessage() {
        return reportMessage;
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

    /**
     * @param reportedUsername the reportedUsername to set
     */
    public void setReportedUsername(String reportedUsername) {
        this.reportedUsername = reportedUsername;
    }

}