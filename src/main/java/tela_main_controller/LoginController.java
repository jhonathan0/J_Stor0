package tela_main_controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import java.util.prefs.Preferences;

public class LoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtSenha;
    @FXML private CheckBox checkLembrar;
    @FXML private Button btnEntrar;

    private Preferences prefs;

    @FXML
    public void initialize() {
        prefs = Preferences.userNodeForPackage(LoginController.class);
        carregarUsuarioSalvo();
        
        // Enter para login
        txtSenha.setOnAction(e -> fazerLogin());
    }

    private void carregarUsuarioSalvo() {
        String usuarioSalvo = prefs.get("usuario", "");
        if (!usuarioSalvo.isEmpty()) {
            txtUsuario.setText(usuarioSalvo);
            checkLembrar.setSelected(true);
        }
    }

    @FXML
    private void fazerLogin() {
        String usuario = txtUsuario.getText().trim();
        String senha = txtSenha.getText().trim();

        if (usuario.isEmpty() || senha.isEmpty()) {
            mostrarAlerta("Campos obrigatórios", "Preencha usuário e senha!");
            return;
        }

        // Login simples (substituir por autenticação real)
        if (autenticar(usuario, senha)) {
            if (checkLembrar.isSelected()) {
                prefs.put("usuario", usuario);
            } else {
                prefs.remove("usuario");
            }
            abrirSistema();
        } else {
            mostrarAlerta("Login inválido", "Usuário ou senha incorretos!");
        }
    }

    private boolean autenticar(String usuario, String senha) {
        // Autenticação simples - substituir por sistema real
        return usuario.equals("admin") && senha.equals("admin123");
    }

    private void abrirSistema() {
        try {
            Stage stage = (Stage) btnEntrar.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/telas/view/MainLayout.fxml"));
            Scene scene = new Scene(root, 1200, 800);
            scene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());
            stage.setScene(scene);
            stage.setTitle("👕 J_STOR - Sistema de Gestão de Roupas");
            stage.setMaximized(true);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro", "Não foi possível abrir o sistema: " + e.getMessage());
        }
    }

    private void mostrarAlerta(String titulo, String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }
}