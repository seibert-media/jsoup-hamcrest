package net.seibermedia.jsouphamcrest;

import static net.seibermedia.jsouphamcrest.HasElementMatcher.hasElement;
import static net.seibermedia.jsouphamcrest.HasTextMatcher.hasText;
import static net.seibermedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class HasTextMatcherTest {
	@Test
	public void matchesEmptyString() {
		String html = "<b></b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText(""))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsEmptyString() {
		String html = "<b>World</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText(""))));
	}

	@Test
	public void matchesString() {
		String html = "<b>Hello</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("Hello"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsString() {
		String html = "<b>Hello</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("World"))));
	}

	@Test
	public void matchesSubMatcher() {
		String html = "<b>HELLO</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText(equalToIgnoringCase("hello")))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsSubMatcher() {
		String html = "<b>HELLO</b>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText(equalToIgnoringCase("world")))));
	}

	@Test
	public void describesCorrectlyWithValue() {
		String html = "<div><span>Bye World</span></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("span", hasText("Hello World")));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matchesSafely(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"span\" that " +
				"has text-content that is \"Hello World\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"span\" that " +
				"has text-content that was \"Bye World\""));
	}

	@Test
	public void describesCorrectlyWithSubMatcher() {
		String html = "<div><span>Bye World</span></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("span", hasText(equalToIgnoringCase("hello world"))));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matchesSafely(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"span\" that " +
				"has text-content that is equalToIgnoringCase(\"hello world\")"));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"span\" that " +
				"has text-content that was \"Bye World\""));
	}
}
