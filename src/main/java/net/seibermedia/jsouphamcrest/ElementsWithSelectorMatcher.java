package net.seibermedia.jsouphamcrest;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ElementsWithSelectorMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final String selector;
	private final List<Matcher<Element>> additionalMatcher;

	private ElementsWithSelectorMatcher(String selector, List<Matcher<Element>> additionalMatcher) {
		this.selector = selector;
		this.additionalMatcher = additionalMatcher;
	}

	public static ElementsWithSelectorMatcher elementsWithSelector(String selector) {
		return new ElementsWithSelectorMatcher(selector, emptyList());
	}

	@SafeVarargs
	public static ElementsWithSelectorMatcher elementsWithSelector(String selector, Matcher<Element>... additionalMatcher) {
		return new ElementsWithSelectorMatcher(selector, Arrays.asList(additionalMatcher));
	}

	protected boolean matchesSafely(Element element, Description mismatchDescription) {
		Elements elements = element.select(selector);
		if (elements.size() == 0) {
			mismatchDescription
					.appendText("did not have an element matching ")
					.appendValue(selector);

			return false;
		}

		return true;
	}

	public void describeTo(Description description) {
		description.appendText("has an element matching ").appendValue(this.selector);
		AdditionalMatcherDescriber.describeAdditionalMatcher(additionalMatcher, description);
	}
}
