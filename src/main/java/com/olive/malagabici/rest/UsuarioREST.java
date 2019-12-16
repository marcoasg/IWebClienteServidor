/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.olive.malagabici.repo.IUsuarioRepo;
import com.olive.malagbici.model.Usuario;
import java.util.List;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Trigi
 */

@RestController
@RequestMapping("entity.usuario")
public class UsuarioREST {

    @Autowired
    private IUsuarioRepo repo;
    

    @PostMapping
    public void create(@RequestBody Usuario entity) {
        repo.save(entity);
    }
    
    @PostMapping("postByString")
    public void registerByEmailAndName(@RequestParam("email") String email, @RequestParam("name") String name) {
        Usuario u = new Usuario();
        u.setEmail(email);
        u.setToken(name);
        repo.save(u);
    }

    @PutMapping("{id}")
    public void edit(@PathVariable("id") String id, @RequestBody Usuario entity) {
        repo.save(entity);
    }

    @DeleteMapping("{id}")
    public void remove(@PathVariable("id") String id) {
        repo.delete(repo.getOne(id));
    }

    @GetMapping("{id}")
    public Usuario find(@PathVariable("id") String id) {
        return repo.getOne(id);
    }

    @GetMapping
    public List<Usuario> findAll() {
        return repo.findAll();
    }

    @GetMapping("count")
    public String countREST() {
        return String.valueOf(repo.count());
    }

    @GetMapping("like/{email}")
    public List<Usuario> findLikeEmail(@PathVariable("email") String email){
        return repo.findLikeEmail(email);
        
    }
    @GetMapping("n_mensajes/{id}")
    public Long countMensajes(@PathVariable("id") String id){
        return (Long) repo.countMensajes(id);
        
    }
}

