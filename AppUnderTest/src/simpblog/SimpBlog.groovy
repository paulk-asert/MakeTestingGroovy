package simpblog

import grails.persistence.Entity
import org.springframework.core.io.Resource
import org.springframework.core.io.ByteArrayResource

// solution inspired from: http://nvisia.com/techs/?p=237
@Grab('org.slf4j:slf4j-simple:1.5.8')
@Grab('org.hibernate:hibernate-annotations:3.4.0.GA')
@Grab('hsqldb:hsqldb:1.8.0.10')
@Grab('org.grails:grails-bootstrap:1.2.5;transitive=false')
@Grab('org.grails:grails-core:1.2.5')
@Grab('org.grails:grails-gorm:1.2.5')
@Grab('javassist:javassist:3.12.1.GA')
@Grab('org.mortbay.jetty:jetty-embedded:6.1.0')
class MyAppContext extends org.springframework.context.support.ClassPathXmlApplicationContext {
    protected Resource[] getConfigResources() {
        def config = """\
        <?xml version="1.0" encoding="UTF-8"?>
        <beans xmlns="http://www.springframework.org/schema/beans"
            xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
            xmlns:context="http://www.springframework.org/schema/context"
            xmlns:gorm="http://grails.org/schema/gorm"
            xmlns:util="http://www.springframework.org/schema/util"
            xmlns:tx="http://www.springframework.org/schema/tx"
            xsi:schemaLocation="
                http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context-2.5.xsd
                http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd
                http://grails.org/schema/gorm http://grails.org/schema/gorm/gorm.xsd
                http://www.springframework.org/schema/util http://www.springframework.org/schema/util/spring-util-2.0.xsd
                http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd"
            default-lazy-init="false">

            <bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
                <property name="driverClassName" value="org.hsqldb.jdbcDriver" />
                <property name="url" value="jdbc:hsqldb:mem:simpblogdb" />
                <property name="username" value="sa" />
                <property name="password" value="" />
            </bean>

            <bean id="messageSource" class="org.springframework.context.support.ResourceBundleMessageSource">
                <property name="basename" value="messages" />
            </bean>

            <context:annotation-config/>
            <tx:annotation-driven/>

            <gorm:sessionFactory base-package="simpblog"
                data-source-ref="dataSource"
                message-source-ref="messageSource">
                <property name="hibernateProperties">
                    <util:map><entry key="hibernate.hbm2ddl.auto" value="update"/></util:map>
                </property>
            </gorm:sessionFactory>

            <!--
                Ideally Spring configuration would end here and the above gorm:sessionFactory definition would
                automatically scan the classpath and register our domain objects, including ones defined in this script.
                However, gorm:sessionFactory currently uses the Spring classpath scanning logic which requires an
                actual .class file in the classpath and doesn't see ones defined within this script.
                So for now we explicitly override the definition of grailsApplication and enumerate each domain class.
                Also, for each domain class we have to manually define the bean, domain bean, and validator bean.
                Hopefully GORM will handle this in the future and we can remove everything below.
            -->
            ${manualGormConfig}
        </beans>
        """.stripIndent()
        return (Resource[]) [new ByteArrayResource(config.getBytes())]
    }

    // As stated above, none of the rest of this code would be needed if the gorm:sessionFactory definition
    // could read GORM resources from the runtime Groovy classpath; however, for now we have to explicitly
    // list them and use the getManualGormConfig method to expand them into bean definitions
    def entities = [Post, Author, Category]

    String getManualGormConfig() {
        def manualConfig = """
        <bean id="grailsApplication" class="org.codehaus.groovy.grails.commons.DefaultGrailsApplication" init-method="initialise">
            <constructor-arg>
                <list>${entities.collect{'<value>' + it.name + '</value>'}.join()}</list>
            </constructor-arg>
            <constructor-arg><bean class="groovy.lang.GroovyClassLoader" /></constructor-arg>
        </bean>
        """
        entities.each {entity ->
            manualConfig += """
            <bean id="$entity.name" class="$entity.name" scope="prototype" />
            <bean id="${entity.name}Domain" class="org.springframework.beans.factory.config.MethodInvokingFactoryBean">
                <property name="targetObject" ref="grailsApplication" />
                <property name="targetMethod" value="getArtefact" />
                <property name="arguments">
                    <list><value>Domain</value><value>$entity.name</value></list>
                </property>
            </bean>
            <bean id="${entity.name}Validator" class="org.codehaus.groovy.grails.orm.hibernate.validation.HibernateDomainClassValidator">
                <property name="messageSource" ref="messageSource" />
                <property name="domainClass" ref="${entity.name}Domain" />
            </bean>
            """
        }
        manualConfig
    }
}

@Entity class Author {
    String name
}

@Entity class Category {
    String name
}

@Entity class Post {
    static constraints = { title(size:1..50) }
    static belongsTo = [author: Author, category: Category]
    String title
    Date submitted
    String content
}

import org.mortbay.jetty.servlet.Context
import org.mortbay.jetty.Server

def runServer(int port) {
    // Spring setup, explicitly call refresh to force load of inline XML config
    def appContext = new MyAppContext()
    appContext.refresh()
    Bootstrap.initData()

    // Jetty setup with a custom servlet
    def server = new Server(port)
    def context = new Context(server, "/", Context.SESSIONS)
    context.resourceBase = "."
    context.setAttribute("appContext", appContext)
    context.addServlet(MyServlet, "/")
    server.start()
}

class Bootstrap {
    static initData() {
        // GORM bootstrap data: Authors
        def (bart, homer, marge, lisa, maggie) =
            ["Bart", "Homer", "Marge", "Lisa", "Maggie"].collect { author ->
                Author.withTransaction { new Author(name: author).save() }
            }

        // Categories
        def (work, school, home, travel, food) =
            ["Work", "School", "Home", "Travel", "Food"].collect { category ->
                Category.withTransaction { new Category(name: category).save() }
            }

        // Posts
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
