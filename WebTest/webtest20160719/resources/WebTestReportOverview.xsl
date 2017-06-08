<?xml version="1.0"?>
<!-- 
This XSL allows to generate the report overview.
 -->

<!DOCTYPE xsl:stylesheet [
        <!ENTITY space "&#32;">
        <!ENTITY nbsp "&#160;">
        <!-- either '&#8470;' for No, or '#' -->
        <!ENTITY no "#">
        ]>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="html" encoding="us-ascii" indent="yes"
                doctype-public="-//W3C//DTD HTML 4.01 Transitional//EN"
                doctype-system="http://www.w3.org/TR/html4/loose.dtd"
            />

    <!-- Parameter passed from ant with the creation time -->
    <xsl:param name="reporttime"/>
    <xsl:param name="title" select="'WebTest'"/>

    <!-- Customization hook for site-specific differences -->
    <!-- path to the deployed resources (css, ...); absolute, or relative to the WebTestReport.html -->
    <xsl:param name="resources.dir" select="'.'"/>
    <xsl:variable name="company.logo.alt" select="'WebTest'"/>
    <xsl:param name="outputDir" select="'../reports'"/>


    <!-- global variable -->
    <xsl:variable name="duration.total" select="sum(/overview/folder/summary/@duration)"/>

    <xsl:variable name="img.ok" select="concat($resources.dir, '/images/ok.gif')"/>
    <xsl:variable name="img.todo" select="concat($resources.dir, '/images/todo.gif')"/>
    <xsl:variable name="img.optional" select="concat($resources.dir, '/images/optional.gif')"/>
    <xsl:variable name="img.expandPlus" select="concat($resources.dir, '/images/expandPlus.png')"/>
    <xsl:variable name="img.canoo" select="concat($resources.dir, '/images/canoo.gif')"/>
    <xsl:variable name="img.favicon" select="concat($resources.dir, '/images/favicon.ico')"/>

	<xsl:variable name="webtestVersion" select="/overview/@Implementation-Version"/>

    <xsl:template match="/">
        <!-- HTML prefix -->
        <html lang="en">
            <head>
                <title>
                    <xsl:value-of select="$title"/>
                    <xsl:text>&space;- Test Result Overview</xsl:text>
                </title>
				<link href="{$img.favicon}" type="image/x-icon" rel="shortcut icon"/>
				<link href="{$img.favicon}" type="image/x-icon" rel="icon"/>
                <meta http-equiv="content-style-type" content="text/css"/>
                <link rel="stylesheet" type="text/css" href="{$resources.dir}/report.css"/>
                <script type="text/javascript" src="{$resources.dir}/sorttable.js"></script>
            </head>

            <!-- ###################################################################### -->
            <body>
                <div class="header">
                    <img src="{$resources.dir}/images/webtest.jpg" alt="{$company.logo.alt}"/>
                    <br/>
                    <xsl:value-of select="$title"/>
                    <xsl:text>&nbsp;&nbsp;&nbsp;&nbsp;Tests started at&space;</xsl:text>
                    <xsl:value-of select="/overview/folder[1]/summary/@starttime"/>
                </div>

                <!-- Header and summary table -->
                <xsl:call-template name="StepStatistics"/>

                <xsl:call-template name="OverviewTable"/>

                <!-- Footer & fun -->
                <hr/>

			<table width="100%">
			<tr>
			<td valign="bottom">
                <xsl:text>Created using&space;</xsl:text>
                <a href="http://webtest.canoo.com">
                    <xsl:value-of select="/overview/@Implementation-Title"/>
                </a>
                <xsl:text>&space;(</xsl:text>
                <xsl:value-of select="$webtestVersion"/>
                <xsl:text>). Report created at&space;</xsl:text>
                <xsl:value-of select="$reporttime"/>
			</td>
			<td align="right">
			<!-- remove this line if you want to avoid outgoing requests when looking at the results -->
			<img alt="WebTest" src="http://webtest.canoo.com/webtest/manual/images/webtest.jpg?build={$webtestVersion}"/>
			<xsl:text> is an Open Source project founded and hosted by </xsl:text>
			<a href="http://www.canoo.com"><img alt="Canoo" src="{$img.canoo}"/></a>
			</td>
			</tr>
			</table>
                <!-- HTML postfix -->
            </body>
        </html>

    </xsl:template>

    <xsl:template name="OverviewTable">
        <h4>
            <a name="overview">
                <xsl:text>Test Scenario Overview (</xsl:text>
                <xsl:call-template name="time">
                    <xsl:with-param name="msecs" select="$duration.total"/>
                </xsl:call-template>
                <xsl:text>)</xsl:text>
            </a>
        </h4>

        <!--
                        Create summary table entries choosing the td-class depending on successful yes/no
                        and create a link to the appropriate detail section (e.g. #testspec1).
                    -->
        <table cellpadding="5" border="0" cellspacing="0" width="100%" class="sortable">
            <thead>
                <tr>
                    <th rowspan="2">&no;</th>
                    <th rowspan="2">Result</th>
                    <th rowspan="2">Name</th>
                    <th rowspan="2" title="Number of successful executed steps / Total number of steps"># Steps</th>
                    <th colspan="3">Timing profile</th>
                    <th rowspan="2" style="white-space: nowrap">Failing step</th>
                </tr>
                <tr>
                    <th>Duration</th>
                    <th>%</th>
                    <th>Graph</th>
                </tr>
            </thead>
            <tbody>
                <xsl:apply-templates select="/overview/folder/summary" mode="summary"/>
            </tbody>
        </table>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template name="StepStatistics_tests">
        <xsl:variable name="webtests.total" select="count(/overview/folder/summary)"/>
        <xsl:variable name="webtests.ok" select="count(/overview/folder/summary[@successful='yes'])"/>
        <xsl:variable name="webtests.failed" select="count(/overview/folder/summary[@successful='no'])"/>

        <tbody>
            <tr>
                <th>WebTests</th>
                <th align="right">#</th>
                <th align="right">%</th>
                <th>Graph</th>
            </tr>
            <tr>
                <td class="light">
                    <img src="{$img.ok}" alt="ok"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="$webtests.ok"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="round($webtests.ok * 100 div $webtests.total)"/>
                </td>
                <xsl:call-template name="colorBar">
                    <xsl:with-param name="percentage" select="$webtests.ok * 100 div $webtests.total"/>
                    <xsl:with-param name="color" select="'lightgreen'"/>
                    <xsl:with-param name="title" select="'Successful WebTests'"/>
                </xsl:call-template>
            </tr>
            <tr>
                <td class="light">
                    <img src="{$img.todo}" alt="x"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="$webtests.failed"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="round($webtests.failed * 100 div $webtests.total)"/>
                </td>
                <xsl:call-template name="colorBar">
                    <xsl:with-param name="percentage" select="$webtests.failed * 100 div $webtests.total"/>
                    <xsl:with-param name="color" select="'red'"/>
                    <xsl:with-param name="title" select="'Failed WebTests'"/>
                </xsl:call-template>
            </tr>
        </tbody>
        <tbody>
            <tr>
                <td class="light">
                    <b>Sum</b>
                </td>
                <td class="light" align="right">
                    <b>
                        <xsl:text>&nbsp;</xsl:text>
                        <xsl:value-of select="$webtests.total"/>
                    </b>
                </td>
                <td class="light" align="right">
                    <b>&nbsp;100</b>
                </td>
                <td class="light">&nbsp;</td>
            </tr>
        </tbody>
    </xsl:template>

    <xsl:template name="StepStatistics_steps">
        <xsl:variable name="steps.total" select="sum(/overview/folder/summary/topsteps/@total)"/>
        <xsl:variable name="steps.ok" select="sum(/overview/folder/summary/topsteps/@successful)"/>
        <xsl:variable name="steps.failed" select="sum(/overview/folder/summary/topsteps/@failed)"/>
        <xsl:variable name="steps.else" select="sum(/overview/folder/summary/topsteps/@notexecuted)"/>

        <tbody>
            <tr>
                <th>Steps</th>
                <th align="right">#</th>
                <th align="right">%</th>
                <th>Graph</th>
            </tr>
            <tr>
                <td class="light">
                    <img src="{$img.ok}" alt="ok"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="$steps.ok"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="round($steps.ok * 100 div $steps.total)"/>
                </td>
                <xsl:call-template name="colorBar">
                    <xsl:with-param name="percentage" select="$steps.ok * 100 div $steps.total"/>
                    <xsl:with-param name="color" select="'lightgreen'"/>
                    <xsl:with-param name="title" select="'Successful steps'"/>
                </xsl:call-template>
            </tr>
            <tr>
                <td class="light">
                    <img src="{$img.todo}" alt="x"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="$steps.failed"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="round($steps.failed * 100 div $steps.total)"/>
                </td>
                <xsl:call-template name="colorBar">
                    <xsl:with-param name="percentage" select="$steps.failed * 100 div $steps.total"/>
                    <xsl:with-param name="color" select="'red'"/>
                    <xsl:with-param name="title" select="'Failed steps'"/>
                </xsl:call-template>
            </tr>
            <tr>
                <td class="light">
                    <img src="{$img.optional}" alt="o"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="$steps.else"/>
                </td>
                <td class="light" align="right">
                    <xsl:value-of select="round($steps.else * 100 div $steps.total)"/>
                </td>
                <xsl:call-template name="colorBar">
                    <xsl:with-param name="percentage" select="$steps.else * 100 div $steps.total"/>
                    <xsl:with-param name="color" select="'yellow'"/>
                    <xsl:with-param name="title" select="'Skipped steps'"/>
                </xsl:call-template>
            </tr>
        </tbody>
        <tbody>
            <tr>
                <td class="light">
                    <b>Sum</b>
                </td>
                <td class="light" align="right">
                    <b>
                        <xsl:text>&nbsp;</xsl:text>
                        <xsl:value-of select="$steps.total"/>
                    </b>
                </td>
                <td class="light" align="right">
                    <b>&nbsp;100</b>
                </td>
                <td class="light">&nbsp;</td>
            </tr>
        </tbody>
    </xsl:template>

    <xsl:template name="StepStatistics_serverRoundtrips">
        <h4>Server Roundtrip Timing Profile</h4>
        <!--        ================================ server roundtrip statistic table =============================    -->

        <xsl:variable name="timingProfiles" select="/overview/folder/summary/timingprofile"/>
        <xsl:variable name="profileRanges" select="/overview/folder/summary/timingprofile/range"/>
        <xsl:variable name="steps.last" select="sum($profileRanges[@from=30]/@number)"/>
        <xsl:variable name="steps.fourth" select="sum($profileRanges[@from=10 and @to=30]/@number)"/>
        <xsl:variable name="steps.third" select="sum($profileRanges[@from=5 and @to=10]/@number)"/>
        <xsl:variable name="steps.second" select="sum($profileRanges[@from=3 and @to=5]/@number)"/>
        <xsl:variable name="steps.first" select="sum($profileRanges[@from=1 and @to=3]/@number)"/>
        <xsl:variable name="steps.begin" select="sum($profileRanges[@to=1]/@number)"/>
        <xsl:variable name="steps.total" select="sum($profileRanges/@number)"/>

        <xsl:variable name="averageTime" select="round(sum(//summary/@duration) div sum($timingProfiles/@number))"/>

        <table cellpadding="3" border="0" cellspacing="0" width="100%">
            <thead>
                <tr>
                    <th align="center">Secs</th>
                    <th align="right">#</th>
                    <th align="right">%</th>
                    <th>Histogram</th>
                </tr>
            </thead>
            <tfoot>
                <tr>
                    <td class="light">
                        <b>Sum</b>
                    </td>
                    <td class="light" align="right">
                        <b>
                            <xsl:text>&nbsp;</xsl:text>
                            <xsl:value-of select="$steps.total"/>
                        </b>
                    </td>
                    <td class="light" align="right">
                        <b>&nbsp;100</b>
                    </td>
                    <td class="light">&nbsp;</td>
                </tr>
                <tr>
                    <td class="light">
                        <b>Avg</b>
                    </td>
                    <td class="light" align="right">
                        &nbsp;
                    </td>
                    <td class="light" align="right">
                        &nbsp;
                    </td>
                    <td class="light">
                        <b>
                            <xsl:value-of select="$averageTime"/>
                            <xsl:text> ms</xsl:text>
                        </b>
                    </td>
                </tr>
            </tfoot>
            <tbody>
                <xsl:call-template name="histogramRow">
                    <xsl:with-param name="label">0 - 1</xsl:with-param>
                    <xsl:with-param name="steps" select="$steps.begin"/>
                    <xsl:with-param name="total" select="$steps.total"/>
                </xsl:call-template>

                <xsl:call-template name="histogramRow">
                    <xsl:with-param name="label">1 - 3</xsl:with-param>
                    <xsl:with-param name="steps" select="$steps.first"/>
                    <xsl:with-param name="total" select="$steps.total"/>
                </xsl:call-template>

                <xsl:call-template name="histogramRow">
                    <xsl:with-param name="label">3 - 5</xsl:with-param>
                    <xsl:with-param name="steps" select="$steps.second"/>
                    <xsl:with-param name="total" select="$steps.total"/>
                </xsl:call-template>

                <xsl:call-template name="histogramRow">
                    <xsl:with-param name="label">5 - 10</xsl:with-param>
                    <xsl:with-param name="steps" select="$steps.third"/>
                    <xsl:with-param name="total" select="$steps.total"/>
                </xsl:call-template>

                <xsl:call-template name="histogramRow">
                    <xsl:with-param name="label">10 - 30</xsl:with-param>
                    <xsl:with-param name="steps" select="$steps.fourth"/>
                    <xsl:with-param name="total" select="$steps.total"/>
                </xsl:call-template>

                <xsl:call-template name="histogramRow">
                    <xsl:with-param name="label">&gt; 30</xsl:with-param>
                    <xsl:with-param name="steps" select="$steps.last"/>
                    <xsl:with-param name="total" select="$steps.total"/>
                </xsl:call-template>
            </tbody>
        </table>
    </xsl:template>

    <xsl:template name="StepStatistics_results">
        <h4>Result Summary</h4>
        <table cellpadding="3" border="0" cellspacing="0" width="100%">
            <xsl:call-template name="StepStatistics_tests"/>
            <xsl:call-template name="StepStatistics_steps"/>
        </table>
    </xsl:template>

    <xsl:template name="StepStatistics">
        <table cellpadding="0" border="0" cellspacing="2" width="100%">
            <tr>
                <td valign="top" width="50%">
                    <xsl:call-template name="StepStatistics_results"/>
                </td>
                <td valign="top">
                    <xsl:call-template name="StepStatistics_serverRoundtrips"/>
                </td>
            </tr>
        </table>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template name="histogramRow">
        <xsl:param name="label"/>
        <xsl:param name="steps"/>
        <xsl:param name="total"/>
        <tr>
            <td class="light" nowrap="nowrap" align="center">
                <xsl:value-of select="$label"/>
            </td>
            <td class="light" align="right">
                <xsl:value-of select="$steps"/>
            </td>
            <td class="light" align="right">
                <xsl:value-of select="round($steps * 100 div $total)"/>
            </td>
            <xsl:call-template name="colorBar">
                <xsl:with-param name="percentage" select="$steps * 100 div $total"/>
                <xsl:with-param name="color" select="'lightblue'"/>
            </xsl:call-template>
        </tr>
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

    <xsl:template match="summary[@successful='no']" mode="successIndicator">
        <img src="{$img.todo}" alt="x"/>
    </xsl:template>

    <xsl:template match="summary[@successful='yes']" mode="successIndicator">
        <img src="{$img.ok}" alt="ok"/>
    </xsl:template>

    <xsl:template match="testresult" mode="indexedFileName">
        <xsl:text>File</xsl:text>
        <xsl:number/>
        <xsl:text>.html</xsl:text>
    </xsl:template>
    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template match="summary" mode="summary">
        <tr>
            <td class="light" align="right">
                <xsl:number count="folder"/>
            </td>
            <td class="light" align="center">
                <xsl:apply-templates select="." mode="successIndicator"/>
            </td>
            <td class="light" nowrap="nowrap">
                <a href="{../@name}/WebTestReport.html">
                    <xsl:value-of select="@name"/>
                </a>
                
                <xsl:apply-templates select="testInfo"/>
            </td>
            <td class="light" align="right" style="white-space: nowrap">
                <xsl:value-of select="steps/@successful"/>
                <xsl:text> / </xsl:text>
                <xsl:value-of select="steps/@total"/>
            </td>
            <td class="light" align="right" nowrap="nowrap">
                <xsl:call-template name="time">
                    <xsl:with-param name="msecs" select="@duration"/>
                </xsl:call-template>
            </td>
            <td class="light" align="right">
                <xsl:value-of select="round(@duration * 100 div $duration.total)"/>
            </td>

            <xsl:call-template name="colorBar">
                <xsl:with-param name="percentage" select="@duration * 100 div $duration.total"/>
                <xsl:with-param name="color" select="'lightblue'"/>
                <xsl:with-param name="width" select="'20%'"/>
            </xsl:call-template>

            <td class="light"> <!--  the failing top step, if any -->
                <xsl:apply-templates select="failingstep"/>
            </td>
        </tr>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template match="testInfo">
         <br/>
         <b><xsl:value-of select="text()"/></b>
         <xsl:value-of select="concat(' ', @description)"/>
    </xsl:template>

    <!-- +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++ -->
    <xsl:template match="failingstep">
    	<xsl:if test="parent::failingstep">
    		<br/>
           <xsl:text>&gt; </xsl:text>
        </xsl:if>
        <b>
            <xsl:value-of select="@name"/>
        </b>
        <xsl:text> </xsl:text>
        <xsl:value-of select="@description"/>
        <xsl:apply-templates select="failingstep"/>
    </xsl:template>

</xsl:stylesheet>
