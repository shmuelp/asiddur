<?xml version="1.0" encoding="iso-8859-8" ?>

<!--
    Document   : TefillaAnnotator.xsl
    Created on : October 29, 2006, 1:43 AM
    Author     : shmuelp
    Description:
        Purpose of transformation follows.
-->

<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output method="xml" doctype-system="../../tools/asiddur-tefilla-annotated.dtd"/>
    <xsl:output method="xml" encoding="windows-1255"/>
    <!-- TODO customize transformation rules 
         syntax recommendation http://www.w3.org/TR/xslt 
    -->
    <xsl:template match="/">
        <asiddur-annotated format="0.1">
           <toc>
              <texts>
        <xsl:for-each select="/asiddur/text">
                 <text-header>
                    <xsl:attribute name="name">
                       <xsl:value-of select="@name"/>
                    </xsl:attribute>
                    <xsl:attribute name="id">
                       <xsl:value-of select="position()"/>
                    </xsl:attribute>
                 </text-header>
        </xsl:for-each>
              </texts>
              <tefillot>
                 <xsl:copy-of select="/asiddur/toc/tefilla"/>
              </tefillot>
           </toc>
           <xsl:copy-of select="/asiddur/text"/>
        </asiddur-annotated>
    </xsl:template>

</xsl:stylesheet>
