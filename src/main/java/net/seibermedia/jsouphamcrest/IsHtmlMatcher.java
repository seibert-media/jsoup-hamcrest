package net.seibermedia.jsouphamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class IsHtmlMatcher extends TypeSafeDiagnosingMatcher<String> {
	private final Matcher<Element> additionalMatcher;

	private IsHtmlMatcher(Matcher<Element> additionalMatcher) {
		if (additionalMatcher == null) {
			throw new IllegalArgumentException("IsHtmlMatcher must at a successive matcher.");
		}

		this.additionalMatcher = additionalMatcher;
	}

	public static IsHtmlMatcher isHtmlMatching(Matcher<Element> additionalMatcher) {
		return new IsHtmlMatcher(additionalMatcher);
	}

	protected boolean matchesSafely(String html, Description mismatchDescription) {
		Document document = Jsoup.parse(html);
		mismatchDescription.appendText("a parsable HTML-Document");

		if (!additionalMatcher.matches(document)) {
			mismatchDescription.appendText(" that ");
			additionalMatcher.describeMismatch(document, mismatchDescription);
			return false;
		}

		return true;
	}

	public void describeTo(Description description) {
		description
				.appendText("a parsable HTML-Document that ")
				.appendDescriptionOf(additionalMatcher);
	}
}
