package br.com.sharingan.orm.modelo;

import java.math.BigDecimal;

import br.com.sharingan.anotacoes.Entidade;
import br.com.sharingan.anotacoes.Id;
import br.com.sharingan.anotacoes.Tabela;

@Entidade
@Tabela("carros")
public class Carro {

    @Id
    private Long id;

    private String nome;

    private BigDecimal preco;

    private Double peso;

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

    public BigDecimal getPreco() {
        return preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

}
