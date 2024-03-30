package com.example.demo.repository;

import com.example.demo.model.KeyStoreAccess;
import com.example.demo.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyStoreAccessRepository extends JpaRepository<KeyStoreAccess, String>  {
}
