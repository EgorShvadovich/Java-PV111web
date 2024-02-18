package step.learning.services.hash;

import com.google.inject.Singleton;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Singleton
public class Sha1HashService implements HashService {
    @Override
    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA") ;
            StringBuilder stringBuilder = new StringBuilder() ;
            for( byte b :
                    digest.digest( input.getBytes(StandardCharsets.UTF_8) ) ) {
                stringBuilder.append( String.format("%02x", b) ) ;
            }
            return stringBuilder.toString() ;
        }
        catch (NoSuchAlgorithmException e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
