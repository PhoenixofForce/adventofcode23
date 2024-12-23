package dev.phoenixofforce.adventofcode.y2024.day23;

import org.springframework.stereotype.Component;
import dev.phoenixofforce.adventofcode.meta.Puzzle;
import dev.phoenixofforce.adventofcode.meta.PuzzleInput;

import java.util.*;
import java.util.stream.Stream;

@Component
public class Day23_2024 implements Puzzle {

    @Override
    public Object solvePart1(PuzzleInput input) {
        Map<String, List<String>> connections = new HashMap<>();
        Set<String> allComputers = new HashSet<>();

        for(String line: input.getLines()) {
            String computer0 = line.split("-")[0];
            String computer1 = line.split("-")[1];

            if(!connections.containsKey(computer0)) connections.put(computer0, new ArrayList<>());
            if(!connections.containsKey(computer1)) connections.put(computer1, new ArrayList<>());

            connections.get(computer0).add(computer1);
            connections.get(computer1).add(computer0);

            allComputers.add(computer0);
            allComputers.add(computer1);
        }

        Set<String> threeTuplesWithT = new HashSet<>();
        for(String computer0: allComputers) {
            if(!computer0.startsWith("t")) continue;
            List<String> connection = connections.get(computer0);

            for(int i = 1; i < connection.size(); i++) {
                String computer1 = connection.get(i);
                for(int j = 0; j < i; j++) {
                    String computer2 = connection.get(j);
                    if(connections.get(computer1).contains(computer2)) {
                        String groupAsString = groupToString(List.of(computer0, computer1, computer2));

                        System.out.println(groupAsString);
                        threeTuplesWithT.add(groupAsString);
                    }
                }
            }
        }

        return threeTuplesWithT.size();
    }

    @Override
    public Object solvePart2(PuzzleInput input) {
        Map<String, Set<String>> connections = new HashMap<>();
        Set<String> allComputers = new HashSet<>();

        for(String line: input.getLines()) {
            String computer0 = line.split("-")[0];
            String computer1 = line.split("-")[1];

            if(!connections.containsKey(computer0)) connections.put(computer0, new HashSet<>());
            if(!connections.containsKey(computer1)) connections.put(computer1, new HashSet<>());

            connections.get(computer0).add(computer1);
            connections.get(computer1).add(computer0);

            allComputers.add(computer0);
            allComputers.add(computer1);
        }

        List<List<String>> meshedGroups = new ArrayList<>();
        boolean change = true;

        while(change) {
            change = false;

            for(String computer: allComputers) {
                boolean foundGroup = false;

                for(List<String> group: meshedGroups) {
                    if(group.contains(computer)) {
                        foundGroup = true;
                        continue;
                    }

                    if(group.stream().allMatch(e -> connections.get(e).contains(computer))) {
                        group.add(computer);
                        foundGroup = true;
                        change = true;
                    }
                }

                if(!foundGroup) {
                    List<String> newGroup = new ArrayList<>();
                    newGroup.add(computer);
                    meshedGroups.add(newGroup);

                    change = true;
                }
            }
        }

        int biggestGroupSize = 0;
        String biggestGroup = "";
        for(List<String> group: meshedGroups) {
            System.out.println(group.size() + " " + groupToString(group));
            if(group.size() <= biggestGroupSize) continue;
            biggestGroupSize = group.size();
            biggestGroup = groupToString(group);
        }

        return biggestGroup;
    }

    private String groupToString(List<String> group) {
       return group.stream()
            .sorted()
            .toList()
            .toString()
            .replace("[", "")
            .replace("]", "")
            .replace(" ", "");
    }

}
