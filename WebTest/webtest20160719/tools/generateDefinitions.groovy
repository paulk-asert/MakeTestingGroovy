/**
Called from webTest.xml.
Generates the definitions.xml file from the content of the definitions dir
*/

def templateText = '''<?xml version="1.0"?>

<!--
This file is generated automatically from the information contained in the folder definitions.
Do not edit it else you risk to lose your changes.
-->

<!DOCTYPE project ${projectDtd}
[
<% entities.each { 
%> <!ENTITY ${it.key} SYSTEM "${it.value}">
<% } %>
]
>

<project name="${projectName}" basedir="." default="wt.nothing">

	<!-- ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
	<target name="wt.defineMacros" description="Defines macros and project specific Steps" unless="${projectName}.macroDefined">
		<property name="${projectName}.macroDefined" value="true"/>
		<echo message="Defining project macros and custom steps (if any)"/>

<% entities.each { 
%> &${it.key}; 
<% } %>
	</target>
	
	<target name="wt.nothing">
		
	</target>

	
</project>
'''


def definitionsDir = new File(properties["wt.generateDefinitions.dir"]) // TODO: use a fileset
if (!definitionsDir.exists())
{
	println "Definitions dir not found: ${definitionsDir}. Ignoring."
	return
}
def definitionsFile = new File(properties["wt.generateDefinitions.file"])

def baseDirURI = definitionsDir.parentFile.toURI() as String
def entities = new TreeMap() // as TreeMap to have elements alphabetically sorted

println "Scanning ${definitionsDir} for definitions..."
definitionsDir.eachFileRecurse
{
	if (it.file && it.name ==~ /.*\.xml/)
	{
		def relPath = it.toURI().toString() - baseDirURI
		def entityName = relPath.replaceAll(/\W/, "__")
		entities[entityName] = relPath
	}
}
println "${entities.size()} definitions found"

def dtdDir = new File("dtd")
def definitionsProjectName = project.name +".WebTest-projectDefinitions"
def projectDtd = dtdDir.exists() ? 'SYSTEM "dtd/Project.dtd"' : ''
def binding = ["entities": entities, 
	"projectName": definitionsProjectName,
	"projectDtd": projectDtd]

def engine = new groovy.text.GStringTemplateEngine()
def template = engine.createTemplate(templateText)

def newDefinitions = template.make(binding) as String

// test if this would generate a new version
if (!definitionsFile.exists() || newDefinitions != definitionsFile.text)
{
	println "Generating ${definitionsFile}"
	definitionsFile.withWriter
	{
		it << newDefinitions
	}
}
else
{
	println "Already uptodate: ${definitionsFile}"
}