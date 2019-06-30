package net.seibermedia.jsouphamcrest;

import static net.seibermedia.jsouphamcrest.ElementsWithSelectorMatcher.elementsWithSelector;
import static net.seibermedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class ElementsWithSelectorMatcherTest {
	@Test
	public void matchesTagSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(elementsWithSelector("b")));
		assertThat(html, isHtmlMatching(not(elementsWithSelector("a"))));
	}

	@Test
	public void matchesDeepSelector() {
		String html = "<div><b><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(elementsWithSelector("div > b > i")));
		assertThat(html, isHtmlMatching(not(elementsWithSelector("div > a > i"))));
	}

	@Test
	public void matchesIdSelector() {
		String html = "<div><b id='thebold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(elementsWithSelector("div #thebold")));
		assertThat(html, isHtmlMatching(not(elementsWithSelector("div #thewild"))));
	}

	@Test
	public void matchesClassSelector() {
		String html = "<div><b class='not so bold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(not(elementsWithSelector("div b.very.bold"))));
	}

	@Test
	public void describesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(elementsWithSelector("i"));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matchesSafely(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"i\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did not have an element matching \"i\""));
	}

	@Test
	public void describesMultipleMismatchesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(
				elementsWithSelector("i"),
				elementsWithSelector("a")
		);
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matchesSafely(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document " +
				"that has an element matching \"i\" " +
				"and has an element matching \"a\""));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document " +
				"that did not have an element matching \"i\" " +
				"and did not have an element matching \"a\""));
	}

	@Test
	public void describesPartialMismatchesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(
				elementsWithSelector("i"),
				elementsWithSelector("b")
		);
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matchesSafely(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document " +
				"that has an element matching \"i\" " +
				"and has an element matching \"b\""));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document " +
				"that did not have an element matching \"i\""));
	}

}
