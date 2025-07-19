package br.com.sharingan.orm.modelo;

import br.com.sharingan.anotacoes.Entidade;
import br.com.sharingan.anotacoes.Id;

@Entidade
public class Pessoa {

    @Id
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
