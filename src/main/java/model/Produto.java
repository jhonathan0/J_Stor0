package model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produtos")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "codigo", nullable = false, unique = true)
    private String codigo;

    @Column(name = "nome", nullable = false)
    private String nome;

    @Column(name = "categoria", nullable = false)
    private String categoria;

    @Column(name = "tamanho", nullable = false)
    private String tamanho;

    @Column(name = "data_cadastro", nullable = false) // Nome correto da coluna
    private LocalDate dataCadastro;

    @Column(name = "estoque", nullable = false) // Nome correto da coluna
    private int estoque;

    @Column(name = "descricao", length = 2000)
    private String descricao;

    @Column(name = "preco", nullable = false)
    private double preco;

    @Column(name = "cor", nullable = false)
    private String cor;

    @Column(name = "marca")
    private String marca;

    public Produto() {
    }

    public Produto(String codigo, String nome, String categoria, String tamanho, 
                  LocalDate dataCadastro, int estoque, String descricao, 
                  double preco, String cor, String marca) {
        this.codigo = codigo;
        this.nome = nome;
        this.categoria = categoria;
        this.tamanho = tamanho;
        this.dataCadastro = dataCadastro;
        this.estoque = estoque;
        this.descricao = descricao;
        this.preco = preco;
        this.cor = cor;
        this.marca = marca;
    }

    // Getters e Setters (mantenha os mesmos)
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getTamanho() { return tamanho; }
    public void setTamanho(String tamanho) { this.tamanho = tamanho; }
    public LocalDate getDataCadastro() { return dataCadastro; }
    public void setDataCadastro(LocalDate dataCadastro) { this.dataCadastro = dataCadastro; }
    public int getEstoque() { return estoque; }
    public void setEstoque(int estoque) { this.estoque = estoque; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public double getPreco() { return preco; }
    public void setPreco(double preco) { this.preco = preco; }
    public String getCor() { return cor; }
    public void setCor(String cor) { this.cor = cor; }
    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    @Override
    public String toString() {
        return "Produto [id=" + id + ", codigo=" + codigo + ", nome=" + nome + 
               ", categoria=" + categoria + ", tamanho=" + tamanho + ", dataCadastro=" + dataCadastro + 
               ", estoque=" + estoque + ", preco=" + preco + ", cor=" + cor + 
               ", marca=" + marca + ", descricao=" + descricao + "]";
    }
}