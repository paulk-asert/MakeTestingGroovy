//@GrabResolver('https://repo.grails.org/grails/core/')
//@GrabExclude('org.codehaus.groovy:groovy-all')
//@Grab('org.codehaus.groovy:groovy-all:2.4.12')
//@Grab('org.grails:grails-datastore-gorm-hibernate5:6.1.5.RELEASE')
//@Grab('com.h2database:h2:1.4.196')
//@Grab('org.slf4j:slf4j-simple:1.7.21')
//@Grab('org.apache.tomcat:tomcat-jdbc:8.5.0')
import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class Post implements GormEntity<Post> {
    String title
    Date submitted
    String content
    static constraints = { title(size:1..50) }
    static belongsTo = [author: Author, category: Category]
}

@Entity
class Author implements GormEntity<Author> {
    String name
    static constraints = {
        name blank: false
    }
}

@Entity
class Category implements GormEntity<Category> {
    String name
    static constraints = {
        name blank: false
    }
}

import org.grails.orm.hibernate.HibernateDatastore
Map configuration = [
    'hibernate.hbm2ddl.auto':'create-drop',
    'dataSource.url':'jdbc:h2:mem:myDB;DB_CLOSE_ON_EXIT=FALSE;DB_CLOSE_DELAY=-1'
]
def store = new HibernateDatastore(configuration, Post, Author, Category)

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
Post.withNewSession {
    println "Posts:\n${Post.list().collect{"$it.id $it.title"}.join('\n')}"
}

