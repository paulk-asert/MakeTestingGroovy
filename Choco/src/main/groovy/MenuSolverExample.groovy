import org.chocosolver.solver.Model
import org.chocosolver.solver.variables.IntVar

def m = new Model()

def menu = [
    'Mixed fruit'       : 215,
    'French fries'      : 275,
    'Side salad'        : 335,
    'Hot wings'         : 355,
    'Mozzarella sticks' : 420,
    'Sampler plate'     : 580
]
def numOrdered = new IntVar[menu.size()]
def priceEach = new int[menu.size()]
def sum = 1505
menu.eachWithIndex { name, price, i ->
    // number ordered >= 0
    // number ordered * price <= sum
    numOrdered[i] = m.intVar(name, 0, sum.intdiv(price))
    priceEach[i] = price
}
m.scalar(numOrdered, priceEach, "=", sum).post()

def found = false
while (m.solver.solve()) {
    found = true
    println "Found a solution:"
    numOrdered.each {
        if (it.value) println "  $it.value * $it.name"
    }
}
if (!found) {
    println "No Solutions"
}
