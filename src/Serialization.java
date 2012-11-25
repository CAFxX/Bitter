import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.math.BigInteger;


public class Serialization {

	static byte[] toByteArray(Serializable o) throws IOException {
		ByteArrayOutputStream buf = new ByteArrayOutputStream();
		ObjectOutputStream os = new ObjectOutputStream(buf);
		os.writeObject(o);
		return buf.toByteArray();
	}
	
	static Serializable fromByteArray(byte[] buf, Class<?> cl) throws IOException, ClassNotFoundException {
		ObjectInputStream os = new ObjectInputStream(new ByteArrayInputStream(buf));
		return (Serializable) cl.cast(os.readObject());
	}
	
	public static String toHex(byte[] bytes) {
	    BigInteger bi = new BigInteger(1, bytes);
	    return String.format("%0" + (bytes.length << 1) + "X", bi);
	}
	
}
