package cl.sergiocarocca.cita_ideal_cl.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sergiocarocca.cita_ideal_cl.entity.Plan;
import cl.sergiocarocca.cita_ideal_cl.repository.PlanRepository;

@RestController
@RequestMapping("/api/planes")
@CrossOrigin(origins = "*") // Para que tu frontend pueda consultar sin bloqueos
public class PlanController {

    @Autowired
    private PlanRepository planRepository;

    @GetMapping
    public List<Plan> listarPlanes() {
        return planRepository.findAll();
    }
}