package com.example.winninglog.Controllers;

import com.example.winninglog.Models.Project;
import com.example.winninglog.Models.User;
import com.example.winninglog.Services.ProjectService;
import com.example.winninglog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/assign")
public class AssignController {

    private final UserService userService;
    private final ProjectService projectService;

    @Autowired
    public AssignController(UserService userService, ProjectService projectService) {
        this.userService = userService;
        this.projectService = projectService;
    }

    @RequestMapping(path = {"/create"}, method = RequestMethod.GET)
    public String user(Model model){
        List<User> users = userService.findAll().stream().sorted(Comparator.comparing(User::getEmail)).collect(Collectors.toList());
        List<Project> projects = projectService.findAll().stream().sorted(Comparator.comparing(Project::getCustomer)).collect(Collectors.toList());
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        model.addAttribute("users", users);
        model.addAttribute("projects", projects);
        return "assign/create";
    }

    @RequestMapping(path = "/create/add", method = RequestMethod.GET)
    public String create(RedirectAttributes redirectAttributes,
                         @RequestParam(name = "user") Long userId,
                         @RequestParam(name = "project") Long projectId
    ){
        if (userId == null || projectId == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Invalid input!");
            return "redirect:/assign/create";
        }
        try {
            Project project = projectService.findById(projectId);
            User user = userService.findById(userId);
            project.setUsers(user);
            projectService.save(project);
            redirectAttributes.addFlashAttribute("successMessage", "Success: Project " + project.getCustomerPlusName() + " was assigned to " + user.getEmail()+ ".") ;
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/assign/create";
    }
}
