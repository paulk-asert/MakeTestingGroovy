import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder

// option values assumes a fresh start of the server
def postBody = [
        title   : 'Bart was here (HttpBuilderNG)',
        content : 'Cowabunga Dude!',
        author  : '1', // Bart
        category: '2'  // School
]

def blog = HttpBuilder.configure {
    request.uri = 'http://localhost:8080'
}

def form = blog.get {
    request.uri.path = '/postForm'
}

// update index values in case the database wasn't fresh
postBody.author = form.select('option:contains(Bart)')[0].attr('value')
postBody.category = form.select('option:contains(School)')[0].attr('value')

blog.post {
    request.uri.path = '/addPost'
    request.body = postBody
    request.contentType = 'application/x-www-form-urlencoded'
    response.success { FromServer from, Object data ->
        assert from.contentType == 'text/html'
        assert from.statusCode == 200
        assert data.select('h1')[0].text().matches(/Post \d*: Bart was here \(HttpBuilderNG\)/)
        assert data.select('h3')[1].text() == 'Category: School'
        assert data.select('h3')[2].text() == 'Author: Bart'
        assert data.select('tr>td>p').text() == 'Cowabunga Dude!'
    }
}
