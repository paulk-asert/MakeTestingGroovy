import net.thucydides.core.annotations.Step

class HelloSteps {
    int first, second

    @Step("Given I start with {0}")
    void start_with(int first) {
        this.first = first
    }

    @Step("When I add {0}")
    void add(int second) {
        this.second = second
    }

    @Step("Then the result should be {0}")
    void result_should_be(int result) {
        assert first + second == result
    }
}