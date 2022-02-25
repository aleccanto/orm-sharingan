package br.com.sharingan.noorm.model;

import br.com.sharingan.ddd.orm.entidade.Entidade;

public class Pessoa implements Entidade {

    private Long id;

    private String nome;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

}
