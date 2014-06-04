package com.dianping.lion.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.lion.entity.Product;
import com.dianping.lion.entity.Team;
import com.dianping.lion.service.ProductService;
import com.dianping.lion.service.TeamService;

@Controller
@RequestMapping("/product2")
public class ProductController {

    @Autowired
    private TeamService teamService;
    @Autowired
    private ProductService productService;
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list(@RequestParam(value="team", required=false) String teamName) {
        Team team = null;
        Result result = null;
        List<Product> products = null;
        List<String> prdNames = null;
        
        if(teamName != null) {
            team = teamService.findTeam(teamName);
            if(team == null) {
                result = Result.createErrorResult(String.format("Team %s does not exist", teamName));
            } else {
                products = productService.findProductByTeamID(team.getId());
            }
        } else {
            products = productService.findAll();
        }
        
        if(products != null) {
            prdNames = new ArrayList<String>();
            for(Product p : products) {
                prdNames.add(p.getName());
            }
            result = Result.createSuccessResult(prdNames);
        }
        
        return result;
    }
    
}
