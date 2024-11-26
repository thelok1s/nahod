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
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Date;
import java.util.Objects;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.io.IoBuilder;
import org.apache.commons.io.FileUtils;

/**
 * Основной класс приложения javafx.
 */
public class Nahod extends Application {
    private static final Logger logger = LogManager.getLogger(Nahod.class);
    private TreeView<File> fileTree;
    private VBox rightPane;
    private MacosCheckBox showHiddenFilesCheckBox;
    private MacosTextField fieldTreePath;
    private final Image folderIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/folder-icon-alt.png")));
    private final Image fileIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/file-icon.png")));
    private final Image appIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/appicon.png")));
    private final Image errorIcon = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/assets/error.png")));

    /**
     * Точка входа в приложение javafx.
     * Инициализирует главное окно и загружает основной макет.
     *
     * @param primaryStage Главное окно приложения.
     */
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

    /**
     * Создает основной макет приложения с разделителем.
     *
     * @return Главный контейнер SplitPane.
     */
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

    /**
     * Создает левую панель с элементами управления и деревом файлов.
     *
     * @return Контейнер VBox с элементами управления.
     */
    private VBox createLeftPane() {
        VBox leftPane = new VBox(5);
        leftPane.getStyleClass().add("left-pane");
        showHiddenFilesCheckBox = createShowHiddenFilesCheckBox();
        fieldTreePath = createPathField();
        fileTree = treeBuilder();
        leftPane.getChildren().addAll(fieldTreePath, fileTree, showHiddenFilesCheckBox);
        return leftPane;
    }

    /**
     * Создает дерево файловой системы.
     *
     * @return Компонент TreeView для отображения дерева файлов.
     */
    private TreeView<File> treeBuilder() {
        TreeView<File> treeView = new TreeView<>();
        if (fieldTreePath.getText().isEmpty() || fieldTreePath.getText().equals(" ")) {
            treeView.setRoot(createFileTree(new File(System.getProperty("user.home"))));
        } else {
            treeView.setRoot(createFileTree(new File(fieldTreePath.getText())));
        }
        treeView.setShowRoot(true);
        treeView.getStyleClass().add("file-tree");
        treeView.getSelectionModel().selectedItemProperty().addListener((observable, oldSelection, newSelection) -> {
            if (newSelection != null) {
                showFileInfo(newSelection.getValue());
            } else {
                setPlaceholder();
            }
        });
        return treeView;
    }

    /**
     * Создает флажок для отображения скрытых файлов.
     *
     * @return Компонент MacosCheckBox.
     */
    private MacosCheckBox createShowHiddenFilesCheckBox() {
        MacosCheckBox checkBox = new MacosCheckBox("Show Hidden Files");
        checkBox.setSelected(false);
        checkBox.getStyleClass().add("check-box");
        checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> refreshFileTree());
        return checkBox;
    }

    /**
     * Создает поле ввода пути к каталогу.
     *
     * @return Компонент MacosTextField для ввода пути.
     */
    private MacosTextField createPathField() {
        MacosTextField pathField = new MacosTextField();
        pathField.setPromptText("Path to analyze (home by default)");
        pathField.getStyleClass().add("path-field");
        pathField.setOnAction(event -> {
            File dir = new File(pathField.getText());
            if (dir.isDirectory()) {
                fileTree.setRoot(createFileTree(dir));
            } else if (Objects.equals(pathField.getText(), "")) {
                fileTree.setRoot(createFileTree(new File(System.getProperty("user.home"))));
            } else {
                logger.error("Path \"{}\" does not exist or not available", pathField.getText());
            }
        });
        return pathField;
    }


    /**
     * Обновляет дерево файлов с учетом текущего пути.
     */
    private void refreshFileTree() {
        fileTree.setRoot(createFileTree(new File(System.getProperty("user.home"))));
    }

    /**
     * Создает дерево файлов для указанного каталога.
     *
     * @param dir Корневой каталог.
     * @return Корневой элемент дерева.
     */
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

    /**
     * Настраивает ленивую загрузку содержимого папки.
     *
     * @param item Элемент дерева.
     * @param file Файл или папка.
     */
    private void setupLazyLoading(TreeItem<File> item, File file) {
        item.expandedProperty().addListener((observable, oldValue, isNowExpanded) -> {
            if (isNowExpanded && item.getChildren().size() == 1 && item.getChildren().get(0).getValue() == null) {
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

    /**
     * Создает элемент дерева для файла или папки.
     *
     * @param file Файл или папка.
     * @return Элемент TreeItem.
     */
    private TreeItem<File> createTreeItem(File file) {
        ImageView iconView = new ImageView(file.isDirectory() ? folderIcon : fileIcon);
        iconView.setFitHeight(16);
        iconView.setFitWidth(16);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.getStyleClass().add("icon-view");
        return new TreeItem<>(file, iconView);
    }

    /**
     * Отображает информацию о выбранном файле.
     *
     * @param file Файл или папка.
     */
    private void showFileInfo(File file) {
        rightPane.getChildren().clear();
        ImageView iconView = new ImageView(file.isDirectory() ? folderIcon : fileIcon);
        iconView.setFitHeight(128);
        iconView.setFitWidth(128);
        iconView.setPreserveRatio(true);
        iconView.setSmooth(true);
        iconView.getStyleClass().add("icon");

        Label nameLabel = createStyledLabel("Name: " + file.getName(), "name-label");
        Label sizeLabel;

        try {
            if (file.isDirectory()) {
                long folderSize = FileUtils.sizeOfDirectory(file);
                sizeLabel = createStyledLabel("Size: " + FileUtils.byteCountToDisplaySize(folderSize), "size-label");
            } else {
                sizeLabel = createStyledLabel("Size: " + FileUtils.byteCountToDisplaySize(file.length()), "size-label");
            }
            BasicFileAttributes attr = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
            Label createdDateLabel = createStyledLabel("Creation date: " + new Date(attr.creationTime().toMillis()), "date-label");
            Label modifiedDateLabel = createStyledLabel("Last modification date: " + new Date(attr.lastModifiedTime().toMillis()), "date-label");
            rightPane.getChildren().addAll(iconView, nameLabel, sizeLabel, createdDateLabel, modifiedDateLabel);
        } catch (IOException e) {
            logger.error(e);
        } catch (UncheckedIOException e) {
            logger.error("Cannot access {}", file);
            setErrorAlert();
        }
    }


    /**
     * Устанавливает контент панели в правой части интерфейса.
     */
    private void setPlaceholderContent(Image icon, String headerText, String subheaderText) {
        rightPane.getChildren().clear();

        ImageView placeholderImage = new ImageView(icon);
        placeholderImage.setFitHeight(256);
        placeholderImage.setFitWidth(256);
        placeholderImage.setPreserveRatio(true);
        placeholderImage.setSmooth(true);

        Label placeholderHeader = createStyledLabel(headerText, "placeholder-header");
        Label placeholderSubheader = createStyledLabel(subheaderText, "placeholder-subheader");

        rightPane.getChildren().addAll(placeholderImage, placeholderHeader, placeholderSubheader);
    }

    /**
     * Устанавливает информационную панель в правой части интерфейса.
     */
    private void setPlaceholder() {
        setPlaceholderContent(appIcon, "Welcome to NAHOD v1.1", "Select a file or folder to analyze");
    }

    /**
     * Устанавливает панель ошибки в правой части интерфейса.
     */
    private void setErrorAlert() {
        setPlaceholderContent(errorIcon, "No access", "Check permissions and try again");
    }

    /**
     * Создает стилизованный элемент.
     *
     * @param text Текст метки.
     * @param styleClass Класс стилей.
     * @return Объект Label.
     */
    private Label createStyledLabel(String text, String styleClass) {
        Label label = new Label(text);
        label.getStyleClass().add(styleClass);
        return label;
    }


    /**
     * Запуск приложения.
     *
     * @param args Аргументы командной строки.
     */
    public static void main(String[] args) {
        System.setOut(IoBuilder.forLogger(logger).buildPrintStream());
        System.setErr(IoBuilder.forLogger(logger).buildPrintStream());

        logger.info("Application launched");
        launch(args);
        logger.info("Application closed");
    }
}