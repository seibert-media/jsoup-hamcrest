package net.seibermedia.jsouphamcrest;

import static net.seibermedia.jsouphamcrest.HasClassMatcher.hasClass;
import static net.seibermedia.jsouphamcrest.HasClassMatcher.hasClasses;
import static net.seibermedia.jsouphamcrest.HasElementMatcher.hasElement;
import static net.seibermedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class HasClassMatcherTest {
	@Test
	public void matchesClass() {
		String html = "<div><br class='hello world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClass("hello"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsClass() {
		String html = "<div><br class='hello world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClass("bye"))));
	}

	@Test
	public void matchesClassWithSubMatcher() {
		String html = "<div><br class='fa far-world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClass(endsWith("-world")))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsClassWithSubMatcher() {
		String html = "<div><br class='fa far-world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClass(endsWith("-icon")))));
	}

	@Test
	public void matchesClasses() {
		String html = "<div><br class='hello world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClasses("hello", "world"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsClasses() {
		String html = "<div><br class='hello world'></div>";
		assertThat(html, isHtmlMatching(hasElement("br", hasClasses("bye", "world"))));
	}

	@Test
	public void matchesClassesWithSubMatcher() {
		String html = "<div><br class='fa far-world'></div>";

		//noinspection unchecked
		assertThat(html, isHtmlMatching(hasElement("br", hasClasses(containsInAnyOrder(
				equalTo("fa"),
				endsWith("-world")))
		)));
	}

	@Test(expected = AssertionError.class)
	public void rejectsClassesWithSubMatcher() {
		String html = "<div><br class='fa far-world'></div>";

		//noinspection unchecked
		assertThat(html, isHtmlMatching(hasElement("br", hasClasses(containsInAnyOrder(
				equalTo("fa"), endsWith("-icon")))
		)));
	}

	@Test
	public void describesHasClassCorrectly() {
		String html = "<div><br class='hello world'></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("br", hasClass("bye")));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription
				.toString(), is("a parsable HTML-Document that has an element matching \"br\" that has a class \"bye\""));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"br\" " +
				"that had no class \"bye\""));
	}

	@Test
	public void describesHasClassWithSubMatcherCorrectly() {
		String html = "<div><br class='fa far-world'></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("br", hasClass(endsWith("-icon"))));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"br\" that " +
				"has a class a string ending with \"-icon\""));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"br\" that " +
				"had no class a string ending with \"-icon\""));
	}

	@Test
	public void describesHasClassesCorrectly() {
		String html = "<div><br class='hello world'></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("br", hasClasses("bye", "world")));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"br\" " +
				"that has classes iterable with items [\"bye\", \"world\"] in any order"));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"br\" " +
				"that has classes that not matched: \"hello\""));
	}

	@Test
	public void describesHasClassesWithSubMatcherCorrectly() {
		String html = "<div><br class='fa far-world'></div>";

		//noinspection unchecked
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("br", hasClasses(containsInAnyOrder(
				equalTo("fa"), endsWith("-icon")))
		));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"br\" " +
				"that has classes iterable with items [\"fa\", a string ending with \"-icon\"] in any order"));

		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"br\" " +
				"that has classes that not matched: \"far-world\""));
	}
}
