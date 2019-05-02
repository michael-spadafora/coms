package seafoamgreen.coms.services;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Panel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.repositories.ComicRepository;
import seafoamgreen.coms.repositories.PanelRepository;

import java.io.InputStream;
import java.io.StringWriter;
import java.net.URL;

import java.util.List;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;

@Service
public class PanelService {

    @Autowired
    private PanelRepository panelRepository;

    @Autowired
    private ComicRepository comicRepository;

    // Create
    public Panel create(String Username, String comicID, String fabricJSON, String blob) {

        Panel panel = new Panel(Username, comicID, fabricJSON);
        panelRepository.save(panel);

        String URL = storeBlobInAWS(panel, blob);
        panel.setAWSURL(URL);
        panelRepository.save(panel);

        return panel;
    }

    private String storeBlobInAWS(Panel panel, String blob) {
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJIKZPRZSWRVS6SLQ",
                "2IZ4gI/pxi8L82qeIWFl2txPIE1eslMxdbrHpYjq ");

        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();

        String bucketName = "coms.com-comics";
        String comicId = panel.getComicID();
        Comic comic = comicRepository.findByComicId(comicId);
        String seriesId = comic.getSeriesID();
        String panelId = panel.getId();
        // String key = "key";
        // key = seriesId/comicId/panelNumber
        String key = seriesId + "/" + comicId + "/" + panelId;

        s3client.putObject(bucketName, key, blob); // saves
        String urlstring = s3client.getUrl(bucketName, key).toString();

        return urlstring;
    }

    public String getBlob(String panelId) {
        AWSCredentials credentials = new BasicAWSCredentials("AKIAJIKZPRZSWRVS6SLQ",
        "2IZ4gI/pxi8L82qeIWFl2txPIE1eslMxdbrHpYjq ");
        
        AmazonS3 s3client = AmazonS3ClientBuilder.standard()
        .withCredentials(new AWSStaticCredentialsProvider(credentials)).withRegion(Regions.US_EAST_2).build();
        
        Panel panel = panelRepository.findById(panelId).get();
        String bucketName = "coms.com-comics";
        String comicId = panel.getComicID();
        Comic comic = comicRepository.findByComicId(comicId);
        String seriesId = comic.getSeriesID();
        String key = seriesId + "/" + comicId + "/" + panelId;

        // String key = "5cca2508519e228d91fd3bfa/5cca2508519e228d91fd3bfb/5cca250b519e228d91fd3bfc";

        S3Object obj = s3client.getObject(bucketName, key);

        S3ObjectInputStream s = obj.getObjectContent();
        InputStream stream = s.getDelegateStream();

        String ret = convertStreamToString(stream);
        return ret;
    }

    static String convertStreamToString(java.io.InputStream is) {
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

    public List<Panel> getAllPanels()
    {
        return panelRepository.findAll();
    }

    public List<Panel> getByComicID(String comicID){ return panelRepository.findByComicID(comicID);}

    public List<Panel> getByUserID(String userID){ return panelRepository.findByUserID(userID);}
    //Update


    //Delete

    public void deleteById(String panelID)
    {

        panelRepository.deleteById(panelID);

    }
    public void deleteAll() { panelRepository.deleteAll(); }





}



