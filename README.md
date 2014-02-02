LolSpecAnalyzer
===============

LolSpecAnalyzer is a Java debugging tool/framework for analyzing the League of Legends (http://leagueoflegends.com) spectator data format.

It works by loading a decrypted and decompressed chunk from a file and running a number of analyzers against it. Analyzers do various things, but most importantly annotates each byte of the binary data they recognize. The framework then prints out a byte map with all the collected annotations, making reverse engineering the format much more fun.

You can add your own analyzer by subclassing AbstractAnalyzer in the analyzers package. Feel free to contribute!

Included analyzers
------

* TimestampAnalyzer: Heuristic for finding timestamps. They are marked 'TIME' in the byte map.
* StringAnalyzer: Heuristic for finding various kinds of strings. Characters are passed through to the byte map while length prefixes and null terminators are marked '"'. It also supports counting and printing all strings separately.

Links
===============

* Relevant discussion: https://github.com/robertabcd/lol-ob/issues/1
* Decryption and decompression of chunks: https://github.com/robertabcd/lol-ob/wiki/ROFL-Container-Notes
