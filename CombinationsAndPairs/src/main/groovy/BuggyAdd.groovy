def myAdder(String one, String two) {
    if (!one.isInteger()) one = '0'
    else if (!two.isInteger()) two = '0'
    one.toInteger() + two.toInteger()
}

assert myAdder('40', '2') == 42
assert myAdder('40', '') == 40
assert myAdder('', '2') == 2
//assert myAdder('', '') == 0

def valid = '40'
def negative = '-40'
def empty = ''
//def letters = 'XL'
def cases = [valid, negative, empty]
//def cases = [valid, negative, empty, letters]
def combos = [cases, cases].combinations()
println "Combinations: ${combos.size()}"
combos.each { one, two ->
    println "'$one' '$two'"
    //assert myAdder(one, two) instanceof Integer
}
