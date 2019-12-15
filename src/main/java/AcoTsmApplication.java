import aco.AntAlgorithm;
import aco.City;
import aco.Road;
import graphics.ProblemDesigner;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

public class AcoTsmApplication extends Application {

    public static ArrayList<City> CITIES = new ArrayList();
    public static List<Road> ROADS = new ArrayList<>();

    public static Integer WIDTH = 1360;
    public static Integer HEIGHT  = 768;
    public static Integer RADIUS = 30;

    public static void main(String[] args) {
        AcoTsmApplication.initGraph(6);

        launch(args);
    }

    public static void initGraph(int nbCities) {
        int matrixDimension = (int) Math.ceil(Math.sqrt(nbCities));

        int xStart = 2 * AcoTsmApplication.RADIUS;
        int yStart = 2 * AcoTsmApplication.RADIUS;

        int xEnd = AcoTsmApplication.WIDTH - 2 * AcoTsmApplication.RADIUS - xStart;
        int yEnd = AcoTsmApplication.HEIGHT - 2 * AcoTsmApplication.RADIUS - yStart;

        Random random = new Random();

        // -------------- Cities initialisation ---------------

        IntStream.range(0, nbCities).forEach((i) -> {
            int a = i / matrixDimension;
            int b = i % matrixDimension;

            int currentXStart = ((xEnd - xStart) / matrixDimension) * b + xStart;
            int currentYStart = ((yEnd - yStart) / matrixDimension) * a + yStart;

            int currentXEnd = currentXStart + (xEnd - xStart) / matrixDimension;
            int currentYEnd = currentYStart + (yEnd - yStart) / matrixDimension;

            City city = new City();
            city.setX((double) random.nextInt(currentXEnd - currentXStart) + currentXStart);
            city.setY((double) random.nextInt(currentYEnd - currentYStart) + currentYStart);
            city.setLabel("City " + (i + 1));

            AcoTsmApplication.CITIES.add(city);
        });

        // -------------- Roads initialisation ------------------

        IntStream.range(0, nbCities - 1).forEach(i -> {
            IntStream.range(i + 1, nbCities).forEach(j -> {
                City start = AcoTsmApplication.CITIES.get(i);
                City end = AcoTsmApplication.CITIES.get(j);

                Road road = new Road();
                road.setFirstCity(start);
                road.setSecondCity(end);
                road.setWeight((double) random.nextInt(10));

                AcoTsmApplication.ROADS.add(road);
            });
        });

        AcoTsmApplication.ROADS.stream().forEach(road -> {
            road.getFirstCity().getRoads().put(road.getSecondCity(), road);
            road.getSecondCity().getRoads().put(road.getFirstCity(), road);
        });
    }


    @Override
    public void start(Stage stage) {
        BackgroundFill background = new BackgroundFill(Color.DARKGRAY, new CornerRadii(0.0), new Insets(0.0));

        Pane root = new Pane();
        root.setBackground(new Background(background));

        stage.setScene(new Scene(root));
        stage.setTitle("MSSM's Ants Colony Optimization TSM Solver");
        stage.setOnCloseRequest(e -> Platform.exit());
        stage.setMaximized(true);
        stage.show();

        AntAlgorithm antAlgorithm = new AntAlgorithm(AcoTsmApplication.CITIES,
                AcoTsmApplication.ROADS, 1500);
        antAlgorithm.solve(100);

        ProblemDesigner problemDesigner = new ProblemDesigner();
        problemDesigner.drawRoads(stage, AcoTsmApplication.ROADS);
        problemDesigner.drawCities(stage, AcoTsmApplication.CITIES, AcoTsmApplication.RADIUS);
        problemDesigner.drawAntRoadsDescription(stage, antAlgorithm.getBestAnt(), AcoTsmApplication.WIDTH, AcoTsmApplication.HEIGHT);
        problemDesigner.colorifyRoads(antAlgorithm.getBestRoad());
    }
}
