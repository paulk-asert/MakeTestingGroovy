//@Grab('org.codehaus.groovy.modules.http-builder:http-builder:0.7.1')
//@GrabExclude('org.codehaus.groovy:groovy')
//@GrabExclude('xml-apis:xml-apis')
import groovyx.net.http.HTTPBuilder
import static groovyx.net.http.ContentType.*
import static groovyx.net.http.Method.*

def http = new HTTPBuilder('http://localhost:8080')
def postBody = [
        title   : 'Bart was here (HttpBuilder)',
        content : 'Cowabunga Dude!',
        author  : '1', // Bart
        category: '2'  // School
]
String redirectUrl = null
http.request(POST) {
    uri.path = '/addPost'
    send URLENC, postBody
    response.'302' = { resp ->
        redirectUrl = resp.getFirstHeader('Location').value
    }
}
if (redirectUrl) {
    http.request(GET) {
        uri = new groovyx.net.http.URIBuilder(redirectUrl)
        response.success = { resp, html ->
            assert resp.contentType == 'text/html'
            assert resp.status == 200
            assert html.BODY.H1.text().matches('Post.*: Bart was here.*')
            assert html.BODY.H3[1].text() == 'Category: School'
            assert html.BODY.H3[2].text() == 'Author: Bart'
            assert html.BODY.TABLE.TBODY.TR.TD.P.text() == 'Cowabunga Dude!'
        }
    }
}
