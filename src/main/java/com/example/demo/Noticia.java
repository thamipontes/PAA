package com.example.demo;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class Noticia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition="text")
    private String titulo;
    @Column(columnDefinition="text")
    private String link;
    @Column(columnDefinition="text", length=10485760)
    private String descricao;

    public Noticia(Long id, String titulo, String link, String descricao) {
        this.id = id;
        this.titulo = titulo;
        this.link = link;
        this.descricao = descricao;
    }

    public Noticia() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Noticia noticia = (Noticia) o;
        return Objects.equals(id, noticia.id) && Objects.equals(titulo, noticia.titulo) && Objects.equals(link, noticia.link) && Objects.equals(descricao, noticia.descricao);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, titulo, link, descricao);
    }
}
