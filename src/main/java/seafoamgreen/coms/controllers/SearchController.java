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
import seafoamgreen.coms.services.UserService;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("search")
public class SearchController {
    @Autowired
    SearchService searchService;

    @Autowired
    UserService userService;

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
        List<Comic> publishedComics = new ArrayList<Comic>();

        String searchWord = request.getParameter("searchWord");
        comics.addAll(searchService.findAllByComicTitle(searchWord));
        comics.addAll(searchService.findAllByTag(searchWord));
        comics.addAll(searchService.findAllComicsByUsername(searchWord));

        for(Comic comic: comics)
            if(comic.isPublished())
                publishedComics.add(comic);

        mav.addObject("comicList" , publishedComics);
        mav.addObject("searchWord",searchWord);

        HttpSession session = request.getSession(false);
        String activeUsername = (String)session.getAttribute("username");
        User user = userService.findByUsername(activeUsername);
        mav.addObject("user", user);
        mav.addObject("username", activeUsername);
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);

        System.out.println(comics);
        return mav;

    }

}