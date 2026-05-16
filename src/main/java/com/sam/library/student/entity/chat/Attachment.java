package com.sam.library.student.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Attachment {
    private String url;
    private String originalName;
    private String mimeType;
    private Long size;
}
