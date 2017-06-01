import org.chocosolver.solver.Model

def m = new Model()

daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday",
              "Thursday", "Friday", "Saturday"]
def (SUN, MON, TUE, WED, THU, FRI, SAT) = 0..6

def bart = m.intVar('Bart', 0, 6)
def homer = m.intVar('Homer', 0, 6)
def marge = m.intVar('Marge', 0, 6)
def lisa = m.intVar('Lisa', 0, 6)
def maggie = m.intVar('Maggie', 0, 6)
def authors = [bart, homer, marge, lisa, maggie]

// They never blog on the same day
m.allDifferent(*authors).post()

// Marge blogs only on a Saturday or Sunday
m.or(m.arithm(marge, "=", SAT), m.arithm(marge, "=", SUN)).post()

// Maggie blogs only on a Tuesday or Thursday
m.or(m.arithm(maggie, "=", TUE), m.arithm(maggie, "=", THU)).post()

// Lisa blogs only on a Monday, Wednesday or Friday
m.or(m.arithm(lisa, "=", MON), m.arithm(lisa, "=", WED), m.arithm(lisa, "=", FRI)).post()

// Bart blogs only on the day after Lisa
m.arithm(bart, "-", lisa, "=", 1).post()

// Homer only blogs if noone else blogged the previous
// day and doesn't allow anyone to blog the next day
m.and(m.distance(homer, marge, "!=", 1),
        m.distance(homer, bart, "!=", 1),
        m.distance(homer, maggie, "!=", 1),
        m.distance(homer, lisa, "!=", 1)).post()

def solutions = []
while (m.solver.solve()) {
    solutions << pad('') + authors.collect {
        pad(daysOfWeek[it.value])
    }.join()
}
if (solutions) {
    println pad("Solutions:") + authors.collect {
        pad(it.name)
    }.join()
    println solutions.join('\n')
} else {
    println "No Solutions"
}

def pad(s) { s.padRight(12) }
