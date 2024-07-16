package com.AppRecursosHumanos.controllers;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.AppRecursosHumanos.models.Candidato;
import com.AppRecursosHumanos.models.Vaga;
import com.AppRecursosHumanos.repository.CandidatoRepository;
import com.AppRecursosHumanos.repository.VagaRepository;

@Controller
public class VagaController {

	@Autowired
	private VagaRepository vr;
	
	@Autowired
	private CandidatoRepository cr;

	// Método para exibir o formulário de cadastro de vaga
	@GetMapping("/cadastrarVaga")
	public String form() {
		return "vaga/cadastrar-vaga";
	}

	// Método para cadastrar uma nova vaga
	@PostMapping("/cadastrarVaga")
	public String cadastrarVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors()) {
			attributes.addFlashAttribute("mensagem", "Verifique os campos...");
			return "redirect:/cadastrarVaga";
		}
		vr.save(vaga);
		attributes.addFlashAttribute("mensagem", "Vaga cadastrada com sucesso!");
		return "redirect:/cadastrarVaga";
	}

	// Método para listar todas as vagas cadastradas
	@GetMapping("/vagas")
	public ModelAndView listaVagas() {
		ModelAndView mv = new ModelAndView("vaga/lista-vaga");
		mv.addObject("vagas", vr.findAll());
		return mv;
	}

	// Método para exibir detalhes de uma vaga específica
	@GetMapping("/vaga/{codigo}")
	public ModelAndView detalhesVaga(@PathVariable("codigo") long codigo) {
		ModelAndView mv = new ModelAndView("vaga/detalhes-vaga");
		Vaga vaga = vr.findByCodigo(codigo);
		mv.addObject("vaga", vaga);
		mv.addObject("candidatos", cr.findByVaga(vaga));
		return mv;
	}

	// Método para deletar uma vaga específica
	@GetMapping("/deletarVaga")
	public String deletarVaga(long codigo) {
		vr.delete(vr.findByCodigo(codigo));
		return "redirect:/vagas";
	}

	// Método para adicionar um candidato a uma vaga específica
	@PostMapping("/vaga/{codigo}")
	public String adicionarCandidato(@PathVariable("codigo") long codigo, @Valid Candidato candidato,
			BindingResult result, RedirectAttributes attributes) {
		if (result.hasErrors() || cr.findByRg(candidato.getRg()) != null) {
			attributes.addFlashAttribute("mensagem", result.hasErrors() ? "Verifique os campos" : "RG duplicado");
			return "redirect:/vaga/{codigo}";
		}
		candidato.setVaga(vr.findByCodigo(codigo));
		cr.save(candidato);
		attributes.addFlashAttribute("mensagem", "Candidato adicionado com sucesso!");
		return "redirect:/vaga/{codigo}";
	}

	// Método para deletar um candidato com base no RG
	@GetMapping("/deletarCandidato")
	public String deletarCandidato(String rg) {
		Candidato candidato = cr.findByRg(rg);
		cr.delete(candidato);
		return "redirect:/vaga/" + candidato.getVaga().getCodigo();
	}

	// Método para exibir o formulário de edição de uma vaga específica
	@GetMapping("/editar-vaga")
	public ModelAndView editarVaga(long codigo) {
		ModelAndView mv = new ModelAndView("vaga/update-vaga");
		mv.addObject("vaga", vr.findByCodigo(codigo));
		return mv;
	}

	// Método para atualizar uma vaga existente
	@PostMapping("/editar-vaga")
	public String atualizarVaga(@Valid Vaga vaga, BindingResult result, RedirectAttributes attributes) {
		if (!result.hasErrors()) {
			vr.save(vaga);
			attributes.addFlashAttribute("success", "Vaga alterada com sucesso!");
		}
		return "redirect:/vaga/" + vaga.getCodigo();
	}
}
