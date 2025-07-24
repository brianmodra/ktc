package com.ktc.structure;

import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.Resource;
import com.ktc.text.DocumentText;

public class DocumentStructure implements DocumentStructureInterface {
  Resource documentResource;
  
  public DocumentStructure(Model model, DocumentText documentText) {
    documentResource = DocumentStructureInterface.createDocument(model, documentText);
  }

  public Resource getDocumentResource() {
    return documentResource;
  }
}
