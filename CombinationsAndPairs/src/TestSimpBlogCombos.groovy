def tester = new BlogTester('http://localhost:8080/postForm')

def combos = [["Title 1", "Title 2", "Title 3", "Title 4"],
              ["Bart", "Homer", "Marge", "Lisa", "Maggie"],
              ["Work", "School", "Home", "Travel", "Food"],
              ["Content A", "Content B", "Content C", "Content D"]].combinations()

println "Found ${combos.size()} combos"

combos.each { title, author, category, content ->
    tester.postAndCheck title, category, author, content
}
