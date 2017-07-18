import groovyx.net.http.FromServer
import groovyx.net.http.HttpBuilder

def postBody = [
        title   : 'Bart was here (HttpBuilderNG)',
        content : 'Cowabunga Dude!',
        author  : '1', // Bart
        category: '2'  // School
]

HttpBuilder.configure {
    request.uri = 'http://localhost:8080'
}.post {
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
