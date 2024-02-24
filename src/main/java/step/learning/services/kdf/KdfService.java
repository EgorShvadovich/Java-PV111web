package step.learning.services.kdf;

public interface KdfService {
    String dk(String password,String salt);
}
