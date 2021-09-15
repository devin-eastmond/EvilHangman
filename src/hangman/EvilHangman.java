package hangman;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class EvilHangman {

    public static void main(String[] args) {
        String dictionaryName = "";
        int wordLength = 0;
        int guesses = 0;
        try {
            if (args.length != 3) {
                throw new Exception();
            }
            dictionaryName = args[0];
            wordLength = Integer.parseInt(args[1]);
            guesses = Integer.parseInt(args[2]);
            if (wordLength < 2 || guesses < 1) {
                throw new Exception();
            }
            File dictionary = new File(dictionaryName);
            EvilHangmanGame game = new EvilHangmanGame();
            game.startGame(dictionary, wordLength);
            getGuesses(guesses, game);
        } catch (FileNotFoundException e) {
            System.out.println(dictionaryName + ": no such file or directory");
        } catch (EmptyDictionaryException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("usage: java hangman.EvilHangman [dictionary] [wordLength] [guesses]");
            System.out.println("  dictionary: the name of the dictionary file to use");
            System.out.println("  wordLength: the length of the word to guess (2 or more)");
            System.out.println("  guesses: the number of guesses available (1 or more)");
        }
    }

    private static void getGuesses(int guesses, EvilHangmanGame game) {
        int guessesRemaining = guesses;
        int numGuessedCorrectly = 0;
        Scanner scanner = new Scanner(System.in);
        while (guessesRemaining > 0) {
            printGuessInfo(guessesRemaining, game);
            getGuess(scanner, game);
            if (numGuessedCorrectly != game.getNumGuessedCharacters()) {
                numGuessedCorrectly = game.getNumGuessedCharacters();
            } else {
                guessesRemaining--;
            }
            if (game.getHasWonGame()) {
                System.out.println("You win! You guessed the word: " + game.getEndingWord());
                break;
            }
            if (guessesRemaining == 0) {
                System.out.println("You lose!");
                System.out.println("The word was: " + game.getEndingWord());
            } else {
                System.out.println("");
            }
        }
    }

    private static void getGuess(Scanner scanner, EvilHangmanGame game) {
        boolean validInput = false;
        while (!validInput) {
            System.out.print("Enter guess: ");
            String input = scanner.next();
            try {
                if (input.length() != 1) {
                    throw new Exception("Invalid input!");
                } else {
                    char guess = input.toLowerCase().charAt(0);
                    if (!Character.isLetter(guess)) {
                        throw new Exception("Invalid input!");
                    }
                    game.makeGuess(guess);
                    validInput = true;
                }
            } catch (Exception e) {
                System.out.print(e.getMessage() + " ");
            }
        }
    }

    private static void printGuessInfo(int guessesRemaining, EvilHangmanGame game) {
        if (guessesRemaining > 1) {
            System.out.println("You have " + guessesRemaining + " guesses left");
        } else {
            System.out.println("You have " + guessesRemaining + " guess left");
        }

        System.out.print("Used letters: ");
        for (Character usedLetter : game.getGuessedLetters()) {
            System.out.print(usedLetter.toString() + ' ');
        }
        System.out.print('\n');

        System.out.println("Word: " + game.getWord());
    }

}
