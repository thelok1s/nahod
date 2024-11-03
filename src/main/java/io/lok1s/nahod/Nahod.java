package io.lok1s.nahod;

import eu.hansolo.applefx.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Objects;

public class Nahod extends Application {

    private TreeView<File> fileTree;
    private VBox rightPane;
    private MacosCheckBox showHiddenFilesCheckBox;
    private final Image folderIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/folder-icon-alt.png")));
    private final Image fileIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/file-icon.png")));
    private final Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/appicon.png")));

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("NAHOD File System Analyzer");
        primaryStage.getIcons().add(appIcon);
        SplitPane root = createMainLayout();
        Scene scene = new Scene(root, 750, 400);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/styles.css")).toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private SplitPane createMainLayout() {
        SplitPane root = new SplitPane();
        root.getStyleClass().add("split-pane");
        VBox leftPane = createLeftPane();
        rightPane = new VBox(10);
        rightPane.getStyleClass().add("right-pane");
        setPlaceholder();
        root.getItems().addAll(leftPane, rightPane);
        return root;
    }

    private VBox createLeftPane() {
        VBox leftPane = new VBox(5);
        leftPane.getStyleClass().add("left-pane");
        showHiddenFilesCheckBox = createShowHiddenFilesCheckBox();
        fileTree = new TreeView<>();
        fileTree.setRoot(createFileTree(new File(System.getProperty("user.home"))));
        fileTree.setShowRoot(true);
        fileTree.getStyleClass().add("file-tree");
        fileTree.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showFileInfo(newSelection.getValue());
            } else {
                setPlaceholder();
            }
        });
        leftPane.getChildren().addAll(showHiddenFilesCheckBox, fileTree);
        return leftPane;
    }

    private MacosCheckBox createShowHiddenFilesCheckBox() {
        MacosCheckBox checkBox = new MacosCheckBox("Show Hidden Files");
        checkBox.setSelected(false);
        checkBox.getStyleClass().add("check-box");
        checkBox.selectedProperty().addListener((obs, oldVal, newVal) -> refreshFileTree());
        return checkBox;
    }

    private void refreshFileTree() {
        fileTree.setRoot(createFileTree(new File(System.getProperty("user.home"))));
    }

    private TreeItem<File> createFileTree(File dir) {
        TreeItem<File> root = createTreeItem(dir);
        root.setExpanded(true);

        if (dir.isDirectory()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (!showHiddenFilesCheckBox.isSelected() && file.isHidden()) {
                        continue;
                    }
                    TreeItem<File> item = createTreeItem(file);
                    if (file.isDirectory()) {
                        item.getChildren().add(new TreeItem<>());
                        setupLazyLoading(item, file);
                    }
                    root.getChildren().add(item);
                }
            }
        }
        return root;
    }

    private void setupLazyLoading(TreeItem<File> item, File file) {
        item.expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> {
            if (isNowExpanded && item.getChildren().size() == 1 && item.getChildren().getFirst().getValue() == null) {
                item.getChildren().clear();
                for (File child : Objects.requireNonNull(file.listFiles())) {
                    if (!showHiddenFilesCheckBox.isSelected() && child.isHidden()) {
                        continue;
                    }
                    item.getChildren().add(createTreeItem(child));
                }
            }
        });
    }

    private TreeItem<File> createTreeItem(File file) {
        ImageView iconView = new ImageView(file.isDirectory() ? folderIcon : fileIcon);
        iconView.setFitHeight(16);
        iconView.setFitWidth(16);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.getStyleClass().add("icon-view");
        return new TreeItem<>(file, iconView);
    }

    private void showFileInfo(File file) {
        rightPane.getChildren().clear();
        ImageView iconView = new ImageView(file.isDirectory() ? folderIcon : fileIcon);
        iconView.setFitHeight(128);
        iconView.setFitWidth(128);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.getStyleClass().add("icon");

        Label nameLabel = createStyledLabel("Name: " + file.getName(), "name-label");
        Label sizeLabel = createStyledLabel("Size: " + (file.isFile() ? file.length() + " bytes" : "Directory"), "size-label");

        try {
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Label dateLabel = createStyledLabel("Creation date: " + new Date(attr.creationTime().toMillis()), "date-label");
            rightPane.getChildren().addAll(iconView, nameLabel, sizeLabel, dateLabel);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setPlaceholder() {
        rightPane.getChildren().clear();
        ImageView placeholderImage = new ImageView(appIcon);
        placeholderImage.setFitHeight(256);
        placeholderImage.setFitWidth(256);
        placeholderImage.setPreserveRatio(true);
        placeholderImage.setSmooth(true);
        Label placeholderHeader = createStyledLabel("Welcome to NAHOD v0.1", "placeholder-header");
        Label placeholderSubheader = createStyledLabel("Select a file or folder to analyze", "placeholder-subheader");
        rightPane.getChildren().addAll(placeholderImage, placeholderHeader, placeholderSubheader);
    }

    private Label createStyledLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }

    public static void main(String[] args) {
        launch(args);
    }
}