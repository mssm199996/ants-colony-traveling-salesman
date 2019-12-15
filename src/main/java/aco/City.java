package aco;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"label", "roads"})
@NoArgsConstructor
@AllArgsConstructor
public class City {

    private String label;
    private Double x, y;
    private Map<City, Road> roads = new HashMap<>();

    @Override
    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append(this.label + "(" + this.x + ", " + this.y + "):\n");

        this.roads.values().stream().forEach(road -> stringBuffer.append("\t" + road.toString() + "\n"));

        return stringBuffer.toString();
    }

}
