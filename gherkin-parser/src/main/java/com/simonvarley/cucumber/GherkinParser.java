package com.simonvarley.cucumber;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.TokenMatcher;
import gherkin.ast.GherkinDocument;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class GherkinParser {

    private GherkinDocument bdd;

    public GherkinParser(String path) {
        String gherkin = "";
        try {
            gherkin = FileUtils.readFileToString( new File( path ), StandardCharsets.UTF_8 );
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
            TokenMatcher matcher = new TokenMatcher();
            bdd = parser.parse(gherkin, matcher);
        }
    }

    public String getName() {
        return bdd.getFeature().getName();
    }

}
