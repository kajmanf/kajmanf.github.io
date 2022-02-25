package com.example.winninglog.Controllers;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Models.Project;
import com.example.winninglog.Models.User;
import com.example.winninglog.Services.EntityService;
import com.example.winninglog.Services.ProjectService;
import com.example.winninglog.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/log")
public class EntityController {

    private final EntityService entityService;
    private final ProjectService projectService;
    private final UserService userService;

    private final Map<String, String> months = new HashMap<>() {{
        put("01", "January");
        put("02", "February");
        put("03", "March");
        put("04", "April");
        put("05", "May");
        put("06", "June");
        put("07", "July");
        put("08", "August");
        put("09", "September");
        put("10", "October");
        put("11", "November");
        put("12", "December");
    }};

    private long userId = 0;
    private String authority = "USER";

    @Autowired
    public EntityController(EntityService entityService, ProjectService projectService, UserService userService) {
        this.entityService = entityService;
        this.projectService = projectService;
        this.userService = userService;
    }

    @RequestMapping(path = {"/",""}, method = RequestMethod.GET)
    public String log(Model model,
                      @RequestParam(value = "user", required = false) Long userToShow
                    ){
        Set<Project> projects = new HashSet<>();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            String currentUserName = authentication.getName();
            User user = userService.findByUsername(currentUserName);
            authority = user.getRoles();
            userId = user.getId();
            projects = user.getProjects();
        }
        if (userToShow != null){
            userId = userToShow;
        }
        if (!model.containsAttribute("user") && userId == 0){
            return "redirect:/";
        }
        if (this.authority.contains("TEAMLEADER")||this.authority.contains("ADMIN")&& userToShow==null){
            return "redirect:/log/teamleader";
        }
        String yearMonth1 = LocalDate.now().toString().substring(0,7);
        if (model.containsAttribute("yearMonth")){
            yearMonth1 = Objects.requireNonNull(model.getAttribute("yearMonth")).toString();
        }
        String yearMonth = yearMonth1;
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        if (!model.containsAttribute("lastEntity")){
            model.addAttribute("lastEntity", new LogEntity(null, null, null, null, null, null, null, null, null, null, 0));
            model.addAttribute("lastEntityName", null);
            model.addAttribute("remake", false);
        }
        if (!model.containsAttribute("entitys")){
            List<LogEntity> entitys = entityService.findAll().stream()
                    .sorted(Comparator.comparing(LogEntity::getDate))
                    .filter(x -> x.getDate().substring(0,7).equals(yearMonth) && x.getUser().getId() == userId)
                    .collect(Collectors.toList());
            Collections.reverse(entitys);
            model.addAttribute("entitys", entitys);
            model.addAttribute("hoursSum", countHours(entitys));
        }
        model.addAttribute("projects", projects);
        model.addAttribute("month", months.get(yearMonth.substring(5,7)));
        model.addAttribute("year", yearMonth.substring(0,4));
        return "user/log";
    }

    @RequestMapping(path = {"/nextDate"}, method = RequestMethod.GET)
    public String logTeamleaderFilter(RedirectAttributes redirectAttributes,
                                      @RequestParam(name = "next") String next,
                                      @RequestParam(name = "month") String monthString,
                                      @RequestParam(name = "year") String yearString
                                    ){
        try{
            int month = 0;
            for(Map.Entry<String, String> entry: months.entrySet()) {
                if(entry.getValue().equals(monthString)) {
                    month = Integer.parseInt(entry.getKey());
                    break;
                }
            }
            int year = Integer.parseInt(yearString);
            if (next.equals("next") && month != 12){
                month++;
            } else if (next.equals("next")){
                month = 1;
                year++;
            } else if (next.equals("back") && month != 1){
                month--;
            } else if (next.equals("back")){
                month = 12;
                year--;
            }
            String yearMonth = year + "-";
            if (month < 10){
                yearMonth = yearMonth + "0" + month;
            } else {
                yearMonth = yearMonth + month;
            }
            redirectAttributes.addFlashAttribute("yearMonth", yearMonth);
            return "redirect:/log";
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "ERROR: Something didn't workout!");
            return "redirect:/log";
        }
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public String create(RedirectAttributes redirectAttributes,
                         @RequestParam(name = "remake") boolean remake,
                         @RequestParam(name = "entityId", required = false) Long entityId,
                         @RequestParam(name = "project") Long projectId,
                         @RequestParam(name = "datum") String date,
                         @RequestParam(name = "gebaude") String gebaude,
                         @RequestParam(name = "niveau") String niveau,
                         @RequestParam(name = "planType") String planType,
                         @RequestParam(name = "planName") String planName,
                         @RequestParam(name = "logIndex") String planIndex,
                         @RequestParam(name = "arbeitsart") String arbeitsart,
                         @RequestParam(name = "stunden") float stunden
                        ) throws MissingServletRequestParameterException {
        Project project = projectService.findById(projectId);
        User user = userService.findById(userId);
        LogEntity logEntity = new LogEntity(null, project, user, date, gebaude, niveau, planType, planName, planIndex, arbeitsart, stunden);
        if (remake){
            redirectAttributes.addFlashAttribute("successMessage", "Success: Log Entry was updated!");
            logEntity.setId(entityId);
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Success: Log Entry was created!");
        }
        entityService.save(logEntity);
        return "redirect:/log";

    }

    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String deleteEntity(@RequestParam(value = "deleteButton") Long id) throws MissingServletRequestParameterException{
        try {
            LogEntity entity = entityService.findById(id);
            entityService.delete(entity);
        } catch (Exception e){
            e.printStackTrace();
        }
        return "redirect:/log";
    }

    @RequestMapping(path = "/last", method = RequestMethod.GET)
    public String addLast(RedirectAttributes redirectAttributes){
        List<LogEntity> entitys = entityService.findAll();
        if (entitys.size() > 0){
            redirectAttributes.addFlashAttribute("lastEntity", entitys.get(entitys.size()-1));
            redirectAttributes.addFlashAttribute("lastEntityName", entitys.get(entitys.size()-1).getProject().getCustomerPlusName());
            redirectAttributes.addFlashAttribute("remake", false);
        }
        return "redirect:/log";
    }

    @RequestMapping(path = "/edit", method = RequestMethod.GET)
    public String edit(RedirectAttributes redirectAttributes,
                       @RequestParam(value = "editButton") Long id
                    ) throws MissingServletRequestParameterException {
        LogEntity entity = entityService.findById(id);
        if (entity != null) {
            redirectAttributes.addFlashAttribute("lastEntity", entity);
            redirectAttributes.addFlashAttribute("remake", true);
            redirectAttributes.addFlashAttribute("lastEntityName", entity.getProject().getCustomerPlusName());
        }
        return "redirect:/log";
    }

    @RequestMapping(path = {"/teamleader"}, method = RequestMethod.GET)
    public String logTeamleader(Model model){
        List<User> users = userService.findAll();
        List<Project> projects = projectService.findAll();
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("entitys")){
            List<LogEntity> entitys = entityService.findAll().stream().sorted(Comparator.comparing(LogEntity::getDate)).collect(Collectors.toList());
            Collections.reverse(entitys);
            model.addAttribute("entitys", entitys);
            model.addAttribute("hoursSum", countHours(entitys));
        }
        model.addAttribute("users", users);
        model.addAttribute("projects", projects);
        return "user/teamleader";
    }

    @RequestMapping(path = {"/teamleader/filter"}, method = RequestMethod.POST)
    public String logTeamleaderFilter(RedirectAttributes redirectAttributes,
                                @RequestParam(name = "datumStart", required = true) String startDate,
                                @RequestParam(name = "datumEnd") String endDate,
                                @RequestParam(name = "user", required = false) Long userId,
                                @RequestParam(name = "project", required = false) Long projectId
                                ) {
        try{
            List<LogEntity> entitys = entityService.findAll().stream().sorted(Comparator.comparing(LogEntity::getDate)).collect(Collectors.toList());
            List<LogEntity> filteredEntitys = new ArrayList<>();
            Collections.reverse(entitys);
            if (userId == null && projectId == null){
                filteredEntitys = entitys.stream()
                        .filter(x -> entityService.checkDateInterval(x.getDate(), startDate, endDate))
                        .collect(Collectors.toList());
            } else if (userId != null && projectId == null){
                filteredEntitys = entitys.stream()
                        .filter(x -> x.getUser().getId() == userId && entityService.checkDateInterval(x.getDate(), startDate, endDate))
                        .collect(Collectors.toList());
            } else if (userId == null && projectId != null){
                filteredEntitys = entitys.stream()
                        .filter(x -> x.getProject().getId() == projectId && entityService.checkDateInterval(x.getDate(), startDate, endDate))
                        .collect(Collectors.toList());
            }
            redirectAttributes.addFlashAttribute("entitys", filteredEntitys);
            redirectAttributes.addFlashAttribute("hoursSum", countHours(filteredEntitys));
            return "redirect:/log/teamleader";
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "ERROR: Something didn't workout!");
            return "redirect:/log/teamleader";
        }

    }

    public float countHours(List<LogEntity> entities){
        float sumOfStunden = entities.stream()
                .map(LogEntity::getStunden)//same as x -> x.getStunden()
                .reduce(new Float(0), Float::sum);//same as a+b
        return sumOfStunden;
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        return "redirect:/log";
    }
}
