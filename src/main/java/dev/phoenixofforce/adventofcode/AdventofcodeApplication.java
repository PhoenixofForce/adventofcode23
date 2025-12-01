package dev.phoenixofforce.adventofcode;

import dev.phoenixofforce.adventofcode.meta.*;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@SpringBootApplication
public class AdventofcodeApplication {

	static void main(String[] args) {
		new SpringApplicationBuilder(AdventofcodeApplication.class)
				.headless(false)
				.run(args);
	}

	@Autowired
	private final ApplicationContext context;

	@Autowired
	private final List<Puzzle> puzzles;

	@Autowired
	private final InputFetcher fetcher;

	@Autowired
	private final CodeGenerator generator;

	@Value("${skipAutoRun:false}")
	private boolean skipAutoRun;

	@PostConstruct
	public void run() {
		if(skipAutoRun) {
			return;
		}

		LocalDate today = LocalDate.now();

		int day = 0;
		int year = 0;

		if(today.getMonthValue() == 12 && (day <= 0 || day >= 26 || year < 2015)) {
			day = today.getDayOfMonth();
			year = today.getYear();
		}

		if(day <= 0 || day >= 26 || year < 2015) return;
		solveSingleDay(day, year);

		((ConfigurableApplicationContext) context).close();
		System.exit(0);
	}

	private void solveSingleDay(int day, int year) {
		PuzzleInput input = grabInput(day, year, true);
		DaysSolution solution = solveRiddle(day, year, input, true);
		if(solution == null) return;

		String lastSolution;
		int part = 1;

        printHeader(day, year);
		printSolution(part, solution.part1Solution, solution.part1Duration);
		Clipboard.save(solution.part1Solution());
		lastSolution = solution.part1Solution;

		if(!(solution.part2Solution() == null || solution.part2Solution().isBlank())) {
			part = 2;
			printSolution(part, solution.part2Solution, solution.part2Duration);
			lastSolution = solution.part2Solution;
		}

		if(!(lastSolution != null && !lastSolution.isBlank() && !"0".equals(lastSolution))) {
			System.out.println();
			return;
		}

		log.info("Do you want to upload the solution {} automatically? (Y/N)", lastSolution);
		String uploadSolution = TimedInput.getChoiceWithTimeout(7);

		if(uploadSolution == null || uploadSolution.isEmpty() || uploadSolution.toLowerCase().charAt(0) != 'y') {
			return;
		}

		String response = fetcher.postAnswer(day, year, part, lastSolution);
		System.out.println();
		System.out.println(response.trim());
		System.out.println();
	}

	public void solveYear(int year) {
		List<DaysSolution> solutions = new ArrayList<>();
		for(int day = 1; day <= 25; day++) {
			PuzzleInput input = grabInput(day, year, false);
			DaysSolution solution = solveRiddle(day, year, input, false);
			solutions.add(solution);
		}

		for(int day = 0; day < 25; day++) {
			DaysSolution solution = solutions.get(day);
			if(solution == null) continue;

			System.out.println("Day " + dayAsString(day));
			System.out.println("\tPart 1(" + parseTime(solution.part1Duration) + "): |" + solution.part1Solution + "|");
			System.out.println("\tPart 2(" + parseTime(solution.part2Duration) + "): |" + solution.part2Solution + "|");
		}
	}

	private PuzzleInput grabInput(int day, int year, boolean logFiles) {
		boolean isNew = false;
		if(!FileUtils.doesFileExist(day, year)) {
			FileUtils.createInputFile(day, year);
			isNew = true;
		}

		if((isNew || FileUtils.getFile(day, year).get().length() == 0)) {
			String input = fetcher.getInputFromSite(day, year, logFiles);
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
			if(!generate) {
				return null;
			}

			log.error("No puzzle solver found in {}, generating template", packageSuffix);
			generator.generateDay(day, year);
			return null;
		}

		Puzzle today = optionalPuzzle.get();

		long start = System.nanoTime();
		String part1Solution = today.solvePart1(input).toString();
		double part1Duration = (System.nanoTime() - start) * 0.000001;
		printSolution(1, part1Solution, part1Duration);

		start = System.nanoTime();
		String part2Solution = today.solvePart2(input).toString();
		double part2Duration = (System.nanoTime() - start) * 0.000001;

		return new DaysSolution(part1Solution, part1Duration, part2Solution, part2Duration);
	}

    private void printHeader(int day, int year) {
        System.out.println("====================");
        System.out.println("=    " + dayAsString(day) + ".12." + year + "    =");
        System.out.println("====================");
    }


	private void printSolution(int part, String solution, double durationInMillis) {
		System.out.println("Part " + part + " (" + parseTime(durationInMillis) + ")");
		System.out.println("|" + solution + "|");
		System.out.println();
		Clipboard.save(solution);
	}

	private String parseTime(double timeInMillis) {
		double millis = timeInMillis;
		int seconds = (int) Math.floor(timeInMillis / 1000.0);
		int minutes = (int) Math.floor(seconds / 60.0);
		int hours = (int) Math.floor(minutes / 60.0);

		millis -= seconds * 1000;
		seconds -= minutes * 60;
		minutes -= hours * 60;

		String out = "";
		if(hours > 0) out += hours + "h   ";
		if(hours > 0 || minutes > 0) out += minutes + "min   ";
		if(hours > 0 || minutes > 0 || seconds > 0) out += seconds + "s   ";

		String millisAsString = millis + "";
		if(millisAsString.contains(".")) millisAsString = millisAsString.substring(0, Math.min(millisAsString.length(), millisAsString.indexOf(".") + 4));
		out += millisAsString + "ms";

		return out;
	}

    private String dayAsString(int day) {
        String dayString = day + "";
        if(dayString.length() == 1) dayString = "0" + dayString;
        return dayString;
    }

	private record DaysSolution(String part1Solution, double part1Duration,
						String part2Solution, double part2Duration) {}

}
