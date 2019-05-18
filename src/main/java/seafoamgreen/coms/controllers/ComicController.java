package seafoamgreen.coms.controllers;


import seafoamgreen.coms.model.*;
import seafoamgreen.coms.services.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("comic")
public class ComicController {

    String comicViewName = "comicView";


    @Autowired
    ComicService comicService;

    @Autowired
    PanelService panelService;

    @Autowired
    SeriesService seriesService;

    @Autowired
    UserService userService;

    @Autowired
    InteractionService interactionService;


    @PostMapping("/create")
    public void addNewComic(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mav = new ModelAndView("myComics");
        HttpSession session = request.getSession();


        String activeUsername = (String)session.getAttribute("username");
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);


        String username = (String)session.getAttribute("username");
        String seriesId= request.getParameter("seriesId");
        String comicName = request.getParameter("comicName");
        String tagString = request.getParameter("tags");
        String publishDate = request.getParameter("publishDate");
        Comic comic = comicService.create(username, comicName, seriesId, tagString , publishDate);


        Series series = seriesService.findByID(seriesId).get();
        seriesService.addComic(series.getId(), comic.getId());

        List<Series> seriesList = seriesService.findAllByUsername(username);
        Map<Series, List<Comic>> map = new HashMap<Series, List<Comic>>();

        for(Series seriesElement : seriesList)
        {
            map.put(seriesElement, comicService.findAllBySeriesId(series.getId()));
        }
        //Get all of users series

        //Map each series to a list of comics
        try {
            response.sendRedirect("/series/mySeries");
        }
        catch(Exception e) {

        }

    }

    @PostMapping("/edit")
    public ModelAndView editComic(HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView("createComic");
        String comicId = request.getParameter("editComicId");
        if(comicId != null) {
            request.getSession().setAttribute("currentComicId", comicId);
        }
        else {
            comicId = (String)request.getSession().getAttribute("currentComicId");
        }

        String editPanelId = request.getParameter("editPanelId");

        if(editPanelId == null)
        {
            List<Panel> panelList= panelService.findAllByCoimcId(comicId);
            if(panelList.size() > 0 )
            {
                Panel currentPanel = panelService.findAllByCoimcId(comicId).get(0);
                HttpSession session = request.getSession();
                session.setAttribute("currentPanelId", currentPanel.getId());
                mav.addObject("currentPanel", currentPanel);
            }

        }
        else{
            HttpSession session = request.getSession();
            session.setAttribute("currentPanelId", editPanelId);
            Panel currentPanel = panelService.findById(editPanelId);
            mav.addObject("currentPanel", currentPanel);


        }
        //add panel list
        List<Panel> panelList = panelService.findAllByCoimcId(comicId);
        mav.addObject("panelList", panelList);
        return mav;
    }

    @GetMapping("/delete")
    public ModelAndView deleteComic(HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView("myComics");
        String comicId = (String)request.getParameter("comicId");
        comicService.deleteById(comicId);

        HttpSession session = request.getSession();

        String activeUsername = (String)session.getAttribute("username");
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);


        String username = (String)session.getAttribute("username");
        List<Series> seriesList = seriesService.findAllByUsername(username);

        //TODO: ADD SORT

        Map<Series, List<Comic>> map = new HashMap<Series, List<Comic>>();

        for(Series series : seriesList)
        {
            map.put(series, comicService.findAllBySeriesId(series.getId()));
        }
        //Get all of users series

        mav.addObject("seriesMap", map);

        System.out.println(map);

        //Map each series to a list of comics

        return mav;
    }

    @GetMapping("/view/{comicID}")
    public ModelAndView viewComic(HttpServletRequest request, HttpServletResponse response, @PathVariable String comicID) {
        ModelAndView mav = new ModelAndView("viewComic");
        Comic c =  comicService.findById(comicID);
        System.out.println("VIEWING COMIC: " + c);
        List<Panel> panelList = panelService.findAllByCoimcId(c.getId());
        //add to history

        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                comicService.addToHistory(username, comicID);
            }
        }
        String activeUsername = (String)session.getAttribute("username");
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);

        mav.addObject("comic", c);
        List<String> panels = new ArrayList<>();
        for(Panel panel : panelList)
        {
            panels.add(panelService.getBlob(panel.getId()));
        }
        mav.addObject("panels", panels);

        String seriesId = c.getSeriesID();
        Series series = seriesService.findByID(seriesId).get();
        if(series.getSubscriberList().contains(activeUsername))
        {
            mav.addObject("subscribeType", "Unsubscribe");
        }
        else
        {
            mav.addObject("subscribeType", "Subscribe");
        }
        mav.addObject("comments", comicService.getCommentsForComicid(comicID));
        mav.addObject("username", activeUsername);

        User user = userService.findByUsername(activeUsername);
        if(user.getSubscriptions().contains(c.getSeriesID()))
        {
            mav.addObject("isSubscribed", true);
        }
        else
        {
            mav.addObject("isSubscribed",false);
        }

        List<Comment> comments = interactionService.getCommentsForComicid(comicID);
        System.out.println(comments);
        mav.addObject("comments", comments);

        return mav;

    }


    @GetMapping("/view/thumbnail")
    public ModelAndView viewComicThumbnail(HttpServletRequest request, HttpServletResponse response) {
        String comicID = request.getParameter("comidID");
        ModelAndView mav = new ModelAndView("comicView");
        Comic c =  comicService.findById(comicID);
        List<String> blobs = comicService.getPanelObjects(c);
        mav.addObject("comic", c);
        mav.addObject("thumbnail", blobs.get(0));
        return mav;

    }

    @GetMapping("/findById")
    public Comic findById(HttpServletRequest request, HttpServletResponse response )throws IOException {


        String comicID = request.getParameter("comidID");
        return comicService.findById(comicID);

    };

    @GetMapping("/deleteComicByID")
    public void deleteByID(HttpServletRequest request, HttpServletResponse response) throws IOException {


        String comicID = request.getParameter("comidID");
        comicService.deleteById(comicID);

    };

    @GetMapping("/random")
    public ModelAndView randomComic(HttpServletRequest request, HttpServletResponse response) {
        Comic comic = comicService.getRandomComic();
        //add to history
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                comicService.addToHistory(username, comic.getId());
            }
        }

        ModelAndView mav = new ModelAndView(comicViewName);
        mav.addObject("comic", comic);

        return mav;
    }



    @PostMapping("/addTags")
    public ModelAndView addTag(HttpServletRequest request, HttpServletResponse response) {
        String tags = request.getParameter("tags");
        String comicId = request.getParameter("comicId");
        comicService.addTags(comicId, tags);


        ModelAndView mav = new ModelAndView(comicViewName);

        Comic comic = comicService.findById(comicId);
        mav.addObject("comic", comic);
        return mav;

    }


    @GetMapping("/searchByTag")
    public List<Comic> searchByTag(HttpServletRequest request, HttpServletResponse response) {
        String tag = request.getParameter("tag");
        return comicService.findAllByTag(tag);

    }

    @PostMapping("/publish")
    public ModelAndView publishComic(HttpServletRequest request, HttpServletResponse response) {
        //what does this return?? I guess a view
        String comicId = request.getParameter("comicId");
        String publishDate = request.getParameter("publishDate");


        // request.getParameter("panelList");
        //Date posttime = request.getParameter("postTime"); still gotta figure this out for rn
        Comic c = null;
        if (publishDate != null) {
            c = comicService.publishComic(comicId, publishDate);
            System.out.println("publishing comic");
        } else {
            c = comicService.publishComic(comicId, true);
            System.out.println("publishing comic");
        }




        //returns the view of the comic
        //we should have 2 different comic views - 1 if u created it, one if you didnt
        ModelAndView mav = new ModelAndView("myComics");

        return new ModelAndView( "redirect:/series/mySeries");

    }


    @GetMapping("/editable")
    public ModelAndView getEditableComics(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            //TODO: redirect to login page
            return null;
        }

        String username = (String) session.getAttribute("username");
        List<Comic> comics = comicService.getEditableComicsByUsername(username);

        //TODO: change mav reference
        ModelAndView mav = new ModelAndView("AllEditable");
        mav.addObject("comics", comics);
        return mav;
    }

    @GetMapping("/edit")
    public ModelAndView getComicForEdit(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) {
            //TODO: redirect to login page
            return null;
        }
        String comicId = (String) request.getParameter("comicId");

        String username = (String) session.getAttribute("username");


        Comic comic = comicService.getEditComic(username, comicId);
        if (comic == null) {
            //TODO: throw error?
            return null;
        }
        //TODO: possibly add comic to session?

        //TODO: change mav ref
        ModelAndView mav = new ModelAndView("ComicEdit");
        mav.addObject("comic", comic);

        return mav;

    }

    @GetMapping("/{genre}")
    public ModelAndView comicGenre(HttpServletRequest request, HttpServletResponse response, @PathVariable String genre) {
        ModelAndView mav = new ModelAndView("genreComics");
        List<Comic> comics = comicService.findAllByTag(genre);

        HttpSession session = request.getSession(false);

        String activeUsername = (String)session.getAttribute("username");
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);

        mav.addObject("genre", genre + ' ');
        mav.addObject("comicList",comics);
        mav.addObject("username",activeUsername);

        return mav;
    }
}