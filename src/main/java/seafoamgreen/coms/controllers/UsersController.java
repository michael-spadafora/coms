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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            if (userService.create(username, password) != null){
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
        System.out.println("Updating all comics with publish dates");



        List<Comic> usersComics = comicService.findAllByUsername(activeUsername);
        List<Series> userSeries = seriesService.findAllByUsername(activeUsername);
        mav.addObject("userComics", usersComics);
        mav.addObject("username", activeUsername);
        mav.addObject("userSeries", userSeries);
        //TODO: add list objects for popular, continue reading, recommended
        List<Comic> popularComics = userService.getPopular();

        List<String> popularThumbnails = userService.getThumbnails(popularComics);
        List<Comic> recommendedComics = userService.getRecommended(activeUsername);

        mav.addObject("popularComics", popularComics);
        mav.addObject("popularThumbnails", popularThumbnails);
        mav.addObject("recommendedComics", recommendedComics);
        mav.addObject("featuredComic",comicService.getRandomComic());

        if(activeUsername == null) {
            mav.addObject("notLoggedIn", true);
            //TODO: this stuff
            // userService.getContinueReading(activeUsername);
        }
        else {
            List<Comic> history = userService.getUserHistory(activeUsername);
            List<String> historyThumbnails = userService.getThumbnails(history);
            mav.addObject("history", history);
            mav.addObject("historyThumbnails", historyThumbnails);

            mav.addObject("isLoggedIn", true);
            // List<Comic> popularComic = userService.getPopular();
            // List<String> popularThumbnail = userService.getPopularThumbnails();
        }
        User currentUser = userService.findByUsername(activeUsername);
        session.setAttribute("user", currentUser);
        mav.addObject("user", currentUser);
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

        List<Comic> popularComics = userService.getPopular();
        List<Comic> recommendedComics = userService.getRecommended(activeUsername);
        //List<String> popularThumbnails = userService.getThumbnails(popularComics);
        mav.addObject("popularComics", popularComics);
        mav.addObject("recommendedComics", recommendedComics);
        mav.addObject("user", session.getAttribute("user"));
        //mav.addObject("popularThumbnails", popularThumbnails);
        mav.addObject("featuredComic",comicService.getRandomComic());

        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else {
            List<Comic> history = userService.getUserHistory(activeUsername);
            mav.addObject("history", history);
            mav.addObject("isLoggedIn", true);
        }

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
        User user = userService.findByUsername(activeUsername);
        mav.addObject("userProfile", user);
        mav.addObject("username", activeUsername);
        mav.addObject("userComics",comicService.findAllByUsername(activeUsername));
        mav.addObject("userList",userService.getUsersMyList(activeUsername));

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
    @GetMapping("/profile/{userProfile}")
    public ModelAndView viewOther(HttpServletRequest request, HttpServletResponse response, @PathVariable String userProfile) throws IOException {
        User user = userService.findByUsername(userProfile);
        if (userProfile == null) {
            response.sendError(401, "User not found");
        }

        //mav add my comics
        //mav add my list

        List<Comic> madeComics = userService.getUsersComics(user.getUsername());
        List<Comic> myList = userService.getUsersMyList(user.getUsername());

        ModelAndView mav = new ModelAndView("viewUser");
        mav.addObject("userProfile", user);
        mav.addObject("myComics", madeComics);
        mav.addObject("myList", myList);
        mav.addObject("userComics",comicService.findAllByUsername(user.getUsername()));
        mav.addObject("userList",userService.getUsersMyList(userProfile));

        HttpSession session = request.getSession(false);
        String activeUsername = (String)session.getAttribute("username");
        mav.addObject("username", activeUsername);
        if(activeUsername == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;

    }

    @GetMapping("/profile/inbox")
    public ModelAndView viewInbox(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = (String) request.getSession(false).getAttribute("username");

        System.out.println("inbox username: "+ username);
        if (username == null) {
            response.sendError(401, "User not logged in");
        }

        ModelAndView mav = new ModelAndView("inbox");
        List<Message> userMessages = userService.getInbox(username);
        List<Message> messages = new ArrayList<Message>();
        for(int i = userMessages.size()-1; i >= 0; i--)
            messages.add(userMessages.get(i));
        mav.addObject("Messages" , messages);

        mav.addObject("username", username);
        if(username == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;

    }

    @GetMapping("/profile/sent")
    public ModelAndView viewSentMessages(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(401, "User not logged in");
        }

        String username = (String) session.getAttribute("username");
        
        List<Message> sent = userService.getSent(username);
        ModelAndView mav = new ModelAndView("messages");
        mav.addObject("Messages", sent);
        return mav;

    }

    
    @GetMapping ("/subscriptions") 
    public ModelAndView viewSubList(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(401, "User not logged in");
        }
        String username = (String) session.getAttribute("username");
        


        List<Comic> mySubs = userService.getComicsFromSubscriptions(username);
        User user = userService.findByUsername(username);
        Map<Series, List<Comic>> map = new HashMap<Series, List<Comic>>();
        for(String seriesID: user.getSubscriptions())
        {
            map.put(seriesService.findByID(seriesID).get(), comicService.findAllBySeriesId(seriesID));
        }

        ModelAndView mav = new ModelAndView("mySubscriptions");
        mav.addObject("mySubs", mySubs);
        mav.addObject("seriesMap", map);
        return mav;
    }

    @GetMapping("/mylist/add/{comicID}")
    public ModelAndView addToMyList(HttpServletRequest request, HttpServletResponse response,@PathVariable String comicID) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(401, "User not logged in");
        }
        String username = (String) session.getAttribute("username");

        System.out.println("ADD COMIC TO MY LIST " +comicID);

        userService.addToMyList(username, comicID);

        User user = userService.findByUsername(username);

        session.setAttribute("user",user);


        //TODO: figure out what to return here
        //idk if this is the right mav to return
        //or if i should return anything tbh
        //ModelAndView mav = new ModelAndView("myList");
        //mav.addObject("myList", userService.getUsersMyList(username));
        //return mav;
        return new ModelAndView( "redirect:/home");
    }

    @GetMapping("/mylist/remove/{comicID}")
    public ModelAndView removeFromMyList(HttpServletRequest request, HttpServletResponse response,@PathVariable String comicID) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(401, "User not logged in");
        }
        String username = (String) session.getAttribute("username");


        userService.removeFromMyList(username, comicID);

        User user = userService.findByUsername(username);

        session.setAttribute("user",user);


        //TODO: figure out what to return here
        //idk if this is the right mav to return
        //or if i should return anything tbh
        //ModelAndView mav = new ModelAndView("myList");
        //mav.addObject("myList", userService.getUsersMyList(username));
        //return mav;
        return new ModelAndView( "redirect:/home");
    }


    @GetMapping("/myList")
    public ModelAndView viewMyList(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(401, "User not logged in");
        }
        String username = (String) session.getAttribute("username");

        List<Comic> myList = userService.getUsersMyList(username);
        ModelAndView mav = new ModelAndView("myList");
        mav.addObject("myList", myList);
        mav.addObject("username",username);
        if(username == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;
    }

    @GetMapping("/mylist/removeComic/{comicID}")
    public ModelAndView removeFromList(HttpServletRequest request, HttpServletResponse response,@PathVariable String comicID) throws IOException {
        HttpSession session = request.getSession(false);
        if (session == null) {
            response.sendError(401, "User not logged in");
        }
        String username = (String) session.getAttribute("username");


        userService.removeFromMyList(username, comicID);

        User user = userService.findByUsername(username);

        session.setAttribute("user",user);


        //TODO: figure out what to return here
        //idk if this is the right mav to return
        //or if i should return anything tbh
        //ModelAndView mav = new ModelAndView("myList");
        //mav.addObject("myList", userService.getUsersMyList(username));
        //return mav;
        return new ModelAndView( "redirect:/myList");
    }
}