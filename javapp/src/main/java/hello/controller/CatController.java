package hello.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.beans.propertyeditors.CustomDateEditor;
 
import hello.dao.CatRepository;
import hello.model.Cat;
import hello.CatService;
 
@Controller
public class CatController {
        @Autowired
        private CatRepository catrepository;
        @Autowired
        private CatService catservice;
        private ArrayList catModelList;
        private List<String> catrisklist = null;
        
        @GetMapping(value = "/")
        public String cathome(
                @RequestParam(value = "search", required = false) 
                @DateTimeFormat(pattern = "yyyy-MM-dd") Date q,
                Model model) {
                        if (q != null) {
                                catModelList = new ArrayList();
                                System.out.println("q is = " + q);
                                catrisklist = catservice.atriskcats(q);
                                for (String name : catrisklist) {
                                        System.out.println("Cats in repository are : " + catrepository.findAll());
                                        Cat catgy = catrepository.findByName(name);
                                        System.out.println(catgy.toString() + "catgy name : " + catgy.getName());
                                        catModelList.add(catgy);
                                        System.out.println("This cat's name is : " + catgy.getName());
                                }
                        }
                        model.addAttribute("search", catModelList);
                        model.addAttribute("cats", catrepository.findAll());
                        return "index";
                }
 
        @PostMapping(value = "/")
        public String addcat(@RequestParam("name") String name,
                        @RequestParam("rescued") @DateTimeFormat(pattern = "yyyy-MM-dd") Date rescued,
                        @RequestParam("vaccinated") Boolean vaccinated, Model model) {
                catservice.addCat(name, rescued, vaccinated);
                System.out.println("name = " + name + ",rescued = " + rescued + ", vaccinated = " + vaccinated);
                return "redirect:/";
        }
 
        @PostMapping(value = "/delete")
        public String deleteCat(@RequestParam("name") String name,
                        @RequestParam("id") Long id) {
                catservice.deleteCat(name, id);
                System.out.println("Cat named = " + name + "was removed from our database. Hopefully he or she was adopted.");
                return "redirect:/";
 
        }
        
        @PostMapping(value = "/genkey")
        public String genkey(@RequestParam("name") String name,
                        @RequestParam("rescued") @DateTimeFormat(pattern = "yyyy-MM-dd") Date rescued,
                        @RequestParam("vaccinated") Boolean vaccinated, Model model) {
                catservice.getGeneratedKey(name, rescued, vaccinated);
                System.out.println("name = " + name + ",rescued = " + rescued + ", vaccinated = " + vaccinated);
                return "redirect:/";
        }
        
        @GetMapping(value = "/createdb")
        public String createdb(
                @RequestParam(value = "search", required = false) 
                @DateTimeFormat(pattern = "yyyy-MM-dd") Date q,
                Model model) {
                        catservice.createDb();
                        System.out.println("Created db");
                        return "redirect:/";
                }
}