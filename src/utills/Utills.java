package utills;

import java.io.*;

/**
 * Created by Oceanos on 09.11.2016.
 */
public class Utills {

    public static Object cloneObj(Object object) throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream ous = new ObjectOutputStream( baos);
        ous.writeObject(object);
        ous.close();

        ByteArrayInputStream bais = new ByteArrayInputStream(baos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bais);
        Object cloned = ois.readObject();
        return cloned;
    }
}
