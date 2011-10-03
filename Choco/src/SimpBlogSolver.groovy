// requires choco 2.1.0-basic.jar from http://choco.emn.fr/
import static choco.Choco.*

def m = new choco.cp.model.CPModel()
def s = new choco.cp.solver.CPSolver()

daysOfWeek = ["Sunday", "Monday", "Tuesday", "Wednesday",
              "Thursday", "Friday", "Saturday"]

def bart = makeIntVar('Bart', 0, 6)
def homer = makeIntVar('Homer', 0, 6)
def marge = makeIntVar('Marge', 0, 6)
def lisa = makeIntVar('Lisa', 0, 6)
def maggie = makeIntVar('Maggie', 0, 6)
def simpsons = [bart, homer, marge, lisa, maggie]

// They never blog on the same day
for (i in 0..<simpsons.size())
    for (j in 0..<i)
        m.addConstraint(neq(simpsons[i], simpsons[j]))

// Marge blogs only on a Saturday or Sunday
m.addConstraint(or(eq(marge, 0), eq(marge, 6)))

// Maggie blogs only on a Tuesday or Thursday
m.addConstraint(or(eq(maggie, 2), eq(maggie, 4)))

// Lisa blogs only on a Monday, Wednesday or Friday
m.addConstraint(or(eq(lisa, 1), eq(lisa, 3), eq(lisa, 5)))

// Bart blogs only on the day after Lisa
m.addConstraint(eq(plus(lisa, 1), bart))

// Homer only blogs if noone else blogged the previous
// day and doesn't allow anyone to blog the next day
m.addConstraint(and(distanceNEQ(homer, marge, 1),
                    distanceNEQ(homer, bart, 1),
                    distanceNEQ(homer, maggie, 1),
                    distanceNEQ(homer, lisa, 1)))

s.read(m)
def more = s.solve()
if (!more) println "No Solutions Found"
else println pad("Solutions:") + simpsons.collect{ pad(it.name) }.join()
while (more) {
    print pad("")
    println simpsons.collect {
        def v = s.getVar(it)
        pad(daysOfWeek[v.val])
    }.join()
    more = s.nextSolution()
}

def pad(s) { s.padRight(12) }
