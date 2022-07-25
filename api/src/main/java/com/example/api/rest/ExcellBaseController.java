package com.example.api.rest;


import com.example.api.services.ExcellBaseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping("base-excell")
@Slf4j
public class ExcellBaseController {

    @Autowired
    ExcellBaseService excellBaseService;

    @PostMapping()
    public String uploadBase(
            @RequestParam("file") final MultipartFile file
    ) {

        try {
            this.excellBaseService.saveUploadedFile(file);
            return "ok";
        } catch (Exception exception) {
            log.error("Error on try to upload file {}", file.getName());
            log.error("Exception {}", exception);
            return "nok";
        }
    }


}
