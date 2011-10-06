<?xml version="1.0" encoding="us-ascii" ?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output
            method="xml"
            version="1.0"
            encoding="us-ascii"
            omit-xml-declaration="yes"
            standalone="no"
            indent="no"
            media-type="text/xml"
            />

    <xsl:param name="version"/>

    <xsl:template match="@*|node()">
        <xsl:copy>
            <xsl:apply-templates select="@*|node()"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/project/version">
        <xsl:copy>
            <xsl:value-of select="$version"/>
        </xsl:copy>
    </xsl:template>

    <xsl:template match="/project/repositories"/>

</xsl:stylesheet>