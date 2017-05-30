package simpblog

import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class Author implements GormEntity<Author> {
    String name
    static constraints = {
        name blank: false
    }
}