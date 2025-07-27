package com.ktc.websocket;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import org.apache.pekko.actor.typed.ActorRef;
import com.ktc.actor.DocumentSystem;
import com.ktc.actor.DocumentCommand;
import com.ktc.text.DocumentBaseText;
import com.ktc.text.DocumentText;
import com.ktc.text.SentenceText;
import com.ktc.text.PhraseText;
import com.ktc.text.WordText;
import com.ktc.text.PunctuationEnum;
import com.ktc.text.EnclosingPunctuationEnum;

// JSON parsing imports
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;
import java.util.ArrayList;
import java.util.List;

public class TripleSocket extends SimpleChannelInboundHandler<TextWebSocketFrame> {
  private final DocumentSystem documentSystem;
  private final ObjectMapper objectMapper;

  public TripleSocket(DocumentSystem documentSystem) {
      this.documentSystem = documentSystem;
      this.objectMapper = new ObjectMapper();
  }

  @Override
  protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) {
      try {
          String jsonText = msg.text();
          System.out.println("Received JSON: " + jsonText);

          JsonNode rootNode = objectMapper.readTree(jsonText);
          JsonNode contentNode = rootNode.get("content");
          
          if (contentNode == null || !contentNode.isArray()) {
              System.out.println("Invalid JSON format");
              return;
          }

          for (JsonNode paragraphNode : contentNode) {
              if (!paragraphNode.isArray()) {
                continue;
              }
              ArrayList<PhraseText> phrases = new ArrayList<>();
              for (JsonNode sentenceNode : paragraphNode) {
                  String sentenceText = sentenceNode.asText();
                  ArrayList<WordText> words = new ArrayList<>();
                  for (String wordText : sentenceText.split(" ")) {
                    WordText word = new WordText(wordText);
                    words.add(word);
                  }
                  PunctuationEnum punctuation = PunctuationEnum.NONE;
                  EnclosingPunctuationEnum enclosingPunctuation = EnclosingPunctuationEnum.NONE;
                  PhraseText phrase = new PhraseText(words, punctuation, enclosingPunctuation);
                  phrases.add(phrase);
              }
              SentenceText sentence = new SentenceText(phrases);
              documentSystem.processSentence(sentence);
          }
      } catch (Exception e) {
          System.err.println("Error processing message: " + e.getMessage());
          e.printStackTrace();
      }
  }

  @Override
  public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
      if (evt == WebSocketServerProtocolHandler.ServerHandshakeStateEvent.HANDSHAKE_COMPLETE) {
          System.out.println("WebSocket handshake completed");
      } else {
          super.userEventTriggered(ctx, evt);
      }
  }
}
