package seafoamgreen.coms.controllers;

import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.requestBodyTypes.SeriesBody;
import seafoamgreen.coms.services.SeriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("series")
public class SeriesController {

    @Autowired
    SeriesService seriesService;

    @PostMapping("/create")
    public ModelAndView createSeries(HttpServletRequest request, HttpServletResponse response) throws IOException
    {


        String seriesName = request.getParameter("series");
        String comicName = request.getParameter("comic");
        System.out.println(seriesName);
        System.out.println(comicName);
        request.getSession().setAttribute("currentSeriesName", seriesName);
        request.getSession().setAttribute("currentComicName", comicName);


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
