package com.sam.library.student.mapper;

import org.springframework.stereotype.Component;

import com.sam.library.student.dto.ClientDTO;
import com.sam.library.student.dto.CreateClientDTO;

import com.sam.library.student.entity.Client;

@Component
public class ClientMapper {
    public ClientDTO toClientDTO(Client c) {
        ClientDTO dto = new ClientDTO();
        dto.setId(c.getId());
        dto.setName(c.getName());
        dto.setEmail(c.getEmail());
        dto.setPhone(c.getPhone());
        dto.setGender(c.getGender());
        dto.setAddress(c.getAddress());
        return dto;
    }

    public Client toClient(ClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setGender(dto.getGender());
        client.setAddress(dto.getAddress());
        return client;
    }

    public Client toClient(CreateClientDTO dto) {
        Client client = new Client();
        client.setName(dto.getName());
        client.setEmail(dto.getEmail());
        client.setPhone(dto.getPhone());
        client.setGender(dto.getGender());
        client.setAddress(dto.getAddress());

        return client;
    }
}
