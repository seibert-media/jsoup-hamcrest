package net.seibermedia.jsouphamcrest;

import static java.util.Objects.requireNonNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;

public class HasTextMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final Matcher<String> textContentMatcher;

	private HasTextMatcher(Matcher<String> textContentMatcher) {
		this.textContentMatcher = requireNonNull(textContentMatcher);
	}

	public static HasTextMatcher hasText(String exactContent) {
		return new HasTextMatcher(Matchers.equalTo(exactContent));
	}

	public static HasTextMatcher hasText(Matcher<String> textContentMatcher) {
		return new HasTextMatcher(textContentMatcher);
	}

	@Override
	protected boolean matchesSafely(Element item, Description mismatchDescription) {
		String itemText = item.text();
		boolean matches = textContentMatcher.matches(itemText);

		if (!matches) {
			mismatchDescription.appendText("has text-content that ");
			textContentMatcher.describeMismatch(itemText, mismatchDescription);
		}

		return matches;
	}

	@Override
	public void describeTo(Description description) {
		description
				.appendText("has text-content that is ")
				.appendDescriptionOf(textContentMatcher);
	}
}
