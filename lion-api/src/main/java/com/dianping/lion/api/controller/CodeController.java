package com.dianping.lion.api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.dianping.lion.util.SecurityUtils;

@Controller
public class CodeController {

    @RequestMapping(value = "/encode", method = RequestMethod.GET)
    @ResponseBody
    public Result encode(@RequestParam(value="text") String text) {
        return Result.createSuccessResult(SecurityUtils.tryEncode(text));
    }
   
    @RequestMapping(value = "/decode", method = RequestMethod.GET)
    @ResponseBody
    public Result decode(@RequestParam(value="text") String text) {
        return Result.createSuccessResult(SecurityUtils.tryDecode(text));
    }
    
}
