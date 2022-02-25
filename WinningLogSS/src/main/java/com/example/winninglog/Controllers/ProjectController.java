package com.example.winninglog.Controllers;

import com.example.winninglog.Models.LogEntity;
import com.example.winninglog.Models.Project;
import com.example.winninglog.Models.ProjectStatistics;
import com.example.winninglog.Services.EntityService;
import com.example.winninglog.Services.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;
    private final EntityService entityService;

    @Autowired
    public ProjectController(ProjectService projectService, EntityService entityService) {
        this.projectService = projectService;
        this.entityService = entityService;
    }

    @RequestMapping(path = {"","/"}, method = RequestMethod.GET)
    public String users(Model model){
        List<Project> projects = projectService.findAll().stream().sorted(Comparator.comparing(Project::getCustomerPlusName)).collect(Collectors.toList());
        if (!model.containsAttribute("successMessage")){
            model.addAttribute("successMessage", null);
        }
        model.addAttribute("projects", projects);
        return "project/index";
    }

    @RequestMapping(path = "/create", method = RequestMethod.GET)
    public String project(Model model){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
            model.addAttribute("projectModel", new Project(null, null, null));
        }
        return "project/create";
    }

    @RequestMapping(path = "/statistics", method = RequestMethod.GET)
    public String projectStatistics(Model model){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("statisticsList")){
            List<ProjectStatistics> list = new ArrayList<>();
            model.addAttribute("statisticsList", list);
            model.addAttribute("total", null);
            model.addAttribute("dateInterval", null);
        }
        return "project/statistics";
    }

    @RequestMapping(path = {"/statistics/filter"}, method = RequestMethod.GET)
    public String projectStatisticsFilter(RedirectAttributes redirectAttributes,
                                          @RequestParam(name = "datumStart") String startDate,
                                          @RequestParam(name = "datumEnd") String endDate,
                                          @RequestParam(name = "projectId", required = false) Long id
                                         ){
        try{
            List<ProjectStatistics> listOfStatistics = new ArrayList<>();
            List<LogEntity> entitys = new ArrayList<>();
            List<String> uniqueProjects = new ArrayList<>();
            if (id != null){
                entitys = entityService.findAll().stream()
                        .filter(x -> entityService.checkDateInterval(x.getDate(), startDate, endDate) && x.getProject().getId() == id)
                        .collect(Collectors.toList());
                uniqueProjects = entitys.stream()//projectants...
                        .map(x -> x.getUser().getEmail())
                        .distinct()
                        .collect(Collectors.toList());
            } else {
                entitys = entityService.findAll().stream()
                        .filter(x -> entityService.checkDateInterval(x.getDate(), startDate, endDate))
                        .collect(Collectors.toList());
                uniqueProjects = entitys.stream()
                        .map(x -> x.getProject().getCustomerPlusName())
                        .distinct()
                        .collect(Collectors.toList());
            }

            float projektTotal = 0;
            float korrekturInTotal = 0;
            float korrekturExtMistakeTotal = 0;
            float korrekturExtClientTotal = 0;
            float freigabeTotal = 0;
            float mehrarbeitTotal = 0;
            for (int i = 0; i < uniqueProjects.size(); i++) {
                int icko = i;
                List<LogEntity> entityOfProject = new ArrayList<>();
                List<String> unique = uniqueProjects;
                if (id != null){
                    entityOfProject = entitys.stream()
                            .filter(x -> x.getUser().getEmail().equals(unique.get(icko)))
                            .collect(Collectors.toList());
                } else {
                    entityOfProject = entitys.stream()
                            .filter(x -> x.getProject().getCustomerPlusName().equals(unique.get(icko)))
                            .collect(Collectors.toList());
                }
                String name = uniqueProjects.get(i);
                float projekt = 0;
                float korrekturIn = 0;
                float korrekturExtMistake = 0;
                float korrekturExtClient = 0;
                float freigabe = 0;
                float mehrarbeit = 0;
                for (LogEntity logEntity : entityOfProject) {
                    float stunden = logEntity.getStunden();
                    if (logEntity.getArbeitsart().equals("Projekt")) {
                        projekt = projekt + stunden;
                        projektTotal = projektTotal + stunden;
                    } else if (logEntity.getArbeitsart().equals("KorrekturIn")) {
                        korrekturIn = korrekturIn + stunden;
                        korrekturInTotal = korrekturInTotal + stunden;
                    } else if (logEntity.getArbeitsart().equals("KorrekturExtMistake")) {
                        korrekturExtMistake = korrekturExtMistake + stunden;
                        korrekturExtMistakeTotal = korrekturExtMistakeTotal + stunden;
                    } else if (logEntity.getArbeitsart().equals("KorrekturExtClient")) {
                        korrekturExtClient = korrekturExtClient + stunden;
                        korrekturExtClientTotal = korrekturExtClientTotal + stunden;
                    } else if (logEntity.getArbeitsart().equals("Freigabe")) {
                        freigabe = freigabe + stunden;
                        freigabeTotal = freigabeTotal + stunden;
                    } else if (logEntity.getArbeitsart().equals("Mehrarbeit")) {
                        mehrarbeit = mehrarbeit + stunden;
                        mehrarbeitTotal = mehrarbeitTotal + stunden;
                    }
                }
                listOfStatistics.add(new ProjectStatistics(name, projekt, korrekturIn, korrekturExtMistake, korrekturExtClient, freigabe, mehrarbeit));
                projekt = 0;
                korrekturIn = 0;
                korrekturExtMistake = 0;
                korrekturExtClient = 0;
                freigabe = 0;
                mehrarbeit = 0;
            }
            List<ProjectStatistics> listOfStatisticsSorted = listOfStatistics.stream()
                    .sorted(Comparator.comparing(ProjectStatistics::getProjectName))
                    .collect(Collectors.toList());
            redirectAttributes.addFlashAttribute("statisticsList", listOfStatisticsSorted);
            redirectAttributes.addFlashAttribute("dateInterval", startDate + " - " + endDate);
            redirectAttributes.addFlashAttribute("total", new ProjectStatistics("Total:", projektTotal, korrekturInTotal, korrekturExtMistakeTotal, korrekturExtClientTotal, freigabeTotal, mehrarbeitTotal));
            if (id != null){
                redirectAttributes.addFlashAttribute("projectId", id);
                return "redirect:/project/details";
            } else {
                return "redirect:/project/statistics";
            }
        } catch (Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "ERROR: Something didn't workout!");
            return "redirect:/project/statistics";
        }
    }

    @RequestMapping(path = "/details", method = RequestMethod.GET)
    public String projectDetails(Model model,
                                    @RequestParam(name = "detailsButton", required = false) Long projectId
                                ){
        if (!model.containsAttribute("errorMessage")){
            model.addAttribute("errorMessage", null);
        }
        if (!model.containsAttribute("statisticsList")){
            List<ProjectStatistics> list = new ArrayList<>();
            model.addAttribute("statisticsList", list);
            model.addAttribute("total", null);
        }
        if (projectId == null && !model.containsAttribute("projectId")){
            return "redirect:/project/";
        } else if (projectId == null && model.containsAttribute("projectId")){
            model.addAttribute("projectName", projectService.findById(Long.parseLong(Objects.requireNonNull(model.getAttribute("projectId")).toString())));
        } else {
            model.addAttribute("projectName", projectService.findById(projectId));
        }
        return "project/details";
    }


    @RequestMapping(path = "/create/add", method = RequestMethod.GET)
    public String create(Model model,
                         RedirectAttributes redirectAttributes,
                         @RequestParam(name = "customer") String customer,
                         @RequestParam(name = "projectName") String projektName
                        ) throws MissingServletRequestParameterException {
        if (customer == null || projektName == null){
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Invalid input!");
            return "redirect:/project/create";
        }

        Project project = new Project(null, customer, projektName);
        try {
            redirectAttributes.addFlashAttribute("successMessage", "Success: Project was created!");
            projectService.save(project);
            return "redirect:/project";
        } catch (Exception e) {
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", "Error: Project could not be assigned!");
            return "redirect:/project";
        }
    }

    @RequestMapping(path = "/delete", method = RequestMethod.GET)
    public String deleteUser(@RequestParam(value = "deleteButton") Long id){
        try {
            Project project = projectService.findById(id);
            List<LogEntity> entities = entityService.findAll().stream().filter(x -> x.getProject().getId() == id).collect(Collectors.toList());
            for (LogEntity entity : entities) {entityService.delete(entity);};
            projectService.delete(project);
            return "redirect:/project";
        } catch (Exception e){
            e.printStackTrace();
            return "redirect:/project";
        }
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public String handleMissingParams(MissingServletRequestParameterException ex) {
        String name = ex.getParameterName();
        System.out.println(name + " parameter is missing");
        return "redirect:/project";
    }
}
