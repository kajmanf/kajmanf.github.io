package com.example.winninglog.Controllers;

import com.example.winninglog.Models.User;
import com.example.winninglog.Services.EmailService;
import com.example.winninglog.Services.UserService;
import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.mail.MessagingException;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HomeController {

    private final UserService userService;
    private final EmailService emailService;

    @Autowired
    public HomeController(UserService userService, EmailService emailService) {
        this.userService = userService;
        this.emailService = emailService;
    }

    @GetMapping({"/", ""})
    public String home(){
        //return "index";
        return "redirect:/log";
    }

    @RequestMapping(path = "/login", method = RequestMethod.GET)
    public String loginPage(){
        return "login";
    }

    @RequestMapping(path = "/createAdminUser", method = RequestMethod.POST)
    public String createAdmin(){
        List<User> users = userService.findAll();
        if (users.size() == 0){
            userService.save(new User("filip.kajanovic", "3AgQQgl6", "USER,TEAMLEADER,ADMIN", 0000));
        }
        return "redirect:/";
    }

    @RequestMapping(path = "/forgotpassword", method = RequestMethod.GET)
    public String sendMail(Model model){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        return "user/forgotPassword";
    }

    @RequestMapping(path = "/forgotpassword/retrieve", method = RequestMethod.GET)
    public String sendMail(RedirectAttributes redirectAttributes,
                           @RequestParam(name = "username") String username
                          ) throws MessagingException {
        long users = userService.findAll().stream()
                .filter(x -> x.getUserName().equals(username))
                .count();
        if (users != 0){
            User user = userService.findByUsername(username);
            String generatedString = RandomString.make(20);
            user.setRetrieval(generatedString);
            emailService.sendRetrievalMail(generatedString, user.getEmail());
            userService.save(user);
            redirectAttributes.addFlashAttribute("successMessage", "Email with password reset link was sent to email adress associated with user: " + username);
            return "redirect:/forgotpassword";
        }
        redirectAttributes.addFlashAttribute("errorMessage", "User with username: " + username + " does not exist!");
        return "redirect:/forgotpassword";
    }

    @RequestMapping(path = "/changepassword/{retrieval}", method = RequestMethod.GET)
    public String changePasswordForm(Model model,
                                 @PathVariable(value = "retrieval", required = true) String retrieval
                                ){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        model.addAttribute("isLogged", false);
        model.addAttribute("retrieval", retrieval);
        return "user/changePassword";
    }

    @RequestMapping(path = "/retrievepassword", method = RequestMethod.GET)
    public String changePassword(RedirectAttributes redirectAttributes,
                                 @RequestParam(name = "password") String password,
                                 @RequestParam(name = "oldPassword", required = false) String oldPassword,
                                 @RequestParam(name = "repeatPassword") String repeatPassword,
                                 @RequestParam(value = "retrieval", required = false) String retrieval
                                ) throws MissingServletRequestParameterException {
        if (retrieval == null){
            redirectAttributes.addFlashAttribute("password", password);
            redirectAttributes.addFlashAttribute("repeatPassword", repeatPassword);
            redirectAttributes.addFlashAttribute("oldPassword", oldPassword);
            return "redirect:/user/changepassword/change";
        }
        if (!userService.checkPassword(password)){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Wrong password format!");
            return "redirect:/changepassword/" + retrieval;
        }
        if (!password.equals(repeatPassword)){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Password does not match!");
            return "redirect:/changepassword/" + retrieval;
        }
        List<User> users = userService.findAll().stream()
                .filter(x -> x.getRetrieval().equals(retrieval))
                .collect(Collectors.toList());
        if (users.size() != 1){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Something went wrong!");
            return "redirect:/changepassword/" + retrieval;
        }
        User user = users.get(0);
        if (!user.getRetrieval().equals(retrieval)){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Retrieval link is out of date!");
            return "redirect:/";
        }
        try {
            user.setPassword(password);
            user.setRetrieval(null);
            userService.save(user);
            return "redirect:/";
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Password retrieval failed! Please repeat process");
            return "redirect:/";
        }
    }

    @ExceptionHandler(MessagingException.class)
    public String handleMessagingException(RedirectAttributes redirectAttributes,
                                           MessagingException ex) {
        System.out.println(ex);
        redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong, please repeat process!");
        return "redirect:/changepassword/";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(RedirectAttributes redirectAttributes,
                                      MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        redirectAttributes.addFlashAttribute("errorMessage", "Something went wrong, please repeat process!");
        return "redirect:/forgotpassword";
    }
}
