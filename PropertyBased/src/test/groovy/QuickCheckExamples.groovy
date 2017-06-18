// quickcheck example illustrating combining generators and numeric distributions
// randomly returns 3-letter pets, 3-letter months, or 3-digit numbers
//import static net.java.quickcheck.generator.PrimitiveGeneratorSamples.*
import static net.java.quickcheck.generator.CombinedGenerators.excludeValues
import static net.java.quickcheck.generator.CombinedGeneratorsIterables.*
import static net.java.quickcheck.generator.PrimitiveGenerators.*
import static net.java.quickcheck.generator.distribution.Distribution.*
import net.java.quickcheck.generator.support.DefaultFrequencyGenerator
import net.java.quickcheck.Generator

//10.times {
//  def num = excludeValues(integers(10,20), [15, 16]).next()
//  assert num in 10..14 || num in 17..20
//}

for (words in someNonEmptyLists(strings())) {
    assert words*.size().sum() == words.sum().size()
}

class MonthGenerator implements Generator<String> {
    Generator<Date> genDate = dates()
    String next() { genDate.next().format("MMM") }
}

def pets = fixedValues(['Ant', 'Bee', 'Cat', 'Dog'])
def nums = excludeValues(integers(100, 999, INVERTED_NORMAL), 500..599)
def months = new Generator<String>() {
    Generator<Date> genDate = dates()
    String next() { genDate.next().format("MMM") }
}
def gen = new DefaultFrequencyGenerator(pets, 50)
gen.add(nums, 30)
gen.add(months, 20)

20.times {
    def next = gen.next().toString()
    println next
    assert next.size() == 3
}
