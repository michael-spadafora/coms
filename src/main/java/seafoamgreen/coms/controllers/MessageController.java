package seafoamgreen.coms.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.servlet.ModelAndView;


import seafoamgreen.coms.model.Message;
import seafoamgreen.coms.requestBodyTypes.MessageBody;
import seafoamgreen.coms.services.MessageService;
import seafoamgreen.coms.services.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//EXAMPLE!!
@CrossOrigin
@RestController
public class MessageController {

    @Autowired
    MessageService service;


    @Autowired
    UserService userService;

    @PostMapping("/messages/system")
    public void systemMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody MessageBody msg) {
        String toN = request.getParameter("to");
        String title = msg.getTitle();
        String body = msg.getBody();

        service.sendSystemMessage(toN, title, body);
    }

    @PostMapping("/messages/system/subscription")
    public void subscriptionupdate(HttpServletRequest request, HttpServletResponse response, @RequestBody MessageBody msg) {
        String toN = request.getParameter("to");
        String series =request.getParameter("series");
        service.sendSubscriptionUpdate(toN, series);

    }

    @PostMapping("/profile/inbox")
    public ModelAndView sendMessage(HttpServletRequest request, HttpServletResponse response) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("LoginError", "You cannot send a message if you are not logged in");
            return mav;
        }

        String fromN = (String) session.getAttribute("username");
        String toN = request.getParameter("to");

        String title = request.getParameter("subject");
        String body = request.getParameter("bodyMessage");

    //    String title = msg.getTitle();
    //    String body = msg.getBody();

        String mg = service.sendMessage(title, body, fromN, toN);

        ModelAndView mav = new ModelAndView("inbox");
        mav.addObject("message", mg);
        String username = (String)session.getAttribute("username");

        List<Message> userMessages = userService.getInbox(username);
        List<Message> messages = new ArrayList<Message>();
        for(int i = userMessages.size()-1; i >= 0; i--)
            messages.add(userMessages.get(i));
        mav.addObject("Messages" , messages);

        mav.addObject("username",username);
        if(username == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;
    }

    @PostMapping("/sendMessage/{toUsername}")
    public ModelAndView sendMessageFromProfile(HttpServletRequest request, HttpServletResponse response, @PathVariable String toUsername) throws IOException {

        HttpSession session = request.getSession(false);

        if (session == null) {
            ModelAndView mav = new ModelAndView("login");
            mav.addObject("LoginError", "You cannot send a message if you are not logged in");
            return mav;
        }

        String fromN = (String) session.getAttribute("username");

        String title = request.getParameter("subject");
        String body = request.getParameter("bodyMessage");

        String mg = service.sendMessage(title, body, fromN, toUsername);

        return new ModelAndView( "redirect:/profile/" + toUsername);
    }

    @PostMapping("/messages/delete")
    public ModelAndView deleteMessage(HttpServletRequest request) throws IOException {
        String messageId = (String)request.getParameter("messageId");
        String currentUser = (String)request.getSession().getAttribute("username");
        service.delete(currentUser, messageId);

        ModelAndView mav = new ModelAndView("inbox");
        HttpSession session = request.getSession(false);
        String username = (String)session.getAttribute("username");

        List<Message> userMessages = userService.getInbox(username);
        List<Message> messages = new ArrayList<Message>();
        for(int i = userMessages.size()-1; i >= 0; i--)
            messages.add(userMessages.get(i));

        mav.addObject("Messages" , messages);
        mav.addObject("username",username);
        if(username == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;
    }

    @PostMapping("/messages/read")
    public ModelAndView readMessage(HttpServletRequest request) throws IOException {
        String messageId = (String)request.getParameter("messageId");
        service.readMessage(messageId);

        ModelAndView mav = new ModelAndView("inbox");
        HttpSession session = request.getSession(false);
        String username = (String)session.getAttribute("username");

        List<Message> userMessages = userService.getInbox(username);
        List<Message> messages = new ArrayList<Message>();
        for(int i = userMessages.size()-1; i >= 0; i--)
            messages.add(userMessages.get(i));

        mav.addObject("Messages" , messages);
        mav.addObject("username",username);
        if(username == null)
            mav.addObject("notLoggedIn", true);
        else
            mav.addObject("isLoggedIn", true);
        return mav;
    }

}
