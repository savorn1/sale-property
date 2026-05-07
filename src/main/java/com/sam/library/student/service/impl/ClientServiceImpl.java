package com.sam.library.student.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.sam.library.student.entity.Client;
import com.sam.library.student.repository.ClientRepository;
import com.sam.library.student.service.ClientService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ClientServiceImpl implements  ClientService {
    private final ClientRepository clientRepository;
     
    @Override
    public List<Client> getAllClients() {
        return clientRepository.findAll();
    }

    @Override
    public Page<Client> getAllClients(Pageable pageable) {
        return clientRepository.findAll(pageable);
    }

    @Override
    public Page<Client> getAllClients(String name, Pageable pageable) {
        if (name != null && !name.isBlank()) {
            Specification<Client> spec = (root, query, cb) ->
                cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
            return clientRepository.findAll(spec, pageable);
        }
        return clientRepository.findAll(pageable);
    }

    @Override
    public Client getClientById(Long id) {
        return clientRepository.findById(id).orElse(null);
    }

    @Override
    public Client createClient(Client client) {
        return clientRepository.save(client);
    }
    

    @Override
    public Client updateClient(Long id, Client client) {
        Client existingClient = clientRepository.findById(id).orElse(null);
        if (existingClient == null) {
            throw new IllegalArgumentException("Client not found with id: " + id);
        }
        existingClient.setName(client.getName());
        existingClient.setEmail(client.getEmail());
        existingClient.setPhone(client.getPhone());
        existingClient.setGender(client.getGender());
        existingClient.setAddress(client.getAddress());
        return clientRepository.save(existingClient);
    }

    @Override
    public String deleteClient(Long id) {
        if (!clientRepository.existsById(id)) {
            throw new IllegalArgumentException("Client not found with id: ");
        }
        clientRepository.deleteById(id);
        return "Client deleted successfully with id: " + id;
    }
    
}
