package simpblog

import grails.gorm.annotation.Entity
import org.grails.datastore.gorm.GormEntity

@Entity
class Category implements GormEntity<Category> {
    String name
    static constraints = {
        name blank: false
    }
}