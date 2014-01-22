package de.notepass.general.Cache;

public class DuplicateIndexException extends Exception {
    public DuplicateIndexException() {
        super("The requested index was already taken");
    }

    public DuplicateIndexException(String message) {
        super(message);
    }
}
