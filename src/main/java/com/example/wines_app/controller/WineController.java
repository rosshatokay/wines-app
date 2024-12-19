package com.example.wines_app.controller;

import com.example.wines_app.dto.WineDto;
import com.example.wines_app.service.WineService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller

public class WineController {
    private WineService wineService;

    @Autowired
    public WineController(WineService wineService) {
        this.wineService = wineService;
    }

    @GetMapping("/wines")
    public String listWines(Model model) {
        List<WineDto> wines = wineService.findAllWines();
        model.addAttribute("wines", wines);
        return "wines-list";
    }
}
