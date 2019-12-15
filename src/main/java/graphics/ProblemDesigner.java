package graphics;

import aco.Ant;
import aco.City;
import aco.Road;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ProblemDesigner {

    private Map<Road, Line> roadLineMap = new HashMap<>();
    private Map<Road, Text> roadTextMap = new HashMap<>();

    public void drawCities(Stage stage, List<City> cities, double circleRadius) {
        Pane pane = Pane.class.cast(stage.getScene().getRoot());
        Random random = new Random();

        cities.stream().forEach(city -> {

            Circle circle = new Circle();
            circle.setCenterX(city.getX());
            circle.setCenterY(city.getY());
            circle.setRadius(circleRadius);
            circle.setFill(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            circle.setStroke(Color.class.cast(circle.getFill()).darker());
            circle.setStrokeWidth(4.0);

            Text text = new Text();
            text.setText(city.getLabel());
            text.setX(city.getX() - 18.5);
            text.setY(city.getY() + 4.5);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            text.setFill(Color.class.cast(circle.getFill()).invert());

            pane.getChildren().add(circle);
            pane.getChildren().add(text);
        });
    }

    public void drawRoads(Stage stage, List<Road> roads) {
        Pane pane = Pane.class.cast(stage.getScene().getRoot());

        Random random = new Random();

        roads.stream().forEach(road -> {
            Line line = new Line();
            line.setStartX(road.getFirstCity().getX());
            line.setStartY(road.getFirstCity().getY());
            line.setEndX(road.getSecondCity().getX());
            line.setEndY(road.getSecondCity().getY());
            line.setStrokeWidth(4.0);
            line.setStroke(Color.rgb(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
            line.setStroke(Color.class.cast(line.getStroke()).darker());

            Text text = new Text();
            text.setText(Double.toString(road.getWeight()));
            text.setX((line.getStartX() + line.getEndX()) / 2);
            text.setY((line.getStartY() + line.getEndY()) / 2);
            text.setFont(Font.font("Arial", FontWeight.BOLD, 12));
            text.setFill(Color.class.cast(line.getStroke()).darker());

            pane.getChildren().add(line);
            pane.getChildren().add(text);

            this.roadLineMap.put(road, line);
            this.roadTextMap.put(road, text);
        });
    }

    public void colorifyRoads(List<Road> roads) {
        roads.stream().forEach(road -> {
            this.roadLineMap.get(road).setStroke(Color.WHITE);
            this.roadTextMap.get(road).setStroke(Color.WHITE);
        });
    }

    public void drawAntRoadsDescription(Stage stage, Ant ant, double width, double height) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(ant.getInitialCity().getLabel());

        City currentCity = ant.getCurrentCity();

        for (Road road : ant.getVisitedRoads()) {
            if (currentCity.equals(road.getFirstCity())) {
                stringBuffer.append(" -> " + road.getSecondCity().getLabel());
                currentCity = road.getSecondCity();
            } else {
                stringBuffer.append(" -> " + road.getFirstCity().getLabel());
                currentCity = road.getFirstCity();
            }

            stringBuffer.append(" [" + road.getWeight() + "]");
        }

        stringBuffer.append(" = [" + ant.getTrailWeight() + "]");

        Text text = new Text();
        text.setX(width / 4);
        text.setY(3 * height / 4);
        text.setFont(Font.font("Arial", FontWeight.BOLD, 16));
        text.setFill(Color.WHITE);
        text.setText(stringBuffer.toString());
        text.setWrappingWidth(3 * width / 4);

        Pane pane = Pane.class.cast(stage.getScene().getRoot());
        pane.getChildren().add(text);
    }
}
