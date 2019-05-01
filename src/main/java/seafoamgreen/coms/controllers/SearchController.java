package seafoamgreen.coms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import org.springframework.web.servlet.ModelAndView;
import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.services.SearchService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @GetMapping("/tag")
    public List<Comic> searchByTag(HttpServletRequest request, HttpServletResponse response) {
        String tag = request.getParameter("tag");
        List<Comic> comics = searchService.findAllByTag(tag);

        return comics;

    }

    @GetMapping("/username")
    public List<User> searchByUsername(HttpServletRequest request, HttpServletResponse response) {
        String username = request.getParameter("username");
        List<User> comics = searchService.findAllByUsername(username);
        return comics;
    }

    @GetMapping("/comicName")
    public List<Comic> searchByComicName(HttpServletRequest request, HttpServletResponse response) {
        String comicName = request.getParameter("comicName");
        List<Comic> comics = searchService.findAllByComicTitle(comicName);
        return comics;
    }

    @GetMapping("/seriesName")
    public List<Series> searchBySeriesName(HttpServletRequest request, HttpServletResponse response) {
        String seriesName = request.getParameter("seriesName");
        List<Series> series = searchService.findAllBySeriesName(seriesName);
        return series;

    }
  
    @GetMapping("/keyword")
    public ModelAndView search(HttpServletRequest request)
    {
        ModelAndView mav = new ModelAndView("searchResults");

        List<Comic> comics = new ArrayList<Comic>();

        String searchWord = request.getParameter("searchWord");
        comics.addAll(searchService.findAllByComicTitle(searchWord));
        comics.addAll(searchService.findAllByTag(searchWord));
        comics.addAll(searchService.findAllComicsByUsername(searchWord));
        mav.addObject("comicList" , comics);
        mav.addObject("searchWord",searchWord);

        System.out.println(comics);
        return mav;

    }

}