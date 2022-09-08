package com.rexie.codeFellowship.controllers;

import com.rexie.codeFellowship.models.ApplicationUser;
import com.rexie.codeFellowship.models.Post;
import com.rexie.codeFellowship.repositories.PostRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;
import com.rexie.codeFellowship.repositories.UserRepo;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.time.LocalDateTime;

@Controller
public class UserController {

    @Autowired
    UserRepo userRepo;

    @Autowired
    PostRepo postRepo;

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
            m.addAttribute("firstName", dinoUser.getFirstName());
            m.addAttribute("lastName", dinoUser.getLastName());
            m.addAttribute("dateOfBirth", dinoUser.getDateOfBirth());

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

    @GetMapping("/profile")
    public String getProfilePage(Principal p, Model m)
    {
        if(p != null)
        {
            String username = p.getName();
            ApplicationUser dinoUser = userRepo.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", dinoUser.getFirstName());
            m.addAttribute("lastName", dinoUser.getLastName());
            m.addAttribute("dateOfBirth", dinoUser.getDateOfBirth());
            m.addAttribute("bio", dinoUser.getBio());

        }
        return "profile.html";
    }

    @GetMapping("/users/{id}")
    public String getUserId(Principal p, Model m, @PathVariable Long id)
    {
        if (p != null){
            String username = p.getName();
            ApplicationUser applicationUser = userRepo.findByUsername(username);

            m.addAttribute("username", username);
            m.addAttribute("firstName", applicationUser.getFirstName());
        }

        ApplicationUser dbUser = userRepo.findById(id).orElseThrow();
        m.addAttribute("dbUserUsername", dbUser.getUsername());
        m.addAttribute("dbUserFirstName", dbUser.getFirstName());
        m.addAttribute("dbUserLastName", dbUser.getLastName());
        m.addAttribute("dbUserDOB", dbUser.getDateOfBirth());
        m.addAttribute("dbUserBio", dbUser.getBio());
        m.addAttribute("dbUserId", dbUser.getId());

        m.addAttribute("testDate", LocalDateTime.now());
        
        return "userInfo";
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

    @PutMapping("/users/{id}")
    public RedirectView editUserInfo(Model m, Principal p, @PathVariable Long id, String username, String firstName, RedirectAttributes redir){
        if(p != null && p.getName().equals(username)){
            ApplicationUser newUser = userRepo.findById(id).orElseThrow();
            newUser.setUsername(username);
            newUser.setFirstName(firstName);
            userRepo.save(newUser);
        } else {
            redir.addFlashAttribute("errorMessage", "Cannot edit another user's info");
        }
        return new RedirectView("/users/" + id);
    }

    @PostMapping("/profile")
    public RedirectView makePost(Principal p, String body){
        String username = p.getName();
        ApplicationUser user = userRepo.findByUsername(username);
        Post newPost = new Post(body, LocalDateTime.now(), user);
        postRepo.save(newPost);
        return new RedirectView("/profile");
    }

}
