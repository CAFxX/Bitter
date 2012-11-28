import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SignatureException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;


public class bitter {

	final Identity local;
	
	bitter() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		local = Identity.get();
	}
	
	public static void main(String[] args) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException, SignatureException, NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
		bitter b = new bitter();
		System.out.println(Serialization.toHex(b.local.getPublicKey().getEncoded()));
		Envelope e = new Envelope("Hello world", b.local);
		for (int i=0; i<10; i++)
			e = new Envelope(e, b.local);
		System.out.println(e);
		System.out.println(e.getDeep());
	}

}
