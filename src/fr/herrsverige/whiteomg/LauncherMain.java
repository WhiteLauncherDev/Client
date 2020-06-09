package fr.herrsverige.whiteomg;

import fr.trxyy.alternative.alternative_api.*;
import fr.trxyy.alternative.alternative_api.maintenance.GameMaintenance;
import fr.trxyy.alternative.alternative_api.maintenance.Maintenance;
import fr.trxyy.alternative.alternative_api_ui.LauncherBackground;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.AlternativeBase;
import fr.trxyy.alternative.alternative_api_ui.base.LauncherBase;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class LauncherMain extends AlternativeBase {

    private GameFolder gameFolder = new GameFolder("white");
    private LauncherPreferences launcherPreferences = new LauncherPreferences("WhiteLauncher", 950, 600, true);
    private GameEngine gameEngine = new GameEngine(gameFolder, launcherPreferences, GameVersion.V_1_12_2, GameStyle.VANILLA_PLUS);
    private GameLinks gameLinks = new GameLinks("https://white.ouiheberg.eu/v1.1.0/", "1.12.2.json");
    private GameMaintenance gameMaintenance = new GameMaintenance(Maintenance.USE, gameEngine);

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createContent());
        this.gameEngine.reg(primaryStage);
        this.gameEngine.reg(this.gameLinks);
        this.gameEngine.reg(this.gameMaintenance);
        LauncherBase launcherBase = new LauncherBase(primaryStage, scene, StageStyle.UNDECORATED, gameEngine);
        launcherBase.setIconImage(primaryStage, getResourceLocation().loadImage(gameEngine, "logo.jpg"));
    }

    private Parent createContent() {
        LauncherPane contentPane = new LauncherPane(gameEngine);
        new LauncherBackground(gameEngine, getResourceLocation().getMedia(gameEngine, "background.mp4"), contentPane);
        new LauncherPanel(contentPane, gameEngine);
        return contentPane;
    }

    public GameLinks getGameLinks() {
        return gameLinks;
    }
}