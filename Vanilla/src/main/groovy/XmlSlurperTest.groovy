def page = new XmlSlurper().parse('http://localhost:8080/viewPost?id=1')
assert page.body.h1.text().contains('Christmas')
assert page.body.h3[1].text() == 'Category: Home'
assert page.body.h3[2].text() == 'Author: Bart'
assert page.body.table.tr.td.p.text() ==
        "Aren't we forgeting the true meaning of this day? You know, the birth of Santa."
