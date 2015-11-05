# Yet Another Static Site Generator #

This is Yet Another Static Site Generator. There are many of them, but this one is mine.

You will probably have no interest or desire to use it.

It uses [markdown](http://daringfireball.net/projects/markdown/) for post pages and [string template](http://www.stringtemplate.org/) for templates.

# Using It #

* Build it: cd yassg-search ; mvn install ; cd ../yassg-static ; mvn clean compile test assembly:single
* Run it:   java -jar target/yassg-static-1.0.0-jar-with-dependencies.jar -content content/ -templates templates/ -index index/ -output output/
* Push it:  rsync -av --delete output/ user@some.web.host:~/public_html/blog/

# Interesting Features #

* Builds a lucene index based on post content, linked domains and named entities from linked content (via [AlchemyAPI](http://www.alchemyapi.com/))
* "Related content" widget built at build-time
* (Optional) thrift service to search the lucene index

# Markdown Plugins #

## Flickr ##

```
%%% flickr username=samtingleff width=500 height=350
14521186741
%%%
```

## SoundCloud ##

```
%%% soundcloud
158208762
%%%
```

## Twitter ##

```
%%% tweet user=jack height=250 width=550
20
%%%
```

## Instagram ##

```
%%% ig width=320 height=440
qbq6fIJMVZ
%%%
```

# Usage #

See [my own blog](http:///sam.tingleff.com/) for an example project.
