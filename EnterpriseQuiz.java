import java.util.*;
import java.util.concurrent.*;

class Question {
    private final String questionText;
    private final String[] options;
    private final int correctChoice;

    public Question(String questionText, String[] options, int correctChoice) {
        this.questionText = questionText;
        this.options = options;
        this.correctChoice = correctChoice;
    }

    public String getQuestionText() { return questionText; }
    public String[] getOptions() { return options; }
    public int getCorrectChoice() { return correctChoice; }
}

public class EnterpriseQuiz {
    private static final int TIME_LIMIT_SECONDS = 15;

    public static void main(String[] args) {
        List<Question> bank = new ArrayList<>();
        bank.add(new Question("Which of the following is NOT a fundamental OOP concept in Java?", 
                new String[]{"1. Encapsulation", "2. Compilation", "3. Inheritance", "4. Polymorphism"}, 2));
        bank.add(new Question("What is the memory size of a 'long' data type variable in Java?", 
                new String[]{"1. 4 bytes", "2. 2 bytes", "3. 8 bytes", "4. 16 bytes"}, 3));
        bank.add(new Question("Which component is directly responsible for executing the Java bytecode?", 
                new String[]{"1. JDK", "2. JVM", "3. JRE", "4. Compiler"}, 2));

        System.out.println("=========================================");
        System.out.println("    ENTERPRISE INTERACTIVE TIMER QUIZ    ");
        System.out.println("=========================================");
        System.out.println("Rules: You get " + TIME_LIMIT_SECONDS + " seconds per question.\n");

        int finalScore = 0;
        Scanner scanner = new Scanner(System.in);
        ExecutorService executor = Executors.newSingleThreadExecutor();

        for (int i = 0; i < bank.size(); i++) {
            Question q = bank.get(i);
            System.out.printf("--- QUESTION %d of %d ---\n", (i + 1), bank.size());
            System.out.println(q.getQuestionText());
            for (String opt : q.getOptions()) {
                System.out.println(opt);
            }
            System.out.print("Your response (Enter option number 1-4): ");

            Callable<Integer> userInputTask = () -> {
                while (!scanner.hasNextInt()) {
                    System.out.print("\u274C Type a number (1-4): ");
                    scanner.next();
                }
                return scanner.nextInt();
            };

            Future<Integer> future = executor.submit(userInputTask);
            int selectedOption = -1;

            try {
                selectedOption = future.get(TIME_LIMIT_SECONDS, TimeUnit.SECONDS);
            } catch (TimeoutException e) {
                System.out.println("\n\n⏳ Time Limit Exceeded! Question Locked.");
                future.cancel(true);
            } catch (Exception e) {
                System.out.println("\nAn error occurred while processing input.");
            }

            if (selectedOption != -1) {
                if (selectedOption == q.getCorrectChoice()) {
                    System.out.println("\u2714 Correct Answer! Well done.");
                    finalScore++;
                } else {
                    System.out.println("\u274C Incorrect Choice! The correct answer was option " + q.getCorrectChoice());
                }
            }
            System.out.println();
        }

        executor.shutdownNow();
        System.out.println("=========================================");
        System.out.println("               QUIZ RESULTS              ");
        System.out.println("=========================================");
        System.out.printf("Final Score Evaluated: %d out of %d\n", finalScore, bank.size());
        double performance = ((double) finalScore / bank.size()) * 100;
        System.out.printf("Performance Rating: %.2f%%\n", performance);
        System.out.println("=========================================");
        scanner.close();
    }
}