package com.sam.library.student.service;

import java.util.List;

import com.sam.library.student.entity.Client;

public interface  ClientService {
    List<Client> getAllClients();
    Client getClientById(Long id);
    Client createClient(Client client);
    Client updateClient(Long id, Client client);
    String deleteClient(Long id);
}
