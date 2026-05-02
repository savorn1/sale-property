package com.sam.library.student.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.sam.library.student.entity.Client;

public interface  ClientService {
    List<Client> getAllClients();
    Page<Client> getAllClients(Pageable pageable);
    Client getClientById(Long id);
    Client createClient(Client client);
    Client updateClient(Long id, Client client);
    String deleteClient(Long id);
}
