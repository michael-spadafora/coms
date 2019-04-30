package seafoamgreen.coms.services;


import seafoamgreen.coms.model.Panel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import seafoamgreen.coms.repositories.PanelRepository;

import java.util.List;


@Service
public class PanelService {

    @Autowired
    private PanelRepository panelRepository;

    //Create
    public Panel create(String UserID, String comicID, String snapshotJSON)
    {
        Panel panel = new Panel(UserID, comicID, snapshotJSON);
        panelRepository.save(panel);
        return panel;
    }


    //Retrieve
    public Panel findBysnapshotJSON(String JSON)
    {
        return panelRepository.findBysnapshotJSON(JSON);
    }

    public List<Panel> getAllPanels()
    {
        return panelRepository.findAll();
    }

    public List<Panel> getByComicID(String comicID){ return panelRepository.findByComicID(comicID);}

    public List<Panel> getByUserID(String userID){ return panelRepository.findByUserID(userID);}
    //Update
    public Panel update(String snapshotJSON, String newJSON) {
        Panel panel = panelRepository.findBysnapshotJSON(newJSON);
        panel.setSnapshotJSON(newJSON);
        return panelRepository.save(panel);
    }



    //Delete

    public void deleteById(String panelID)
    {

        panelRepository.deleteById(panelID);

    }
    public void deleteAll() { panelRepository.deleteAll(); }





}



