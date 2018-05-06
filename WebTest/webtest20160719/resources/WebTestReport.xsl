<?xml version="1.0"?>
<!DOCTYPE xsl:stylesheet [
        <!ENTITY space "&#32;">
        <!ENTITY nbsp "&#160;">
        <!-- either '&#8470;' for No, or '#' -->
        <!ENTITY no "#">
        ]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0"
                xmlns:file="java.io.File"
                xmlns:redirect="http://xml.apache.org/xalan/redirect"
                extension-element-prefixes="file redirect"
        >
    <xsl:output method="html" encoding="us-ascii" indent="yes"
                doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
                doctype-system="http://www.w3.org/TR/html4/loose.dtd"
            />

    <!-- Parameter passed from ant with the creation time -->
    <xsl:param name="reporttime"/>
    <xsl:param name="title" select="'WebTest'"/>
    <xsl:param name="testInfo.issueNumber.baseUrl"/>

    <!-- Customization hook for site-specific differences -->
    <!-- path to the deployed lastResponse_XXX.html; absolute, or relative to the WebTestReport.html -->
    <xsl:param name="responses.dir"/>
    <!-- path to the deployed resources (css, ...); absolute, or relative to the WebTestReport.html -->
    <xsl:param name="resources.dir" select="'.'"/>
    <xsl:variable name="company.logo.alt" select="'WebTest'"/>
    <xsl:param name="indexTests" select="'../index.html'"/>


    <!-- global variable -->
    <xsl:variable name="duration.total"
                  select="sum(/summary/testresult/results/step/@duration)"/>

    <xsl:variable name="img.ok" select="concat($resources.dir, '/images/ok.gif')"/>
    <xsl:variable name="img.failed" select="concat($resources.dir, '/images/todo.gif')"/>
    <xsl:variable name="img.optional" select="concat($resources.dir, '/images/optional.gif')"/>
    <xsl:variable name="img.expandPlus" select="concat($resources.dir, '/images/expandPlus.png')"/>
    <xsl:variable name="img.expandMinus" select="concat($resources.dir, '/images/expandMinus.png')"/>
    <xsl:variable name="img.canoo" select="concat($resources.dir, '/images/canoo.gif')"/>
    <xsl:variable name="img.favicon" select="concat($resources.dir, '/images/favicon.ico')"/>

	<xsl:variable name="webtestVersion" select="/summary/@Implementation-Version"/>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template match="/">
          <html lang="en">
              <head>
                  <title>
                      <xsl:text>Test results for: </xsl:text>
                      <xsl:value-of select="@testspecname"/>
                  </title>
				<link href="{$img.favicon}" type="image/x-icon" rel="shortcut icon"/>
				<link href="{$img.favicon}" type="image/x-icon" rel="icon"/>
				<link rel="contents up" title="Test Report Overview" href="{$indexTests}"/>
				<link rel="stylesheet" type="text/css" href="{$resources.dir}/report.css"/>
                  <script type="text/javascript" src="{$resources.dir}/showHide.js"></script>
                  <script type="text/javascript" src="{$resources.dir}/responseBrowser.js"
                          id="scriptResponseBrowser"></script>
              </head>

              <body onload="initExpandCollapse('expandPlus.png','expandMinus.png')">
                  <xsl:apply-templates select="//testresult"/>
                  <br/>
                  <div style="text-align: center">
                      <a href="{$indexTests}">Back to Test Report Overview</a>
                  </div>
              </body>
          </html>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template name="colorBar">
        <xsl:param name="percentage"/>
        <xsl:param name="color"/>
        <xsl:param name="title"/>
        <xsl:param name="width" select="'80%'"/>

        <td width="{$width}" class="light">
            <xsl:if test="$percentage > 0">
                <div class="colorBar" style="width: {$percentage}%; background: {$color};">
                    <xsl:if test="$title">
                        <xsl:attribute name="title">
                            <xsl:value-of select="$title"/>
                        </xsl:attribute>
                    </xsl:if>
                </div>
            </xsl:if>
        </td>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template name="time">
        <xsl:param name="msecs"/>
        <xsl:param name="detail"/>

        <xsl:choose>
            <xsl:when test="$msecs > 5000">
                <xsl:variable name="base" select="round($msecs div 1000)"/>
                <xsl:variable name="hours" select="floor($base div 3600)"/>
                <xsl:variable name="mins" select="floor(($base - $hours*3600) div 60)"/>
                <xsl:variable name="secs" select="floor(($base - $hours*3600) - $mins*60)"/>

                <xsl:if test="10 > $hours">0</xsl:if>
                <xsl:value-of select="$hours"/>
                <xsl:text>:</xsl:text>
                <xsl:if test="10 > $mins">0</xsl:if>
                <xsl:value-of select="$mins"/>
                <xsl:text>:</xsl:text>
                <xsl:if test="10 > $secs">0</xsl:if>
                <xsl:value-of select="$secs"/>

                <xsl:if test="$detail">
                    <xsl:text>&space;(</xsl:text>
                    <xsl:value-of select="$msecs"/>
                    <xsl:text>&space;ms)</xsl:text>
                </xsl:if>
            </xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$msecs"/>
                <xsl:if test="$detail">&space;ms</xsl:if>
            </xsl:otherwise>
        </xsl:choose>
    </xsl:template>

    <xsl:template match="testresult[@successful='no']" mode="successIndicator">
        <img src="{$img.failed}" alt="x"/>
    </xsl:template>

    <xsl:template match="testresult[@successful='yes']" mode="successIndicator">
        <img src="{$img.ok}" alt="ok"/>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->

    <xsl:template match="testresult">
        <!-- general info left -->
        <!-- Header and red/green box for success/failure overview-->
        <blockquote>
            <h4>
                <xsl:apply-templates select="." mode="successIndicator"/>
                <xsl:text>&nbsp;</xsl:text>
                <xsl:value-of select="@testspecname"/>
            </h4>
            <xsl:apply-templates select="description"/>

            <xsl:call-template name="displayTestInfo"/>

            <xsl:text>Test started at&space;</xsl:text>
            <xsl:value-of select="@starttime"/>
            <xsl:text>, lasting&space;</xsl:text>
            <xsl:call-template name="time">
                <xsl:with-param name="msecs"
                                select="sum(results/step/@duration)"/>
                <xsl:with-param name="detail" select="true()"/>
            </xsl:call-template>
            <xsl:text>.</xsl:text>
            <br/>
            <xsl:text>Source:&space;</xsl:text>
            <span class="location">
                <xsl:value-of select="@location"/>
            </span>
            <br/>
            <xsl:apply-templates select="config"/>
        </blockquote>

        <!-- ###################################################################### -->
        <!-- tj: show/hide all -->
        <xsl:if test="descendant::step/descendant::step">
            <p>
                <xsl:text>Expand/Collapse nested steps:</xsl:text>
                <img onclick="showAllSubstepsOfTestspec(this)" src="{$img.expandPlus}"
                     class="withPointer"
                     alt="show all nested steps in testspec" title="show all nested steps in testspec"/>
                <xsl:text>&nbsp; </xsl:text>
                <img onclick="hideAllSubstepsOfTestspec(this)" src="{$img.expandMinus}"
                     class="withPointer"
                     alt="hide all nested steps in testspec" title="hide all nested steps in testspec"/>
            </p>
        </xsl:if>

        <!-- ###################################################################### -->
        <!-- The test step results in sequence below -->
        <xsl:apply-templates select="results"/>

        <xsl:apply-templates select="results/failure"/>
        <xsl:apply-templates select="results/error"/>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <!-- Special representation for the <testInfo .../> steps if any -->
    <xsl:template name="displayTestInfo">

        <xsl:if test=".//step[@taskName = 'testInfo']">
            <div class="testInfo">
                <div class="testInfoTitle">Test info</div>
                <ul class="testInfo">
                	<xsl:for-each select=".//step[@taskName = 'testInfo']">
				        <xsl:variable name="info" select="concat(parameter[@name = 'info']/@value, parameter[@name = 'nested text']/@value)"/>
				        <li>
				            <b>
				                <xsl:value-of select="parameter[@name = 'type']/@value"/>
				                <xsl:text>:&space;</xsl:text>
				            </b>
					        <xsl:choose>
					            <xsl:when test="parameter[@name = 'type']/@value = 'issueNumber'">
					                <a>
						                <xsl:attribute name="href">
						                    <xsl:value-of select="concat($testInfo.issueNumber.baseUrl, $info)"/>
						                </xsl:attribute>
						                <xsl:value-of select="$info"/>
					                </a>
					            </xsl:when>
					            <xsl:otherwise>
						            <xsl:value-of select="$info"/>
					            </xsl:otherwise>
					        </xsl:choose>
				            <xsl:value-of select="concat(' ', @description)"/>
				        </li>
                	</xsl:for-each>
                </ul>
            </div>
        </xsl:if>
    </xsl:template>

    <!-- Individual templates -->
    <xsl:template match="config">
        <xsl:text>Base URL (used by&space;</xsl:text>
        <b>invoke</b>
        <xsl:text>&space;steps with a relative URL):&space;</xsl:text>

        <xsl:variable name="port" select="parameter[@name='port']/@value"/>
        <xsl:variable name="basepath" select="parameter[@name='basepath']/@value"/>
        <xsl:variable name="configHref">
            <xsl:value-of select="parameter[@name='protocol']/@value"/>
            <xsl:text>://</xsl:text>
            <xsl:value-of select="parameter[@name='host']/@value"/>
            <xsl:if test="$port != 80">
                <xsl:text>:</xsl:text>
                <xsl:value-of select="$port"/>
            </xsl:if>
            <xsl:text>/</xsl:text>
            <xsl:if test="$basepath != 'null'">
                <xsl:value-of select="$basepath"/>
                <xsl:text>/</xsl:text>
            </xsl:if>
        </xsl:variable>
        <a target="_blank" href="{$configHref}" class="baseUrl"> <!-- class important to allow js code to read it  -->
            <xsl:value-of select="$configHref"/>
        </a>
        <br/>

        <xsl:text>Simulated browser:&space;</xsl:text>
        <span>
            <xsl:value-of select="parameter[@name = 'browser']/@value"/>
        </span>
        <br/>
    </xsl:template>

    <!-- Renders the description of a webtest. Currently a webtest can have 0 or 1 description -->
    <xsl:template match="description">
        <div class="description">
            <xsl:value-of select="text()"/>
        </div>
    </xsl:template>

    <!-- Renders the link to a saved result page -->
    <xsl:template match="resultFile">
        <xsl:param name="linkText" select="'Resulting page'"/>
        <xsl:param name="class"/>
        <br/>
        <a target="_blank">
            <xsl:if test="string-length($class) > 0">
                <xsl:attribute name="class">
                    <xsl:value-of select="$class"/>
                </xsl:attribute>
            </xsl:if>
            <xsl:attribute name="href">
                <xsl:if test="$responses.dir">
                    <xsl:value-of select="$responses.dir"/>
                    <xsl:text>/</xsl:text>
                </xsl:if>
                <xsl:value-of select="@name"/>
            </xsl:attribute>
            <xsl:value-of select="$linkText"/>
        </a>
    </xsl:template>

    <xsl:template match="parameter">
        <tr>
            <td class="parameterName">
                <xsl:value-of select="@name"/>
            </td>
            <td class="parameterValue">
                <xsl:choose>
                    <xsl:when test="@name='filename' and not(starts-with(@value, '#{'))">
                        <a target="_blank" href="concat('file://', translate(@value, '\', '/'))">
                            <xsl:value-of select="@value"/>
                        </a>
                    </xsl:when>
                    <xsl:otherwise>
                        <xsl:value-of select="@value"/>
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>

    <xsl:template match="step">
        <tr>
            <!-- ###################################################################### -->
            <!-- tj: create image to show/hide substeps if row contains substeps and add onclick and groupStep attribute to image -->
            <td class="light" style="border-bottom:1px inset black;">
                <b>&nbsp;
                    <xsl:number value="position()"/>
                </b>
                <xsl:if test="descendant::step">
                    <br/>
                    <img name="collapseButton" onclick="changeLevelOfDetailForGroup(this)"
                         class="withPointer" alt="Show/Hide all substeps" title="Show/Hide all substeps">
		                <xsl:attribute name="src">
		                	<xsl:call-template name="computeExpansionStatus">
		                		<xsl:with-param name="expanded" select="$img.expandMinus"/>
		                		<xsl:with-param name="collapsed" select="$img.expandPlus"/>
		                	</xsl:call-template>
		                </xsl:attribute>
                    </img>
                </xsl:if>
            </td>
            <!-- ###################################################################### -->

            <xsl:apply-templates select="@result"/>
            <xsl:call-template name="stepNameCell"/>
            <xsl:call-template name="stepParameterCell"/>
            <td style="border-bottom:1px inset black;" valign="top" align="right" class="light">
                <xsl:choose>
                    <xsl:when test="@result = 'completed' or @result = 'failed'">
                        <xsl:call-template name="time">
                            <xsl:with-param name="msecs"
                                            select="@duration"/>
                        </xsl:call-template>
                    </xsl:when>
                    <xsl:otherwise>
                        &nbsp;
                    </xsl:otherwise>
                </xsl:choose>
            </td>
        </tr>
    </xsl:template>


    <xsl:template name="stepNameCell">
        <td style="border-bottom:1px inset black;" valign="top" class="light">
            <b>
		        <xsl:if test="@_filename">
	                <xsl:attribute name="title">
	                	<xsl:value-of select="concat(@_filename, ' (line: ', @_line, ')') "/>
	                </xsl:attribute>
		        </xsl:if>

                <xsl:value-of select="@taskName"/>
            </b>
            <br/>
            <!-- Hide title unknown-->
            <xsl:choose>
                <xsl:when test="@description = '&lt;unknown&gt;'">
                    &nbsp;
                </xsl:when>
                <xsl:otherwise>
                    <xsl:value-of select="@description"/>
                </xsl:otherwise>
            </xsl:choose>
            <xsl:apply-templates select="resultFile"/>

            <!--
				if the step is a container, display link to last result page of this container.
				This allows to see the last result of a group without to have to click the nested steps of this group
		   	-->
			<xsl:apply-templates select="(.//step/resultFile)[last()]">
				<xsl:with-param name="linkText" select="'last result page of this group'"/>
			</xsl:apply-templates>
        </td>
    </xsl:template>

    <xsl:template name="stepParameterCell">
        <td style="border-bottom:1px inset black;" valign="top" class="light">
            <xsl:variable name="parameter.list"
                          select="parameter[@name!='text' or @value!='null'][@name!='regex' or @value!='false']"/>
            <xsl:choose>
                <xsl:when test="count($parameter.list) > 0">
                    <div class="parameterWrapper"> <!-- to have a scrollbar when parameter values are very long -->
                        <table cellpadding="2" cellspacing="0">
                            <xsl:apply-templates select="$parameter.list[@name!='=> value']">
                                <xsl:sort select="@name"/>
                            </xsl:apply-templates>
                            <xsl:apply-templates select="$parameter.list[@name='=> value']"/>
                        </table>
                    </div>
                </xsl:when>
                <xsl:otherwise>
                    &nbsp;
                </xsl:otherwise>
            </xsl:choose>
            <xsl:call-template name="renderNestedStepTable"/>
        </td>
    </xsl:template>

    <!--
          Template to be applied for the element results, and called by name for children of step (group, not, ...)
          -->
    <xsl:template match="results" name="renderNestedStepTable">
        <xsl:if test="count(step) > 0">
            <table cellpadding="3" border="0" cellspacing="0" width="100%">
                <xsl:attribute name="class">
                	<xsl:call-template name="computeExpansionStatus"/>
                </xsl:attribute>
                <tr>
                    <th>&no;</th>
                    <th>Result</th>
                    <th>Name</th>
                    <th>Parameter</th>
                    <th>Duration</th>
                </tr>
                <xsl:apply-templates select="step"/>
            </table>
        </xsl:if>
    </xsl:template>

	<!--
		Compute the class to use for a table displaying steps.
		Depending on the status of current step, the table should be visible or not. 
	 -->
	<xsl:template name="computeExpansionStatus">
		<xsl:param name="expanded" select="'expanded'"/>
		<xsl:param name="collapsed" select="'collapsed'"/>
		<xsl:choose>
			<xsl:when test="@result = 'completed' or @result = 'notexecuted'">
                <xsl:value-of select="$collapsed"/>
			</xsl:when>
            <xsl:otherwise>
                <xsl:value-of select="$expanded"/>
            </xsl:otherwise>
		</xsl:choose>
	</xsl:template>

    <xsl:template match="@result[. = 'completed']">
        <td style="border-bottom:1px inset black;" class="light" align="center">
            <img src="{$img.ok}" alt="ok"/>
        </td>
    </xsl:template>

    <xsl:template match="@result[. = 'failed']">
        <td style="border-bottom:1px inset black;" class="light" align="center">
            <img src="{$img.failed}" alt="x"/>
            <!--
             Marks only the step that fails.
             This step has an even number of 'not' ancestor and no failed child or a not with a failed child
             -->
            <xsl:if test="(count(../ancestor::step[@taskName = 'not']) mod 2 = 0)  and (count(../step[@result = 'failed']) = 0 or (../@taskName = 'not' and count(../step[@result != 'failed']) != 0))">
                <br/>
                <a class="linkToError">
                    <xsl:attribute name="name">
                        <xsl:text>testspec</xsl:text>
                        <xsl:number count="testresult"/>
                        <xsl:text>-error</xsl:text>
                    </xsl:attribute>
                    <xsl:attribute name="href">
                        <xsl:text>#error</xsl:text>
                        <xsl:number count="testresult"/>
                    </xsl:attribute>
                    <xsl:text>Error</xsl:text>
                </a>
                <xsl:apply-templates
                        select="(ancestor::results//step/resultFile)[last()]">
                    <xsl:with-param name="linkText">Page</xsl:with-param>
                    <xsl:with-param name="class">linkToError</xsl:with-param>
                </xsl:apply-templates>
            </xsl:if>
        </td>
    </xsl:template>

    <xsl:template match="@result[. = 'notexecuted']">
        <td style="border-bottom:1px inset black;" class="light" align="center">
            <img src="{$img.optional}" alt="o"/>
        </td>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <!-- common handling for failure and error (perhaps should them be merged?) -->
    <xsl:template match="failure|error">
        <h2>
            <a>
                <xsl:attribute name="name">
                    <xsl:text>error</xsl:text>
                    <xsl:number count="testresult"/>
                </xsl:attribute>
                <xsl:text>Error</xsl:text>
            </a>
        </h2>

        <h3>Message</h3>
        <p>
            <xsl:value-of select="@message"/>
        </p>

        <xsl:if test="@currentResponse">
	        <h3>Current response</h3>
	        <p><xsl:value-of select="@currentResponse"/></p>
	    </xsl:if>

        <h3>Location</h3>
        <p>
           	<xsl:for-each select="antStack/call">
           		<xsl:if test="position() != 1"><xsl:text>at </xsl:text></xsl:if>
	            <xsl:value-of select="@filename"/>
	            <xsl:text> (line: </xsl:text>
	            <xsl:value-of select="@line"/>
	            <xsl:text>)</xsl:text><br/>
           	</xsl:for-each>
        </p>

        <xsl:if test="detail">
            <h3>Details</h3>
            <table>
                <tbody>
                    <xsl:apply-templates select="detail"/>
                </tbody>
            </table>
        </xsl:if>

        <xsl:if test="@exception">
            <h3>Exception</h3>
            <p>
                <xsl:value-of select="@exception"/>
            </p>
        </xsl:if>

        <xsl:if test="stacktrace">
            <h3 onclick="toggleDisplayNext(this, 'PRE')" title="Show/hide stacktrace" class="withPointer">
                <img alt="Show " src="{$img.expandPlus}"/>
                <xsl:text> Stacktrace</xsl:text>
            </h3>
            <pre style="display: none">
                <xsl:value-of select="stacktrace/text()" disable-output-escaping="no"/>
            </pre>
        </xsl:if>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <!-- the details of a failure/error -->
    <xsl:template match="detail">
        <tr>
            <td class="detailName">
                <xsl:value-of select="@name"/>
            </td>
            <td class="detailText">
                <xsl:value-of select="text()"/>
            </td>
        </tr>
    </xsl:template>
</xsl:stylesheet>
