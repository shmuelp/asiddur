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
                    <xsl:call-template name="length-counter"/>
                 </text-header>
        </xsl:for-each>
              </texts>
              <tefillot>
                 <xsl:copy-of select="/asiddur/toc/tefilla"/>
              </tefillot>
           </toc>
           <!--xsl:copy-of select="/asiddur/text"/-->
           <xsl:apply-templates select="/asiddur/text"/>
        </asiddur-annotated>
    </xsl:template>
    
    <xsl:template match="text|if|set">
       <xsl:element name="{name()}">
         <xsl:for-each select="@*">
            <xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
         </xsl:for-each>
         <xsl:call-template name="length-counter"/>
         <xsl:apply-templates/>
       </xsl:element>
    </xsl:template>
    
   <xsl:template match="function|br|navmark|get|p|else|include">
      <xsl:element name="{name()}">
         <xsl:for-each select="@*">
            <xsl:attribute name="{name()}"><xsl:value-of select="."/></xsl:attribute>
         </xsl:for-each>
         <xsl:apply-templates/>
      </xsl:element>
   </xsl:template>
    
    <xsl:template name="length-counter">
      <xsl:variable name="length">
         <xsl:value-of select="1* ( count(descendant::function) +
                                    count(descendant::br) + 
                                    count(descendant::navmark)  +
                                    count(descendant::p) ) +
                               2* ( count(descendant::get) ) +
                               3* ( count(descendant::else) + 
                                    count(descendant::include) ) +
                               4* ( count(descendant::if[@function]) + 
                                    count(descendant::if[@variable]) +
                                    count(descendant::set) +
                                    count(descendant::if[@day]) +
                                    count(descendant::if[@month]) +
                                    count(descendant::if[@or]) )"/>
      </xsl:variable>
      <xsl:call-template name="length-counter-recursive">
         <xsl:with-param name="length" select="$length"/>
         <xsl:with-param name="current-node" select="0"/>
         <xsl:with-param name="all-nodes" select="descendant-or-self::text()"/>
      </xsl:call-template>
<!--
   Node name = <xsl:value-of select="@name"/>
   #if function = <xsl:value-of select="count(descendant::if[@function])"/>
   #if day = <xsl:value-of select="count(descendant::if[@day])"/>
   #if month = <xsl:value-of select="count(descendant::if[@month])"/>
   #if or = <xsl:value-of select="count(descendant::if[@or])"/>
      #function = <xsl:value-of select="count(descendant::function)"/>
   #if variable = <xsl:value-of select="count(descendant::if[@variable])"/>
   #else = <xsl:value-of select="count(descendant::else)"/>
   #include = <xsl:value-of select="count(descendant::include)"/>
   #get = <xsl:value-of select="count(descendant::get)"/>
   #set = <xsl:value-of select="count(descendant::set)"/>
   #p = <xsl:value-of select="count(descendant::p)"/>
   #br = <xsl:value-of select="count(descendant::br)"/>
   #navmark = <xsl:value-of select="count(descendant::navmark)"/>
   Extra length = <xsl:value-of select="$length"/>
-->
    </xsl:template>
    
    <xsl:template name="length-counter-recursive">
       <xsl:param name="length"/>
       <xsl:param name="current-node"/>
       <xsl:param name="all-nodes"/>
       <xsl:choose>
          <xsl:when test="$current-node = count($all-nodes)">
              <xsl:attribute name="length">
                 <xsl:value-of select="$length + string-length($all-nodes[$current-node])"/>
              </xsl:attribute>
          </xsl:when>
          <xsl:when test="$current-node &lt; count($all-nodes)">
              <xsl:call-template name="length-counter-recursive">
                  <xsl:with-param name="length" select="$length + string-length($all-nodes[$current-node])"/>
                  <xsl:with-param name="current-node" select="$current-node + 1"/>
                  <xsl:with-param name="all-nodes" select="$all-nodes"/>
              </xsl:call-template>
       </xsl:when>
       </xsl:choose>
<!--
       length-counter - length is <xsl:value-of select="$length"/>, node <xsl:value-of select="$current-node"/> of <xsl:value-of select="count($all-nodes)"/>
       Total length = <xsl:value-of select="$length + string-length($all-nodes[$current-node])"/>
       <xsl:value-of select="string($all-nodes[$current-node])"/>
-->
    </xsl:template>

</xsl:stylesheet>
