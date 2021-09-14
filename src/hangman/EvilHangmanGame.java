package hangman;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class EvilHangmanGame implements IEvilHangmanGame {
    private final HashSet<String> dictionary;
    private final SortedSet<Character> usedLetters;
    private Set<String> wordSet;
    private String word;

    public EvilHangmanGame() {
        dictionary = new HashSet<>();
        wordSet = dictionary;
        usedLetters = new TreeSet<>();
    }

    @Override
    public void startGame(File dictionary, int wordLength) throws IOException, EmptyDictionaryException {
        Scanner scanner = new Scanner(dictionary);
        while (scanner.hasNext()) {
            String word = scanner.next();
            if (word.length() == wordLength) {
                this.dictionary.add(word);
            }
        }
        word = "";
        for (int i = 0; i < wordLength; i++) {
            word = word.concat("-");
        }
        if (this.dictionary.size() == 0) {
            throw new EmptyDictionaryException(dictionary.getName(), wordLength);
        }
    }

    public void getGuess(int guessesRemaining) {
        printGuessInfo(guessesRemaining);

        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter guess: ");
            Scanner scanner = new Scanner(System.in);
            String input = scanner.next();
            try {
                if (input.length() != 1) {
                    throw new Exception("Invalid input!");
                } else {
                    char guess = input.toLowerCase().charAt(0);
                    if (!Character.isLetter(guess)) {
                        throw new Exception("Invalid input!");
                    }
                    makeGuess(guess);
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.print(e.getMessage() + " ");
            }
        }
    }

    private void printGuessInfo(int guessesRemaining) {
        if (guessesRemaining > 1) {
            System.out.println("You have " + guessesRemaining + " guesses left");
        } else {
            System.out.println("You have " + guessesRemaining + " guess left");
        }

        System.out.print("Used letters: ");
        for (Character usedLetter : usedLetters) {
            System.out.print(usedLetter.toString() + ' ');
        }
        System.out.print('\n');

        System.out.println("Word: " + word);
    }

    @Override
    public Set<String> makeGuess(char guess) throws GuessAlreadyMadeException {
        if (usedLetters.contains(guess)) {
            throw new GuessAlreadyMadeException();
        }
        usedLetters.add(guess);

        HashMap<String, Set<String>> map = getMap(guess);
        int biggestSetLength = 0;
        String biggestSetPattern = "";
        Set<String> biggestSet = new HashSet<>();
        for (String pattern : map.keySet()) {
            Set<String> set = map.get(pattern);
            if (set.size() > biggestSetLength) {
                biggestSetLength = set.size();
                biggestSetPattern = pattern;
                biggestSet = set;
                // TODO: resolve when more than one set with biggest size
            }
        }
        word = biggestSetPattern;
        wordSet = biggestSet;
        int numCorrect = 0;
        for (int i = 0; i < biggestSetPattern.length(); i++) {
            if (biggestSetPattern.charAt(i) == guess) {
                numCorrect++;
            }
        }
        if (numCorrect == 0) {
            System.out.println("Sorry, there are no " + guess + "'s");
        } else if (numCorrect == 1) {
            System.out.println("Yes, there is 1 " + guess);
        } else {
            System.out.println("Yes, there are " + numCorrect + " " + guess + "'s");
        }

        return biggestSet;
    }

    private HashMap<String, Set<String>> getMap(char guess) {
        HashMap<String, Set<String>> map = new HashMap<>();
        for (String word : wordSet) {
            String pattern = "";
            for (int i = 0; i < word.length(); i++) {
                if (word.charAt(i) == guess) {
                    pattern = pattern.concat(Character.toString(guess));
                } else {
                    pattern = pattern.concat(Character.toString(this.word.charAt(i)));
                }
            }
            if (map.containsKey(pattern)) {
                map.get(pattern).add(word);
            } else {
                Set<String> set = new HashSet<>();
                set.add(word);
                map.put(pattern, set);
            }
        }

        return map;
    }

    @Override
    public SortedSet<Character> getGuessedLetters() {
        return usedLetters;
    }

    public boolean getHasWonGame() {
        return (!word.contains("-"));
    }

    public int getNumGuessedCharacters() {
        return (word.replace("-", "").length());
    }
}
