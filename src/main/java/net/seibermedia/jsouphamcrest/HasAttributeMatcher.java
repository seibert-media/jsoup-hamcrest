package net.seibermedia.jsouphamcrest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;

public class HasAttributeMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final String name;
	private final Matcher<String> valueMatcher;

	private HasAttributeMatcher(String name, Matcher<String> valueMatcher) {
		this.name = name;
		this.valueMatcher = valueMatcher;
	}

	public static HasAttributeMatcher hasAttribute(String name) {
		return new HasAttributeMatcher(name, null);
	}

	public static HasAttributeMatcher hasAttribute(String name, String exactValue) {
		return new HasAttributeMatcher(name, Matchers.equalTo(exactValue));
	}

	public static HasAttributeMatcher hasAttribute(String name, Matcher<String> valueMatcher) {
		return new HasAttributeMatcher(name, valueMatcher);
	}

	@Override
	protected boolean matchesSafely(Element item, Description mismatchDescription) {
		boolean hasAttribute = item.attributes().hasKeyIgnoreCase(name);
		if (!hasAttribute) {
			mismatchDescription
					.appendText("did not have an attribute ")
					.appendValue(name);

			return false;
		}

		if (valueMatcher != null) {
			String attributeValue = item.attributes().getIgnoreCase(name);
			if (!valueMatcher.matches(attributeValue)) {
				mismatchDescription.appendText("did have an attribute ").appendValue(name).appendText(" that ");
				valueMatcher.describeMismatch(attributeValue, mismatchDescription);
				return false;
			}
		}

		return true;
	}

	@Override
	public void describeTo(Description description) {
		description
				.appendText("has attribute ")
				.appendValue(name);

		if (valueMatcher != null) {
			description.appendText(" that is ")
					.appendDescriptionOf(valueMatcher);
		}
	}
}
