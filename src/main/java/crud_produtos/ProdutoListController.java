package crud_produtos;

import dao.DAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.StackPane;
import model.Produto;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ProdutoListController {

    @FXML private TableView<Produto> tableProdutos;
    @FXML private TableColumn<Produto, Long> colId;
    @FXML private TableColumn<Produto, String> colCodigo;
    @FXML private TableColumn<Produto, String> colNome;
    @FXML private TableColumn<Produto, String> colCategoria;
    @FXML private TableColumn<Produto, String> colTamanho;
    @FXML private TableColumn<Produto, String> colData;
    @FXML private TableColumn<Produto, Integer> colEstoque;
    @FXML private TableColumn<Produto, Double> colPreco;
    @FXML private TableColumn<Produto, String> colCor;
    @FXML private TextField txtPesquisa;

    private final ObservableList<Produto> dados = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        configurarColunas();
        carregarProdutos();
        configurarTooltips();
    }
private void configurarColunas() {
    colId.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getId()));
    colCodigo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCodigo()));
    colNome.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNome()));
    colCategoria.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCategoria()));
    colTamanho.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTamanho()));
    colPreco.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getPreco()));
    colCor.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getCor()));

    // Formatar preço
    colPreco.setCellFactory(column -> new TableCell<Produto, Double>() {
        @Override
        protected void updateItem(Double preco, boolean empty) {
            super.updateItem(preco, empty);
            if (empty || preco == null) {
                setText(null);
            } else {
                setText(String.format("R$ %.2f", preco));
            }
        }
    });

    colData.setCellValueFactory(c ->
        new javafx.beans.property.SimpleStringProperty(
            c.getValue().getDataCadastro() != null
                ? c.getValue().getDataCadastro().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
                : "Não definida"
        )
    );

    colEstoque.setCellValueFactory(c -> new javafx.beans.property.SimpleObjectProperty<>(c.getValue().getEstoque()));
}

    private void configurarTooltips() {
        txtPesquisa.setTooltip(new Tooltip("Digite para pesquisar por código, nome ou categoria"));
    }

    private void carregarProdutos() {
        List<Produto> lista = new DAO<>(Produto.class).obterTodos(100, 0);
        dados.setAll(lista);
        tableProdutos.setItems(dados);
    }

    @FXML
    private void pesquisarProdutos() {
        String termo = txtPesquisa.getText().trim();
        if (termo.isEmpty()) {
            carregarProdutos();
        } else {
            List<Produto> resultados = new DAO<>(Produto.class).consultar(
                "SELECT p FROM Produto p WHERE p.codigo LIKE ?1 OR p.nome LIKE ?1 OR p.categoria LIKE ?1",
                "%" + termo + "%"
            );
            dados.setAll(resultados);
        }
    }

    @FXML
    private void abrirCadastro() {
        try {
            Node tela = FXMLLoader.load(getClass().getResource("/telas/view/TelaCadastroProduto.fxml"));
            StackPane painel = (StackPane) tableProdutos.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(tela);
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir tela de cadastro: " + e.getMessage());
        }
    }

    @FXML
    private void editarProduto() {
        Produto selecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um produto para editar.");
            return;
        }
        
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/telas/view/TelaCadastroProduto.fxml"));
            Node tela = loader.load();
            
            ProdutoCreateController controller = loader.getController();
            preencherCamposEdicao(controller, selecionado);
            
            StackPane painel = (StackPane) tableProdutos.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(tela);
            
        } catch (IOException e) {
            e.printStackTrace();
            mostrarErro("Erro ao abrir tela de edição: " + e.getMessage());
        }
    }

    private void preencherCamposEdicao(ProdutoCreateController controller, Produto produto) {
        controller.preencherParaEdicao(produto);
    }

    @FXML
    private void excluirProduto() {
        Produto selecionado = tableProdutos.getSelectionModel().getSelectedItem();
        if (selecionado == null) {
            mostrarAlerta("Selecione um produto para excluir.");
            return;
        }
        
        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION);
        confirmacao.setTitle("Confirmar Exclusão");
        confirmacao.setHeaderText("Excluir Produto");
        confirmacao.setContentText("Tem certeza que deseja excluir o produto: " + selecionado.getNome() + "?");
        
        if (confirmacao.showAndWait().get() == ButtonType.OK) {
            new DAO<>(Produto.class).removerPorIdTransacional(selecionado.getId());
            carregarProdutos();
            mostrarSucesso("Produto excluído com sucesso!");
        }
    }

    private void mostrarAlerta(String mensagem) {
        new Alert(Alert.AlertType.WARNING, mensagem).showAndWait();
    }

    private void mostrarErro(String mensagem) {
        new Alert(Alert.AlertType.ERROR, mensagem).showAndWait();
    }

    private void mostrarSucesso(String mensagem) {
        new Alert(Alert.AlertType.INFORMATION, mensagem).showAndWait();
    }
    @FXML
private void pesquisarProduto() {
    String termo = txtPesquisa.getText().trim();
    if (termo.isEmpty()) {
        carregarProdutos();
    } else {
        List<Produto> resultados = new DAO<>(Produto.class).consultar(
            "SELECT p FROM Produto p WHERE " +
            "LOWER(p.codigo) LIKE LOWER(?1) OR " +
            "LOWER(p.nome) LIKE LOWER(?1) OR " + 
            "LOWER(p.categoria) LIKE LOWER(?1) OR " +
            "LOWER(p.cor) LIKE LOWER(?1) OR " +
            "LOWER(p.marca) LIKE LOWER(?1)",
            "%" + termo + "%"
        );
        dados.setAll(resultados);
        
        if (resultados.isEmpty()) {
            mostrarAlerta("Nenhum produto encontrado para: " + termo);
        }
    }
}}
