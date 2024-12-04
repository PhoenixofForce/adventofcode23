package dev.phoenixofforce.adventofcode.y2023.day25;

import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;
import dev.phoenixofforce.adventofcode.solver.Dijkstra;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
public class Day25_2023 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {

        List<String> allPoints = new ArrayList<>();
        Map<String, List<String>> connections = new HashMap<>();

        //Parse Input
        for(String line: input.getLines()) {
            String[] rawConnections = line.replace(":", "")
                .split(" ");

            for(int i = 1; i < rawConnections.length; i++) {
                if(!connections.containsKey(rawConnections[0])) connections.put(rawConnections[0], new ArrayList<>());
                if(!connections.containsKey(rawConnections[i])) connections.put(rawConnections[i], new ArrayList<>());
                if(!allPoints.contains(rawConnections[0])) allPoints.add(rawConnections[0]);
                if(!allPoints.contains(rawConnections[1])) allPoints.add(rawConnections[1]);

                connections.get(rawConnections[0]).add(rawConnections[i]);
                connections.get(rawConnections[i]).add(rawConnections[0]);
            }
        }

        //Get two opposing elements
        String firstElement = Dijkstra.findPathWithMultipleEndsAndHeuristicAndMaybeLast(
            allPoints.get(0),
            (e) -> true,
            connections::get,
            (e, sum) -> sum + 1,
            t -> 0,
            false
        ).getEndElement();

        String secondElement = Dijkstra.findPathWithMultipleEndsAndHeuristicAndMaybeLast(
            firstElement,
            (e) -> true,
            connections::get,
            (e, sum) -> sum + 1,
            t -> 0,
            false
        ).getEndElement();

        //Get three paths and remove them, since there are three bridged, each bridge must be inside of one path
        Dijkstra.End<String> path = findPathWithoutPaths(firstElement, secondElement, connections, List.of());
        List<String> firstPath = path.getPath();
        List<List<String>> allPaths = new ArrayList<>();
        for(int i = 0; i < firstPath.size() - 1; i++) {
            allPaths.add(List.of(firstPath.get(i), firstPath.get(i + 1)));
        }

        path = findPathWithoutPaths(firstElement, secondElement, connections, allPaths);
        List<String> secondPath = path.getPath();
        for(int i = 0; i < secondPath.size() - 1; i++) {
            allPaths.add(List.of(secondPath.get(i), secondPath.get(i + 1)));
        }

        path = findPathWithoutPaths(firstElement, secondElement, connections, allPaths);
        List<String> thirdPath = path.getPath();
        for(int i = 0; i < thirdPath.size() - 1; i++) {
            allPaths.add(List.of(thirdPath.get(i), thirdPath.get(i + 1)));
        }

        //iterate through all paths and remove one bridge each
        //if there are two groups add them to the answer
        List<Integer> answers = new ArrayList<>();
        for(int i = 0; i < firstPath.size() - 1; i++) {
            for(int j = 0; j < secondPath.size() - 1; j++) {
                for(int k = 0; k < thirdPath.size() - 1; k++) {

                    List<List<String>> removedPaths = List.of(
                        List.of(firstPath.get(i), firstPath.get(i + 1)),
                        List.of(secondPath.get(j), secondPath.get(j + 1)),
                        List.of(thirdPath.get(k), thirdPath.get(k + 1))
                    );
                    Dijkstra.End<String> possiblePath = findPathWithoutPaths(firstElement, secondElement, connections, removedPaths);

                    if(possiblePath.getDistance() < 10 * allPoints.size()) continue;
                    System.out.println(removedPaths + " must be removed");

                    List<List<String>> groups = findGroups(connections, allPoints, removedPaths);
                    if(groups.size() == 2) {
                        answers.add(groups.get(0).size() * groups.get(1).size());
                    }
                }
            }
        }

        System.out.println("Possible answers: " + answers);
        return answers.size() == 1 ? answers.get(0) : 0;
    }

    private Dijkstra.End<String> findPathWithoutPaths(String firstElement, String secondElement, Map<String, List<String>> connections, List<List<String>> removed) {
        Map<String, List<String>> connectionsWithoutRemoved = new HashMap<>();
        for(String key: connections.keySet()) {
            connectionsWithoutRemoved.put(key, new ArrayList<>(connections.get(key)));
        }

        if(!removed.isEmpty()) {
            removed.forEach(e -> connectionsWithoutRemoved.get(e.get(0)).remove(e.get(1)));
            removed.forEach(e -> connectionsWithoutRemoved.get(e.get(1)).remove(e.get(0)));
        }

        return Dijkstra.findPathWithMultipleEndsAndHeuristicAndMaybeLast(
            firstElement,
            secondElement::equals,
            connectionsWithoutRemoved::get,
            (e, sum) -> sum + 1,
            t -> 0,
            true
        );
    }

    private List<List<String>> findGroups(Map<String, List<String>> connections, List<String> allPoints, List<List<String>> removed) {
        Map<String, List<String>> connectionsWithoutRemoved = new HashMap<>();
        for(String key: connections.keySet()) {
            connectionsWithoutRemoved.put(key, new ArrayList<>(connections.get(key)));
        }

        if(!removed.isEmpty()) {
            removed.forEach(e -> connectionsWithoutRemoved.get(e.get(0)).remove(e.get(1)));
            removed.forEach(e -> connectionsWithoutRemoved.get(e.get(1)).remove(e.get(0)));
        }

        List<String> open = new ArrayList<>();
        List<String> visited = new ArrayList<>();
        List<List<String>> groups = new ArrayList<>();

        for(String initialPoint: allPoints) {
            open.add(initialPoint);

            while(!open.isEmpty()) {
                String point = open.remove(0);
                if(visited.contains(point)) continue;

                Optional<List<String>> possibleContainingGroup = groups.stream().filter(group -> group.stream().anyMatch(pointInGroup -> connectionsWithoutRemoved.get(pointInGroup).contains(point))).findFirst();
                List<String> group = possibleContainingGroup.orElse(new ArrayList<>());
                if(possibleContainingGroup.isEmpty()) groups.add(group);

                List<String> toAdd = new ArrayList<>(connectionsWithoutRemoved.get(point));
                toAdd.add(point);
                visited.add(point);
                open.addAll(toAdd);
                toAdd.forEach(e -> {
                    if(!group.contains(e)) {
                        group.add(e);
                    }
                });
            }
        }

        return groups;
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        return "";
    }

}
