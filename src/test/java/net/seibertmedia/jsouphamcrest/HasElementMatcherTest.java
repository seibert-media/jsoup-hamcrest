package net.seibertmedia.jsouphamcrest;

import static net.seibertmedia.jsouphamcrest.HasElementMatcher.hasElement;
import static net.seibertmedia.jsouphamcrest.HasTextMatcher.hasText;
import static net.seibertmedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class HasElementMatcherTest {
	@Test
	public void matchesTagSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsTagSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("a")));
	}

	@Test
	public void matchesTextSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b:contains(Hello World)")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsTextSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b:contains(Bye World)")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsMultipleElements() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b")));
	}

	@Test
	public void matchesDeepSelector() {
		String html = "<div><b><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div > b > i")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsDeepSelector() {
		String html = "<div><b><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div > a > i")));
	}

	@Test
	public void matchesIdSelector() {
		String html = "<div><b id='thebold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div #thebold")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsIdSelector() {
		String html = "<div><b id='thebold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div #thewild")));
	}

	@Test
	public void matchesClassSelector() {
		String html = "<div><b class='not so bold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div b.bold")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsClassSelector() {
		String html = "<div><b class='not so bold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElement("div b.very.bold")));
	}

	@Test
	public void acceptsSubMatcher() {
		String html = "<div><b>Hello</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("Hello"))));
	}

	@Test(expected = AssertionError.class)
	public void evaluatesSubMatcher() {
		String html = "<div><b>Hello</b></div>";
		assertThat(html, isHtmlMatching(hasElement("b", hasText("World"))));
	}

	@Test
	public void describesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(hasElement("i"));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"i\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did not have an element matching \"i\""));
	}

	@Test
	public void describesMultipleMismatchesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(allOf(
				hasElement("i"),
				hasElement("a")
		));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that " +
				"(has an element matching \"i\" and has an element matching \"a\")"));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that " +
				"has an element matching \"i\" did not have an element matching \"i\""));
	}

	@Test
	public void describesPartialMismatchesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(allOf(
				hasElement("i"),
				hasElement("b")
		));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that " +
				"(has an element matching \"i\" and has an element matching \"b\")"));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document " +
				"that has an element matching \"i\" did not have an element matching \"i\""));
	}

	@Test
	public void describesMultipleMatchRejectionCorrectly() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("b"));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that " +
				"has an element matching \"b\""));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have (<2>) elements " +
				"while expecting only 1 matching \"b\""));
	}

}
