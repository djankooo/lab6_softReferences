package softReferency;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import java.io.BufferedReader;
import java.io.FileReader;

public class DynamicTable extends Pane {

    final TableView<ObservableList<StringProperty>> table = new TableView<>();
    private String urlTextEntry;

    public DynamicTable(String urlTextEntry) throws Exception {
        this.urlTextEntry = urlTextEntry;
        populateTable(table, urlTextEntry);
        this.setBorder(new Border(new BorderStroke(Color.BLACK, BorderStrokeStyle.SOLID, CornerRadii.EMPTY, BorderWidths.DEFAULT)));
        table.prefWidthProperty().bind(this.widthProperty().multiply(1.0D));
        //table.prefHeightProperty().bind(this.widthProperty().multiply(1.0D));
        this.getChildren().add(table);
    }

    private void populateTable(final TableView<ObservableList<StringProperty>> table, final String urlSpec) throws Exception {

        table.getItems().clear();
        table.getColumns().clear();

        BufferedReader in = new BufferedReader(new FileReader(urlSpec));

        // Header line
        final String headerLine = in.readLine();
        final String[] headerValues = headerLine.split(",");

        for (int column = 0; column < headerValues.length; column++) {
            table.getColumns().add(createColumn(column, headerValues[column]));
        }

        // Data:

        String dataLine;

        while ((dataLine = in.readLine()) != null) {
            final String[] dataValues = dataLine.split(",");

            ObservableList<StringProperty> data = FXCollections.observableArrayList();
            for (String value : dataValues) {
                data.add(new SimpleStringProperty(value));
            }
            table.getItems().add(data);

        }

    }

    private TableColumn<ObservableList<StringProperty>, String> createColumn(final int columnIndex, String columnTitle) {

        TableColumn<ObservableList<StringProperty>, String> column = new TableColumn<>();
        String title;
        if (columnTitle == null || columnTitle.trim().length() == 0) {
            title = "Column " + (columnIndex + 1);
        } else {
            title = columnTitle;
        }
        column.setText(title);
        column.setCellValueFactory(cellDataFeatures ->  {
            ObservableList<StringProperty> values = cellDataFeatures.getValue();
            if (columnIndex >= values.size()) {
                return new SimpleStringProperty("");
            } else {
                return cellDataFeatures.getValue().get(columnIndex);
            }
        });
        return column;
    }
}