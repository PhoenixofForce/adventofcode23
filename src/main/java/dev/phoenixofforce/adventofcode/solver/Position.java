package dev.phoenixofforce.adventofcode.solver;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Position implements Comparable<Position> {

    private final long x;
    private final long y;

    public Position offset(long dx, long dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    public Position applyDirection(Direction direction) {
        return this.applyDirection(direction, 1);
    }

    public Position applyDirection(Direction direction, long length) {
        return offset(direction.getDx() * length, direction.getDy() * length);
    }

    private Position copy() {
        return new Position(x, y);
    }


    @Override
    public int compareTo(Position o) {
        if(this.x == o.x) return Long.compare(y, o.y);
        return Long.compare(x, o.x);
    }
}
