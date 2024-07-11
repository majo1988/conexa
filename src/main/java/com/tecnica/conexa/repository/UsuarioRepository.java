package com.tecnica.conexa.repository;

import com.tecnica.conexa.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<User, Integer> {

    User findByUsername(String username);
}
