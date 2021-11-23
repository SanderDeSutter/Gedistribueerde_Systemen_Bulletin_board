package Common;

import java.io.Serializable;

public class Value implements Serializable {
    private String message;
    private String nextTag;
    private int nextIdx;

    public Value(){
        this.message="dit werkt";
        this.nextIdx = 0;
        this.nextTag = "nextTag";
    }

    public Value(String message, String nextTag, int nextIdx) {
        this.message = message;
        this.nextTag = nextTag;
        this.nextIdx = nextIdx;
    }

    public String getMessage() {
        return message;
    }

    public String getNextTag() {
        return nextTag;
    }

    public int getNextIdx() {
        return nextIdx;
    }

    @Override
    public String toString() {
        return "Value{" +
                "message='" + message + '\'' +
                ", nextTag='" + nextTag + '\'' +
                ", nextIdx=" + nextIdx +
                '}';
    }
}
