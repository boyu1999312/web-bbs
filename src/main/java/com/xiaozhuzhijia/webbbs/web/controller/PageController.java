package com.xiaozhuzhijia.webbbs.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {

    @RequestMapping("/xzzj/{page}")
    public String jump(@PathVariable String page){

        return page;
    }
}
