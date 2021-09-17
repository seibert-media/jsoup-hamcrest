package net.seibertmedia.jsouphamcrest;

import java.util.Set;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeDiagnosingMatcher;
import org.jsoup.nodes.Element;

public class HasClassMatcher extends TypeSafeDiagnosingMatcher<Element> {

	private final Matcher<String> classMatcher;
	private final Matcher<Iterable<? extends String>> classesMatcher;

	private HasClassMatcher(Matcher<String> classMatcher, Matcher<Iterable<? extends String>> classesMatcher) {
		this.classMatcher = classMatcher;
		this.classesMatcher = classesMatcher;
	}


	public static HasClassMatcher hasClass(String cssClass) {
		return new HasClassMatcher(Matchers.equalTo(cssClass), null);
	}

	public static HasClassMatcher hasClass(Matcher<String> cssClassMatcher) {
		return new HasClassMatcher(cssClassMatcher, null);
	}

	public static HasClassMatcher hasClasses(String... cssClasses) {
		return new HasClassMatcher(null, Matchers.containsInAnyOrder(cssClasses));
	}

	public static HasClassMatcher hasClasses(Matcher<Iterable<? extends String>> cssClassesMatcher) {
		return new HasClassMatcher(null, cssClassesMatcher);
	}

	@Override
	protected boolean matchesSafely(Element item, Description mismatchDescription) {
		Set<String> classNames = item.classNames();

		if (classMatcher != null) {
			// check all classes individually. at lease one class must comply to the classMatcher

			for (String className : classNames) {
				if (classMatcher.matches(className)) {
					return true;
				}
			}
			mismatchDescription
					.appendText("had no class ")
					.appendDescriptionOf(classMatcher);

			return false;
		} else if (classesMatcher != null) {
			if (!classesMatcher.matches(classNames)) {
				mismatchDescription.appendText("has classes that ");
				classesMatcher.describeMismatch(classNames, mismatchDescription);

				return false;
			}
		}

		return true;
	}

	@Override
	public void describeTo(Description description) {
		if (classMatcher != null) {
			description
					.appendText("has a class ")
					.appendDescriptionOf(classMatcher);
		} else if (classesMatcher != null) {
			description
					.appendText("has classes ")
					.appendDescriptionOf(classesMatcher);
		}
	}
}
