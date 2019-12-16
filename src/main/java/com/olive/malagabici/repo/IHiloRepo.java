/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.repo;

import com.olive.malagbici.model.Hilo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Trigi
 */

@Repository
public interface IHiloRepo extends JpaRepository<Hilo,Integer> {
    List<Hilo> hilosPorMensajesUsuario(String id);
    List<Hilo> findByTema(String tema);
}