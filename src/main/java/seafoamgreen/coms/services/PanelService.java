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
    public Panel create(String Username, String comicID, String fabricJSON, String blob)
    {
        Panel panel = new Panel(Username, comicID, fabricJSON, blob);
        panelRepository.save(panel);
        return panel;
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



