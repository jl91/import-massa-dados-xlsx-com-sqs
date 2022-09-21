package com.example.postgresuuidjpa.DTO;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class NewFileRequestDTO {
    private String filename;
}
