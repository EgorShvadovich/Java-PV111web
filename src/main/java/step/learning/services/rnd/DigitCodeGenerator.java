package step.learning.services.rnd;

import com.google.inject.Inject;

import java.util.Random;

public class DigitCodeGenerator implements CodeGenerator {
    private final Random random ;

    @Inject
    public DigitCodeGenerator(Random random) {
        this.random = random;
    }

    @Override
    public String newCode(int length) {
        char[] chars = new char[length];
        for (int i = 0; i < length; i++) {
            chars[i] = (char)((int)'0' + random.nextInt(10));
        }
        return new String(chars);
    }
}
