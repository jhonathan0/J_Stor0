package crud_produtos;

import dao.DAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Produto;

import java.time.LocalDate;
import java.util.List;

/**
 * 🚀 ProdutoCreateController -----------------------------------------
 * Este controller gerencia a tela de cadastro de produtos.
 */
public class ProdutoCreateController {

    @FXML
    private TextField txtCodigo;

    @FXML
    private DatePicker dateCadastro;

    @FXML
    private TextField txtNome;

    @FXML
    private ComboBox<String> comboTamanho;

    @FXML
    private ComboBox<String> comboCategoria;

    @FXML
    private Spinner<Integer> spinnerEstoque;

    @FXML
    private TextArea txtDescricao;

    @FXML
    private TextField txtMarca;

    @FXML
    private ComboBox<String> comboCor;

    @FXML
    private Spinner<Double> spinnerPreco;

    private boolean modoEdicao = false;

    @FXML
    public void initialize() {
        // Configurações para produtos de roupa
        comboTamanho.getItems().addAll("P", "M", "G", "GG", "XG", "Único");
        comboCategoria.getItems().addAll("Camisetas", "Calças", "Vestidos", "Casacos", "Acessórios", "Roupas Íntimas", "Esportivo");
        comboCor.getItems().addAll("Preto", "Branco", "Azul", "Vermelho", "Verde", "Amarelo", "Rosa", "Cinza", "Marrom");

        SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 1000, 10);
        spinnerEstoque.setValueFactory(valueFactory);

        SpinnerValueFactory<Double> precoFactory = new SpinnerValueFactory.DoubleSpinnerValueFactory(0, 10000, 50, 0.5);
        spinnerPreco.setValueFactory(precoFactory);

        // Tooltips para melhor usabilidade
        txtCodigo.setTooltip(new Tooltip("Código único do produto (obrigatório)"));
        txtNome.setTooltip(new Tooltip("Nome do produto (obrigatório)"));
        comboCategoria.setTooltip(new Tooltip("Selecione a categoria do produto"));
        comboTamanho.setTooltip(new Tooltip("Selecione o tamanho do produto"));
        comboCor.setTooltip(new Tooltip("Selecione a cor do produto"));
        txtMarca.setTooltip(new Tooltip("Marca do produto (opcional)"));
        spinnerPreco.setTooltip(new Tooltip("Preço do produto em R$"));
        spinnerEstoque.setTooltip(new Tooltip("Quantidade em estoque"));
        dateCadastro.setTooltip(new Tooltip("Data de cadastro do produto"));
        txtDescricao.setTooltip(new Tooltip("Descrição detalhada do produto"));
    }

    private void limparEstiloErro() {
        limparBordaVermelha(txtCodigo);
        limparBordaVermelha(txtNome);
        limparBordaVermelha(comboCategoria);
        limparBordaVermelha(comboTamanho);
        limparBordaVermelha(dateCadastro);
        limparBordaVermelha(comboCor);
    }

    private void colocarBordaVermelha(Control campo) {
        campo.setStyle("-fx-border-color: red; -fx-border-width: 2;");
    }

    private void limparBordaVermelha(Control campo) {
        campo.setStyle("");
    }

    private boolean validarCamposComVisual() {
        limparEstiloErro();
        boolean valido = true;

        if (txtCodigo.getText() == null || txtCodigo.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtCodigo);
            valido = false;
        }

        if (txtNome.getText() == null || txtNome.getText().trim().isEmpty()) {
            colocarBordaVermelha(txtNome);
            valido = false;
        }

        if (comboCategoria.getValue() == null || comboCategoria.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboCategoria);
            valido = false;
        }

        if (comboTamanho.getValue() == null || comboTamanho.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboTamanho);
            valido = false;
        }

        if (dateCadastro.getValue() == null) {
            colocarBordaVermelha(dateCadastro);
            valido = false;
        }

        if (comboCor.getValue() == null || comboCor.getValue().trim().isEmpty()) {
            colocarBordaVermelha(comboCor);
            valido = false;
        }

        return valido;
    }

    @FXML
    private void salvarProduto() {
        try {
            if (!validarCamposComVisual()) {
                Alert alerta = new Alert(Alert.AlertType.WARNING);
                alerta.setTitle("Campos Obrigatórios");
                alerta.setHeaderText("Preencha os campos obrigatórios destacados em vermelho.");
                alerta.setContentText("Os campos com borda vermelha são obrigatórios e não podem ficar vazios.");
                alerta.showAndWait();
                return;
            }

            String codigo = txtCodigo.getText();
            
            // VALIDAÇÃO DE CÓDIGO ÚNICO - apenas no modo cadastro
            if (!modoEdicao) {
                DAO<Produto> dao = new DAO<>(Produto.class);
                List<Produto> existentes = dao.consultar("SELECT p FROM Produto p WHERE p.codigo = ?1", codigo);
                
                if (!existentes.isEmpty()) {
                    Alert alerta = new Alert(Alert.AlertType.ERROR);
                    alerta.setTitle("Erro de Cadastro");
                    alerta.setHeaderText("Código já existe");
                    alerta.setContentText("Já existe um produto com este código. Por favor, use um código diferente.");
                    alerta.showAndWait();
                    colocarBordaVermelha(txtCodigo);
                    return;
                }
            }

            String nome = txtNome.getText();
            String categoria = comboCategoria.getValue();
            String tamanho = comboTamanho.getValue();
            String cor = comboCor.getValue();
            String marca = txtMarca.getText();
            double preco = spinnerPreco.getValue();
            int estoque = spinnerEstoque.getValue();
            String descricao = txtDescricao.getText();
            LocalDate dataCadastro = dateCadastro.getValue();

            Produto produto;
            if (modoEdicao) {
                // Em modo edição, carregar o produto existente e atualizar
                DAO<Produto> dao = new DAO<>(Produto.class);
                List<Produto> produtos = dao.consultar("SELECT p FROM Produto p WHERE p.codigo = ?1", codigo);
                if (!produtos.isEmpty()) {
                    produto = produtos.get(0);
                    produto.setNome(nome);
                    produto.setCategoria(categoria);
                    produto.setTamanho(tamanho);
                    produto.setCor(cor);
                    produto.setMarca(marca);
                    produto.setPreco(preco);
                    produto.setEstoque(estoque);
                    produto.setDescricao(descricao);
                    produto.setDataCadastro(dataCadastro);
                    dao.atualizarTransacional(produto);
                } else {
                    throw new Exception("Produto não encontrado para edição");
                }
            } else {
                // Modo cadastro - criar novo produto
                produto = new Produto(codigo, nome, categoria, tamanho, dataCadastro, estoque, descricao, preco, cor, marca);
                new DAO<>(Produto.class).incluirTransacional(produto);
            }

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);
            alerta.setTitle("Produto");
            alerta.setHeaderText("Sucesso");
            alerta.setContentText(modoEdicao ? "Produto atualizado com sucesso!" : "Produto salvo com sucesso!");
            alerta.showAndWait();

            if (modoEdicao) {
                // Voltar para a lista após edição
                voltarParaLista();
            } else {
                limparCampos();
            }

        } catch (Exception e) {
            e.printStackTrace();
            Alert alerta = new Alert(Alert.AlertType.ERROR);
            alerta.setTitle("Erro");
            alerta.setHeaderText("Falha ao salvar produto");
            alerta.setContentText("Erro: " + e.getMessage());
            alerta.showAndWait();
        }
    }

    @FXML
    private void limparCampos() {
        txtCodigo.clear();
        dateCadastro.setValue(null);
        txtNome.clear();
        comboTamanho.setValue(null);
        comboCategoria.setValue(null);
        comboCor.setValue(null);
        txtMarca.clear();
        spinnerEstoque.getValueFactory().setValue(10);
        spinnerPreco.getValueFactory().setValue(50.0);
        txtDescricao.clear();
        limparEstiloErro();
        modoEdicao = false;
        txtCodigo.setDisable(false);
    }

    @FXML
    private void voltarParaLista() {
        try {
            javafx.scene.Node tela = javafx.fxml.FXMLLoader.load(getClass().getResource("/telas/view/TelaListaProduto.fxml"));
            javafx.scene.layout.StackPane painel = (javafx.scene.layout.StackPane) txtCodigo.getScene().lookup("#painelConteudo");
            painel.getChildren().setAll(tela);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Método para preencher campos na edição
    public void preencherParaEdicao(Produto produto) {
        modoEdicao = true;
        txtCodigo.setText(produto.getCodigo());
        txtCodigo.setDisable(true); // Não permite alterar o código na edição
        txtNome.setText(produto.getNome());
        comboCategoria.setValue(produto.getCategoria());
        comboTamanho.setValue(produto.getTamanho());
        comboCor.setValue(produto.getCor());
        txtMarca.setText(produto.getMarca());
        spinnerPreco.getValueFactory().setValue(produto.getPreco());
        spinnerEstoque.getValueFactory().setValue(produto.getEstoque());
        dateCadastro.setValue(produto.getDataCadastro());
        txtDescricao.setText(produto.getDescricao());
    }
}