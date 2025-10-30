package tela_main_controller;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import model.Produto;

import java.util.List;

public class RelatoriosController {

    @FXML
    private Label labelStatus;
    
    @FXML
    private Label labelTotalProdutos;
    
    @FXML
    private Label labelValorTotalEstoque;
    
    @FXML
    private Label labelCategoriaMaisComum;

    @FXML
    public void initialize() {
        carregarDadosRelatorios();
    }

    private void carregarDadosRelatorios() {
        try {
            DAO<Produto> dao = new DAO<>(Produto.class);
            List<Produto> produtos = dao.obterTodos(1000, 0);
            
            int totalProdutos = produtos.size();
            double valorTotalEstoque = produtos.stream().mapToDouble(p -> p.getPreco() * p.getEstoque()).sum();
            
            // Encontrar categoria mais comum
            String categoriaMaisComum = produtos.stream()
                .collect(java.util.stream.Collectors.groupingBy(Produto::getCategoria, java.util.stream.Collectors.counting()))
                .entrySet().stream()
                .max(java.util.Map.Entry.comparingByValue())
                .map(entry -> entry.getKey() + " (" + entry.getValue() + " produtos)")
                .orElse("Nenhuma");
            
            labelTotalProdutos.setText(String.valueOf(totalProdutos));
            labelValorTotalEstoque.setText(String.format("R$ %.2f", valorTotalEstoque));
            labelCategoriaMaisComum.setText(categoriaMaisComum);
            labelStatus.setText("Relatórios carregados com sucesso!");
            
        } catch (Exception e) {
            e.printStackTrace();
            labelStatus.setText("Erro ao carregar relatórios: " + e.getMessage());
        }
    }
}