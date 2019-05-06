package seafoamgreen.coms.controllers;



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

    @GetMapping("/getBlob")
    public String getPanelBlob(HttpServletRequest request) {
        String panelId = request.getParameter("panelId");
        if (panelId == null) {
            panelId = request.getParameter("panelID");
        }

        return panelService.getBlob(panelId);
    }



    //TODO: HAVE SEPARATE CONTROLLER CALLED ADD PANEL  THAT DOES SOMETHING SIMIALR TO THIS, CREATING A NEW PANEL
    @GetMapping("/addPanel")
    public ModelAndView addPanel(HttpServletRequest request)
    {

        //TODO: IN ORDER TO SAVE, SHOULD BE CALLED BY JAVASCRIPT FUNCTION THYMELEAF SHOULD HAVE PANELID . PANELID SHOULD BE PASSED AND UPDATED IN DB
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

        System.out.println("=====================PANEL ADD DETAILS===================");
        System.out.println("current series : " + request.getSession().getAttribute("currentSeries"));
        System.out.println("current comic id : " + request.getSession().getAttribute("currentComicId"));
        System.out.println("current panel id : " + request.getSession().getAttribute("currentPanelId"));
        System.out.println("=====================PANEL ADD DETAILS===================");
        //Create the panel in the DB, add it to the comic
        String currentComicId = (String)session.getAttribute("currentComicId");
        Panel panel = panelService.initalizeEmptyPanel(currentUser, currentComicId);

        comicService.addPanel(currentComicId, panel.getId());
        //Update the Session Comic
        session.setAttribute("currentComic", comicService.findById(currentComicId));
        session.setAttribute("currentPanelId", panel.getId());
        ModelAndView mav = new ModelAndView("createComic");

        mav.addObject("currentPanel", panel);
        mav.addObject("panelList", panelService.findAllByCoimcId(currentComicId));

        return mav;
    }

    @PostMapping("/savePanel")
    public String savePanel(HttpServletRequest request)
    {

        //TODO: IN ORDER TO SAVE, SHOULD BE CALLED BY JAVASCRIPT FUNCTION THYMELEAF SHOULD HAVE PANELID . PANELID SHOULD BE PASSED AND UPDATED IN DB
        HttpSession session = request.getSession();
        Enumeration params = request.getParameterNames();

        String currentUser =(String) session.getAttribute("username");
        System.out.println("=====================PANEL SAVE DETAILS===================");
        System.out.println("current series : " + request.getSession().getAttribute("currentSeries"));
        System.out.println("current comic id : " + request.getSession().getAttribute("currentComicId"));
        System.out.println("current panel id : " + request.getSession().getAttribute("currentPanelId"));
        System.out.println("=====================PANEL SAVE DETAILS===================");

        // Get the Panel, update all the information
        String currentPanelId = (String)session.getAttribute("currentPanelId");
        String blob = request.getParameter("image");
        String json = request.getParameter("body");

        Panel panel = panelService.update(currentPanelId, json, blob);


        return "save triggered";
    }


}