package tela_main_controller;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.StackPane;
import javafx.scene.Node;
import javafx.util.Duration;
import javafx.scene.control.Label;
import java.util.Locale;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 🚀 MainLayoutController -----------------------------------------
 * Este controller gerencia o menu lateral do sistema J_STOR.
 */
public class MainLayoutController {

    @FXML
    private Label labelRelogio;

    @FXML
    private Label labelEstacao;

    @FXML
    private StackPane painelConteudo;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, HH:mm", Locale.forLanguageTag("pt-BR"));

    @FXML
    public void initialize() {
        iniciarRelogio();
        abrirDashboard();
    }

    private void iniciarRelogio() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(0), _ -> {
            LocalDateTime agora = LocalDateTime.now();
            String textoFormatado = formatter.format(agora);
            String textoComMesMaiusculo = capitalizarMes(textoFormatado);
            labelRelogio.setText(textoComMesMaiusculo);
            labelEstacao.setText(obterEstacao(agora.getMonthValue()));
        }), new KeyFrame(Duration.seconds(60)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    private String capitalizarMes(String texto) {
        if (texto == null || texto.isEmpty()) return texto;
        String[] partes = texto.split(" ");
        if (partes.length < 2) return texto;
        String mes = partes[1].replace(",", "");
        mes = mes.substring(0, 1).toUpperCase() + mes.substring(1);
        int indexVirgula = texto.indexOf(",");
        String resto = indexVirgula != -1 ? texto.substring(indexVirgula) : "";
        return partes[0] + " " + mes + resto;
    }

    private String obterEstacao(int mes) {
        if (mes == 12 || mes <= 2) {
            return "Verão - Coleção de roupas leves e frescas.";
        } else if (mes >= 3 && mes <= 5) {
            return "Outono - Looks aconchegantes e elegantes.";
        } else if (mes >= 6 && mes <= 8) {
            return "Inverno - Moda inverno com casacos e agasalhos.";
        } else if (mes >= 9 && mes <= 11) {
            return "Primavera - Cores vibrantes e looks florais.";
        }
        return "";
    }

    public void abrirDashboard() {
        carregarTela("/telas/view/TelaDashboard.fxml");
    }

    public void abrirListaProduto() {
        carregarTela("/telas/view/TelaListaProduto.fxml");
    }

    // NOVOS MÉTODOS PARA AS FUNCIONALIDADES
    public void abrirRelatorios() {
        carregarTela("/telas/view/TelaRelatorios.fxml");
    }

    public void abrirVisaoGeral() {
        carregarTela("/telas/view/TelaVisaoGeral.fxml");
    }

    public void abrirGestao() {
        carregarTela("/telas/view/TelaGestao.fxml");
    }

    public void abrirAssistente() {
        carregarTela("/telas/view/TelaAssistente.fxml");
    }

    public void abrirFinanceiro() {
        carregarTela("/telas/view/TelaFinanceiro.fxml");
    }

    private void carregarTela(String caminho) {
        try {
            Node tela = FXMLLoader.load(getClass().getResource(caminho));
            tela.setOpacity(0);
            painelConteudo.getChildren().setAll(tela);
            FadeTransition fade = new FadeTransition(Duration.millis(900), tela);
            fade.setFromValue(0);
            fade.setToValue(1);
            fade.play();
        } catch (IOException e) {
            e.printStackTrace();
            // Carrega uma tela de placeholder se o arquivo não existir
            carregarTelaPlaceholder("Funcionalidade em desenvolvimento: " + caminho);
        }
    }

    private void carregarTelaPlaceholder(String mensagem) {
        Label placeholder = new Label(mensagem);
        placeholder.setStyle("-fx-text-fill: #00ffff; -fx-font-size: 18px; -fx-font-weight: bold;");
        placeholder.setOpacity(0);
        painelConteudo.getChildren().setAll(placeholder);
        FadeTransition fade = new FadeTransition(Duration.millis(900), placeholder);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }

    @FXML
    private void sair() {
        Platform.exit();
    }
}