package seafoamgreen.coms.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;
import seafoamgreen.coms.services.InteractionService;

@RestController
@RequestMapping("interaction")
public class InteractionController {
    @Autowired
    InteractionService interactionService;


    @GetMapping("/vote")
    public int checkUserVote(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) return 0;
        String username = (String) session.getAttribute("usename");
        String comicId = request.getParameter("comic"); //possibly need to switch to id

        return interactionService.checkVote(username, comicId);
    }

    @PostMapping("/vote")
    public int vote(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) return 0;

        String username = (String) session.getAttribute("usename");
        String valueStr = request.getParameter("value");
        int val = Integer.parseInt(valueStr);
        String comicId = request.getParameter("comic"); //possibly need to switch to id
              
        interactionService.vote(comicId, val, username);
        return val;

    }


    @GetMapping("/subscribers/addByComic/")
    public ModelAndView subscribeAddByComic(HttpServletRequest request, HttpServletResponse response) {
        String comicId = request.getParameter("comicId");
        HttpSession session = request.getSession(false);
        if (session == null) return null;

        String username = (String) session.getAttribute("username");
       // String comicId = request.getParameter("comicId"); //possibly need to switch to id
        System.out.println("SUBSCRING USER TO SEIRES");
        interactionService.subscribeByComic(comicId, username);
        return new ModelAndView( "redirect:/comic/view/" + comicId);
    }

    @GetMapping("/subscribers/removeByComic") 
    public void subscribeRemoveByComic(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) return;

        String username = (String) session.getAttribute("username");
        String comicId = request.getParameter("comicId"); //possibly need to switch to id

        interactionService.unsubscribeByComic(comicId, username);
    }

    
    @GetMapping("/subscribers/add") 
    public void subscribeAdd(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) return;

        String username = (String) session.getAttribute("username");
        String comicId = request.getParameter("seriesId"); //possibly need to switch to id

        interactionService.subscribe(comicId, username);
    }

    @GetMapping("/subscribers/remove") 
    public void subscribeRemove(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession(false);
        if (session == null) return;

        String username = (String) session.getAttribute("username");
        String comicId = request.getParameter("seriesId"); //possibly need to switch to id

        interactionService.unsubscribe(comicId, username);
    }


    @GetMapping("/subscribers/series") 
    public void getSubscribers(HttpServletRequest request, HttpServletResponse response) {

    }
}
