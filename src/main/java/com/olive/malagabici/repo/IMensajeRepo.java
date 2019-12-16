/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.olive.malagabici.repo;

import com.olive.malagbici.model.Mensaje;
import java.util.Date;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

/**
 *
 * @author Trigi
 */

@Repository
public interface IMensajeRepo extends JpaRepository<Mensaje,Integer> {
    List<Mensaje> findByHilo(Integer id);
    List<Mensaje> findByIntervaloFechas(Date fechaMinima, Date fechaMaxima);
}
