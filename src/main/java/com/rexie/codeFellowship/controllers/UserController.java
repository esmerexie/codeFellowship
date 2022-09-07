package com.rexie.codeFellowship.controllers;

import com.rexie.codeFellowship.models.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.view.RedirectView;
import com.rexie.codeFellowship.repositories.UserRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    HttpServletRequest request;

    @GetMapping("/")
    public String getHomePage(Principal p, Model m)
    {
        if(p != null)
        {
            String username = p.getName();
            ApplicationUser dinoUser = userRepo.findByUsername(username);

            m.addAttribute("username", username);

        }
        return "index.html";
    }

    @GetMapping("/login")
    public String getLoginPage(){
        return "login";
    }

    @GetMapping("/signup")
    public String getSignupPage(){
        return "signup";
    }

    @PostMapping("/signup")
    public RedirectView createUser(String username, String password,
                                   String firstName, String lastName,
                                   String dateOfBirth, String bio) throws ServletException {
        String hashedPassword = passwordEncoder.encode(password);
        ApplicationUser newUser = new ApplicationUser(username, hashedPassword, firstName,
                lastName,
                dateOfBirth,bio);
        userRepo.save(newUser);
        request.login(username,password);
        return new RedirectView("/login");
    }


}
