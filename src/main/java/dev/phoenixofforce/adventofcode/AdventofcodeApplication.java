package dev.phoenixofforce.adventofcode;

import dev.phoenixofforce.adventofcode.common.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

import java.time.LocalDate;
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
			solve(today.getDayOfMonth(), today.getYear());
		} else {
			solve(4,2021);
		}
	}

	private void solve(int day, int year) {
		PuzzleInput input = grabInput(day, year);
		solveRiddle(day, year, input);
	}

	private PuzzleInput grabInput(int day, int year) {
		boolean isNew = false;
		if(!FileUtils.doesFileExist(day, year)) {
			FileUtils.createInputFile(day, year);
			isNew = true;
		}

		if(isNew || FileUtils.getFile(day, year).get().length() == 0) {
			String input = fetcher.getInputFromSite(day, year);
			FileUtils.write2file(FileUtils.getFile(day, year).get(), input);
		}

		return new PuzzleInput(day, year);
	}

	private void solveRiddle(int day, int year, PuzzleInput input) {
		String packageSuffix = generator.getPackageSuffixForDay(day, year);

		Optional<Puzzle> optionalPuzzle = puzzles.stream()
				.filter(p -> p.getClass().getPackageName().endsWith(packageSuffix))
				.findFirst();

		if(optionalPuzzle.isEmpty()) {
			log.error("No puzzle solver found in {}, generating template", packageSuffix);
			generator.generateDay(day, year);
			return;
		}

		Puzzle today = optionalPuzzle.get();

		long start = System.currentTimeMillis();
		String part1Solution = today.solvePart1(input).toString();
		long part1Duration = System.currentTimeMillis() - start;

		start = System.currentTimeMillis();
		String part2Solution = today.solvePart2(input).toString();
		long part2Duration = System.currentTimeMillis() - start;

		System.out.println();
		System.out.println("Part 1 (" + part1Duration + "ms)");
		System.out.println("|" + part1Solution + "|");
		Clipboard.save(part1Solution);

		if(!(part2Solution == null || part2Solution.isBlank())) {
			System.out.println();
			System.out.println("Part 2 (" + part2Duration + "ms)");
			System.out.println("|" + part2Solution + "|");
			Clipboard.save(part2Solution);
		}

		System.out.println();
	}

}
