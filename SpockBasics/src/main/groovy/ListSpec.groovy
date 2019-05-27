import spock.lang.*

class ListSpec extends Specification {

    def "No elements lost or added upon concatenation"() {
        given:
        def weekdays = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri']
        def weekend = ['Sat', 'Sun']

        when:
        def week = weekdays + weekend

        then:
        weekdays.size() + weekend.size() == week.size()
    }
}
