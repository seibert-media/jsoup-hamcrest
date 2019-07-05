package net.seibermedia.jsouphamcrest;

import static java.util.Objects.requireNonNull;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;

public class HasTextMatcher extends TypeSafeDiagnosingMatcher<Element> {
	private final Matcher<String> textContentMatcher;
	private boolean own;

	private HasTextMatcher(Matcher<String> textContentMatcher, boolean own) {
		this.textContentMatcher = requireNonNull(textContentMatcher);
		this.own = own;
	}

	public static HasTextMatcher hasText(String exactContent) {
		return new HasTextMatcher(Matchers.equalTo(exactContent), false);
	}

	public static HasTextMatcher hasOwnText(String exactContent) {
		return new HasTextMatcher(Matchers.equalTo(exactContent), true);
	}

	public static HasTextMatcher hasText(Matcher<String> textContentMatcher) {
		return new HasTextMatcher(textContentMatcher, false);
	}

	public static HasTextMatcher hasOwnText(Matcher<String> textContentMatcher) {
		return new HasTextMatcher(textContentMatcher, true);
	}

	@Override
	protected boolean matchesSafely(Element item, Description mismatchDescription) {
		String itemText = own ? item.ownText() : item.text();
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
