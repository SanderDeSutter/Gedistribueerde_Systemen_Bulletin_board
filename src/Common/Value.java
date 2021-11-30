package Common;

import java.io.*;

public class Value implements Serializable {
    private String message;
    private String nextTag;
    private int nextIdx;


    public Value() {
        this.message = "dit werkt";
        this.nextIdx = 0;
        this.nextTag = "nextTag";
    }

    public Value(String message, String nextTag, int nextIdx) {
        this.message = message;
        this.nextTag = nextTag;
        this.nextIdx = nextIdx;
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

    public static Value fromByteArray(byte[] valueByteArray) {
        ObjectInput in = null;
        Value out = null;
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(valueByteArray);
            in = new ObjectInputStream(bis);
            out = (Value) in.readObject();

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
