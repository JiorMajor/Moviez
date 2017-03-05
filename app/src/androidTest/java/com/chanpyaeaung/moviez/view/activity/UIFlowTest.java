package com.chanpyaeaung.moviez.view.activity;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.action.ViewActions.*;
import static android.support.test.espresso.assertion.ViewAssertions.*;
import static android.support.test.espresso.matcher.ViewMatchers.*;

import com.chanpyaeaung.moviez.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Chan Pyae Aung on 5/3/17.
 */

@LargeTest
@RunWith(AndroidJUnit4.class)
public class UIFlowTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Test
    public void uIFlowTest() {
        ViewInteraction actionMenuItemView = onView(
                allOf(withId(com.chanpyaeaung.moviez.R.id.action_sort), withContentDescription("Sort"), isDisplayed()));
        actionMenuItemView.perform(click());

        ViewInteraction appCompatCheckedTextView = onView(
                allOf(withId(android.R.id.text1), withText(" Popularity"),
                        childAtPosition(
                                allOf(withId(com.chanpyaeaung.moviez.R.id.select_dialog_listview),
                                        withParent(withId(com.chanpyaeaung.moviez.R.id.contentPanel))),
                                1),
                        isDisplayed()));
        appCompatCheckedTextView.perform(click());

        ViewInteraction recyclerView = onView(
                allOf(withId(com.chanpyaeaung.moviez.R.id.recycler_view_movies),
                        withParent(withId(com.chanpyaeaung.moviez.R.id.swipeRefreshLayout)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(7, click()));

        ViewInteraction cardView = onView(
                allOf(withId(com.chanpyaeaung.moviez.R.id.cardBuy), isDisplayed()));
        cardView.perform(click());

        pressBack();

        pressBack();

    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
