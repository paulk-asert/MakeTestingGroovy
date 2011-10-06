<?xml version="1.0" encoding="UTF-8"?>
<!-- 
Transforms WebTest overview results to the standard format of JUnit results
that is recognized by continuous integration tools like Cruise Control
 -->

<!DOCTYPE xsl:stylesheet>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>

    <!-- global variables -->
    <xsl:variable name="summaries" select="/overview/folder/summary"/>
    <xsl:variable name="duration.total" select="sum($summaries/@duration) div 1000"/>


    <xsl:template match="/">
    	<testsuite errors="{count($summaries[@successful='no'])}" failures="0" name="WebTest" tests="{count($summaries)}" time="{$duration.total}">
			<xsl:comment>
			This is an overview of the WebTest results in JUnit "standard" format.
			It doesn't present all the information available in WebTest reports but can be easily be integrated
			in reports of continuous integration tools like Cruise Control.
			</xsl:comment>
    		<xsl:apply-templates select="$summaries"/>
    	</testsuite>
    </xsl:template>
    
    <xsl:template match="summary[@successful='yes']">
		<testcase classname="WebTest" name="{@name}" time="{@duration div 1000}"></testcase>
    </xsl:template>
    
    <xsl:template match="summary[@successful='no']">
		<testcase classname="WebTest" name="{@name}" time="{@duration div 1000}">
		    <failure message="Failing step: {@name} ({@description})" type="Failed test">
			</failure>
		</testcase>
    </xsl:template>
</xsl:stylesheet>
