package aco;

import lombok.Getter;
import lombok.Setter;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Getter
@Setter
public class AntAlgorithm {

    private double alpha = 1;
    private double beta = 5;
    private double evaporation = 0.5;
    private double q = 500;
    private double randomFactor = 0.01;

    private Random random = new Random();

    private Collection<Ant> ants = new ArrayList<>();
    private Collection<Road> roads;

    private List<City> cities;
    private List<Road> bestRoad;
    private Ant bestAnt;

    public AntAlgorithm(List<City> cities, Collection<Road> roads, Integer antsNumber) {
        this.setRoads(roads);
        this.setCities(cities);

        this.initCities(antsNumber);
    }

    public AntAlgorithm(List<City> cities, Collection<Ant> ants, Collection<Road> roads) {
        this.setAnts(ants);
        this.setRoads(roads);
        this.setCities(cities);

        this.initAnts(ants);
    }

    public void solve(int maxNumberOfIterations) {
        IntStream.range(0, maxNumberOfIterations).forEach(i -> {
            this.moveAnts();
            this.updateRoads();
            this.updateBestRoad();
        });
    }

    private void updateBestRoad() {
        this.bestAnt = this.ants.stream().sorted(Comparator.comparingDouble(Ant::getTrailWeight)).findFirst().get();
        this.bestRoad = this.bestAnt.getVisitedRoads();
    }

    private void updateRoads() {
        this.roads.stream().forEach(road -> road.evaporatePheromones(this.evaporation));
        this.ants.stream().forEach(ant -> ant.getLastTakenRoad().addPheromones(this.q / ant.getTrailWeight()));
    }

    private void moveAnts() {
        // This is done to give a certain randomness to the algorithms

        this.ants.forEach((ant) -> {
            boolean visitedRandomCity = false;

            if (this.random.nextDouble() < this.randomFactor) {
                Integer cityIndex = (new Random()).nextInt(this.cities.size());

                City randomCity = this.cities.get(cityIndex);

                if (ant.isVisitPossible(randomCity)) {
                    ant.visitCity(randomCity);

                    visitedRandomCity = true;
                }
            }

            if (!visitedRandomCity) {
                Map<City, Double> cityDoubleMap = ant.getUnvisitedCities().stream()
                        .filter(city -> ant.isVisitPossible(city))
                        .collect(Collectors.toMap(
                                (city) -> city,
                                (city) -> this.calculateProbability(ant, city)
                        ));

                if (!cityDoubleMap.isEmpty()) {
                    City city = cityDoubleMap.entrySet().stream().sorted((c1, c2) -> (c2.getValue() > c1.getValue() ? 1 : (c1.getValue() < c2.getValue() ? -1 : 0)))
                            .findFirst().get().getKey();

                    ant.visitCity(city);
                }
            }
        });
    }

    private void initAnts(Collection<Ant> ants) {
        Random random = new Random();

        ants.forEach((ant) -> {
            ant.setCurrentCity(this.cities.get(random.nextInt(this.cities.size() - 1)));
            ant.getUnvisitedCities().addAll(this.cities);
        });
    }

    private void initCities(Integer numberAnts) {
        Integer ants = numberAnts / this.cities.size();

        this.cities.stream().forEach(city ->
                IntStream.range(0, ants).forEach(i -> this.ants.add(new Ant(city, this.cities)))
        );
    }

    private Double calculateProbability(Ant ant, City city) {
        City antCurrentCity = ant.getCurrentCity();
        Road targetedRoad = city.getRoads().get(antCurrentCity);

        if (targetedRoad != null) {
            Double visibility = 1 / targetedRoad.getWeight(); // nabla value
            Double intensity = targetedRoad.getPheromones(); // tho value

            return Math.pow(visibility, this.beta) * Math.pow(intensity, this.alpha);
        }

        return 0.0;
    }
}
