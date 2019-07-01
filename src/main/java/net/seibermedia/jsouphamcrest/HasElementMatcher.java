package net.seibermedia.jsouphamcrest;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HasElementMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final String selector;
	private final List<Matcher<Element>> additionalMatcher;

	private HasElementMatcher(String selector, List<Matcher<Element>> additionalMatcher) {
		this.selector = selector;
		this.additionalMatcher = additionalMatcher;
	}

	public static HasElementMatcher hasElement(String selector) {
		return new HasElementMatcher(selector, emptyList());
	}

	@SafeVarargs
	public static HasElementMatcher hasElement(String selector, Matcher<Element>... additionalMatcher) {
		return new HasElementMatcher(selector, Arrays.asList(additionalMatcher));
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

		mismatchDescription
				.appendText("did have an element");

		Element foundElement = elements.get(0);
		return AdditionalMatcherDescriber.assertAndDescribeAdditionalMatcher(foundElement, additionalMatcher, mismatchDescription);
	}

	public void describeTo(Description description) {
		description.appendText("has an element matching ").appendValue(this.selector);
		AdditionalMatcherDescriber.describeAdditionalMatcher(additionalMatcher, description);
	}
}
