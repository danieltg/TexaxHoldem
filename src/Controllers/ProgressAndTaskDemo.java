package Controllers;

import Engine.GameDescriptor.ReadGameDescriptorFile;
import javafx.application.Application;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.FlowPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class ProgressAndTaskDemo extends Application {

    private ReadGameDescriptorFile readFile;

    @Override
    public void start(Stage primaryStage) {

        final Label label = new Label("Validate XML game file");
        final ProgressBar progressBar = new ProgressBar(0);
        final ProgressIndicator progressIndicator = new ProgressIndicator(0);

        final Button startButton = new Button("Start");
        final Button cancelButton = new Button("OK");
        cancelButton.setVisible(false);

        final Label statusLabel = new Label();
        statusLabel.setMinWidth(250);
        statusLabel.setTextFill(Color.BLUE);

        // Start Button.
        startButton.setOnAction(event -> {
            startButton.setDisable(true);
            progressBar.setProgress(0);
            progressIndicator.setProgress(0);

            // Create a Task.
            readFile = new ReadGameDescriptorFile();
            readFile.setFilePath("/Users/danielt/Root/TexaxHoldem/invalidfile.xml");
            // Unbind progress property
            progressBar.progressProperty().unbind();

            // Bind progress property
            progressBar.progressProperty().bind(readFile.progressProperty());

            // Hủy bỏ kết nối thuộc tính progress
            progressIndicator.progressProperty().unbind();

            // Bind progress property.
            progressIndicator.progressProperty().bind(readFile.progressProperty());

            // Unbind text property for Label.
            statusLabel.textProperty().unbind();

            // Bind the text property of Label
            // with message property of Task
            statusLabel.textProperty().bind(readFile.messageProperty());

            readFile.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, //
                    t -> {
                        cancelButton.setVisible(true);
                    });

            // Start the Task.
            new Thread(readFile).start();

        });

        // Cancel
        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(readFile.isValidFile());
            }
        });

        FlowPane root = new FlowPane();
        root.setPadding(new Insets(10));
        root.setHgap(10);

        root.getChildren().addAll(label, progressBar, progressIndicator, //
                statusLabel, startButton, cancelButton);

        Scene scene = new Scene(root, 500, 120, Color.WHITE);
        primaryStage.setTitle("File validation");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        Application.launch(args);
    }

}