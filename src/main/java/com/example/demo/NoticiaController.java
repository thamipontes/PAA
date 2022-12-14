package com.example.demo;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class NoticiaController {

    Test test;

    public NoticiaController(Test test) {
        this.test = test;
    }

    @PostMapping(path = "/")
    public ResponseEntity<?> salvaNoticias(@RequestBody String url) throws Exception {
        test.salvaNoticiasBanco(url);
        return new ResponseEntity<>(HttpStatus.CREATED) ;
    }

    @GetMapping(path = "/{titulo}")
    public ResponseEntity<?> noticiaParecida(@PathVariable(value = "titulo") String titulo) throws Exception {

        return new ResponseEntity<>(test.ranking(titulo), HttpStatus.OK) ;
    }

    @GetMapping(path = "/")
    public ResponseEntity<?> pegaTodasNoticias() throws Exception {

        return new ResponseEntity<>(test.allNoticias(), HttpStatus.OK) ;
    }
}
