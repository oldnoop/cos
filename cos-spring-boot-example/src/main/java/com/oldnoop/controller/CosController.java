package com.oldnoop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.oldnoop.cos.CosServerProperties;
import com.oldnoop.cos.tmpkey.CosTmpKeyProperties;

@Controller
@RequestMapping("/cos")
public class CosController {
	
	@Autowired
	private CosServerProperties serverProperties;
	
	@Autowired
	private CosTmpKeyProperties keyProperties;

	@RequestMapping("/page")
	public String page(Model model){
		model.addAttribute("serverProperties", serverProperties);
		model.addAttribute("keyProperties", keyProperties);
		return "cos/page";
	}
	
}
