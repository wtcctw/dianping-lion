package com.dianping.lion.api.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.lion.entity.Team;
import com.dianping.lion.service.TeamService;

@Controller
@RequestMapping("/team2")
public class TeamController {

    @Autowired
    private TeamService teamService;
    
    @RequestMapping(value = "/list", method = RequestMethod.GET)
    @ResponseBody
    public Result list() {
        List<Team> teams = teamService.findAll();
        List<String> teamNames = new ArrayList<String>();
        for(Team team : teams) {
            teamNames.add(team.getName());
        }
        return Result.createSuccessResult(teamNames);
    }
    
}
