package net.seibermedia.jsouphamcrest;

import static net.seibermedia.jsouphamcrest.HasElementMatcher.hasElement;
import static net.seibermedia.jsouphamcrest.HasTextContentMatcher.hasTextContent;
import static net.seibermedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;

import org.junit.Test;

public class HasTextContentMatcherTest {
	@Test
	public void matchesEmptyString() {
		String html = "<b></b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasTextContent(""))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsEmptyString() {
		String html = "<b>World</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasTextContent(""))));
	}

	@Test
	public void matchesString() {
		String html = "<b>Hello</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasTextContent("Hello"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsString() {
		String html = "<b>Hello</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasTextContent("World"))));
	}

	@Test
	public void matchesSubMatcher() {
		String html = "<b>HELLO</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasTextContent(equalToIgnoringCase("hello")))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsSubMatcher() {
		String html = "<b>HELLO</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasTextContent(equalToIgnoringCase("world")))));
	}
}
