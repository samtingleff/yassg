# Goal #

Build it: mvn clean compile assembly:single
Run it:   java -jar yassg-1.0.0-jar-with-dependencies.jar -content content/ -templates templates/ -static static/  -output output/
Push it:  rsync -av --delete output/ user@some.web.host:~/public_html/blog/

# Inputs #

Pages: individual pages/posts
Templates: templates used to produce pages

# Plugins #

Flickr

%%% flickr username=samtingleff width=500 height=350
14521186741
%%%

# Components #

PageDB: responsible for reading in page metadata and raw body contents.

ContentDB: understands "blogging" as a data model
 - reads from PageDB
 - writes individual pages to html via a ContentWriter
 - writes index pages to html via a ContentWriter
 - writes index pages to rss via a ContentWriter

TemplateEngine: responsible for converting from a source format (markdown) to a destination format (html).

ContentWriter: writes Page objects to an output destination

Rsync: rsync from static/ to output/static/

# Future Directions #

- Build a search index (how to serve statically?)
- Do something with tags?
- Suggestions of related content

