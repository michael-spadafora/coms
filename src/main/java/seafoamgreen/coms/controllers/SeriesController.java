package seafoamgreen.coms.controllers;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.requestBodyTypes.SeriesBody;
import seafoamgreen.coms.services.ComicService;
import seafoamgreen.coms.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("series")
public class SeriesController {

    @Autowired
    SeriesService seriesService;

    @Autowired
    ComicService comicService;

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
        Comic newComic = comicService.create(currentUser, comicName, currentSeries.getId(), "");
        seriesService.addComic(currentSeries.getId(), newComic.getId());
        currentSeries = seriesService.findByID(currentSeries.getId()).get();

        //Get the fully initialized objects from the DB and then set it into the session before going to create comic page
        request.getSession().setAttribute("currentSeries", currentSeries);
        request.getSession().setAttribute("currentComic", newComic);

        /* TODO

            SEND COMIC TO MAV, SO THAT ON MAV WE CAN SHOW NUMBERS REPRESENTING PANELLIST
            A NEW CONTROLLER THAT REACTS TO THE NEW PAGE BUTTON
         */

        ModelAndView mav = new ModelAndView("createComic");
        newComic.addPanel("fakepanel");
        newComic.addPanel("fakepanel");
        newComic.addPanel("fakepanel");
        mav.addObject("comic", newComic);
        return mav;

    }

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
