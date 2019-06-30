package net.seibermedia.jsouphamcrest;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.jsoup.nodes.Element;

class AdditionalMatcherDescriber {
	static void describeAdditionalMatcher(List<Matcher<Element>> additionalMatcher, Description description) {
		if (additionalMatcher.size() > 0) {
			description.appendText(" that ");
			for (int i = 0, length = additionalMatcher.size(); i < length; i++) {
				Matcher<Element> elementMatcher = additionalMatcher.get(i);
				description.appendDescriptionOf(elementMatcher);
				if (i < length - 1) {
					description.appendText(" and ");
				}
			}
		}
	}

	static boolean assertAndDescribeAdditionalMatcher(
			Element element,
			List<Matcher<Element>> additionalMatcher,
			Description mismatchDescription
	) {
		List<Matcher<Element>> mismatchingMatchers = additionalMatcher.stream().filter(
				elementMatcher -> !elementMatcher.matches(element)
		).collect(Collectors.toList());

		if (mismatchingMatchers.isEmpty()) {
			return true;
		}

		mismatchDescription.appendText(" that ");
		for (int i = 0, length = mismatchingMatchers.size(); i < length; i++) {
			Matcher<Element> elementMatcher = additionalMatcher.get(i);

			elementMatcher.describeMismatch(element, mismatchDescription);
			if (i < length - 1) {
				mismatchDescription.appendText(" and ");
			}
		}

		return false;
	}
}
