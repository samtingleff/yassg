# Goal #

Build it: mvn clean assembly:single
Run it:   java -jar yassg-1.0.0-jar-with-dependencies.jar -input input/ -static static/  -output output/
Push it:  rsync -av --delete output/ sam@some.web.host:~/public_html/

# Components #

PageDB: responsible for reading in page metadata and raw body contents.

ContentDB: understands "blogging" as a data model
 - reads from PageDB
 - writes individual pages to html via a ContentWriter
 - writes index pages to html via a ContentWriter
 - writes tag-based indexes to html via a ContentWriter
 - writes index pages to rss via a ContentWriter

ContentWriter: writes Page objects to an output destination

Rsync: rsync from static/ to output/static/

TemplateEngine: responsible for converting from a source format (markdown) to a destination format (html).

# Future Directions #

- Build a search index
- Suggestions of related content

