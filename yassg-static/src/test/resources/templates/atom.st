<?xml version="1.0" encoding="utf-8"?>
<feed xmlns="http://www.w3.org/2005/Atom">
 <title>Sam Tingleff</title>
 <subtitle>fresh nonsense, whenever</subtitle>
 <link rel="alternate" type="text/html" href="http://sam.tingleff.com/" />
 <link rel="self" type="application/atom+xml" href="http://sam.tingleff.com/blog/feeds/atom.xml" />
 <id>http://sam.tingleff.com/blog/feeds/atom.xml</id>
 <updated>$buildDate$</updated>
 <rights>Copyright &copy; 2014, Sam Tingleff</rights>
 $items:{item|
  <entry>
   <title>$item.title$</title>
   <link rel="alternate" type="text/html" href="http://sam.tingleff.com/blog$item.href$"/>
   <id>tag:sam.tingleff.com/$item.id$</id>
   <published>$item.pubDateRSS$</published>
  </entry>
 }$
</feed>
