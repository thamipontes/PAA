package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class NoticiaController {

    Test test;

    public NoticiaController(Test test) {
        this.test = test;
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> salvaNoticias() throws Exception {
        test.salvaNoticiasBanco();
        return new ResponseEntity<>(HttpStatus.CREATED) ;
    }

    @GetMapping(path = "/{titulo}")
    public ResponseEntity<?> noticiaParecida(@PathVariable(value = "titulo") String titulo) throws Exception {

        return new ResponseEntity<>(test.ranking(titulo), HttpStatus.OK) ;
    }
}
