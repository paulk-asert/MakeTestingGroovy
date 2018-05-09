import com.rmn.pairwise.*

String SCENARIOS = '''
title: Title 1, Title 2, Title 3, Title 4
author: Bart, Homer, Marge, Lisa, Maggie
category: Work, School, Home, Travel, Food
content: Content A, Content B, Content C, Content D
'''

def inventory = PairwiseInventoryFactory.generateParameterInventory(SCENARIOS)
def dataSet = inventory.testDataSet
dataSet.logResults()
//println()
//println dataSet.rawTestSets.join('\n')
//println()
//println dataSet.testSets.join('\n')
def rawDataSet = dataSet.testSets

rawDataSet.each {
    println "Looking for: [$it.title] [$it.author] [$it.category] [$it.content]"
}
