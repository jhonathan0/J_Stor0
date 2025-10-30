package tela_main_controller;

import dao.DAO;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.chart.PieChart;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.util.Duration;
import model.Produto;
import javafx.scene.Node;
import javafx.scene.layout.VBox;

import java.util.*;
import java.util.stream.Collectors;

public class TelaDashboardController {

    // ================== LABELS PRINCIPAIS ==================
    @FXML private Label labelTotalProdutos;
    @FXML private Label labelVendasMes;
    @FXML private Label labelClientesAtivos;
    @FXML private Label labelProdutosFalta;
    @FXML private Label labelNovosClientes;
    @FXML private Label labelTicketMedio;
    @FXML private Label labelLucroLiquido;
    @FXML private Label labelMeta;
    @FXML private Label labelMetaDetalhada;
    @FXML private ProgressBar progressMeta;
    @FXML private PieChart chartCategorias;
    @FXML private Label labelProdutoTop;
    
    // ================== CARDS PARA ANIMAÇÃO ==================
    @FXML private VBox cardProdutos;
    @FXML private VBox cardVendas; 
    @FXML private VBox cardClientes;
    @FXML private VBox cardFalta;
    @FXML private VBox cardNovos;
    @FXML private VBox cardTicket;
    @FXML private VBox cardLucro;

    private Timeline animationTimeline;

    @FXML
    public void initialize() {
        System.out.println("🚀 Inicializando Dashboard SUPER COMPLETO...");
        carregarDadosDashboard();
        iniciarAnimacoes();
        iniciarAtualizacaoAutomatica();
    }

    private void carregarDadosDashboard() {
        try {
            DAO<Produto> dao = new DAO<>(Produto.class);
            List<Produto> produtos = dao.obterTodos(1000, 0);
            
            // DADOS REAIS + SIMULAÇÕES AVANÇADAS
            Map<String, Object> metrics = calcularMetricasCompletas(produtos);
            
            // ATUALIZAR UI COM ANIMAÇÃO
            atualizarUIComAnimacao(metrics);
            
            // CONFIGURAR GRÁFICOS
            configurarGraficoCategorias(produtos);
            
        } catch (Exception e) {
            e.printStackTrace();
            definirValoresPadrao();
        }
    }

    private Map<String, Object> calcularMetricasCompletas(List<Produto> produtos) {
        Map<String, Object> metrics = new HashMap<>();
        Random random = new Random();
        
        // Métricas básicas reais
        metrics.put("totalProdutos", produtos.size());
        metrics.put("produtosEmFalta", (int) produtos.stream().filter(p -> p.getEstoque() <= 2).count());
        
        // Simulações avançadas (substituir por dados reais quando tiver as entidades)
        double vendasMes = produtos.stream().mapToDouble(p -> p.getPreco() * (5 + random.nextInt(10))).sum();
        metrics.put("vendasMes", vendasMes);
        metrics.put("clientesAtivos", 2847 + random.nextInt(200));
        metrics.put("novosClientes", 156 + random.nextInt(50));
        metrics.put("ticketMedio", 89.90 + random.nextDouble() * 20);
        metrics.put("lucroLiquido", vendasMes * 0.4); // 40% de lucro
        
        // Produto mais caro como "mais vendido" (simulação)
        metrics.put("produtoTop", produtos.stream()
                .max(Comparator.comparing(Produto::getPreco))
                .map(Produto::getNome)
                .orElse("Camiseta Basic"));
                
        return metrics;
    }

    private void atualizarUIComAnimacao(Map<String, Object> metrics) {
        // Animar contadores
        animarContador(labelTotalProdutos, 0, (Integer) metrics.get("totalProdutos"));
        animarContador(labelVendasMes, 0.0, (Double) metrics.get("vendasMes"), "R$ %.2f");
        animarContador(labelClientesAtivos, 0, (Integer) metrics.get("clientesAtivos"));
        animarContador(labelProdutosFalta, 0, (Integer) metrics.get("produtosEmFalta"));
        animarContador(labelNovosClientes, 0, (Integer) metrics.get("novosClientes"));
        animarContador(labelTicketMedio, 0.0, (Double) metrics.get("ticketMedio"), "R$ %.2f");
        animarContador(labelLucroLiquido, 0.0, (Double) metrics.get("lucroLiquido"), "R$ %.2f");
        
        // Produto top
        labelProdutoTop.setText((String) metrics.get("produtoTop"));
        
        // Meta (simulação)
        double metaProgresso = Math.min((Double) metrics.get("vendasMes") / 38000.0, 1.0);
        animarProgressBar(progressMeta, 0.0, metaProgresso);
        labelMeta.setText(String.format("%.0f%%", metaProgresso * 100));
        labelMetaDetalhada.setText(String.format("R$ %.0f / R$ 38.000", metrics.get("vendasMes")));
        
        // Aplicar estilos dinâmicos
        aplicarEstilosDinamicos((Integer) metrics.get("produtosEmFalta"), metaProgresso);
    }

    private void aplicarEstilosDinamicos(int produtosEmFalta, double progressoMeta) {
        // Alerta para produtos em falta
        if (produtosEmFalta > 10 && cardFalta != null) {
            cardFalta.setStyle(cardFalta.getStyle() + "; -fx-border-color: #ff4444; -fx-border-width: 3;");
        }
        
        // Destaque para meta próxima
        if (progressoMeta > 0.8) {
            progressMeta.setStyle("-fx-accent: #00ff88;");
        } else if (progressoMeta > 0.5) {
            progressMeta.setStyle("-fx-accent: #ffaa00;");
        }
    }

    private void configurarGraficoCategorias(List<Produto> produtos) {
        try {
            // Agrupar produtos por categoria
            Map<String, Long> porCategoria = produtos.stream()
                .filter(p -> p.getCategoria() != null && !p.getCategoria().trim().isEmpty())
                .collect(Collectors.groupingBy(Produto::getCategoria, Collectors.counting()));
                
            ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
            String[] cores = {"#00ffff", "#ff6b00", "#00ff88", "#ffaa00", "#8e2de2", "#fa709a"};
            
            int i = 0;
            for (Map.Entry<String, Long> entry : porCategoria.entrySet()) {
                PieChart.Data pieData = new PieChart.Data(
                    entry.getKey() + " (" + entry.getValue() + ")", 
                    entry.getValue()
                );
                data.add(pieData);
                
                // Cor personalizada
                final String cor = cores[i % cores.length];
                final PieChart.Data finalData = pieData;
                
                Timeline colorTimer = new Timeline(new KeyFrame(Duration.millis(300), e -> {
                    if (finalData.getNode() != null) {
                        finalData.getNode().setStyle("-fx-pie-color: " + cor + ";");
                    }
                }));
                colorTimer.play();
                i++;
            }
            
            chartCategorias.setData(data);
            chartCategorias.setLabelsVisible(true);
            chartCategorias.setLegendVisible(false);
            
        } catch (Exception e) {
            System.err.println("Erro ao configurar gráfico: " + e.getMessage());
        }
    }

    private void animarContador(Label label, double start, double end, String format) {
        Timeline timeline = new Timeline();
        final double duration = 1500;
        final double frames = 60;
        final double increment = (end - start) / frames;
        
        for (int i = 0; i <= frames; i++) {
            final double value = start + (increment * i);
            KeyFrame kf = new KeyFrame(
                Duration.millis((duration / frames) * i),
                e -> label.setText(String.format(format, value))
            );
            timeline.getKeyFrames().add(kf);
        }
        timeline.play();
    }

    private void animarContador(Label label, int start, int end) {
        animarContador(label, (double) start, (double) end, "%.0f");
    }

    private void animarProgressBar(ProgressBar progressBar, double start, double end) {
        Timeline timeline = new Timeline(
            new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), start)),
            new KeyFrame(Duration.seconds(1.5), new KeyValue(progressBar.progressProperty(), end))
        );
        timeline.play();
    }

    private void iniciarAnimacoes() {
        // Animação de flutuação suave nos cards
        animationTimeline = new Timeline(new KeyFrame(Duration.seconds(3), e -> {
            aplicarAnimacaoFlutuacao();
        }));
        animationTimeline.setCycleCount(Animation.INDEFINITE);
        animationTimeline.play();
    }

    private void aplicarAnimacaoFlutuacao() {
        // Aplica efeito de flutuação alternada nos cards
        Node[] cards = {cardProdutos, cardVendas, cardClientes, cardLucro};
        for (Node card : cards) {
            if (card != null) {
                TranslateTransition tt = new TranslateTransition(Duration.millis(2000), card);
                tt.setFromY(0);
                tt.setToY(-5);
                tt.setAutoReverse(true);
                tt.setCycleCount(2);
                tt.play();
            }
        }
    }

    private void iniciarAtualizacaoAutomatica() {
        // Atualiza os dados a cada 30 segundos
        Timeline updateTimeline = new Timeline(
            new KeyFrame(Duration.seconds(30), e -> {
                System.out.println("🔄 Atualizando dados do dashboard...");
                carregarDadosDashboard();
            })
        );
        updateTimeline.setCycleCount(Animation.INDEFINITE);
        updateTimeline.play();
    }

    private void definirValoresPadrao() {
        // Valores padrão em caso de erro
        if (labelTotalProdutos != null) labelTotalProdutos.setText("0");
        if (labelVendasMes != null) labelVendasMes.setText("R$ 0,00");
        if (labelClientesAtivos != null) labelClientesAtivos.setText("0");
        if (labelProdutosFalta != null) labelProdutosFalta.setText("0");
        if (labelNovosClientes != null) labelNovosClientes.setText("0");
        if (labelTicketMedio != null) labelTicketMedio.setText("R$ 0,00");
        if (labelLucroLiquido != null) labelLucroLiquido.setText("R$ 0,00");
        if (labelMeta != null) labelMeta.setText("0%");
        if (progressMeta != null) progressMeta.setProgress(0);
        if (labelProdutoTop != null) labelProdutoTop.setText("Nenhum produto");
    }

    // Método para limpar recursos
    public void cleanup() {
        if (animationTimeline != null) {
            animationTimeline.stop();
        }
    }
}