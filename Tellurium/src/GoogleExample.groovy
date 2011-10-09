selenium.type("//input[@title='Google Search']", input)
selenium.click("//input[@name='btnG' and @type='submit']")

ui.Container(uid: "google_start_page",
        clocator: [tag: "td"], group: "true") {
    InputBox(uid: "searchbox",
        clocator: [title: "Google Search"])
    SubmitButton(uid: "googlesearch",
        clocator: [name: "btnG", value: "Google Search"])
}

type "google_start_page.searchbox", input
click "google_start_page.googlesearch"