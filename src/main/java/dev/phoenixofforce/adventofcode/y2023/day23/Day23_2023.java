package dev.phoenixofforce.adventofcode.y2023.day23;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Direction;
import dev.phoenixofforce.adventofcode.solver.Position;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Day23_2023 implements Puzzle {

    @Data @AllArgsConstructor @EqualsAndHashCode
    private static class State {
        private State previous;
        private Position pos;
        private int length;

        boolean contains(Position p) {
            if(pos.equals(p)) return true;
            if(previous == null) return false;
            return previous.contains(p);
        }
    }

    @Data @AllArgsConstructor @EqualsAndHashCode
    static class Edge {
        Position first, second;
        int length;
    }

    @Override
    public Object solvePart1(PuzzleInput input) {
        State start = new State(null, new Position( 1, 0), 0);
        Stack<State> stack = new Stack<>();
        stack.add(start);

        long most = -1;
        while(!stack.isEmpty()) {
            State current = stack.pop();
            if(current.getPos().getY() == input.height() - 1) {
                most = Math.max(most, current.length);
                System.out.println(most);
                continue;
            }
            for(State s: advance(input, current)) {
                stack.push(s);
            }
        }

        return most;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {

        Set<Position> uniquePoints = new HashSet<>();
        List<Edge> graph = new ArrayList<>();
        for(int x = 0; x < input.width(); x++) {
            for(int y = 0; y < input.height(); y++) {
                if(input.getChar(x, y) != '#') {
                    List<State> neighbors = advance(input, new State(null, new Position(x, y), 0));
                    for(State s: neighbors) {
                        if(!(graph.contains(new Edge(new Position(x, y), s.getPos(), 1)) ||
                            graph.contains(new Edge(s.getPos(), new Position(x, y), 1)))) {
                            graph.add(new Edge(new Position(x, y), s.getPos(), 1));
                        }
                        uniquePoints.add(new Position(x, y));
                    }
                }
            }
        }

        System.out.println("graph size");
        System.out.print(graph.size() + " => ");
        boolean change = true;
        while(change) {
            change = false;

            for(Position uniquePoint: uniquePoints) {
                List<Edge> neighbors = graph.stream()
                    .filter(e -> e.first.equals(uniquePoint) || e.second.equals(uniquePoint))
                    .toList();

                if(neighbors.size() == 2) {
                    change = true;
                    Edge e1 = neighbors.get(0);
                    Edge e2 = neighbors.get(1);

                    Position p1 = e1.first.equals(uniquePoint)? e1.second: e1.first;
                    Position p2 = e2.first.equals(uniquePoint)? e2.second: e2.first;

                    graph.add(new Edge(p1, p2, e1.length + e2.length));
                    graph.remove(e1);
                    graph.remove(e2);
                    uniquePoints.remove(uniquePoint);
                    break;
                }
            }
        }

        System.out.println(graph.size());

        State start = new State(null, new Position( 1, 0), 0);
        Stack<State> stack = new Stack<>();
        stack.add(start);

        long most = -1;
        while(!stack.isEmpty()) {
            State current = stack.pop();
            if(current.getPos().getY() == input.height() - 1) {
                most = Math.max(most, current.length);
                System.out.println(most);
                continue;
            }
            for(State s: advance(graph, current)) {
                stack.push(s);
            }
        }

        return most;
    }

    private List<State> advance(PuzzleInput input, State in) {

        List<Direction> directions = List.of(Direction.values());
        char current = input.getChar(in.getPos().getX(), in.getPos().getY());
        if(current != '.') {
            directions = List.of(Direction.getDirection4(current));
        }

        List<State> out = new ArrayList<>();
        for(Direction d: directions) {
            Position newPos = in.getPos().applyDirection(d);
            if(input.inbounds(newPos.getX(), newPos.getY()) &&
                input.getChar(newPos.getX(), newPos.getY()) != '#' &&
                !in.contains(newPos)
            ) {
                out.add(new State(in,newPos, in.length + 1));
            }
        }

        return out;
    }

    private List<State> advance(List<Edge> graph, State in) {

        List<Edge> outgoingEdges = graph.stream()
            .filter(e -> e.first.equals(in.getPos()) || e.second.equals(in.getPos()))
            .toList();

        List<State> out = new ArrayList<>();
        for(Edge e: outgoingEdges) {
            Position other = e.first.equals(in.getPos())? e.second: e.first;
            if(!in.contains(other)) out.add(new State(in, other, in.length + e.length));
        }

        return out;
    }
}
