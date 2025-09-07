package dev.panuszewski.scenes.amper

import dev.bnorm.storyboard.StoryboardBuilder

/**
 * - [ ] avoid touching build tool
 * - [ ] it tells you that dependency version is overridden by a newer one (Gradle does not do that)
 * - [ ] configuration that reflects reality
 * - [x] ./amper show settings (pokazać default wersję spring boota)
 * - [x] show up when needed, stay out of the way when not
 * - [ ] smart completion (like when typed 'jdk' it finds 'jvm.release' property)
 */
fun StoryboardBuilder.Amper() {
    AmperBriefDescription()
    AmperSpringBoot()
    AmperPrinciples()
    AmperCatchErrorsEarly()
}

