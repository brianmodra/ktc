# KTC

Note! This is a POC.

As such, it is possible I have checked in half working code, or forgotten to check in something.
I'm in the middle of integrating with a tripple extractor written in Python, and adding a PHP/Vue/Typescript simple web front end.
None of that is checked in yet. The Java-based OpenIE is old and not as nimble as the newer one in Python, but I want to use the Java
Pekko and Jena back end for scalability, using other libraries in other languages where appropriate. Connection to Python
will be via an actor that communicates to the Python program.

## Projects

### The Java service is set up as a Maven project:
```
mvn compile
mvn test-compile
mvn exec:java -Dexec.mainClass="com.ktc.App"
```

## Stanford CoreNLP
I installed the source for the library from https://github.com/stanfordnlp/CoreNLP
Then compiled it for Java 17

Then downloaded some models from https://stanfordnlp.github.io/CoreNLP/download.html

Then issued these commands to get the JARs into my maven repository:
```
mvn install:install-file \
  -Dfile=stanford-corenlp-4.5.10.jar \
  -DgroupId=edu.stanford.nlp \
  -DartifactId=stanford-corenlp \
  -Dversion=4.5.10 \
  -Dpackaging=jar

mvn install:install-file \
  -Dfile=stanford-corenlp-models-current.jar \
  -DgroupId=edu.stanford.nlp \
  -DartifactId=stanford-corenlp-models-current \
  -Dversion=4.5.10 \
  -Dclassifier=models-english \
  -Dpackaging=jar

mvn install:install-file \
  -Dfile=stanford-corenlp-4.5.10-models-english.jar \
  -DgroupId=edu.stanford.nlp \
  -DartifactId=stanford-corenlp \
  -Dversion=4.5.10 \
  -Dclassifier=models-english \
  -Dpackaging=jar

mvn install:install-file \
  -Dfile=stanford-corenlp-4.5.10-models-english-kbp.jar \
  -DgroupId=edu.stanford.nlp \
  -DartifactId=stanford-corenlp \
  -Dversion=4.5.10 \
  -Dclassifier=models-english-kbp \
  -Dpackaging=jar
``` 

### The PHP web service is in the ktc-web directory. It is a Codeignite 4 / Vue project.
It uses vite, so you need to start that also:
```
npm run dev
```
Then to start the  web service:
```
php spark serve --port 8888
```

### The Python project is in the triple_extractor directory

## Documentation of Standard Vocabularies Used in the KTC Project

This project now uses established semantic web vocabularies where possible,
falling back to custom "ktc:" namespace only when standard semantics don't match.

## STANDARD VOCABULARIES USED:

- RDF (http://www.w3.org/1999/02/22-rdf-syntax-ns#)
    - rdf:type - for typing resources
    - rdf:subject, rdf:predicate, rdf:object - for reified relation triples

- RDFS (http://www.w3.org/2000/01/rdf-schema#) 
    - rdfs:label - for text content of sentences and words

- Dublin Core Terms (http://purl.org/dc/terms/)
    - dcterms:hasPart - containment relationships (document->chapter, chapter->paragraph, etc.)
    - dcterms:isPartOf - reverse containment (paragraph isPartOf chapter, etc.)
    
- Dublin Core Types (http://purl.org/dc/dcmitype/)
    - dcmitype:Text - for document type classification
    
- PROV (http://www.w3.org/ns/prov#)
    - prov:wasDerivedFrom - links relation triples to their source sentences
    - prov:qualityMeasure - confidence scores for extracted relations
    
- NIF Core (http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#)
    - nif:nextChapter, nif:nextParagraph, nif:nextSentence, nif:nextWord
    (Text sequencing and ordering properties for NLP processing)
    - nif:Chapter, nif:Paragraph, nif:Sentence, nif:Word
    (Document structure types for text annotation and processing)

- OLiA http://purl.org/olia/olia.owl# includes a class hierarchy for punctuation as part of its Part-of-Speech ontology.
    - olia:Punctuation, olia:FullStop, olia:QuestionMark, olia:ExclamationMark
    e.g.
```
    a nif:Word , olia:QuestionMark ;
    nif:anchorOf "?" ;
```
    
## CUSTOM VOCABULARY (ktc: namespace) RETAINED FOR:

- Domain-Specific Relations:
    - ktc:relation/* - extracted semantic relations (domain-specific)
    
- Entity URIs:
    - ktc:entity/* - named entities from text (domain-specific)
    
## BENEFITS OF STANDARD VOCABULARIES:
 
- Interoperability with other semantic web datasets
- Established semantics understood by tools and reasoners
- Better SPARQL query compatibility across systems
- Follows semantic web best practices
- Enables reasoning with standard ontologies
 
## EXAMPLE SPARQL QUERIES WITH STANDARD VOCABULARIES:

### Find all parts of a document using Dublin Core
```
SELECT ?part WHERE {
  ?document dcterms:hasPart+ ?part .
}
```

### Find high-confidence relations using PROV
```
SELECT ?triple WHERE {
  ?triple prov:qualityMeasure ?confidence .
  FILTER(?confidence > 0.8)
}
```

### Get text content using RDFS
```
SELECT ?text WHERE {
  ?sentence rdfs:label ?text .
}
```
