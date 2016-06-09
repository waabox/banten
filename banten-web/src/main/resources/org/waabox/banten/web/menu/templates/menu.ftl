<html>

  <head>
    <title>Menu</title>
  </head>

  <body>

  <#macro render childNodes>
    <#list childNodes as node>
      <#if node_index == 0>
        <ul>
      </#if>
      <#if node.leaf>
        <li><a href="${node.link}"> ${node.displayName}</a></li>
      <#else>
        <li>${node.displayName}</li>
        <@render node.childNodes/>
        </ul>
      </#if>
    </#list>
  </#macro>

  <nav>
    <@render menu.childNodes/>
  </nav>

  </body>

</html>

