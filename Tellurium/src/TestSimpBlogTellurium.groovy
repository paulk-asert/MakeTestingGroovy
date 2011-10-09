ui.UrlLink(uid: "create_new_post", clocator: [tag:'a', text: "New Blog Entry"])

ui.Form(uid: "blogform", clocator: [tag: 'form', name:'post'], group: "true") {
    InputBox(uid: "title", clocator: [name: "title"])
    InputBox(uid: "content", clocator: [tag:'textarea', name: "content"])
    Selector(uid: "category", clocator: [name: "category"])
    Selector(uid: "author", clocator: [name: "author"])
    SubmitButton(uid: "post_button", clocator: [name: 'btnPost', value: "Create Post"])
}

ui.TextBox(uid: 'main_header', clocator: [tag: 'h1'])
ui.TextBox(uid: 'category_header', clocator: [tag: 'h3', position: '2'])
ui.TextBox(uid: 'author_header', clocator: [tag: 'h3', position: '3'])
ui.Container(uid: 'table', clocator: [tag: 'table']) {
    ui.TextBox(uid: 'content_para', locator: '//tr/td/p')
}

openUrl "http://localhost:8080/"
click "create_new_post"
waitForPageToLoad 5000
assert title == 'Welcome to SimpBlog'

// post blog
type "blogform.title", "Bart was here (Tellurium)"
selectByLabel "blogform.category", "School"
selectByLabel "blogform.author", "Bart"
type "blogform.content", "Cowabunga Dude!"
click "blogform.post_button"
waitForPageToLoad 5000

// check contents
assert getText('main_header') ==~ 'Post.*: Bart was here.*'
assert getText('category_header') == 'Category: School'
assert getText('author_header') == 'Author: Bart'
assert getText('table.content_para') == 'Cowabunga Dude!'
shutDown
