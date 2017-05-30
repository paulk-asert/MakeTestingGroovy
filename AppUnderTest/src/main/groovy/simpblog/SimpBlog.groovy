package simpblog

//@GrabResolver('https://repo.grails.org/grails/core/')
//@Grab('org.grails:grails-datastore-gorm-hibernate5:6.1.4.BUILD-SNAPSHOT')
//@Grab('com.h2database:h2:1.4.192')
//@Grab('org.slf4j:slf4j-simple:1.7.10')
//@Grab('org.apache.tomcat:tomcat-jdbc:8.5.0')
//@Grab('javax.servlet:javax.servlet-api:3.0.1')
//@Grab('org.eclipse.jetty:jetty-webapp:9.4.5.v20170502')
//@Grab(group='org.eclipse.jetty', module='jetty-server', version='9.4.5.v20170502', transitive=false)
//@Grab(group='org.eclipse.jetty', module='jetty-servlet', version='9.4.5.v20170502', transitive=false)
//@GrabExclude('org.eclipse.jetty.orbit:javax.servlet')
//@GrabExclude('org.codehaus.groovy:groovy')
import org.eclipse.jetty.servlet.ServletContextHandler
import org.eclipse.jetty.server.Server
import org.grails.orm.hibernate.HibernateDatastore
//import groovy.servlet.*

def runServer(int port) {
    Map configuration = [
        'hibernate.hbm2ddl.auto':'create-drop',
        'dataSource.url':'jdbc:h2:mem:simpblogdb;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1'
//        'dataSource.url':'jdbc:h2:mem:simpblogdb;MVCC=TRUE;LOCK_TIMEOUT=10000;DB_CLOSE_ON_EXIT=FALSE'
    ]
    def store = new HibernateDatastore(configuration, Post, Author, Category)

    Bootstrap.initData()

    // Jetty setup with a custom servlet
    def server = new Server(port)
    def context = new ServletContextHandler(server, '/', ServletContextHandler.SESSIONS)
    context.resourceBase = "."
//    context.setAttribute("appContext", appContext)
    context.addServlet(MyServlet, "/")
    server.start()
    
    /*
    def publishedFolder = args ? args[0] : '.'
    def webappContext = new org.eclipse.jetty.webapp.WebAppContext(publishedFolder, '/jetty')
    context.setHandler(webappContext)
    */
}

class Bootstrap {
    static initData() {
        // GORM bootstrap data: Author
        def (bart, homer, marge, lisa, maggie) =
            ["Bart", "Homer", "Marge", "Lisa", "Maggie"].collect { author ->
                Author.withTransaction { new Author(name: author).save() }
            }

        // Category
        def (work, school, home, travel, food) =
            ["Work", "School", "Home", "Travel", "Food"].collect { category ->
                Category.withTransaction { new Category(name: category).save() }
            }

        // Post
        Post.withTransaction {
            new Post(author: bart, title: "Christmas", category: home, submitted: new Date() - 2,
                    content: "Aren't we forgeting the true meaning of this day? You know, the birth of Santa.").save()
            new Post(author: lisa, title: "Hunger pains", category: food, submitted: new Date() - 1,
                    content: "Do we have any food that wasn't brutally slaughtered?").save()
            new Post(author: homer, title: "If at first you don't succeed", category: home, submitted: new Date(),
                    content: 'Kids, you tried your best and you failed miserably. The lesson is, never try.').save()
            new Post(author: homer, title: "Weasel words", category: home, submitted: new Date() + 1,
                    content: "Weaseling out of things is important to learn. It's what separates us from the animals ... except the weasel.").save()
        }
    }
}

import javax.servlet.*
import javax.servlet.http.*

class MyServlet extends groovy.servlet.AbstractHttpServlet {
    void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp)
    }

    void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        def action = req.requestURI.replaceAll("/", "")
        if (action == "") { action = "index" }
        if (!action.endsWith(".ico")) this."$action"(req, resp)
        else resp.sendError(HttpServletResponse.SC_NOT_FOUND)
    }

    void index(req, resp) { index(req, resp) {} }

    void index(req, resp, msgClosure) {
        def head = {
            script(type: 'text/javascript') {
                mkp.yieldUnescaped '''
                    function hideAuthors() {
                        var posts = document.getElementById('posts').getElementsByTagName("tr");
                        var authorid = document.getElementById('author').value;
                        for (var i=0; i < posts.length; i++) {
                            if (authorid == 0 || posts[i].getAttribute('authorid') == authorid) {
                                posts[i].style.visibility="visible";
                            } else {
                                posts[i].style.visibility="collapse";
                            }
                        }
                    }
                '''
            }
        }

        def body = {
            msgClosure.delegate = delegate
            msgClosure()
            h1('SimpBlog Posts')
            table {
                tr{
                    th(align: 'right', 'Author')
                    td{ select(id: 'author', name: 'author', onchange: 'hideAuthors();') {
                        option(value: '0', '<all>')
                        Author.findAll().each{ option(value: it.id, it.name) }
                    }}
                }
            }
            table(id: 'posts') {
                Post.findAll().each{ p ->
                    tr(authorid: p.author.id) {
                        td(align: 'right', p.author.name)
                        td{ a(href: "viewPost?id=$p.id", p.title) }
                    }
                }
            }
        }
        Post.withTransaction {
            doHtml(resp, head, body)
        }
    }

    void doHtml(resp, headClosure, bodyClosure) {
        resp.contentType = 'text/html'
        new groovy.xml.MarkupBuilder(resp.writer).html {
            head {
                title 'Welcome to SimpBlog'
                headClosure.delegate = delegate
                headClosure()
            }
            body {
                mkp.yield '['; a(href: '/', 'Home'); mkp.yield '] '
                mkp.yield '['; a(href: '/postForm', 'New Blog Entry'); mkp.yield '] '
                bodyClosure.delegate = delegate
                bodyClosure()
            }
        }
    }

    void reset(req, resp) {
        Post.withTransaction { Post.findAll().each{ it.delete() } }
        Author.withTransaction { Author.findAll().each{ it.delete() } }
        Category.withTransaction { Category.findAll().each{ it.delete() } }
        Bootstrap.initData()
        resp.sendRedirect("/")
    }

    void postForm(req, resp) {
        Post.withTransaction {
            doHtml(resp) {} {
                h1('New Post')
                form(name: 'post', action: '/addPost') {
                    table {
                        tr {
                            th(align: 'right', 'Title')
                            td{ input(type: 'text', name: 'title') }
                        }
                        tr {
                            th(align: 'right', 'Author')
                            td{ select(name: 'author') { Author.findAll().each { option(value: it.id, it.name) }}}
                        }
                        tr {
                            th(align: 'right', 'Category')
                            td{ select(name: 'category') { Category.findAll().each { option(value: it.id, it.name) }}}
                        }
                        tr {
                            th(align: 'right', 'Content')
                            td{ textarea(name: 'content', rows: '4', cols: '60', '') }
                        }
                        tr {
                            th('')
                            td{ input(type: 'submit', name: 'btnPost', value: 'Create Post') }
                        }
                    }
                }
            }
        }
    }

    void addPost(req, resp) {
        def post
        Post.withTransaction {
            def category = Category.get(req.getParameter('category'))
            def author = Author.get(req.getParameter('author'))
            def title = req.getParameter('title').trim()
            def content = req.getParameter('content').trim()
            post = new Post(title: title, author: author, category: category, content: content, submitted: new Date()).save()
        }
        if (!post) { error(req, resp, 'Creation failed! Validation Error?'); return }
        resp.sendRedirect("/viewPost?id=$post.id")
    }

    void deletePost(req, resp) {
        def id = req.getParameter('id')
        if (id) {
            def post = Post.get(id)
            if (!post) { error(req, resp, 'Post not found'); return }
            post.delete()
        }
        resp.sendRedirect("/")
    }

    void error(req, resp, msg) { index(req, resp) { br(); br(); font(color: 'red', msg) } }

    void viewPost(req, resp) {
        def id = req.getParameter('id')
        if (!id) { resp.sendRedirect("/"); return }
        Post.withTransaction {
            def post = Post.get(id)
            if (!post) { error(req, resp, 'Post not found'); return }
            doHtml(resp) {} {
                mkp.yield '['; a(href: "/deletePost?id=$post.id", 'Delete Post!'); mkp.yield '] '
                h1("Post $id: $post.title")
                h3('Submitted: ' + new Date(post.submitted.time).format('dd-MMM-yyyy hh:mm a'))
                h3("Category: $post.category.name")
                h3("Author: $post.author.name")
                table(border: '0', cellpadding: '4', style: 'background-color:#ddddff', width: '80%'){
                    tr{ td{ p(post.content) }}
                }
            }
        }
    }
}

int port = args ? Integer.parseInt(args[0]) : 8080
println "Starting on port $port"
runServer(port)
