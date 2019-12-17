/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.rest;

import com.olive.malagabici.repo.IHiloRepo;
import com.olive.malagabici.repo.IMensajeRepo;
import com.olive.malagabici.model.Mensaje;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Trigi
 */


@RestController
@RequestMapping("entity.mensaje")
public class MensajeREST {

    @Autowired
    private IMensajeRepo repo;
    
    @Autowired
    private IHiloRepo hiloRepo;

    @PostMapping
    public void create(@RequestBody Mensaje entity) {
        entity.setFecha(new Date());
        System.err.println(entity.getHilo().toString());
        repo.save(entity);
    }

    @PostMapping("hilo/{hilo}")
    public void create2(@PathVariable("hilo") Integer hilo, @RequestBody Mensaje entity) {
        entity.setFecha(new Date());
        entity.setHilo(hiloRepo.getOne(hilo));
        repo.save(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, @RequestBody Mensaje entity) {
        repo.save(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        repo.delete(repo.getOne(id));
    }

    @GetMapping("{id}")
    public Mensaje find(@PathVariable("id") Integer id) {
        return repo.getOne(id);
    }

    @GetMapping
    public List<Mensaje> findAll() {
        return repo.findAll();
    }

    @GetMapping("count")
    public String countREST() {
        return String.valueOf(repo.count());
    }

    @GetMapping("from_hilo/{id}")
    public List<Mensaje> mensajesHilo(@PathVariable("id") Integer id) {
        return repo.findByHilo(id);
    }

    @GetMapping("findByIntervaloFechas/{fechaMinima}/{fechaMaxima}")
    public List<Mensaje> findByIntervaloFechas(@PathVariable("fechaMinima") Date fechaMinima, @PathVariable("fechaMaxima") Date fechaMaxima) {
        return repo.findByIntervaloFechas(fechaMinima, fechaMaxima);
    }
}
