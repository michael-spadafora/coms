package seafoamgreen.coms.controllers;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.services.ComicService;
import seafoamgreen.coms.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
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
        String comicName = request.getParameter("comic");
        String currentUser = (String)session.getAttribute("username");
        System.out.println(currentUser);
        System.out.println(seriesName);
        System.out.println(comicName);
        request.getSession().setAttribute("currentSeriesName", seriesName);
        request.getSession().setAttribute("currentComicName", comicName);


        //Initialize the new Series and Comic Issue inside the DB
        Series newSeries = seriesService.create(seriesName, currentUser);
        Comic newComic = comicService.create(currentUser, comicName, newSeries.getId(), "");
        seriesService.addComic(newSeries.getId(), newComic.getId());
        newSeries = seriesService.findByID(newSeries.getId()).get();

        //Get the fully initialized objects from the DB and then set it into the session before going to create comic page
        request.getSession().setAttribute("currentSeries", newSeries);
        request.getSession().setAttribute("currentComic", newComic);

        ModelAndView mav = new ModelAndView("createComic");
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
        return seriesService.findByUsername(request.getParameter("username"));
    }


}
