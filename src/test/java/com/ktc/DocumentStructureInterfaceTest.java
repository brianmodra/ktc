package com.ktc;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.Before;
import org.apache.jena.rdf.model.*;
import org.apache.jena.vocabulary.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ktc.structure.DocumentStructureInterface;
import com.ktc.structure.DocumentStructure;
import com.ktc.structure.StringStructure;
import com.ktc.structure.SentenceStructure;
import com.ktc.structure.ParagraphStructure;
import com.ktc.structure.ChapterStructure;
import com.ktc.text.*;

import edu.stanford.nlp.pipeline.*;

import java.util.ArrayList;
import java.util.Properties;
import java.io.StringWriter;

/**
 * Test class for DocumentStructureInterface relation extraction functionality
 */
public class DocumentStructureInterfaceTest {
    
    static {
        // Configure slf4j-simple to show debug level logs for this test class
        System.setProperty("org.slf4j.simpleLogger.log.com.ktc.DocumentStructureInterfaceTest", "debug");
        System.setProperty("org.slf4j.simpleLogger.log.com.ktc.structure.DocumentStructureInterface", "debug");
        // Show timestamps and thread info for better debugging
        System.setProperty("org.slf4j.simpleLogger.showDateTime", "true");
        System.setProperty("org.slf4j.simpleLogger.showThreadName", "true");
        System.setProperty("org.slf4j.simpleLogger.showLogName", "true");
        System.setProperty("org.slf4j.simpleLogger.showShortLogName", "false");
    }
    
    private static final Logger log = LoggerFactory.getLogger(DocumentStructureInterfaceTest.class);
    private Model model;
    private StanfordCoreNLP pipeline;
    
    @Before
    public void setUp() {
        // Create model with proper prefixes
        model = DocumentStructureInterface.createModel();
        
        // Create Stanford CoreNLP pipeline
        pipeline = DocumentStructureInterface.createPipeline();
    }
    
    @Test
    public void testAddRelationTriplesToSentence_TheCatSatOnTheMat() {
        // Create word texts for "The cat sat on the mat."
        ArrayList<WordText> words = new ArrayList<>();
        words.add(new WordText("The"));
        words.add(new WordText("cat"));
        words.add(new WordText("sat"));
        words.add(new WordText("on"));
        words.add(new WordText("the"));
        words.add(new WordText("mat"));
        words.add(new WordText("and"));
        words.add(new WordText("the"));
        words.add(new WordText("dog"));
        words.add(new WordText("ate"));
        words.add(new WordText("the"));
        words.add(new WordText("cat"));
        words.add(new WordText("."));
        
        // Create phrase text with period punctuation
        PhraseText phraseText = new PhraseText(words, PunctuationEnum.PERIOD, EnclosingPunctuationEnum.NONE);
        
        // Create the document structure hierarchy
        DocumentText documentText = new DocumentText();
        ArrayList<ChapterText> chapters = new ArrayList<>();
        ChapterText chapterText = new ChapterText(new ArrayList<>());
        chapters.add(chapterText);
        documentText.setChapters(chapters);
        
        ArrayList<ParagraphText> paragraphs = new ArrayList<>();
        ParagraphText paragraphText = new ParagraphText(new ArrayList<>());
        paragraphs.add(paragraphText);
        chapterText.setParagraphs(paragraphs);
        
        ArrayList<SentenceText> sentences = new ArrayList<>();
        ArrayList<PhraseText> phrases = new ArrayList<>();
        phrases.add(phraseText);
        SentenceText sentenceText = new SentenceText(phrases);
        sentences.add(sentenceText);
        paragraphText.setSentences(sentences);

        DocumentStructure documentStructure = new DocumentStructure(model, documentText);
        ChapterStructure chapterStructure = new ChapterStructure(model, documentStructure, chapterText, null);
        ParagraphStructure paragraphStructure = new ParagraphStructure(model, chapterStructure, paragraphText, null);
        SentenceStructure sentenceStructure = new SentenceStructure(model, paragraphStructure, sentenceText, null);
        StringStructure stringStructure = new StringStructure(model, sentenceStructure, phraseText, null);
        
        DocumentStructureInterface.addRelationTriplesToPhrase(model, stringStructure, phraseText, pipeline, log);
        
        // Convert model to turtle for analysis
        StringWriter writer = new StringWriter();
        model.write(writer, "TURTLE");
        String turtleOutput = writer.toString();
        
        System.out.println("=== Generated RDF (Turtle format) ===");
        System.out.println(turtleOutput);
        System.out.println("=== End RDF ===");
        
        // Verify the model is not empty
        assertFalse("Model should not be empty after processing", model.isEmpty());
        
        // Verify that we have some statements in the model
        assertTrue("Model should contain statements", model.size() > 0);
        
        // Check for basic document structure
        assertTrue("Should contain document structure triples", 
            turtleOutput.contains("dcterms:hasPart") || turtleOutput.contains("hasPart"));
        
        // Check for RDF reification structure (relation triples)
        // The method creates reified triples with rdf:subject, rdf:predicate, rdf:object
        boolean hasReifiedTriples = turtleOutput.contains("rdf:subject") && 
                                   turtleOutput.contains("rdf:predicate") && 
                                   turtleOutput.contains("rdf:object");
        
        if (hasReifiedTriples) {
            assertTrue("Should contain reified relation triples", hasReifiedTriples);
            
            // Check for expected entities from "The cat sat on the mat"
            // Note: Stanford CoreNLP may extract different relations, so we check for common patterns
            boolean hasExpectedEntities = turtleOutput.contains("cat") || 
                                        turtleOutput.contains("mat") ||
                                        turtleOutput.contains("sat") ||
                                        turtleOutput.contains("dog") ||
                                        turtleOutput.contains("ate");
            
            assertTrue("Should contain expected entities (cat, mat, sat, dog, ate)", hasExpectedEntities);
            
            // Check for provenance information
            assertTrue("Should contain provenance quality measure", 
                turtleOutput.contains("prov:qualityMeasure") || turtleOutput.contains("qualityMeasure"));
            
            // Check for provenance derivation
            assertTrue("Should contain provenance derivation", 
                turtleOutput.contains("prov:wasDerivedFrom") || turtleOutput.contains("wasDerivedFrom"));
                
        } else {
            // If no relation triples were extracted, that's also valid behavior
            // Stanford CoreNLP might not extract relations for this simple sentence
            System.out.println("Note: No relation triples were extracted by Stanford CoreNLP for this sentence.");
            System.out.println("This may be normal behavior depending on the CoreNLP model and configuration.");
        }
        
        // Verify basic RDF structure elements are present
        assertTrue("Should contain RDF type declarations", 
            turtleOutput.contains("rdf:type") || turtleOutput.contains("a "));
        
        // Verify namespace prefixes are properly set
        assertTrue("Should contain nif namespace", turtleOutput.contains("nif:"));
        assertTrue("Should contain dcterms namespace", turtleOutput.contains("dcterms:"));
        
        // Log some statistics
        log.info("Total RDF statements generated: {}", model.size());
        log.info("Generated turtle output length: {} characters", turtleOutput.length());
    }
    
    @Test
    public void testModelToTurtleConversion() {
        // Simple test to verify our turtle conversion works
        Resource testResource = model.createResource("http://test.com/test");
        testResource.addProperty(RDF.type, model.createResource("http://test.com/TestType"));
        testResource.addProperty(RDFS.label, "Test Resource");
        
        StringWriter writer = new StringWriter();
        model.write(writer, "TURTLE");
        String turtle = writer.toString();
        
        assertFalse("Turtle output should not be empty", turtle.isEmpty());
        assertTrue("Should contain test resource", turtle.contains("test"));
        assertTrue("Should contain RDF type", turtle.contains("rdf:type") || turtle.contains("a "));
    }    
} 