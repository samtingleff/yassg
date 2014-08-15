# Yet Another Static Site Generator #

This is Yet Another Static Site Generator. There are many of them, but this one is mine.

You will probably have no interest or desire to use it.

It uses [markdown](http://daringfireball.net/projects/markdown/) for post pages and [string template](http://www.stringtemplate.org/) for templates.

# Using It #

Build it: mvn clean compile assembly:single
Run it:   java -jar yassg-1.0.0-jar-with-dependencies.jar -content content/ -templates templates/ -output output/
Push it:  rsync -av --delete output/ user@some.web.host:~/public_html/blog/

# Markdown Plugins #

Flickr

%%% flickr username=samtingleff width=500 height=350
14521186741
%%%

SoundCloud

%%% soundcloud
158208762
%%%

# Future Directions #

- Build a search index (how to serve statically?)
- Do something with tags?
- Suggestions of related content

