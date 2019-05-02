package seafoamgreen.coms.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


import seafoamgreen.coms.requestBodyTypes.MessageBody;
import seafoamgreen.coms.services.MessageService;


import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

//EXAMPLE!!
@CrossOrigin
@RestController
public class MessageController {

    @Autowired
    MessageService service;

    @PostMapping("/messages/send")
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
        return mav;
    }

    @PostMapping("/messages/delete")
    public ModelAndView deleteMessage(HttpServletRequest request, HttpServletResponse response, @RequestBody MessageBody msg) throws IOException {
        service.delete(request.getParameter("deleteID"));
        ModelAndView mav = new ModelAndView("messages");

        return mav;
    }

}
