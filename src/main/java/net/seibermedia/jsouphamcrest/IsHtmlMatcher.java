package net.seibermedia.jsouphamcrest;

import static java.util.Objects.requireNonNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.hamcrest.Description;
import org.hamcrest.DiagnosingMatcher;
import org.hamcrest.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class IsHtmlMatcher extends DiagnosingMatcher<Object> {
	public static final String CHARSET = "UTF-8";
	private final Matcher<Element> additionalMatcher;

	private IsHtmlMatcher(Matcher<Element> additionalMatcher) {
		this.additionalMatcher = requireNonNull(additionalMatcher);
	}

	public static IsHtmlMatcher isHtmlMatching(Matcher<Element> additionalMatcher) {
		return new IsHtmlMatcher(additionalMatcher);
	}

	@Override
	protected boolean matches(Object item, Description mismatchDescription) {
		Document document;
		try {
			if (item instanceof String) {
				document = Jsoup.parse((String) item);
			} else if (item instanceof InputStream) {
				document = Jsoup.parse((InputStream) item, CHARSET, "/");
			} else if (item instanceof File) {
				document = Jsoup.parse((File) item, CHARSET, "/");
			} else {
				throw new IllegalArgumentException(String.format("Unknown Docuement Data-Type: %s", item.getClass().toString()));
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		mismatchDescription.appendText("a parsable HTML-Document");

		if (!additionalMatcher.matches(document)) {
			mismatchDescription.appendText(" that ");
			additionalMatcher.describeMismatch(document, mismatchDescription);
			return false;
		}

		return true;
	}

	public void describeTo(Description description) {
		description
				.appendText("a parsable HTML-Document that ")
				.appendDescriptionOf(additionalMatcher);
	}
}
