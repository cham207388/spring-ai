package com.abcham.springai.rag;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.tika.TikaDocumentReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class HRPolicyLoader {

    private final VectorStore vectorStore;

    private final List<String> loadedDocumentIds = new ArrayList<>();

    @Value("classpath:HR_Policies.pdf")
    Resource policyFile;

    public HRPolicyLoader(VectorStore vectorStore) {

        this.vectorStore = vectorStore;
    }

    @PostConstruct
    public void loadPDF() {
        log.info("Loading HR policies...");

        List<Document> docs = docs();
        TextSplitter textSplitter = TokenTextSplitter.builder()
                        .withChunkSize(200)
                        .withMaxNumChunks(400)
                        .build();
        List<Document> splitDocs = textSplitter.split(docs);
        vectorStore.add(splitDocs);
        splitDocs.forEach(doc -> loadedDocumentIds.add(doc.getId()));
    }

    @PreDestroy
    public void destroy() {
        log.info("Destroying HR policies...");

        if (!loadedDocumentIds.isEmpty()) {
            vectorStore.delete(loadedDocumentIds);
        }
    }

    private List<Document> docs() {
        TikaDocumentReader tikaDocumentReader = new TikaDocumentReader(policyFile);
        return tikaDocumentReader.get();
    }

}