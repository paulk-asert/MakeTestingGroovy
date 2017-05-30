import grails.gorm.annotation.Entity
import grails.gorm.annotation.GormEntity

@Entity
class Person implements GormEntity<Person> { 
    String firstName
    String lastName
    static constraints = {
        firstName blank:false
        lastName blank:false
    }
}
