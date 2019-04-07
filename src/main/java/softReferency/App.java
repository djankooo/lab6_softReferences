package softReferency;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Forms");
        GridPane gridPane = createPane();
        setUIControls(gridPane, primaryStage);
        primaryStage.setScene(new Scene(gridPane, 1000, 500));
        primaryStage.show();
    }

    private GridPane createPane() {

        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setPadding(new Insets(20, 20, 20, 20));
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        return gridPane;
    }

    private void setUIControls(final GridPane gridPane, final Stage primaryStage) throws Exception {

        HBox header = new HBox();

        TextField urlTextField = new TextField("Podaj sciezke do folderu");
        urlTextField.setPromptText("C:\\Users\\djankooo\\Desktop\\test");

        Button next = new Button("Next ->");
        Button previous = new Button("<- Previous");

        final int[] iterator = {1};

        next.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.2D));
        previous.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.2D));

        header.getChildren().addAll(urlTextField);


        urlTextField.setOnAction(event -> {
            if (!urlTextField.getText().isEmpty()) try {

                // C:\\Users\\djankooo\\Desktop\\test"

                Object[] objects = App.this.listAllFiles(urlTextField.getText());

                if (objects.length > 1) {

                    Pane dTablePane = App.this.createCSVTable(objects, iterator[0], gridPane, primaryStage);
                    System.out.println("Iterator = " + iterator[0]);

                    dTablePane.prefWidthProperty().bind(primaryStage.widthProperty().multiply(1.0D));
                    gridPane.add(dTablePane, 0, 1);
                    urlTextField.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.6D));

                    header.getChildren().remove(urlTextField);
                    header.getChildren().addAll(previous, urlTextField, next);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        next.setOnAction(event -> {

            Object[] objects = App.this.listAllFiles(urlTextField.getText());

            if(iterator[0]<(objects.length)-1) {

                Pane dTablePane = null;

                try {
                    dTablePane = createCSVTable(objects, ++iterator[0], gridPane, primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                gridPane.getChildren().remove(dTablePane);
                dTablePane.prefWidthProperty().bind(primaryStage.widthProperty().multiply(1.0D));
                gridPane.add(dTablePane, 0, 1);
            }
        });

        previous.setOnAction(event -> {

            Object[] objects = App.this.listAllFiles(urlTextField.getText());

            if(iterator[0]>=2) {

                Pane dTablePane = null;

                try {
                    dTablePane = createCSVTable(objects, --iterator[0], gridPane, primaryStage);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                gridPane.getChildren().remove(dTablePane);
                dTablePane.prefWidthProperty().bind(primaryStage.widthProperty().multiply(1.0D));
                gridPane.add(dTablePane, 0, 1);
            }
        });

        urlTextField.prefWidthProperty().bind(primaryStage.widthProperty().multiply(1.0D));
        gridPane.add(header, 0, 0);
    }

    public Object[] listAllFiles(String path) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths.toArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Pane createCSVTable(Object[] objects, int iterator, final GridPane gridPane, final Stage primaryStage) throws Exception {
        return new DynamicTable(objects[iterator].toString());
    }
}
