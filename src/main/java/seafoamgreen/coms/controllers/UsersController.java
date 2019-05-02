package seafoamgreen.coms.controllers;

import seafoamgreen.coms.model.Comic;
import seafoamgreen.coms.model.Series;
import seafoamgreen.coms.services.ComicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import seafoamgreen.coms.model.Message;
import seafoamgreen.coms.model.User;
import seafoamgreen.coms.services.SeriesService;
import seafoamgreen.coms.services.UserService;
import org.springframework.web.servlet.ModelAndView;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//EXAMPLE!!
@CrossOrigin(origins = "*", allowCredentials = "true", allowedHeaders = "*")
@RestController
//@SessionAttributes("username")
public class UsersController {

    @Autowired
    UserService userService;

    @Autowired
    ComicService comicService;

    @Autowired
    SeriesService seriesService;


    @GetMapping("/")
    public ModelAndView login(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //System.out.println(request.getSession().getAttribute("username"));
        ModelAndView mav = new ModelAndView("login");
        return mav;
    };

    /*

    @GetMapping("/user")
    public User userFromUsername(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // returns a json object describing the user who's id is :id from URL
        // 1. Using REST controller, get the data from the database
        // NOTE: PARSING/DIFFERENT CALLS SHOULD BE DONE IN THE REST CONTROLLER. NOT HERE
        // 2. return the data from the controller.
        String username = request.getParameter("username");
        User item = service.findByUsername(username);

        if (item == null) {
            response.sendError(500,  "User not found");
        }
        return item;
    }
    */
    /**
     * returns 200 for successful registration, 500 for duplicate username
     *
     * @param member
     * @return
     * @throws IOException
     */

    @PostMapping("/loginOrRegister")
    public ModelAndView loginOrRegister(HttpServletRequest request, HttpServletResponse response) throws IOException {

        //Check to see if login or register button was clicked on the form. Gets value from its buttons' name attribute
        String login = request.getParameter("loginButton");
        String register = request.getParameter("registerButton");
        HttpSession session;

        ModelAndView mav = new ModelAndView("index");
        //TODO: get lists of comics for popular, continue, recommended
        if(login!=null)
        {
            //LOG IN BUTTON HIT
            String username = request.getParameter("username");
            String password = request.getParameter("password");

            User usr = userService.login(username, password);

            if (usr == null){
                //Wrong username or password redirect to login page with error
                mav = new ModelAndView("login");
                mav.addObject("LoginError", "Invalid login");
                return mav;
            }
            else {
                response.setStatus(200);
                session = request.getSession();
                System.out.println(usr.getUsername());
                session.setAttribute("userID", usr.getId());
                session.setAttribute("username", usr.getUsername());
                session.setAttribute("admin", usr.isAdmin());


                //************** LOG IN IS SUCCESSFUL ADD THE USER INFORMATION TO THE INDEX.HTML MODEL BY USING MAV.ADDOBJECT....****//
            }

        }
        else if(register != null)
        {

            //Register Button was clicked


            String username = request.getParameter("username");
            String password = request.getParameter("password");

            //After register it should return index
            if (userService.findByUsername(username) == null){
                userService.create(username, password);
                User usr = userService.login(username, password);
                response.setStatus(200);
                session = request.getSession();
                session.setAttribute("userID", usr.getId());
                session.setAttribute("username", usr.getUsername());


                //******** ADD USER DATA TO INDEX PAGE ****************//
            }
            else {
                //Return the same page but add an error to the HTML
                mav = new ModelAndView("login");
                // RegisterError added so Thymeleaf can show it
                mav.addObject("RegisterError", "Username Exists Already");
                return mav;
            }
        }

        //RETURN THE DEFAULT MAV WHICH IS THE INDEX
        session = request.getSession();
        String activeUsername = (String)session.getAttribute("username");

        System.out.println("The current active username is: " + activeUsername);

        List<Comic> usersComics = comicService.findAllByUsername(activeUsername);
        List<Series> userSeries = seriesService.findAllByUsername(activeUsername);
        mav.addObject("userComics", usersComics);
        mav.addObject("username", activeUsername);
        mav.addObject("userSeries", userSeries);
        //TODO: add list objects for popular, continue reading, recommended
        
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
            //TODO: this stuff
            // List<Comic> popularComic = userService.getPopular();
            // List<String> popularThumbnail = userService.getPopularThumbnails();
            // userService.getContinueReading(activeUsername);

        else {
            mav.addObject("isLoggedIn", true);
            // List<Comic> popularComic = userService.getPopular();
            // List<String> popularThumbnail = userService.getPopularThumbnails();
        }

        return mav;
    }


    @GetMapping("/logout")
    public ModelAndView logout(HttpServletRequest request, HttpServletResponse response) {
        HttpSession session = request.getSession();
        session.invalidate();
        ModelAndView mav = new ModelAndView("login");
        return mav;
    }


    @GetMapping("/home")
    public ModelAndView homepage(HttpServletRequest request, HttpServletResponse response)
    {
        ModelAndView mav = new ModelAndView("index");
        HttpSession session = request.getSession();
        String activeUsername = (String)session.getAttribute("username");
        System.out.println("The current active username is: " + activeUsername);

        List<Comic> usersComics = comicService.findAllByUsername(activeUsername);
        mav.addObject("userComics", usersComics);
        mav.addObject("username",activeUsername);
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);

        return mav;
    }


    @GetMapping("profile/self")
    public ModelAndView getOwnProfile(HttpServletRequest request, HttpServletResponse response)
    {
        HttpSession session = request.getSession(false);
        if (session == null) {
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("LoginError", "You must be logged in to view your profile");
            return mav;
        }

        ModelAndView mav = new ModelAndView("viewUser");
        String activeUsername = (String)session.getAttribute("username");
        mav.addObject("username", activeUsername);
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);

        return mav;
    }


    @GetMapping("/accountSettings")
    public ModelAndView viewOwnProfile(String data, HttpServletRequest request, HttpServletResponse response) throws IOException {
        /*
        String username = (String) request.getSession(false).getAttribute("username");
        if (username == null) {
            response.sendError(401, "User not logged in");
        }*/
        HttpSession session = request.getSession(false);
        if (session == null) {
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("LoginError", "You must be logged in to view your profile");
            return mav;
        }

        ModelAndView mav = new ModelAndView("account");
        String activeUsername = (String)session.getAttribute("username");
        mav.addObject("username", activeUsername);
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);

        return mav;
    }


    //this should be edited. im not sure what exactly should go here
    @GetMapping("/profile")
    public ModelAndView viewOther(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        User usr = userService.findByUsername(username);
        if (usr == null) {
            response.sendError(401, "User not found");

        }
        ModelAndView mav = new ModelAndView("profile");
        mav.addObject("User", usr);
        return mav;

    }

    @GetMapping("/profile/inbox")
    public ModelAndView viewInbox(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession(false).getAttribute("username");
        if (username == null) {
            response.sendError(401, "User not logged in");
        }

        ModelAndView mav = new ModelAndView("inbox");
        mav.addObject("Messages" , userService.getInbox(username));
        System.out.println(userService.getInbox(username));
        mav.addObject("username", username);
        if(username == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;

    }

    @GetMapping("/profile/sent")
    public ModelAndView viewSentMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession(false).getAttribute("username");
        if (username == null) {
            response.sendError(401, "User not logged in");
        }
        List<Message> sent = userService.getSent(username);
        ModelAndView mav = new ModelAndView("messages");
        mav.addObject("Messages", sent);
        return mav;

    }
}
