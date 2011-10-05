import org.watij.webspec.dsl.WebSpec

//new WebSpec().mozilla().with {
new WebSpec().ie().with {
    open 'http://localhost:8080'
    find.title('Welcome to SimpBlog').shouldExist()

    open 'http://localhost:8080/postForm'
    find.input().with.name("title").set.value('Bart was here (Watij)')
    find.option().with("Bart").set("selected=true")
    find.option().with("School").set("selected=true")
    find.textArea().with.name("content").set.value('Cowabunga dude!')
    find.input().with.type('submit').with.value("Create Post").flash()
    find.input().with.name("btnPost").click()

    find.h1().get.innerHTML().matches(/Post \d+: Bart was here.*/)
    assert find.h3(1).get.innerHTML() == 'Category: School'
    assert find.h3(2).get.innerHTML() == 'Author: Bart'
    def cell = find.table().child('tbody').child('tr').child('td')
    assert cell.child('p').get.innerHTML() == 'Cowabunga dude!'
    close()
}
