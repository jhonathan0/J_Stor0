package tela.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

/**
 * 🚀 App.java
 * --------------------------------------------------------
 * Classe principal do J_STOR "Sistema de Gestão de Roupas".
 */
public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/telas/view/MainLayout.fxml"));
        Scene scene = new Scene(root, 900, 600);
        scene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());
        primaryStage.setTitle("👕 J_STOR - Sistema de Gestão de Roupas");
        primaryStage.setScene(scene);
        primaryStage.setMaximized(true);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}