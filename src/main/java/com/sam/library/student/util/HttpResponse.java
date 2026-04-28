package com.sam.library.student.util;

import org.springframework.http.ResponseEntity;

public class HttpResponse {

    public ResponseEntity<Object> responseSuccess(Object obj) {
        return ResponseEntity.ok(obj);
    }

    public ResponseEntity<Object> responseBadRequest(String message) {
        return ResponseEntity.status(400).body(message);
    }

    public ResponseEntity<Object> responseForbidden() {
        return ResponseEntity.status(403).body("Resource forbidden");
    }

    public ResponseEntity<Object> responseNotFound(String message) {
        return ResponseEntity.status(404).body("Resource not found");
    }

    public ResponseEntity<Object> responseServerErr(String message) {
        return ResponseEntity.status(500).body("Internal server error");
    }

}
