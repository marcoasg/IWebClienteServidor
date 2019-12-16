/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.rest;

import com.olive.malagbici.model.Tema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.olive.malagabici.repo.ITemaRepo;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 *
 * @author Trigi
 */
@RestController
@RequestMapping("entity.tema")
public class TemaREST {
    
    @Autowired
    private ITemaRepo repo;

    @PostMapping
    public void create(@RequestBody Tema entity) {
        repo.save(entity);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") String id, @RequestBody Tema entity) {
        repo.save(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") String id) {
        repo.delete(repo.getOne(id));
    }

    @GetMapping("{id}")
    public Tema find(@PathVariable("id") String id) {
        return repo.getOne(id);
    }

    @GetMapping
    public List<Tema> findAll() {
        return repo.findAll();
    }

    @GetMapping("count")
    public String countREST() {
        return String.valueOf(repo.count());
    }
}
