package  com.sam.library.student.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sam.library.student.dto.ClientDTO;
import com.sam.library.student.entity.Client;
import com.sam.library.student.mapper.ClientMapper;
import com.sam.library.student.service.ClientService;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/client")
@Tag(name = "Client", description = "Client services APIs")
@RequiredArgsConstructor
public class ClientController {
    private final ClientService clientService;
    private final ClientMapper clientMapper;


    @GetMapping
    public ResponseEntity<List<ClientDTO>> getAllClients() {
        List<ClientDTO> clients = clientService.getAllClients()
                .stream()
                .map(clientMapper::toClientDTO)
                .toList();
        return ResponseEntity.ok(clients);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClientDTO> getClientById(@PathVariable Long id) {
        Client client = clientService.getClientById(id);
        return ResponseEntity.ok(clientMapper.toClientDTO(client));
    }

    @PostMapping
    public ResponseEntity<ClientDTO> createClient(@RequestBody ClientDTO dto) {
        Client created = clientService.createClient(clientMapper.toClient(dto));
        return ResponseEntity.status(201).body(clientMapper.toClientDTO(created));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody ClientDTO dto) {
        Client client = clientService.updateClient(id, clientMapper.toClient(dto));
        return ResponseEntity.ok(client);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClient(@PathVariable Long id) {
        String result = clientService.deleteClient(id);
        return ResponseEntity.ok(result);
    }

    // // Bunld delete
    // public ResponseEntity<String> deleteClients(@RequestBody List<Long> ids) {
    //     String result = clientService.deleteClients(ids);
    //     return ResponseEntity.ok(result);
    // }

}
