/*
@Grab('org.junit.jupiter:junit-jupiter-api:5.5.0-M1')
import org.junit.jupiter.api.*
import static Converter.celsius

class ConverterTest {
    @Test
    void freezing() {
        assert celsius(32) == 0
    }

    @Test
    void boiling() {
        assert celsius(212) == 100
    }
}

class Converter {
    static celsius (fahrenheit) {
        (fahrenheit - 32) * 5 / 9
    }
}
/* */
