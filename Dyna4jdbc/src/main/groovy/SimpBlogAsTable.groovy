//@Grab('com.github.peter-gergely-horvath:dyna4jdbc:2.5.0')
//@GrabConfig(systemClassLoader=true)
import groovy.sql.Sql

def script = '''
import org.cyberneko.html.parsers.SAXParser

def page = new XmlParser(new SAXParser()).parse('http://localhost:8080/')
println 'ID::\tAUTHOR::\tTITLE::'
def rows = page.BODY.TABLE.find{ it.@id == 'posts' }.TBODY.TR
if (rows) {
  rows[0..[3, rows.size()- 1].min()].each {
    println "${it.TD[1].A.@href[0] - 'viewPost?id='}\t${it.TD[0].text()}\t${it.TD[1].A.text()}"
  }
}
'''

//new GroovyShell().evaluate(script)

def url = 'jdbc:dyna4jdbc:scriptengine:groovy'
Sql.withInstance(url) { sql ->
  sql.eachRow(script){ row -> println "$row.ID $row.AUTHOR $row.TITLE" }
}
