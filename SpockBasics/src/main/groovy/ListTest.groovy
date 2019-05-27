import org.junit.jupiter.api.*

class ListTest {
    @Test
    void listConcatentationPreservesSize() {
        def weekdays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']
        def weekend = ['Sat', 'Sun']
        def week = weekdays + weekend
        assert weekdays.size() + weekend.size() == week.size()
    }
}
