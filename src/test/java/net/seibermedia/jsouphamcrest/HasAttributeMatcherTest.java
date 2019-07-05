package net.seibermedia.jsouphamcrest;

import static net.seibermedia.jsouphamcrest.HasAttributeMatcher.hasAttribute;
import static net.seibermedia.jsouphamcrest.HasElementMatcher.hasElement;
import static net.seibermedia.jsouphamcrest.IsHtmlMatcher.isHtmlMatching;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.equalToIgnoringCase;
import static org.hamcrest.Matchers.is;

import org.hamcrest.StringDescription;
import org.junit.Test;

public class HasAttributeMatcherTest {
	@Test
	public void matchesHasAttribute() {
		String html = "<div><input required /></div>";
		assertThat(html, isHtmlMatching(hasElement("input", hasAttribute("required"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsHasAttribute() {
		String html = "<div><input required /></div>";
		assertThat(html, isHtmlMatching(hasElement("input", hasAttribute("size"))));
	}

	@Test
	public void matchesAttributeValue() {
		String html = "<div><input size='5' /></div>";
		assertThat(html, isHtmlMatching(hasElement("input", hasAttribute("size", "5"))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsAttributeValue() {
		String html = "<div><input size='5' /></div>";
		assertThat(html, isHtmlMatching(hasElement("input", hasAttribute("size", "7"))));
	}

	@Test
	public void matchesAttributeValueMatcher() {
		String html = "<div><input title='Telephone' /></div>";
		assertThat(html, isHtmlMatching(hasElement("input",
				hasAttribute("title", equalToIgnoringCase("telephone")))));
	}

	@Test(expected = AssertionError.class)
	public void rejectsAttributeValueMatcher() {
		String html = "<div><input title='Telephone' /></div>";
		assertThat(html, isHtmlMatching(hasElement("input",
				hasAttribute("title", equalToIgnoringCase("mobile")))));
	}

	@Test
	public void describesCorrectly() {
		String html = "<div><input size='5' /></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("input", hasAttribute("required")));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"input\" that " +
				"has attribute \"required\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"input\" that " +
				"did not have an attribute \"required\""));
	}

	@Test
	public void describesCorrectlyWithValue() {
		String html = "<div><input size='5' /></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("input", hasAttribute("size", "7")));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"input\" that " +
				"has attribute \"size\" that is \"7\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"input\" that " +
				"did have an attribute \"size\" that was \"5\""));
	}

	@Test
	public void describesCorrectlyWithSubMatcher() {
		String html = "<div><a href='http://localhost:5000/bye/world'>Hello World</a></div>";
		IsHtmlMatcher matcher = isHtmlMatching(hasElement("a", hasAttribute("href", endsWith("/hello/world"))));

		StringDescription expectedDescription = new StringDescription();
		matcher.describeTo(expectedDescription);

		StringDescription mismatchDescription = new StringDescription();
		boolean matches = matcher.matches(html, mismatchDescription);

		assertThat(matches, is(false));
		assertThat(expectedDescription.toString(), is("a parsable HTML-Document that has an element matching \"a\" that " +
				"has attribute \"href\" that is a string ending with \"/hello/world\""));
		assertThat(mismatchDescription.toString(), is("a parsable HTML-Document that did have an element matching \"a\" that " +
				"did have an attribute \"href\" that was \"http://localhost:5000/bye/world\""));
	}
}
