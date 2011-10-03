def tester = new BlogTester('http://localhost:8080/postForm')

def pairs = AllPairs.generate(
        title: ["Title 1", "Title 2", "Title 3", "Title 4"],
        author: ["Bart", "Homer", "Marge", "Lisa", "Maggie"],
        category: ["Work", "School", "Home", "Travel", "Food"],
        content: ["Content A", "Content B", "Content C", "Content D"])

println "Found ${pairs.size()} pairs"

pairs.each {
    println it
    tester.postAndCheck it.title, it.category, it.author, it.content
}
