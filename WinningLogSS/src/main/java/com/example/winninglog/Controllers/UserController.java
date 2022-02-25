package com.example.winninglog.Controllers;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Models.User;
import com.example.winninglog.Services.EntityService;
import com.example.winninglog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final EntityService entityService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, EntityService entityService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.entityService = entityService;
        this.passwordEncoder = passwordEncoder;
    }

    @RequestMapping(path = {"","/"}, method = RequestMethod.GET)
    public String users(Model model){
        List<User> users = userService.findAll();
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        model.addAttribute("users", users);
        return "user/index";
    }

    @RequestMapping(path = {"/create"}, method = RequestMethod.GET)
    public String user(Model model){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
            model.addAttribute("userModel", new User(null, null, null, 0));
        }
        return "user/create";
    }

    @RequestMapping(path = "/create/add", method = RequestMethod.GET)
    public String createUser(RedirectAttributes redirectAttributes,
                             @RequestParam(name = "username") String username,
                             @RequestParam(name = "password") String password,
                             @RequestParam(name = "employee") int employee,
                             @RequestParam(name = "authority") String authority
                             ) throws MissingServletRequestParameterException {
        if (!username.contains(".") || username.split("\\.").length != 2){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Wrong username format");
            redirectAttributes.addFlashAttribute("userModel", new User(username, password, authority, employee));
            return "redirect:/user/create";
        }
        if (!userService.checkPassword(password)){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Wrong password format!");
            redirectAttributes.addFlashAttribute("userModel", new User(username, null, authority, employee));
            return "redirect:/user/create";
        }
        if (employee < 1 || employee > 9999){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Wrong Winning ID format!");
            redirectAttributes.addFlashAttribute("userModel", new User(username, password, authority, 0));
            return "redirect:/user/create";
        }

        long users = userService.findAll().stream()
                .filter(x -> x.getUserName().equals(username) || x.getEmployee() == employee)
                .count();
        if (users > 0){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: User allready exists!");
            redirectAttributes.addFlashAttribute("userModel", new User(null, password, authority, 0));
            return "redirect:/user/create";
        }
        userService.save(new User(username, password, authority, employee));
        redirectAttributes.addFlashAttribute("successMessage", "Success: User was created!");
        return "redirect:/log/teamleader";
    }

    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam(value = "deleteButton") Long id){
        try {
            User user = userService.findById(id);
            //userService.deleteUser(id);
            List<LogEntity> entities = entityService.findAll().stream().filter(x -> x.getUser().getId()==id).collect(Collectors.toList());
            for (LogEntity entity : entities) {
                entityService.delete(entity);
            }
            userService.delete(user);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/user";
    }

    @RequestMapping(path = "/changepassword", method = RequestMethod.GET)
    public String changePasswordForm(Model model
    ){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        model.addAttribute("isLogged", true);
        model.addAttribute("retrieval", null);
        return "user/changePassword";
    }

    @RequestMapping(path = "/changepassword/change", method = RequestMethod.GET)
    public String changePassword(RedirectAttributes redirectAttributes,
                                 Model model
                                ){
        if (!model.containsAttribute("password")||!model.containsAttribute("oldPassword")||!model.containsAttribute("repeatPassword")){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Something went wrong!");
            return "redirect:/log";
        }
        String password = Objects.requireNonNull(model.getAttribute("password")).toString();
        String oldPassword = Objects.requireNonNull(model.getAttribute("oldPassword")).toString();
        String repeatPassword = Objects.requireNonNull(model.getAttribute("repeatPassword")).toString();
        if (!userService.checkPassword(password)){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Wrong password format!");
            return "redirect:/user/changepassword";
        }
        if (!password.equals(repeatPassword)){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Password does not match!");
            return "redirect:/user/changepassword";
        }
        User user = userService.findByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        if (!passwordEncoder.matches(oldPassword, user.getPassword())){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Old password is wrong!");
            return "redirect:/user/changepassword";
        }
        System.out.println("here 1");
        user.setPassword(password);
        System.out.println("here 2: userId=" + user.getId());
        userService.save(user);
        System.out.println("here 3");
        redirectAttributes.addFlashAttribute("successMessage", "Success: Password was changed!");
        return "redirect:/log";
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        return "redirect:/user";
    }

}
