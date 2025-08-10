package com.ktc.nlp;

import com.ktc.text.NodeAnnotation;

import java.util.HashMap;
import java.util.Map;

import org.apache.jena.rdf.model.Property;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

/**
 * Example use in Turtle
 * @prefix olia: <http://purl.org/olia/penn.owl#> .
 * @prefix nif: <http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#> .
 *
 * :word1 a nif:Word ;
 *        nif:anchorOf "cat" ;
 *        nif:posTag olia:NN .
 */

public enum PennTreebankPOSTag implements NodeAnnotation {
  NONE("", ""),

  EXCALAMATION("!", "exclamation mark"),
  DOUBLE_QUOTE("\"", "Quotation marks"),
  HASH("#", "hash"),
  DOLLAR("$", "dollar sign"),
  PERCENT("%", "percentage mark"),
  AMPERSAND("&", "Ampersand"),
  SINGLE_QUOTE("'", "Quotation marks"),
  OPENING_PARENTHESIS("(", "opening parenthesis"),
  CLOSING_PARENTHESIS(")", "closing parenthesis"),
  ASTERISK("*", "asterisk"),
  PLUS("+", "plus"),
  COMMA(",", "Comma"),
  DASH("-", "dash"),
  PERIOD(".", "Sentence-final punctuation"),
  SLASH("/", "forward slash"),

  COLON(":", "Colon"),
  SEMI_COLON(";", "semicolon"),
  LESS("<", "less than sign"),
  EQUALS("=", "equals"),
  GREATER(">", "greater than"),
  QUESTION("?", "question mark"),
  AT("@", "at symbol"),

  CAROT("^", "carot symbol"),
  UNDERSCORE("_", "underscore"),
  GRAVE("`", "grave accent"),

  LEFT_HANDLEBARS("{", "left curly brace"),
  PIPE("|", "vertical bar"),
  RIGHT_HANDLEBARS("}", "right curly brace"),
  TILDE("~", "tilde"),

  EURO("€", "euro sign"),
  LOW_QUOTE("‚", "Single low-9 quotation mark"),
  LATIN_F("ƒ", "Latin small letter f with hook"),
  LOW_DOUBLE_QUOTE("„", "Double low-9 quotation mark"),
  ELLIPSIS("…", "Horizontal ellipsis"),
  DAGGER("†", "dagger"),
  DOUBLE_DAGGER("‡", "double dagger"),
  CIRCUMFLEX("ˆ", "Modifier letter circumflex accent"),
  PER_MILE("‰", "per mile sign"),
  LATIN_S("Š", "Latin capital letter S with caron"),
  LEFT_ANGLE_QUOTE("‹", "Single left-pointing angle quotation"),
  LATIN_OE("Œ", "Latin capital ligature OE"),
  LATIN_Z("Ž", "Latin capital letter Z with caron"),
  LEFT_SINGLE_QUOTATION_MARK("‘", "Left single quotation mark"),
  RIGHT_SINGLE_QUOTE("’", "Right single quotation mark"),
  LEFT_DOUBLE_QUOTE("“", "Left double quotation mark"),
  RIGHT_DOUBLE_QUOTE("”", "Right double quotation mark"),
  BULLET("•", "Bullet"),
  EN_DASH("–", "En dash"),
  EM_DASH("—", "Em dash"),
  SMALL_TILDE("˜", "Small tilde"),
  TRADE_MARK_SIGN("™", "Trade mark sign"),
  RIGHT_ANGLE_QUOTE("›", "Single right-pointing angle quotation mark"),
  LATIN_SMALL_LIGATURE_OE("œ", "Latin small ligature oe"),
  LATIN_CAPITAL_LIGATURE_OE("Œ", "Latin capital ligature OE"),
  SINGLE_LEFT_POINTING_ANGLE_QUOTATION("‹", "Single left-pointing angle quotation mark"),
  LATIN_SMALL_LIGATURE_AE("æ", "Latin small letter ae"),
  LATIN_CAPITAL_LIGATURE_AE("Æ", "Latin capital letter AE"),
  BOX_DRAWINGS_LIGHT_VERTICAL_AND_RIGHT("┤", "Box drawing light vertical and right"),
  BOX_DRAWINGS_LIGHT_HORIZONTAL_AND_LEFT("┴", "Box drawing light horizontal and left"),
  BOX_DRAWINGS_LIGHT_DOWN_AND_RIGHT("┬", "Box drawing light down and right"),
  BOX_DRAWINGS_LIGHT_UP_AND_RIGHT("├", "Box drawing light up and right"),
  BOX_DRAWINGS_LIGHT_HORIZONTAL("─", "Box drawings light horizontal"),
  BOX_DRAWINGS_LIGHT_VERTICAL("│", "Box drawings light vertical"),
  BOX_DRAWINGS_LIGHT_DOWN_AND_LEFT("└", "Box drawing light down and left"),
  BOX_DRAWINGS_LIGHT_UP_AND_LEFT("┌", "Box drawing light up and left"),
  FULL_BLOCK("█", "Full block"),
  LOWER_HALF_BLOCK("▄", "Lower half block"),
  LEFT_HALF_BLOCK("▌", "Left half block"),
  RIGHT_HALF_BLOCK("▐", "Right half block"),
  UPPER_HALF_BLOCK("▀", "Upper half block"),
  LATIN_SMALL_LETTER_SHARP_S("ß", "Latin small letter sharp s - ess-zed"),
  LATIN_SMALL_LETTER_A_WITH_GRAVE("à", "Latin small letter a with grave"),
  LATIN_SMALL_LETTER_A_WITH_ACUTE("á", "Latin small letter a with acute"),
  LATIN_SMALL_LETTER_A_WITH_CIRCUMFLEX("â", "Latin small letter a with circumflex"),
  LATIN_SMALL_LETTER_A_WITH_TILDE("ã", "Latin small letter a with tilde"),
  LATIN_SMALL_LETTER_A_WITH_DIAERESIS("ä", "Latin small letter a with diaeresis"),
  LATIN_SMALL_LETTER_A_WITH_RING_ABOVE("å", "Latin small letter a with ring above"),
  LATIN_SMALL_LETTER_AE("æ", "Latin small letter ae"),
  LATIN_SMALL_LETTER_C_WITH_CEDILLA("ç", "Latin small letter c with cedilla"),
  LATIN_SMALL_LETTER_E_WITH_GRAVE("è", "Latin small letter e with grave"),
  LATIN_SMALL_LETTER_E_WITH_ACUTE("é", "Latin small letter e with acute"),
  LATIN_SMALL_LETTER_E_WITH_CIRCUMFLEX("ê", "Latin small letter e with circumflex"),
  LATIN_SMALL_LETTER_E_WITH_DIAERESIS("ë", "Latin small letter e with diaeresis"),
  LATIN_SMALL_LETTER_I_WITH_GRAVE("ì", "Latin small letter i with grave"),
  LATIN_SMALL_LETTER_I_WITH_ACUTE("í", "Latin small letter i with acute"),
  LATIN_SMALL_LETTER_I_WITH_CIRCUMFLEX("î", "Latin small letter i with circumflex"),
  LATIN_SMALL_LETTER_I_WITH_DIAERESIS("ï", "Latin small letter i with diaeresis"),
  LATIN_SMALL_LETTER_ETH("ð", "Latin small letter eth"),
  LATIN_SMALL_LETTER_N_WITH_TILDE("ñ", "Latin small letter n with tilde"),
  LATIN_SMALL_LETTER_O_WITH_GRAVE("ò", "Latin small letter o with grave"),
  LATIN_SMALL_LETTER_O_WITH_ACUTE("ó", "Latin small letter o with acute"),
  LATIN_SMALL_LETTER_O_WITH_CIRCUMFLEX("ô", "Latin small letter o with circumflex"),
  LATIN_SMALL_LETTER_O_WITH_TILDE("õ", "Latin small letter o with tilde"),
  LATIN_SMALL_LETTER_O_WITH_DIAERESIS("ö", "Latin small letter o with diaeresis"),
  MULTIPLICATION_SIGN("×", "Multiplication sign"),
  LATIN_SMALL_LETTER_O_WITH_STROKE("ø", "Latin small letter o with stroke"),
  LATIN_SMALL_LETTER_U_WITH_GRAVE("ù", "Latin small letter u with grave"),
  LATIN_SMALL_LETTER_U_WITH_ACUTE("ú", "Latin small letter u with acute"),
  LATIN_SMALL_LETTER_U_WITH_CIRCUMFLEX("û", "Latin small letter u with circumflex"),
  LATIN_SMALL_LETTER_U_WITH_DIAERESIS("ü", "Latin small letter u with diaeresis"),
  LATIN_SMALL_LETTER_Y_WITH_ACUTE("ý", "Latin small letter y with acute"),
  LATIN_SMALL_LETTER_THORN("þ", "Latin small letter thorn"),
  LATIN_SMALL_LETTER_Y_WITH_DIAERESIS("ÿ", "Latin small letter y with diaeresis"),

  SYMBOL("other_symbols", "other symbols"),

  CC 	("CC", "Coordinating conjunction"),
  CD 	("CD", "Cardinal number"),
  DT 	("DT", "Determiner"),
  EX 	("EX", "Existential there"),
  FW 	("FW", "Foreign word"),
  IN 	("IN", "Preposition or subordinating conjunction"),
  JJ 	("JJ", "Adjective"),
  JJR	("JJR", "Adjective, comparative"),
  JJS	("JJS", "Adjective, superlative"),
  LS 	("LS", "List item marker"),
  MD 	("MD", "Modal"),
  NN 	("NN", "Noun, singular or mass"),
  NNS	("NNS", "Noun, plural"),
  NNP	("NNP", "Proper noun, singular"),
  NNPS("NPS", "Proper noun, plural"),
  PDT	("PDT", "Predeterminer"),
  POS	("POS", "Possessive ending"),
  PRP	("PRP", "Personal pronoun"),
  PRP$("PRP$", "Possessive pronoun"),
  RB 	("RB", "Adverb"),
  RBR	("RBR", "Adverb, comparative"),
  RBS	("RBS", "Adverb, superlative"),
  RP 	("RP", "Particle"),
  SYM	("SYM", "Symbol"),
  TO 	("TO", "to"),
  UH 	("UH", "Interjection"),
  VB 	("VB", "Verb, base form"),
  VBD	("VBD", "Verb, past tense"),
  VBG	("VBG", "Verb, gerund or present participle"),
  VBN	("VBN", "Verb, past participle"),
  VBP	("VBP", "Verb, non-3rd person singular present"),
  VBZ	("VBZ", "Verb, 3rd person singular present"),
  WDT	("WDT", "Wh-determiner"),
  WP 	("WP", "Wh-pronoun"),
  WP$	("WP$", "Possessive wh-pronoun"),
  WRB	("WRB", "Wh-adverb");

  private static Map<String,PennTreebankPOSTag> valueMap =  new HashMap<String, PennTreebankPOSTag>();
  static Property property;
  static {
    for (PennTreebankPOSTag tag : values()) {
      valueMap.put(tag.getLiteral(), tag);
    }
    property = ResourceFactory.createProperty("http://persistence.uni-leipzig.org/nlp2rdf/ontologies/nif-core#posTag");
  }

  private final String value;
  private final String description;
  private PennTreebankPOSTag(String value, String description) {
    this.value = value;
    this.description = description;
  }

  public static PennTreebankPOSTag create(String str) {
    PennTreebankPOSTag tag = valueMap.get(str);
    if (tag == null) {
      if (str.length() != 1 ||
          (str.charAt(0) >= 'A' && str.charAt(0) <= 'Z') ||
          (str.charAt(0) >= 'a' && str.charAt(0) <= 'z')
      ) {
        throw new IllegalArgumentException(str + " is not a valid tag");
      }
      tag = SYMBOL;
    }
    return tag;
  }

  public String getLiteral() {
    return value;
  }

  public String getDescription() {
    return description;
  }

  public Resource getResource() {
    return ResourceFactory.createResource("http://purl.org/olia/penn.owl#" + getLiteral());
  }

  public Property getProperty() {
    return property;
  }
}
