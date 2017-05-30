import org.grails.orm.hibernate.HibernateDatastore
Map configuration = [
    'hibernate.hbm2ddl.auto':'create-drop',
    'dataSource.url':'jdbc:h2:mem:myDB'
]
HibernateDatastore datastore = new HibernateDatastore( configuration, Person)
