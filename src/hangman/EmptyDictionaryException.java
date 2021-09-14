package hangman;

public class EmptyDictionaryException extends Exception {
	//Thrown when dictionary file is empty or no words in dictionary match the length asked for
    private final String fileName;
    private final int wordLength;

    public EmptyDictionaryException (String fileName, int wordLength) {
        this.fileName = fileName;
        this.wordLength = wordLength;
    }

    @Override
    public String getMessage() {
        return "Error: the file " + fileName + "does not contain strings of length " + wordLength + "!";
    }
}
