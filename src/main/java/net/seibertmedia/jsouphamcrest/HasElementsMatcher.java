package net.seibertmedia.jsouphamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HasElementsMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final String selector;
	private final Matcher<? extends Iterable<? extends Element>> additionalMatcher;

	private HasElementsMatcher(String selector, Matcher<? extends Iterable<? extends Element>> additionalMatcher) {
		this.selector = selector;
		this.additionalMatcher = additionalMatcher;
	}

	public static HasElementsMatcher hasElements(String selector) {
		return new HasElementsMatcher(selector, null);
	}

	public static HasElementsMatcher hasElements(String selector, Matcher<? extends Iterable<? extends Element>> additionalMatcher) {
		return new HasElementsMatcher(selector, additionalMatcher);
	}

	protected boolean matchesSafely(Element element, Description mismatchDescription) {
		Elements elements = element.select(selector);
		if (elements.size() == 0) {
			mismatchDescription
					.appendText("did not have elements matching ")
					.appendValue(selector);

			return false;
		}


		if (additionalMatcher != null && !additionalMatcher.matches(elements)) {
			mismatchDescription.appendText("did have elements that ");
			additionalMatcher.describeMismatch(elements, mismatchDescription);
			return false;
		}

		return true;
	}

	public void describeTo(Description description) {
		description.appendText("has elements matching ").appendValue(this.selector);

		if (additionalMatcher != null) {
			description.appendText(" that ")
					.appendDescriptionOf(additionalMatcher);
		}
	}
}
