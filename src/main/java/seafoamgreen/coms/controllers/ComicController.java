package seafoamgreen.coms.controllers;


import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.services.ComicService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;

import java.io.IOException;
import java.util.List;

@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
@RequestMapping("comic")
public class ComicController {

    String comicViewName = "comicView";


    @Autowired
    ComicService comicService;

    @PostMapping("/create")
    public ModelAndView create(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //TODO: refactor this to "start new comic"
        //


        // String seriesName = request.getParameter("seriesName");
        // String tagList = (String) request.getParameter("tagList");
        // request.getParameter("comicName");
        // request.getParameter("panelList");
        //request.getParameter("postTime");

        //
        /*
        String userID = comicBody.getUserID();
        String comicName = comicBody.getComicName();
        String seriesID = comicBody.getSeriesID();

        */
        //return comicService.create(userID, comicName, seriesID);
        //return panelService.create(userID, comicID, snapshotJSON);
        ModelAndView mav = new ModelAndView("login");
        return mav;
    };

    @GetMapping("/view")
    public ModelAndView viewComic(HttpServletRequest request, HttpServletResponse response) {
        String comicID = request.getParameter("comidID");

        ModelAndView mav = new ModelAndView("comicView");
        Comic c =  comicService.findById(comicID);
        List<String> blobs = comicService.getPanelObjects(c);
        //add to history
        HttpSession session = request.getSession(false);
        if (session != null) {
            String username = (String) session.getAttribute("username");
            if (username != null) {
                comicService.addToHistory(username, comicID);
            }
        }
        mav.addObject("comic", c);
        mav.addObject("panels", blobs);

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
        String seriesName = request.getParameter("seriesName");
        String tagList = (String) request.getParameter("tagList");
        String comicId = request.getParameter("comicId");

        // request.getParameter("panelList");
        //Date posttime = request.getParameter("postTime"); still gotta figure this out for rn

        Comic c = comicService.publishComic(comicId, seriesName, tagList);


        //returns the view of the comic
        //we should have 2 different comic views - 1 if u created it, one if you didnt
        ModelAndView mav = new ModelAndView(comicViewName);
        mav.addObject("comic", c);
        return mav;

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

}

