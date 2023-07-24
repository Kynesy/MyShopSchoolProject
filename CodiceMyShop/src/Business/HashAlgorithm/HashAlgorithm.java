package Business.HashAlgorithm;

public class HashAlgorithm {
    private int hashComplexity;

    public static String getDigest(String password){
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkHash(String password, String hash){
        return BCrypt.checkpw(password, hash);
    }
}
