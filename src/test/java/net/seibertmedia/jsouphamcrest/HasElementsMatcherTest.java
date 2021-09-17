package net.seibertmedia.jsouphamcrest;

import static net.seibertmedia.jsouphamcrest.HasElementsMatcher.hasElements;
import static net.seibertmedia.jsouphamcrest.HasTextMatcher.hasText;
import static net.seibertmedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class HasElementsMatcherTest {
	@Test
	public void matchesTagSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsTagSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("a")));
	}

	@Test
	public void matchesTextSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b:contains(Hello World)")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsTextSelector() {
		String html = "<div><b>Hello World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b:contains(Bye World)")));
	}

	@Test
	public void matchesDeepSelector() {
		String html = "<div><b><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElements("div > b > i")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsDeepSelector() {
		String html = "<div><b><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElements("div > a > i")));
	}

	@Test
	public void matchesIdSelector() {
		String html = "<div><b id='thebold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElements("div #thebold")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsIdSelector() {
		String html = "<div><b id='thebold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElements("div #thewild")));
	}

	@Test
	public void matchesClassSelector() {
		String html = "<div><b class='not so bold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElements("div b.bold")));
	}

	@Test(expected = AssertionError.class)
	public void rejectsClassSelector() {
		String html = "<div><b class='not so bold'><i>Hello World</i></b></div>";
		assertThat(html, isHtmlMatching(hasElements("div b.very.bold")));
	}

	@Test
	public void matchesMultipleSubSelectors() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b", contains(
				hasText("Hello"),
				hasText("World")
		))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsMultipleSubSelectors() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b", contains(
				hasText("World"),
				hasText("Hello")
		))));
	}

	@Test
	public void matchesMultipleElements() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b", hasSize(2))));
	}

	@Test(expected = AssertionError.class)
	public void evaluatedMultipleElementsSubMatcher() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		assertThat(html, isHtmlMatching(hasElements("b", hasSize(3))));
	}

	@Test
	public void describesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(hasElements("i"));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has elements matching \"i\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did not have elements matching \"i\""));
	}

	@Test
	public void describesMultipleMismatchesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(allOf(
				hasElements("i"),
				hasElements("a")
		));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that " +
				"(has elements matching \"i\" and has elements matching \"a\")"));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document " +
				"that has elements matching \"i\" did not have elements matching \"i\""));
	}

	@Test
	public void describesPartialMismatchesCorrectly() {
		String html = "<div><b>Hello World</b></div>";

		IsHtmlMatcher matcher = isHtmlMatching(allOf(
				hasElements("i"),
				hasElements("b")
		));
		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that " +
				"(has elements matching \"i\" and has elements matching \"b\")"));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document " +
				"that has elements matching \"i\" did not have elements matching \"i\""));
	}

	@Test
	public void describesSubMatcherRejectionCorrectly() {
		String html = "<div><b>Hello</b> <b>World</b></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElements("b", hasSize(3)));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that " +
				"has elements matching \"b\" that " +
				"a collection with size <3>")); // TODO make text nicer

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have elements that " +
				"collection size was <2>"));
	}
}
