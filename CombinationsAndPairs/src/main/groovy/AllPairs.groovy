class AllPairs {
    static List<Candidate> candidates
    static Set<Map.Entry> covered

    static pairsFrom(Map entry) {
        def result = [:]
        def items = entry.entrySet().toList()
        while (items.size() >= 2) {
            def h = items.head()
            def t = items.tail()
            [h, t].combinations().each{ x, y ->
                result["$x.key|$y.key".toString()] = "$x.value|$y.value".toString()
            }
            items = t
        }
        result
    }

    static strength(Candidate candidate) {
        pairsFrom(candidate.value).entrySet().minus(covered).size()
    }

    static generate(Map<String, List<String>> configurations) {
        covered = [] as Set
        def pieces = []
        configurations.each{ key, valueList ->
            pieces << valueList.collect{ [(key):it] }
        }
        candidates = pieces.combinations().collect{
            it -> new Candidate(value: it.collectEntries{ [*:it] })
        }
        def active = { int index -> !candidates[index].pruned }
        def prune = { int index -> candidates[index].pruned = true }
        def useless = { int index -> strength(candidates[index]) == 0 }
        while(true) {
            (0..<candidates.size()).findAll(active).findAll(useless).each(prune)
            def remaining = (0..<candidates.size()).findAll(active)
            if (remaining.size() == 0) break
            int next = remaining.sort{ -strength(candidates[it]) }[0]
            candidates[next].selected = true
            candidates[next].pruned = true
            covered += pairsFrom(candidates[next].value).entrySet()
        }
        candidates.findAll{ it.selected }.value
    }

    static class Candidate {
        boolean selected
        boolean pruned
        Map value
    }
}
