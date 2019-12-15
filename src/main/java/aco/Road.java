package aco;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(exclude = {"weight", "pheromones"})
public class Road {

    private City firstCity, secondCity;
    private Double weight = 1.0, pheromones = 0.0;

    public void addPheromones(Double pheromones) {
        this.pheromones += pheromones;
    }

    public void evaporatePheromones(Double evaporationRate) {
        this.pheromones *= evaporationRate;
    }

    @Override
    public String toString() {
        return this.firstCity.getLabel() + " -> " + this.secondCity.getLabel() + " --- W = " + this.weight;
    }
}
