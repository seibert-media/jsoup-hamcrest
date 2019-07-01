package net.seibermedia.jsouphamcrest;

import static java.util.Collections.emptyList;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class HasElementsMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final String selector;
	private final List<Matcher<Collection<? extends Element>>> additionalMatcher;

	private HasElementsMatcher(String selector, List<Matcher<Collection<? extends Element>>> additionalMatcher) {
		this.selector = selector;
		this.additionalMatcher = additionalMatcher;
	}

	public static HasElementsMatcher hasElements(String selector) {
		return new HasElementsMatcher(selector, emptyList());
	}

	@SafeVarargs
	public static HasElementsMatcher hasElements(String selector, Matcher<Collection<? extends Element>>... additionalMatcher) {
		return new HasElementsMatcher(selector, Arrays.asList(additionalMatcher));
	}

	protected boolean matchesSafely(Element element, Description mismatchDescription) {
		Elements elements = element.select(selector);
		if (elements.size() == 0) {
			mismatchDescription
					.appendText("did not have elements matching ")
					.appendValue(selector);

			return false;
		}

		mismatchDescription
				.appendText("did have elements");

		return AdditionalMatcherDescriber.assertAndDescribeAdditionalMatcher(elements, additionalMatcher, mismatchDescription);
	}

	public void describeTo(Description description) {
		description.appendText("has elements matching ").appendValue(this.selector);
		AdditionalMatcherDescriber.describeAdditionalMatcher(additionalMatcher, description);
	}
}
