package dev.panuszewski

import TestFederationInTeamCity
import dev.bnorm.storyboard.easel.ExperimentalStoryStateApi
import dev.panuszewski.template.showStoryboardInDesktop

@OptIn(ExperimentalStoryStateApi::class)
fun main() {
    showStoryboardInDesktop(TestFederationInTeamCity)
}
