package com.AppRecursosHumanos.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import com.AppRecursosHumanos.repository.*;

@Controller
public class BuscaController {

    @Autowired
    private FuncionarioRepository fr;
    @Autowired
    private VagaRepository vr;
    @Autowired
    private DependenteRepository dr;
    @Autowired
    private CandidatoRepository cr;
    
    //GET
    @GetMapping("/Buscar")
    public ModelAndView abrirIndex() {
        return new ModelAndView("/buscar");
    }
    //POST
    @PostMapping("/Buscar")
    public ModelAndView buscarIndex(@RequestParam("buscar") String buscar, @RequestParam("nome") String nome) {    
        ModelAndView mv = new ModelAndView("/buscar");
        mv.addObject("mensagem", "Resultados da busca por " + buscar);
        
        switch (nome) {
            case "nomefuncionario": mv.addObject("funcionarios", fr.findByNomes(buscar)); break;
            case "nomedependente": mv.addObject("dependentes", dr.findByNomesDependentes(buscar)); break;
            case "nomecandidato": mv.addObject("candidatos", cr.findByNomesCandidatos(buscar)); break;
            case "titulovaga": mv.addObject("vagas", vr.findByNomesVaga(buscar)); break;
            default:
                mv.addObject("funcionarios", fr.findByNomes(buscar));
                mv.addObject("dependentes", dr.findByNomesDependentes(buscar));
                mv.addObject("candidatos", cr.findByNomesCandidatos(buscar));
                mv.addObject("vagas", vr.findByNomesVaga(buscar));
        }
        return mv;
    }
}