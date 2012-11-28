import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;


public class Envelope implements Serializable {

	private static final long serialVersionUID = 5549889559381800607L;

	byte[] signature = null;
	byte[] payload;
	
	public String toString() {
		return "Envelope\n\t" +
				"signature: " + Serialization.toHex(signature) + "\n\t" +
				"payload:   " + Serialization.toHex(payload);
	}

	Envelope(Serializable payload, Identity I) throws IOException, InvalidKeyException, NoSuchAlgorithmException, SignatureException {
		this.payload = Serialization.toByteArray(payload);
		I.sign(this);
	}
	
	Serializable get() throws IOException, ClassNotFoundException {
		return Serialization.fromByteArray(payload, Serializable.class);
	}
	
	Serializable getDeep() throws IOException, ClassNotFoundException {
		Serializable s = get();
		if (s instanceof Envelope)
			s = ((Envelope)s).getDeep();
		return s;
	}
	
}
