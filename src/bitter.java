import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;


public class bitter {

	final Identity local;
	
	bitter() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		local = Identity.get();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException {
		bitter b = new bitter();
		System.out.println(Serialization.toHex(Serialization.toByteArray(b.local.getPublicKey())));
		Envelope e = new Envelope("ABCD");
		b.local.sign(e);
		System.out.println(e);
		b.local.verify(e);
	}

}
