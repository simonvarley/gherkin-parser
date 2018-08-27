package com.simonvarley.cucumber;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;

import gherkin.AstBuilder;
import gherkin.Parser;
import gherkin.TokenMatcher;
import gherkin.ast.Feature;
import gherkin.ast.GherkinDocument;
import gherkin.ast.Scenario;
import gherkin.ast.ScenarioDefinition;
import gherkin.ast.Step;
import gherkin.ast.Tag;
import gherkin.util.FixJava;

import org.junit.Test;

public class GerkParsingTests {

	@Test
	public void basicAstParsingTest() throws UnsupportedEncodingException, FileNotFoundException, RuntimeException {
		String path = "src\\test\\resources\\gerktest.feature";
		String gherkin = FixJava.readReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

		Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
		TokenMatcher matcher = new TokenMatcher();

		GherkinDocument d1 = parser.parse("Feature: 1", matcher);
		GherkinDocument d2 = parser.parse("Feature: 2", matcher);
		GherkinDocument d3 = parser.parse(gherkin, matcher);

		assertEquals("1", d1.getFeature().getName());
		assertEquals("2", d2.getFeature().getName());
		assertEquals("Withdraw Cash", d3.getFeature().getName());

	}

	@Test
	public void fullFeatureAndScenarioTags() throws UnsupportedEncodingException, FileNotFoundException, RuntimeException {
		String path = "src\\test\\resources\\gerktest.feature";
		String gherkin = FixJava.readReader(new InputStreamReader(new FileInputStream(path), "UTF-8"));

		Parser<GherkinDocument> parser = new Parser<>(new AstBuilder());
		TokenMatcher matcher = new TokenMatcher();

		GherkinDocument d3 = parser.parse(gherkin, matcher);

		assertEquals("Withdraw Cash", d3.getFeature().getName());

		Feature feature = d3.getFeature();
		String description = feature.getDescription();
		assertEquals( "  This feature allows an account holder to withdraw\n  cash from an ATM. ",
				description );

		List<Tag> tags = feature.getTags();
		assertEquals( 1, tags.size() );
		assertEquals( "@F2421", tags.get(0).getName());


		List<ScenarioDefinition> parts = feature.getChildren();
		assertEquals( 3, parts.size() );

		for( ScenarioDefinition sd : parts ) {
			// Scenario, Background, Scenario Outline, etc.
			System.out.println( "Name: " + sd.getName() );
			System.out.println( "Description: " + sd.getDescription() );
			System.out.println( "Keyword: " + sd.getKeyword() );
			if (sd.getKeyword().equalsIgnoreCase("Scenario")) {
				Scenario s = (Scenario)sd;
				List<Tag> sTags = s.getTags();
				for( Tag tag : sTags ) {
					System.out.println( "Scenario tags: " + tag.getName() );
				}
			}

			List<Step> steps = sd.getSteps();
			for( Step s : steps ) {
				// Given, When, Then, And, etc
				System.out.println( "Steps Keyword: " + s.getKeyword() );
			}
		}
	}
}
