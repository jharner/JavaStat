<?xml version="1.0"?>

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                version="1.0">
  <xsl:template match="/">
    <xsl:apply-templates select="node()"/>
  </xsl:template>
  
  <xsl:template match="applet">
    <a>
      <xsl:attribute name="href">
        <xsl:text>applet://</xsl:text>
        <xsl:value-of select="@name" />
      </xsl:attribute>
      <xsl:value-of select="@alt"/>
    </a>
  </xsl:template>
  
  <xsl:template match="@*|node()">
    <xsl:copy>
      <xsl:apply-templates select="@*|node()"/>
    </xsl:copy>
  </xsl:template>
</xsl:stylesheet>
  