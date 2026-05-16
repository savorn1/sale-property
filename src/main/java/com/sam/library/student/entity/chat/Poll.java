package com.sam.library.student.entity.chat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Poll {
    private String question;
    private boolean allowMultiple;
    private List<PollOption> options = new ArrayList<>();
}
