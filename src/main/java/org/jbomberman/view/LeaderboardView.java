package org.jbomberman.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import org.jbomberman.controller.MainController;
import org.jbomberman.model.User;

import java.util.Comparator;
import java.util.List;

public class LeaderboardView {

    private final MainController controller = MainController.getInstance();
    private final Pane leaderboardPane = SceneManager.createPane("LEADERBOARD", false, false);
    private final ScrollPane scrollPane = new ScrollPane();
    private final VBox contentPane = new VBox(2); //lo spazio tra le scritte

    public LeaderboardView() {
        initScrollPane();

        leaderboardPane.getChildren().add(scrollPane);
    }

    private void initScrollPane() {
        scrollPane.setPrefSize(250, 150);
        contentPane.setAlignment(Pos.CENTER);
        scrollPane.setStyle("-fx-control-inner-background: transparent;");
        contentPane.setStyle("-fx-background-color: transparent;");
        scrollPane.setId("mainScrollPane");
        scrollPane.getStylesheets().add("org/jbomberman/view/scrollPane.css");
        scrollPane.setContent(contentPane);

        SceneManager.setCentred(scrollPane);
    }

    public void updateScrollPane() {
        contentPane.getChildren().clear(); // Rimuove tutti i label attuali

        List<User> leaderboard = controller.loadLeaderboard();
        leaderboard.sort(Comparator.comparingInt(User::score).reversed().thenComparing(User::level).thenComparing(User::name));


        leaderboard.forEach(user -> {
            Label player = new Label(user.name() + ": " + user.score() + " - " + user.level());
            player.setFont(SceneManager.CUSTOM_FONT_SMALL);
            player.setStyle("-fx-text-fill: white;");
            contentPane.getChildren().add(player);
        });
    }

    public Pane getLeaderboardPane() {
        return leaderboardPane;
    }
}
