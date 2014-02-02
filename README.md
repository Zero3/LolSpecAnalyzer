LolSpecAnalyzer
===============

LolSpecAnalyzer is a Java debugging tool/framework for analyzing the League of Legends (http://leagueoflegends.com) spectator data format.

It works by loading a decrypted and decompressed chunk ('frame') from a file and running a number of analyzers against it. Analyzers do various things, but most importantly annotates each byte of the binary data they recognize. The framework then prints out a byte map with all the collected annotations, making the reverse engineering much more fun.

You can add your own analyzer by subclassing AbstractAnalyzer in the analyzers package. Feel free to contribute!

Links
===============

Relevant discussion: https://github.com/robertabcd/lol-ob/issues/1
Decryption an decompression of chunks: https://github.com/robertabcd/lol-ob/wiki/ROFL-Container-Notes
