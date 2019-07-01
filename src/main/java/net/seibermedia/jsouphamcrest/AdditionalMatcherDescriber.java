package net.seibermedia.jsouphamcrest;

import java.util.List;
import java.util.stream.Collectors;

import org.hamcrest.Description;
import org.hamcrest.Matcher;

class AdditionalMatcherDescriber {
	static void describeAdditionalMatcher(List<? extends Matcher<?>> additionalMatcher, Description description) {
		if (additionalMatcher.size() > 0) {
			description.appendText(" that ");
			for (int i = 0, length = additionalMatcher.size(); i < length; i++) {
				Matcher elementMatcher = additionalMatcher.get(i);
				description.appendDescriptionOf(elementMatcher);
				if (i < length - 1) {
					description.appendText(" and ");
				}
			}
		}
	}

	static <T> boolean assertAndDescribeAdditionalMatcher(
			T element,
			List<? extends Matcher<T>> additionalMatcher,
			Description mismatchDescription
	) {
		List<Matcher> mismatchingMatchers = additionalMatcher.stream().filter(
				elementMatcher -> !elementMatcher.matches(element)
		).collect(Collectors.toList());

		if (mismatchingMatchers.isEmpty()) {
			return true;
		}

		mismatchDescription.appendText(" that ");
		for (int i = 0, length = mismatchingMatchers.size(); i < length; i++) {
			Matcher elementMatcher = additionalMatcher.get(i);

			elementMatcher.describeMismatch(element, mismatchDescription);
			if (i < length - 1) {
				mismatchDescription.appendText(" and ");
			}
		}

		return false;
	}
}
