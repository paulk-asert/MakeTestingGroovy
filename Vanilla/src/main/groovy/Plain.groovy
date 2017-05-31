def html = new URL('http://localhost:8080').text

assert html.contains('<title>Welcome to SimpBlog</title>')

html.find(~'<title>(.*)</title>') { all, title ->
    assert title == 'Welcome to SimpBlog'
}
