package com.tts.twitterClone.controller;

import com.tts.twitterClone.model.User;
import com.tts.twitterClone.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class AuthorizationController {

    private UserService userService;


    public AuthorizationController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/signup")
    public  String registration(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "registration";
    }

    @PostMapping("/signup")
    public String createNewUser(@Valid User user, BindingResult bindingResult, Model model) {

        User userExists = userService.findByUsername(user.getUsername());

        if (userExists != null) {
            bindingResult.rejectValue("userName",
                    "error.user",
                    "Username is already taken");
        }
        if (!bindingResult.hasErrors()) {
            userService.saveNewUser(user);
            model.addAttribute("success",
                    "signup successful");
            model.addAttribute("user", new User());
        }
        return "registration";
    }

}
