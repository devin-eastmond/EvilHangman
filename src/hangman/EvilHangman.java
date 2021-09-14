package hangman;

import java.io.File;
import java.io.FileNotFoundException;

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
            int numGuessedCorrectly = 0;
            while (guesses > 0) {
                game.getGuess(guesses);
                if (numGuessedCorrectly != game.getNumGuessedCharacters()) {
                    numGuessedCorrectly = game.getNumGuessedCharacters();
                } else {
                    guesses--;
                }
                if (game.getHasWonGame()) {
                    System.out.println("You win!");
                    break;
                }
                if (guesses == 0) {
                    System.out.println("You loose!");
                } else {
                    System.out.println("");
                }
            }
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

}
