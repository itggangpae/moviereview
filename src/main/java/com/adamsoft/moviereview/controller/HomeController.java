package com.adamsoft.moviereview.controller;

import com.adamsoft.moviereview.dto.PageRequestDTO;
import com.adamsoft.moviereview.service.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Log4j2
@RequiredArgsConstructor
public class HomeController {

    private final MovieService movieService; //final

    @GetMapping("/")
    public String list(PageRequestDTO pageRequestDTO, Model model){
        log.info("pageRequestDTO: " + pageRequestDTO);
        model.addAttribute("result", movieService.getList(pageRequestDTO));
        return "movie/list";
    }
}
