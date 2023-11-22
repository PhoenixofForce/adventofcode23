package dev.phoenixofforce.adventofcode;

import dev.phoenixofforce.adventofcode.common.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class AdventofcodeApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(AdventofcodeApplication.class)
				.headless(false)
				.run(args)
				.close();
	}

	@Autowired
	private final List<Puzzle> puzzles;

	@Autowired
	private final InputFetcher fetcher;

	@Autowired
	private final CodeGenerator generator;

	@PostConstruct
	public void run() {
		LocalDate today = LocalDate.now();

		if(today.getMonthValue() == 12) {
			solveSingleDay(today.getDayOfMonth(), today.getYear());
		} else {
			solveSingleDay(2, 2021);
		}
	}

	private void solveSingleDay(int day, int year) {
		PuzzleInput input = grabInput(day, year, true);
		DaysSolution solution = solveRiddle(day, year, input, true);
		if(solution == null) return;

		String lastSolution = "";
		int part = 1;

		System.out.println();
		System.out.println("Part 1 (" + solution.part1Duration() + "ms)");
		System.out.println("|" + solution.part1Solution() + "|");
		System.out.println();
		Clipboard.save(solution.part1Solution());
		lastSolution = solution.part1Solution;

		if(!(solution.part2Solution() == null || solution.part2Solution().isBlank())) {
			System.out.println("Part 2 (" + solution.part2Duration() + "ms)");
			System.out.println("|" + solution.part2Solution() + "|");
			System.out.println();
			Clipboard.save(solution.part2Solution());
			lastSolution = solution.part2Solution;
			part = 2;
		}

		log.info("Do you want to upload the solution {} automatically? (Y/N)", lastSolution);
		String uploadSolution = TimedInput.getChoiceWithTimeout(5);


		if(uploadSolution != null && !uploadSolution.isEmpty() && uploadSolution.toLowerCase().charAt(0) == 'y') {
			String response = fetcher.postAnswer(day, year, part, lastSolution);
			System.out.println();
			System.out.println(response.trim());
			System.out.println();
		}

		System.out.println();
	}

	private void solveYear(int year) {
		for(int day = 1; day <= 25; day++) {
			PuzzleInput input = grabInput(day, year, false);
			DaysSolution solution = solveRiddle(day, year, input, false);

			if(solution != null) {
				System.out.println("Day " + day);
				System.out.println("\tPart 1(" + solution.part1Duration + "ms): |" + solution.part1Solution + "|");
				System.out.println("\tPart 2(" + solution.part2Duration + "ms): |" + solution.part2Solution + "|");
			}
		}
	}

	private PuzzleInput grabInput(int day, int year, boolean generateFiles) {
		boolean isNew = false;
		if(!FileUtils.doesFileExist(day, year) && generateFiles) {
			FileUtils.createInputFile(day, year);
			isNew = true;
		}

		if((isNew || FileUtils.getFile(day, year).get().length() == 0) && generateFiles) {
			String input = fetcher.getInputFromSite(day, year);
			FileUtils.write2file(FileUtils.getFile(day, year).get(), input);
		}

		return new PuzzleInput(day, year);
	}

	private DaysSolution solveRiddle(int day, int year, PuzzleInput input, boolean generate) {
		String packageSuffix = generator.getPackageSuffixForDay(day, year);

		Optional<Puzzle> optionalPuzzle = puzzles.stream()
				.filter(p -> p.getClass().getPackageName().endsWith(packageSuffix))
				.findFirst();

		if(optionalPuzzle.isEmpty()) {
			if(generate) {
				log.error("No puzzle solver found in {}, generating template", packageSuffix);
				generator.generateDay(day, year);
			}

			return null;
		}

		Puzzle today = optionalPuzzle.get();

		long start = System.nanoTime();
		String part1Solution = today.solvePart1(input).toString();
		double part1Duration = (System.nanoTime() - start) / 1000.0;

		start = System.nanoTime();
		String part2Solution = today.solvePart2(input).toString();
		double part2Duration = (System.nanoTime() - start) / 1000.0;

		return new DaysSolution(part1Solution, part1Duration, part2Solution, part2Duration);
	}

	private record DaysSolution(String part1Solution, double part1Duration,
						String part2Solution, double part2Duration) {}

}
