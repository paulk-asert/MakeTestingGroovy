package simpblog

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