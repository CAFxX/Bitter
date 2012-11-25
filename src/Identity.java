import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Security;
import java.security.SignatureException;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class Identity implements Serializable {
	
	static {
		Security.addProvider(new BouncyCastleProvider());
	}

	private static final long serialVersionUID = -7996937974711664163L;
	private final KeyPair kp;
	
	private Identity() throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
        KeyPairGenerator kg = KeyPairGenerator.getInstance("EC");
        kg.initialize(256, new SecureRandom());
        kp = kg.generateKeyPair();
	}
	
	public void sign(Envelope e) throws IOException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
		if (e.signature != null)
			throw new IllegalArgumentException("Envelope is already signed");
		java.security.Signature dsa = java.security.Signature.getInstance("SHA256withECDSA");
        dsa.initSign(kp.getPrivate());
        dsa.update(e.payload);
        e.signature = dsa.sign();
	}
	
	public boolean verify(Envelope e, PublicKey pub) throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, IOException {
		java.security.Signature dsa = java.security.Signature.getInstance("SHA256withECDSA");
		dsa.initVerify(pub);
		dsa.update(e.payload);
		return dsa.verify(e.signature);
	}
	
	public boolean verify(Envelope e) throws InvalidKeyException, NoSuchAlgorithmException, SignatureException, IOException {
		return verify(e, kp.getPublic());
	}
	
	public PublicKey getPublicKey() {
		return kp.getPublic();
	}
	
	private static Identity create(File f) throws IOException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		ObjectOutputStream os = null;
		try {
			os = new ObjectOutputStream(new FileOutputStream(f));
			Identity I = new Identity();
			os.writeObject(I);
			return I;
		} finally {
			if (os != null) os.close();
		}
	}
	
	static Identity get(String IdentityFile) throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		File f = new File(IdentityFile);
		if (!f.exists())
			return create(f);
		if (!f.canRead())
			throw new IOException("Can not read identity file");
		ObjectInputStream is = null;
		try {
			is = new ObjectInputStream(new FileInputStream(f));
			return (Identity) is.readObject();
		} finally {
			if (is != null) is.close();
		}
	}
	
	static Identity get() throws IOException, ClassNotFoundException, NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException {
		return get("com.strayorange.bitter.identity");
	}
	
}
