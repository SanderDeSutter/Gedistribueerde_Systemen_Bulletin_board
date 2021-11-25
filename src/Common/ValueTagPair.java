package Common;

import java.io.Serializable;

public class ValueTagPair implements Serializable {
    private Value value;
    private String tag;


    public ValueTagPair(Value value, String tag) {
        this.value = value;
        this.tag = tag;
    }
    public Value getValue() {
        return value;
    }

    public String getTag() {
        return tag;
    }

    @Override
    public String toString() {
        return "ValueTagPair{" +
                "value=" + value +
                ", tag='" + tag + '\'' +
                '}';
    }
}
