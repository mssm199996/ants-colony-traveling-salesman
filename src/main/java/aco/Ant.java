package aco;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

@Getter
@Setter
public class Ant {

    private static int INDEX = 0;

    private City currentCity, initialCity;
    private Road lastTakenRoad;

    private List<Road> visitedRoads = new LinkedList<>();
    private Collection<City> unvisitedCities = new LinkedList<>();

    private Double trailWeight = 0.0;
    private String label;

    public Ant(City initialCity, Collection<City> cities) {
        this.label = "Ant " + Ant.INDEX++;

        this.currentCity = initialCity;
        this.initialCity = initialCity;

        this.unvisitedCities.addAll(cities);
    }

    public void visitCity(City city) {
        Road road = this.currentCity.getRoads().get(city);

        this.trailWeight += road.getWeight();
        this.visitedRoads.add(road);

        this.currentCity = city;
        this.lastTakenRoad = road;
        this.unvisitedCities.remove(city);
    }

    public boolean isVisitPossible(City city) {
        if (!city.equals(this.initialCity)) {
            return this.unvisitedCities.contains(city) && this.currentCity.getRoads().get(city) != null;
        } else {
            return this.unvisitedCities.size() == 1 && !this.currentCity.equals(city);
        }
    }

    public Boolean hasVisitedCity(City city) {
        return !this.unvisitedCities.contains(city);
    }
}
