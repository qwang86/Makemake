package api.core;

import javax.servlet.ServletException;

public class MissingRequestPartException extends RuntimeException{
    private final String partName;

    public MissingRequestPartException(String partName) {
        super("Required request part '" + partName + "' is not presented!");
        this.partName = partName;
    }
    public String getRequestPartName() {
        return this.partName;
    }
}
