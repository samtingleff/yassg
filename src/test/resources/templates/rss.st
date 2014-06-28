<?xml version="1.0" encoding="utf-8"?>
<rss version="2.0">
 <channel>
 <title>sam.tingleff.com</title>
 <description>hot air, fresh</description>
 <link>http://sam.tingleff.com/blog/</link>
 <copyright>Copyright &copy; 2014 Sam Tingleff</copyright>
 <language>en-us</language>
 <lastBuildDate>$buildDate$</lastBuildDate>
 <pubDate>$buildDate$</pubDate>
 <generator>yassg 0.1.0</generator>
 $items:{item|
  <item>
   <title>$item.title$</title>
   <description><![CDATA[$item.body$]]></description>
   <link>http://sam.tingleff.com/blog$item.href$</link>
   $item.tags:{tag|
    <category>$tag$</category>
   }$
   <comments>http://sam.tingleff.com/blog$item.href$</comments>
   <pubDate>$item.pubDateRSS$</pubDate>
  </item>
 }$
</rss>
