//@Grab('com.github.peter-gergely-horvath:dyna4jdbc:3.0.0')
//@GrabConfig(systemClassLoader=true)
import groovy.sql.Sql

def script = '''
def fields = [t: 'Ticker', name: 'Name', l_cur: 'Close']
def base = 'http://www.google.com/finance/info?infotype=infoquoteall'
def url = new URL("$base&q=NASDAQ:AAPL,IBM,MSFT,GOOG")
def json = url.text.replaceFirst("//", "")
def data = new groovy.json.JsonSlurper().parseText(json)
println fields.values().collect{ "$it::" }.join('\t')
data.each { row -> println fields.keySet().collect{ row[it] }.join('\t') }
'''

//new GroovyShell().evaluate(script)

def url = 'jdbc:dyna4jdbc:scriptengine:groovy'
Sql.withInstance(url) { sql ->
  sql.eachRow(script){ row -> println "$row.Ticker $row.Close" }
}
