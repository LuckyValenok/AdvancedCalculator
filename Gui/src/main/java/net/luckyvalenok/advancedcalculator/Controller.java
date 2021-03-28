package net.luckyvalenok.advancedcalculator;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import net.luckyvalenok.advancedcalculator.elements.Axes;
import net.luckyvalenok.advancedcalculator.elements.Plot;

import java.util.Map;

public class Controller {
    @FXML
    private Button count;
    @FXML
    private TextField textField;
    
    @FXML
    public void initialize() {
        count.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent -> {
            /*
                TODO: Обработчик ошибок и использование спиннеров
             */
            try {
                Calculator calculator = new Calculator(-8, 8, 0.1, textField.getText());
                Map<Double, Double> results = calculator.getFilledMap();
                drawPlot((Stage) ((Node) mouseEvent.getSource()).getScene().getWindow(), 400, 400, -calculator.getMaxY(), calculator.getMaxY(), calculator.getStart(), calculator.getStop(), calculator.getStep(), results);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    private void drawPlot(Stage stage, int width, int height, double yMin, double yMax, double xMin, double xMax, double step, Map<Double, Double> results) {
        Axes axes = new Axes(
            width, height,
            xMin, xMax, step,
            yMin, yMax, step
        );
        
        Plot plot = new Plot(
            results,
            xMin, xMax, step,
            axes
        );
        
        StackPane layout = new StackPane(
            plot
        );
        layout.setPadding(new Insets(20));
        
        stage.setTitle("y = ?");
        stage.setScene(new Scene(layout));
    }
}
