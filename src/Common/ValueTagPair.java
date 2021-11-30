package Common;

import java.io.*;

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

    public byte[] toByteArray() {
        byte[] valueByteArray = null;
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            ObjectOutputStream out = null;
            out = new ObjectOutputStream(bos);
            out.writeObject(this);
            out.flush();
            valueByteArray = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return valueByteArray;
        // ignore close exception
    }

    public ValueTagPair fromByteArray(byte[] valueByteArray) {
        ObjectInput in = null;
        ValueTagPair out = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(valueByteArray);
            in = new ObjectInputStream(bis);
            out = (ValueTagPair) in.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException ex) {
                // ignore close exception
            }
        }
        return out;
    }

    @Override
    public String toString() {
        return "ValueTagPair{" +
                "value=" + value +
                ", tag='" + tag + '\'' +
                '}';
    }
}
