package com.sam.library.student.repository;


import com.sam.library.student.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface  ClientRepository  extends JpaRepository<Client, Long>, JpaSpecificationExecutor<Client> {
   

}
