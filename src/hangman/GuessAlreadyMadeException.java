package hangman;

public class GuessAlreadyMadeException extends Exception {
    @Override
    public String getMessage() {
        return "Guess already made!";
    }
}
