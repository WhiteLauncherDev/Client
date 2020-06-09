package fr.herrsverige.whiteomg;

import fr.trxyy.alternative.alternative_api.GameEngine;
import fr.trxyy.alternative.alternative_api.GameSize;
import fr.trxyy.alternative.alternative_api.account.AccountType;
import fr.trxyy.alternative.alternative_api.auth.GameAuth;
import fr.trxyy.alternative.alternative_api.updater.GameUpdater;
import fr.trxyy.alternative.alternative_api.utils.FontLoader;
import fr.trxyy.alternative.alternative_api.utils.config.UserConfig;
import fr.trxyy.alternative.alternative_api.utils.config.UsernameSaver;
import fr.trxyy.alternative.alternative_api_ui.LauncherAlert;
import fr.trxyy.alternative.alternative_api_ui.LauncherPane;
import fr.trxyy.alternative.alternative_api_ui.base.IScreen;
import fr.trxyy.alternative.alternative_api_ui.components.*;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.*;
import java.text.DecimalFormat;

public class LauncherPanel extends IScreen {
    private LauncherRectangle topRectangle;
    private LauncherRectangle backgroundWhiteRectangle;
    private LauncherImage titleImage;
    private LauncherLabel titleLabel;
    private LauncherButton closeButton;
    private LauncherButton reduceButton;

    private LauncherButton facebookButton;
    private LauncherButton twitterButton;
    private LauncherButton instagramButton;
    private LauncherButton youtubeButton;

    private LauncherRectangle updateRectangle;
    private LauncherLabel updateLabel;
    private LauncherLabel currentFileLabel;
    private LauncherLabel percentageLabel;
    private LauncherLabel currentStep;

    private LauncherTextField usernameField;
    private LauncherPasswordField passwordField;
    private LauncherButton loginButton;
    private LauncherLabel nomDuServeur;

    public Timeline timeline;
    private DecimalFormat decimalFormat = new DecimalFormat(".#");
    private Thread updateThread;
    private GameUpdater updater = new GameUpdater();
    private Slider versionSlider;
    private UsernameSaver saver;
    public UserConfig userConfig;
    private GameUpdater gameUpdater = new GameUpdater();
    private LauncherImage lcimg;
    private LauncherLabel vslider;
    private LauncherLabel sliderLabel;

    public LauncherPanel(LauncherPane root, GameEngine engine) {

        this.lcimg = new LauncherImage(root);
        this.lcimg.setImage(getResourceLocation().loadImage(engine, "logo.jpg"));
        this.lcimg.setSize(200, 200);
        this.lcimg.setPosition(engine.getWidth() / 2 - 100, 5);

        this.titleImage = new LauncherImage(root);
        this.titleImage.setImage(getResourceLocation().loadImage(engine, "favicon.png"));
        this.titleImage.setSize(engine.getWidth() / 3 + 40, 3);

        this.usernameField = new LauncherTextField(root);
        this.usernameField.setPosition(engine.getWidth() / 2 - 135, engine.getHeight() / 2 - 88);
        this.usernameField.setSize(270, 50);
        this.usernameField.setVoidText("E-mail");

        this.passwordField = new LauncherPasswordField(root);
        this.passwordField.setPosition(engine.getWidth() / 2 - 135, engine.getHeight() / 2 - 30);
        this.passwordField.setSize(270, 50);
        this.passwordField.setVoidText("Mot de passe (Vide = crack)");
        this.passwordField.setVisible(true);
        this.passwordField.setDisable(false);

        this.loginButton = new LauncherButton(root);
        this.loginButton.setText("Jouer.");
        this.loginButton.setPosition(engine.getWidth() / 2 - 97, engine.getHeight() / 2 + 30);
        this.loginButton.setSize(200, 45);

        this.updateRectangle = new LauncherRectangle(root, engine.getWidth() / 2 - 175, engine.getHeight() / 2 - 60, 350, 180);
        this.updateRectangle.setArcWidth(10.0);
        this.updateRectangle.setArcHeight(10.0);
        this.updateRectangle.setFill(Color.rgb(0, 0, 0, 0.60));
        this.updateRectangle.setVisible(false);
        /** =============== LABEL TITRE MISE A JOUR ===============  **/
        this.updateLabel = new LauncherLabel(root);
        this.updateLabel.setText("- MISE A JOUR -");
        this.updateLabel.setAlignment(Pos.CENTER);
        this.updateLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 22F));
        this.updateLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        this.updateLabel.setPosition(engine.getWidth() / 2 - 95, engine.getHeight() / 2 - 55);
        this.updateLabel.setOpacity(1);
        this.updateLabel.setSize(190, 40);
        this.updateLabel.setVisible(false);
        /** =============== ETAPE DE MISE A JOUR ===============  **/
        this.currentStep = new LauncherLabel(root);
        this.currentStep.setText("Preparation de la mise a jour.");
        this.currentStep.setFont(Font.font("Verdana", FontPosture.ITALIC, 18F)); // FontPosture.ITALIC
        this.currentStep.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        this.currentStep.setAlignment(Pos.CENTER);
        this.currentStep.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2 + 83);
        this.currentStep.setOpacity(0.4);
        this.currentStep.setSize(320, 40);
        this.currentStep.setVisible(false);
        /** =============== FICHIER ACTUEL EN TELECHARGEMENT ===============  **/
        this.currentFileLabel = new LauncherLabel(root);
        this.currentFileLabel.setText("launchwrapper-12.0.jar");
        this.currentFileLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 18F));
        this.currentFileLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        this.currentFileLabel.setAlignment(Pos.CENTER);
        this.currentFileLabel.setPosition(engine.getWidth() / 2 - 160, engine.getHeight() / 2 + 25);
        this.currentFileLabel.setOpacity(0.8);
        this.currentFileLabel.setSize(320, 40);
        this.currentFileLabel.setVisible(false);
        /** =============== POURCENTAGE ===============  **/
        this.percentageLabel = new LauncherLabel(root);
        this.percentageLabel.setFont(FontLoader.loadFont("Comfortaa-Regular.ttf", "Comfortaa", 30F));
        this.percentageLabel.setStyle("-fx-background-color: transparent; -fx-text-fill: white;");
        this.percentageLabel.setText("0%");
        this.percentageLabel.setAlignment(Pos.CENTER);
        this.percentageLabel.setPosition(engine.getWidth() / 2 - 50, engine.getHeight() / 2 - 5);
        this.percentageLabel.setOpacity(0.8);
        this.percentageLabel.setSize(100, 40);
        this.percentageLabel.setVisible(false);

        this.loginButton.setAction(event -> {
            if(this.usernameField.getText().length() < 3) {
                new LauncherAlert("Connection Echouée.", "Veuillez réésayer. Le pseudonyme doit contenir plus de 3 caractères.");
            } else if(this.usernameField.getText().length() > 3 && this.passwordField.getText().isEmpty()) {
                GameAuth auth = new GameAuth(this.usernameField.getText(), this.passwordField.getText(), AccountType.OFFLINE);
                if (auth.isLogged()) {
                    this.update(engine, auth);
                }
                } else if (usernameField.getText().length() > 3 && !this.passwordField.getText().isEmpty()) {
                    GameAuth auth = new GameAuth(this.usernameField.getText(), this.passwordField.getText(), AccountType.MOJANG);
                    if (auth.isLogged()) {
                        this.update(engine, auth);
                    }
                }
                else {
                    new LauncherAlert(":/", "Samarshpa.");
                }
            });
        }

    public void update(GameEngine engine, GameAuth auth) {
        this.passwordField.setDisable(true);
        this.usernameField.setDisable(true);
        this.loginButton.setDisable(true);
        this.passwordField.setVisible(false);
        this.usernameField.setVisible(false);
        this.loginButton.setVisible(false);
        this.percentageLabel.setVisible(true);
        this.currentFileLabel.setVisible(true);
        this.currentStep.setVisible(true);
        this.updateRectangle.setVisible(true);

        gameUpdater.reg(engine);
        gameUpdater.reg(auth.getSession());
        engine.reg(this.gameUpdater);
        this.updateThread = new Thread() {
            public void run() {
                engine.getGameUpdater().run();
            }
        };
        this.updateThread.start();
        this.timeline = new Timeline(new KeyFrame(javafx.util.Duration.seconds(0.0D), e -> updateDownload(engine)),
                new KeyFrame (javafx.util.Duration.seconds(0.1D)));
        this.timeline.setCycleCount(Animation.INDEFINITE);
        this.timeline.play();
    }

    public void updateDownload(GameEngine engine) {
        if (engine.getGameUpdater().downloadedFiles > 0) {
            this.percentageLabel.setText(decimalFormat.format(engine.getGameUpdater().downloadedFiles * 100.0D / engine.getGameUpdater().filesToDownload) + "%");
        }
        this.currentFileLabel.setText(engine.getGameUpdater().getCurrentFile());
        this.currentStep.setText(engine.getGameUpdater().getCurrentInfo());
    }

}