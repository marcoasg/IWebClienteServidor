/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.rest;

import com.olive.malagbici.model.Hilo;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.olive.malagabici.repo.IHiloRepo;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 *
 * @author Trigi
 */

@RestController
@RequestMapping("entity.hilo")
public class HiloREST {
    
    @Autowired
    private IHiloRepo repo;

    @PostMapping
    public void create(@RequestBody Hilo entity) {
        entity.setFecha(new Date());
        repo.save(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") Integer id, @RequestBody Hilo entity) {
        repo.save(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") Integer id) {
        repo.delete(repo.getOne(id));
    }

    @GetMapping("{id}")
    public Hilo find(@PathVariable("id") Integer id) {
        return repo.getOne(id);
    }

    @GetMapping
    public List<Hilo> findAll() {
        return repo.findAll();
    }

    @GetMapping("count")
    public String countREST() {
        return String.valueOf(repo.count());
    }

    @GetMapping("hilosPorMensajesUsuario/{id}")
    public List<Hilo> hilosPorMensajesUsuario(@PathVariable("id") String id) {
        return repo.hilosPorMensajesUsuario(id);
    }

    @GetMapping("tema/{tema}")
    public List<Hilo> hilosPorTema(@PathVariable("tema") String tema) {
        return repo.findByTema(tema);
    }
}

