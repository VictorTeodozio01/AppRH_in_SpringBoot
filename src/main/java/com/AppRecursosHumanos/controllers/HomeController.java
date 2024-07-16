package com.AppRecursosHumanos.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

	// Mapeia a requisição GET para a raiz da aplicação ("/")
	@GetMapping("/")
	public ModelAndView abrirIndex() {
		// Cria um objeto ModelAndView apontando para a página "index"
		ModelAndView mv = new ModelAndView("index");
		return mv; // Retorna o objeto ModelAndView para renderizar a página "index"
	}
}
