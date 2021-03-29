import org.transparent.lucent.example.Example;

@Example
public final class Test {
    public static void main(String[] args) {
        final Test test = new Test();
        System.out.println(test.generated); // If it works, this will print the generated string from ExampleTranslator
    }
}
