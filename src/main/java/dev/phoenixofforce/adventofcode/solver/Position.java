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
    private final long z;

    public Position(long x, long y) {
        this(x, y, 0);
    }

    public Position offset(long dx, long dy) {
        return new Position(this.x + dx, this.y + dy);
    }

    public Position offset(long dx, long dy, long dz) {
        return new Position(this.x + dx, this.y + dy, this.z + dz);
    }

    public Position applyDirection(Direction direction) {
        return this.applyDirection(direction, 1);
    }

    public Position applyDirection(Direction direction, long length) {
        return offset(direction.getDx() * length, direction.getDy() * length);
    }

    @Override
    public Position clone() {
        return new Position(x, y, z);
    }


    @Override
    public int compareTo(Position o) {
        if(this.x == o.x) {
            if(this.y == o.y) return Long.compare(z, o.z);
            return Long.compare(y, o.y);
        }
        return Long.compare(x, o.x);
    }

    @Override
    public String toString() {
        return "(" + x + " | " + y + (z != 0? " | " + z: "") + ")";
    }
}
