package seafoamgreen.coms.controllers;



import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Panel;
import seafoamgreen.coms.services.ComicService;
import seafoamgreen.coms.services.PanelService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Enumeration;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
public class PanelController {

    @Autowired
    PanelService panelService;

    @Autowired
    ComicService comicService;


    /*
    @GetMapping
    public Panel panelFromID(HttpServletRequest request, HttpServletResponse response) throws IOException {

        request.getParameter("panelID")

    }
    */

    @GetMapping("/create")
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mav = new ModelAndView("createComic");
        return mav;
    }

    @PostMapping("/savePanel")
    public String savePanel(HttpServletRequest request)
    {
        HttpSession session = request.getSession();
        Enumeration params = request.getParameterNames();
        String img = (String)request.getParameter("image");

        String currentUser =(String) session.getAttribute("username");
        while(params.hasMoreElements()){
            String paramName = (String)params.nextElement();
            System.out.println(paramName + " = " + request.getParameter(paramName));
        }

        System.out.println("current series : " + request.getSession().getAttribute("currentSeries"));
        System.out.println("current comic id : " + request.getSession().getAttribute("currentComicId"));

        //Create the panel in the DB, add it to the comic
        String currentComicId = (String)session.getAttribute("currentComicId");
        Panel panel = panelService.create(currentUser, currentComicId, request.getParameter("body"), request.getParameter("image"));

        comicService.addPanel(currentComicId, panel.getId());
        //Update the Session Comic
        session.setAttribute("currentComic", comicService.findById(currentComicId));
        return "save triggered";
    }


    /*



    @PostMapping("/create")
    public Panel create(HttpServletRequest request, HttpServletResponse response, @RequestBody PanelInfo panelInfo) throws IOException {


        String userID = panelInfo.getUserID();
        String comicID = panelInfo.getComicID();
        String snapshotJSON = panelInfo.getSnapshotJSON();
        Panel newPanel = new Panel(userID, comicID, snapshotJSON);
        return panelService.create(userID, comicID, snapshotJSON);
        //return panelService.create(userID, comicID, snapshotJSON);

    };

    @GetMapping("/deletePanel")
    public void delete(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String panelID = request.getParameter("panelID");
        panelService.deleteById(panelID);


        //return panelService.create(userID, comicID, snapshotJSON);

    };

    @GetMapping("/getAllByComicID")
    public List<Panel> getByComicID(HttpServletRequest request, HttpServletResponse response) throws IOException {



        String comicID = request.getParameter("comicID");
        return panelService.getByComicID(comicID);


        //return panelService.create(userID, comicID, snapshotJSON);

    };

    @GetMapping("/getAllByUserID")
    public List<Panel> getByUserID(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String userID = request.getParameter("userID");
        return panelService.getByUserID(userID);


        //return panelService.create(userID, comicID, snapshotJSON);

    };
    */

}


