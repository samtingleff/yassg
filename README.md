# Goal #
yassg --pages=inputs/ -static=static/ -output=output/
rsync -av --delete output/ sam@some.web.host:~/public_html/

# Components

PageDB: responsible for reading in page metadata and raw body contents.

ContentDB: understands "blogging" as a data model
 - reads from PageDB
 - writes individual pages to html via a ContentWriter
 - writes index pages to html via a ContentWriter
 - writes tag-based indexes to html via a ContentWriter
 - writes index pages to rss via a ContentWriter

ContentWriter: writes Page objects to an output destination

ContentDB: rsync from static/ to output/

TemplateEngine: responsible for converting from a source format (markdown) to a destination format (html).


