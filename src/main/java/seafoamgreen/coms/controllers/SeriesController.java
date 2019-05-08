package seafoamgreen.coms.controllers;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Panel;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.services.ComicService;
import seafoamgreen.coms.services.PanelService;
import seafoamgreen.coms.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.*;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("series")
public class SeriesController {

    @Autowired
    SeriesService seriesService;

    @Autowired
    ComicService comicService;

    @Autowired
    PanelService panelService;

    @PostMapping("/delete")
    public ModelAndView deleteSeries(HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView("myComics");
        String seriesId = (String)request.getParameter("seriesId");
        seriesService.deleteById(seriesId);


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


    @PostMapping("/create")
    public ModelAndView addNewSeries(HttpServletRequest request, HttpServletResponse response)
    {


        ModelAndView mav = new ModelAndView("myComics");
        HttpSession session = request.getSession();
        String activeUsername = (String)session.getAttribute("username");
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);


        String username = (String)session.getAttribute("username");
        String seriesName = request.getParameter("seriesName");

        seriesService.create(seriesName, username);

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

    @GetMapping ("/mySeries")
    public ModelAndView viewMySeries(HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView("myComics");
        HttpSession session = request.getSession();

        String activeUsername = (String)session.getAttribute("username");
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);


        String username = (String)session.getAttribute("username");


        List<Series> seriesList = seriesService.findAllByUsername(username);
        Map<Series, List<Comic>> map = new HashMap<Series, List<Comic>>();

        for(Series series : seriesList)
        {
            map.put(series, comicService.findAllBySeriesId(series.getId()));
        }
        //Get all of users series

        mav.addObject("seriesMap", map);
        mav.addObject("username", username);

        System.out.println(map);

        //Map each series to a list of comics

        return mav;
    }

    /*
        @PostMapping("/create")
        public ModelAndView createSeries(HttpServletRequest request, HttpServletResponse response) throws IOException
        {

            HttpSession session = request.getSession();

            String seriesName = request.getParameter("series");
            String existingSeriesId = request.getParameter("existingSeries");
            String comicName = request.getParameter("comic");
            String tags = request.getParameter("tags");
            String publishDate = request.getParameter("publishDate");

            String currentUser = (String)session.getAttribute("username");
            System.out.println(currentUser);
            System.out.println(seriesName);
            System.out.println(existingSeriesId);
            System.out.println(comicName);
            System.out.println(tags);
            System.out.println(publishDate);

            request.getSession().setAttribute("currentSeriesName", seriesName);
            request.getSession().setAttribute("currentComicName", comicName);

            Series currentSeries;
            //Initialize the new Series and Comic Issue inside the DB
            if(existingSeriesId == null) {
                currentSeries = seriesService.create(seriesName, currentUser);

            }
            else
            {
                currentSeries = seriesService.findByID(existingSeriesId).get();

            }
            Comic newComic = comicService.create(currentUser, comicName, currentSeries.getId(), tags, publishDate);
            seriesService.addComic(currentSeries.getId(), newComic.getId());
            currentSeries = seriesService.findByID(currentSeries.getId()).get();

            //Get the fully initialized objects from the DB and then set it into the session before going to create comic page
            request.getSession().setAttribute("currentSeries", currentSeries);
            request.getSession().setAttribute("currentComic", newComic);


            ModelAndView mav = new ModelAndView("createComic");
            mav.addObject("comic", newComic);
            return mav;

        }
        */

    @GetMapping("/getById")
    public Optional<Series> getById(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return seriesService.findByID(request.getParameter("seriesID"));
    }

    @GetMapping("/getByUserId")
    public List<Series> getByUserId(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return seriesService.findByUserId(request.getParameter("userId"));
    }

    @GetMapping("/username")
    public List<Series> getByUsername(HttpServletRequest request, HttpServletResponse response) throws IOException
    {
        return seriesService.findAllByUsername(request.getParameter("username"));
    }


}