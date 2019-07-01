package net.seibermedia.jsouphamcrest;

import static java.util.Objects.requireNonNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;

public class HasTextContentMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private Matcher<String> textContentMatcher;

	private HasTextContentMatcher(Matcher<String> textContentMatcher) {
		this.textContentMatcher = requireNonNull(textContentMatcher);
	}

	public static HasTextContentMatcher hasTextContent(String exactContent) {
		return new HasTextContentMatcher(Matchers.equalTo(exactContent));
	}

	public static HasTextContentMatcher hasTextContent(Matcher<String> textContentMatcher) {
		return new HasTextContentMatcher(textContentMatcher);
	}

	@Override
	protected boolean matchesSafely(Element item, Description mismatchDescription) {
		String itemText = item.text();
		boolean matches = textContentMatcher.matches(itemText);

		if (!matches) {
			textContentMatcher.describeMismatch(itemText, mismatchDescription);
		}

		return matches;
	}

	@Override
	public void describeTo(Description description) {
		description
				.appendText("Has Text-Content that is ")
				.appendDescriptionOf(textContentMatcher);
	}
}
