package com.AppRecursosHumanos.controllers;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.AppRecursosHumanos.models.Dependente;
import com.AppRecursosHumanos.models.Funcionario;
import com.AppRecursosHumanos.repository.DependenteRepository;
import com.AppRecursosHumanos.repository.FuncionarioRepository;

@Controller
public class FuncionarioController {

	@Autowired
	private FuncionarioRepository fr; // Injeta o repositório de Funcionário

	@Autowired
	private DependenteRepository dr; // Injeta o repositório de Dependente

	@GetMapping("/cadastrarFuncionario")
	public String form() {
		// Retorna o formulário para cadastro de funcionário
		return "funcionario/cadastrar-funcionario";
	}

	@PostMapping("/cadastrarFuncionario")
	public String form(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {
		// Trata a submissão do formulário de cadastro de funcionário
		if (result.hasErrors()) {
			// Se houver erros de validação, redireciona de volta ao formulário com uma mensagem de erro
			attributes.addFlashAttribute("mensagem", "Verifique os campos");
			return "redirect:/cadastrarFuncionario";
		}
		// Salva o funcionário no banco de dados
		fr.save(funcionario);
		// Adiciona uma mensagem de sucesso e redireciona para o formulário de cadastro
		attributes.addFlashAttribute("mensagem", "Funcionário cadastrado com sucesso!");
		return "redirect:/cadastrarFuncionario";
	}

	@GetMapping("/funcionarios")
	public ModelAndView listaFuncionarios() {
		// Retorna a lista de todos os funcionários cadastrados
		ModelAndView mv = new ModelAndView("funcionario/lista-funcionario");
		mv.addObject("funcionarios", fr.findAll());
		return mv;
	}

	@GetMapping("/detalhes-funcionario/{id}")
	public ModelAndView detalhesFuncionario(@PathVariable("id") long id) {
		// Exibe os detalhes de um funcionário específico, incluindo seus dependentes
		ModelAndView mv = new ModelAndView("funcionario/detalhes-funcionario");
		Funcionario funcionario = fr.findById(id);
		mv.addObject("funcionarios", funcionario);
		mv.addObject("dependentes", dr.findByFuncionario(funcionario));
		return mv;
	}

	@PostMapping("/detalhes-funcionario/{id}")
	public String detalhesFuncionarioPost(@PathVariable("id") long id, @Valid Dependente dependente, BindingResult result, RedirectAttributes attributes) {
		// Trata a submissão do formulário de cadastro de dependente para um funcionário específico
		if (result.hasErrors()) {
			// Se houver erros de validação, redireciona de volta aos detalhes do funcionário com uma mensagem de erro
			attributes.addFlashAttribute("mensagem", "Verifique os campos!");
			return "redirect:/detalhes-funcionario/{id}";
		}
		// Verifica se já existe um dependente com o mesmo CPF
		if (dr.findByCpf(dependente.getCpf()) != null) {
			attributes.addFlashAttribute("mensagem_erro", "CPF duplicado");
			return "redirect:/detalhes-funcionario/{id}";
		}
		// Associa o dependente ao funcionário e salva no banco de dados
		dependente.setFuncionario(fr.findById(id));
		dr.save(dependente);
		attributes.addFlashAttribute("mensagem", "Dependente adicionado com sucesso");
		return "redirect:/detalhes-funcionario/{id}";
	}

	@GetMapping("/deletarFuncionario")
	public String deletarFuncionario(long id) {
		// Deleta um funcionário específico pelo seu ID
		fr.delete(fr.findById(id));
		return "redirect:/funcionarios";
	}

	@GetMapping("/editar-funcionario")
	public ModelAndView editarFuncionario(long id) {
		// Retorna o formulário para edição de um funcionário específico
		ModelAndView mv = new ModelAndView("funcionario/update-funcionario");
		mv.addObject("funcionario", fr.findById(id));
		return mv;
	}

	@PostMapping("/editar-funcionario")
	public String updateFuncionario(@Valid Funcionario funcionario, BindingResult result, RedirectAttributes attributes) {
		// Trata a submissão do formulário de edição de funcionário
		fr.save(funcionario);
		// Adiciona uma mensagem de sucesso e redireciona para os detalhes do funcionário atualizado
		attributes.addFlashAttribute("success", "Funcionário alterado com sucesso!");
		return "redirect:/detalhes-funcionario/" + funcionario.getId();
	}

	@GetMapping("/deletarDependente")
	public String deletarDependente(String cpf) {
		// Deleta um dependente específico pelo seu CPF
		Dependente dependente = dr.findByCpf(cpf);
		dr.delete(dependente);
		return "redirect:/detalhes-funcionario/" + dependente.getFuncionario().getId();
	}
}
