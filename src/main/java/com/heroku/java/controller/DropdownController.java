package com.heroku.java.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Controller
@RequestMapping("/dropdown")
public class DropdownController {

    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/values")
    public String getDropdownValues(Model model) {
        Query query = entityManager.createQuery("SELECT a.accountId FROM Account a");
        List<String> accountIds = query.getResultList();
        model.addAttribute("accountIds", accountIds);
        return "dropdownValues";
    }
}
