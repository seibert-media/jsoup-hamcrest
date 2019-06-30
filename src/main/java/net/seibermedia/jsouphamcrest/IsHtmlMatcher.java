package net.seibermedia.jsouphamcrest;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class IsHtmlMatcher extends TypeSafeDiagnosingMatcher<String> {
	private final List<Matcher<Element>> additionalMatcher;

	private IsHtmlMatcher(List<Matcher<Element>> additionalMatcher) {
		if (additionalMatcher.size() == 0) {
			throw new IllegalArgumentException("IsHtmlMatcher must at least have one successive matcher.");
		}

		this.additionalMatcher = additionalMatcher;
	}

	@SafeVarargs
	public static IsHtmlMatcher isHtmlMatching(Matcher<Element>... additionalMatcher) {
		return new IsHtmlMatcher(Arrays.asList(additionalMatcher));
	}

	public static IsHtmlMatcher isHtmlMatching(List<Matcher<Element>> additionalMatcher) {
		return new IsHtmlMatcher(additionalMatcher);
	}

	protected boolean matchesSafely(String html, Description mismatchDescription) {
		Document document = Jsoup.parse(html);
		mismatchDescription.appendText("a parsable HTML-Document");

		return AdditionalMatcherDescriber.assertAndDescribeAdditionalMatcher(document, additionalMatcher, mismatchDescription);
	}

	public void describeTo(Description description) {
		description.appendText("a parsable HTML-Document");
		AdditionalMatcherDescriber.describeAdditionalMatcher(additionalMatcher, description);
	}
}
