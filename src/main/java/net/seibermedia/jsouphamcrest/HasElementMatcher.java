package net.seibermedia.jsouphamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HasElementMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final String selector;
	private final Matcher<Element> additionalMatcher;

	private HasElementMatcher(String selector, Matcher<Element> additionalMatcher) {
		this.selector = selector;
		this.additionalMatcher = additionalMatcher;
	}

	public static HasElementMatcher hasElement(String selector) {
		return new HasElementMatcher(selector, null);
	}

	public static HasElementMatcher hasElement(String selector, Matcher<Element> additionalMatcher) {
		return new HasElementMatcher(selector, additionalMatcher);
	}

	protected boolean matchesSafely(Element element, Description mismatchDescription) {
		Elements elements = element.select(selector);
		if (elements.size() == 0) {
			mismatchDescription
					.appendText("did not have an element matching ")
					.appendValue(selector);

			return false;
		}

		if (elements.size() > 1) {
			mismatchDescription
					.appendText("did have (").appendValue(elements.size()).appendText(") elements while expecting only 1 matching ")
					.appendValue(selector);

			return false;
		}

		Element matchingElement = elements.get(0);
		if (additionalMatcher != null && !additionalMatcher.matches(matchingElement)) {
			mismatchDescription
					.appendText("did have an element matching ")
					.appendValue(selector)
					.appendText(" that ");

			additionalMatcher.describeMismatch(matchingElement, mismatchDescription);
			return false;
		}

		return true;
	}

	public void describeTo(Description description) {
		description
				.appendText("has an element matching ")
				.appendValue(this.selector);

		if (additionalMatcher != null) {
			description.appendText(" that ")
					.appendDescriptionOf(additionalMatcher);
		}
	}
}
