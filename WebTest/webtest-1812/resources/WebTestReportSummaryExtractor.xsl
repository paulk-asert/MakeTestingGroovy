<?xml version="1.0"?>
<!-- 
This xsl extract the information needed for the overview report from the report
for a single webtest.
 -->

<!DOCTYPE xsl:stylesheet>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">

    <xsl:output method="xml" indent="yes"/>

	<xsl:template match="testresult">
	    <xsl:variable name="duration.total"
                  select="sum(results/step[@result = 'completed' or @result ='failed']/@duration)"/>
		<summary 
			successful="{@successful}" 
			name="{@testspecname}" 
			duration="{$duration.total}"
			starttime="{@starttime}">

	        <xsl:variable name="topsteps.total" select="count(results/step)"/>
	        <xsl:variable name="topsteps.ok" select="count(results/step[@result = 'completed'])"/>
	        <xsl:variable name="topsteps.failed" select="count(results/step[@result = 'failed'])"/>
	        <xsl:variable name="topsteps.else" select="count(results/step[@result = 'notexecuted'])"/>
			<topsteps total="{$topsteps.total}" 
				successful="{$topsteps.ok}" 
				failed="{$topsteps.failed}" 
				notexecuted="{$topsteps.else}"/>

	        <xsl:variable name="steps.total" select="count(//step)"/>
	        <xsl:variable name="steps.ok" select="count(//step[@result = 'completed'])"/>
	        <xsl:variable name="steps.failed" select="count(//step[@result = 'failed'])"/>
	        <xsl:variable name="steps.else" select="count(//step[@result = 'notexecuted'])"/>
			<steps total="{$steps.total}" 
				successful="{$steps.ok}" 
				failed="{$steps.failed}" 
				notexecuted="{$steps.else}"/>

			<!-- timing profile -->
			<xsl:call-template name="timing-profile"/>

            <xsl:apply-templates select="results/step[@result = 'failed']" mode="failurecause"/>

            <xsl:apply-templates select="results//step[@taskName = 'testInfo' and parameter[@name = 'type' and @value = 'summary']]"  mode="testInfoSummary"/>
		</summary>
	</xsl:template>

	<xsl:template match="step" mode="failurecause">
		<failingstep>
			<xsl:attribute name="name">
			    <xsl:value-of select="@taskName"/>
			</xsl:attribute>
			<xsl:attribute name="description">
			    <xsl:value-of select="@description"/>
			</xsl:attribute>
			
	        <xsl:choose>
	        	<xsl:when test="@taskName = 'not'">
            		<xsl:apply-templates select="step[@result = 'completed']" mode="failurecause"/>
            	</xsl:when>
            	<xsl:otherwise>
            		<xsl:apply-templates select="step[@result = 'failed'][position() = last()]" mode="failurecause"/>
            	</xsl:otherwise>
            </xsl:choose>
		</failingstep>
    </xsl:template>


	<xsl:template name="timing-profile">
        <xsl:variable name="requestSteps"
                      select="//step[starts-with(@taskName, 'sqlunit') or @taskName = 'invoke' or @taskName = 'clickLink' or @taskName = 'clickButton' or @taskName='clickElement'][@result = 'completed' or @result = 'failed']"/>

        <xsl:variable name="nbSteps" select="count($requestSteps)"/>
        <xsl:variable name="totalDuration" select="sum($requestSteps/@duration)"/>
        <xsl:variable name="averageDuration" select="round($totalDuration div $nbSteps)"/>
        <xsl:variable name="steps.30" select="count($requestSteps[@duration > 30000])"/>
        <xsl:variable name="steps.10_30" select="count($requestSteps[@duration > 10000][30000 >= @duration])"/>
        <xsl:variable name="steps.5_10" select="count($requestSteps[@duration > 5000][100000 >= @duration])"/>
        <xsl:variable name="steps.3_5" select="count($requestSteps[@duration > 3000][50000 >= @duration])"/>
        <xsl:variable name="steps.1_3" select="count($requestSteps[@duration > 1000][3000 >= @duration])"/>
        <xsl:variable name="steps.0_1" select="count($requestSteps[1000 >= @duration])"/>

		<timingprofile average="{$averageDuration}" number="{$nbSteps}">
			<range from="0" to="1" number="{$steps.0_1}"/>
			<range from="1" to="3" number="{$steps.1_3}"/>
			<range from="3" to="5" number="{$steps.3_5}"/>
			<range from="5" to="10" number="{$steps.5_10}"/>
			<range from="10" to="30" number="{$steps.10_30}"/>
			<range from="30" number="{$steps.30}"/>
		</timingprofile>

	</xsl:template>

	<xsl:template match="step" mode="testInfoSummary">
		<testInfo description="{@description}">
			<xsl:value-of select="concat(parameter[@name = 'info']/@value, parameter[@name = 'nested text']/@value)"/>
		</testInfo>
	</xsl:template>

</xsl:stylesheet>
