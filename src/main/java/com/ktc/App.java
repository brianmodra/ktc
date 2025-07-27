package com.ktc;

import com.ktc.actor.DocumentSystem;
import com.ktc.text.ChapterText;
import com.ktc.text.DocumentText;
import com.ktc.text.ParagraphText;
import com.ktc.text.SentenceText;
import com.ktc.text.PhraseText;
import com.ktc.text.WordText;
import com.ktc.text.PunctuationEnum;
import com.ktc.text.EnclosingPunctuationEnum;
import com.ktc.websocket.WebSocketServer;

public class App 
{
    public static void main( String[] args )
    {
        System.out.println( "Hello World!" );
        DocumentSystem system = new DocumentSystem();
        
        // Test the system with sample data
        testDocumentSystem(system);
        
        // Start the WebSocket server
        startWebSocketServer(system);
    }
    
    private static void testDocumentSystem(DocumentSystem system) {
        System.out.println("Testing DocumentSystem with sample data...");
        
        // Create WordText objects from the sentence
        java.util.ArrayList<WordText> words = new java.util.ArrayList<>();
        String[] wordArray = "The Cat sat on the mat".split(" ");
        for (int i = 0; i < wordArray.length; i++) {
            words.add(new WordText(wordArray[i]));
        }

        // Create PhraseText from words
        PhraseText phraseText = new PhraseText(words, PunctuationEnum.PERIOD, EnclosingPunctuationEnum.NONE);
        java.util.ArrayList<PhraseText> phrases = new java.util.ArrayList<>();
        phrases.add(phraseText);

        // Create SentenceText from phrases
        SentenceText sentenceText = new SentenceText(phrases);
        java.util.ArrayList<SentenceText> sentences = new java.util.ArrayList<>();
        sentences.add(sentenceText);

        // Create ParagraphText from sentences
        ParagraphText paragraphText = new ParagraphText(sentences);
        java.util.ArrayList<ParagraphText> paragraphs = new java.util.ArrayList<>();
        paragraphs.add(paragraphText);

        // Create ChapterText from paragraphs
        ChapterText chapterText = new ChapterText(paragraphs);
        java.util.ArrayList<ChapterText> chapters = new java.util.ArrayList<>();
        chapters.add(chapterText);

        //DocumentText documentText = new DocumentText();

        //system.processDocument(documentText);
        system.processSentence(sentenceText);
        
        System.out.println("DocumentSystem test completed.");
    }
    
    private static void startWebSocketServer(DocumentSystem system) {
        try {
            int port = Integer.parseInt(System.getenv().getOrDefault("WEBSOCKET_PORT", "8889"));
            System.out.println("Starting WebSocket server...");
            WebSocketServer server = new WebSocketServer(port, system);
            server.start(); // This will block and keep the server running
        } catch (Exception e) {
            System.err.println("Failed to start WebSocket server: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
