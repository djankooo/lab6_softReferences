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
import java.lang.ref.ReferenceQueue;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.WeakHashMap;
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

        ReferenceQueue<Pane> referenceQueue = new ReferenceQueue<>();

        WeakHashMap<Integer, Pane> map = new WeakHashMap<>();

        final int[] iterator = {1};

        next.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.2D));
        previous.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.2D));

        header.getChildren().addAll(urlTextField);


        urlTextField.setOnAction(event -> {
            if (!urlTextField.getText().isEmpty()) try {

                Object[] objects = listAllFiles(urlTextField.getText());
                // C:\\Users\\djankooo\\Desktop\\test"

                System.out.println(System.getProperty("java.version"));

                map.put(iterator[0], createCSVTable(objects, iterator[0], gridPane, primaryStage));

                if (objects.length > 1) {

                    map.get(iterator[0]).prefWidthProperty().bind(primaryStage.widthProperty().multiply(1.0D));
                    gridPane.add(map.get(iterator[0]), 0, 1);
                    urlTextField.prefWidthProperty().bind(primaryStage.widthProperty().multiply(0.6D));

                    header.getChildren().remove(urlTextField);
                    header.getChildren().addAll(previous, urlTextField, next);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        next.setOnAction(event -> {

            Object[] objects = listAllFiles(urlTextField.getText());

            if(iterator[0]<(objects.length)-1) {

                iterator[0]++;
                System.out.println("Next iterator : " + iterator[0]);
                System.out.println("(objects.length) : " + (objects.length));

                Pane dTablePane = null;
                System.out.println("map.containsKey(iterator[0]) : " + map.containsKey(iterator[0]));

                if(map.containsKey(iterator[0])) {
                    dTablePane = map.get(iterator[0]);
                    System.out.println("map.containsKey(iterator[0]) : " + map.get(iterator[0]));
                }else{
                    try {
                        map.put(iterator[0], createCSVTable(objects, iterator[0], gridPane, primaryStage));
                        dTablePane = map.get(iterator[0]);
                        System.out.println("map.containsKey(iterator[0]) : " + map.get(iterator[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                gridPane.getChildren().remove(dTablePane);
                dTablePane.prefWidthProperty().bind(primaryStage.widthProperty().multiply(1.0D));
                gridPane.add(dTablePane, 0, 1);
            }
        });

        previous.setOnAction(event -> {

            Object[] objects = listAllFiles(urlTextField.getText());

            if(iterator[0]>=2) {

                iterator[0]--;
                System.out.println("Next iterator : " + iterator[0]);
                System.out.println("(objects.length) : " + (objects.length));

                Pane dTablePane = null;
                System.out.println("map.containsKey(iterator[0]) : " + map.containsKey(iterator[0]));

                if(map.containsKey(iterator[0])) {
                    dTablePane = map.get(iterator[0]);
                    System.out.println("map.containsKey(iterator[0]) : " + map.get(iterator[0]));
                }else{
                    try {
                        map.put(iterator[0], createCSVTable(objects, iterator[0], gridPane, primaryStage));
                        dTablePane = map.get(iterator[0]);
                        System.out.println("map.containsKey(iterator[0]) : " + map.get(iterator[0]));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
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
