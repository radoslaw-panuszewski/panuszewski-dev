package dev.panuszewski.template

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.ui.window.CanvasBasedWindow
import dev.bnorm.storyboard.Storyboard
import dev.bnorm.storyboard.easel.WebStoryEasel
import kotlinx.browser.document
import org.w3c.dom.HTMLCanvasElement

fun showStoryboardInBrowser(storyboard: Storyboard) {
    val element = document.getElementById("ComposeTarget") as HTMLCanvasElement
    element.focus()
    CanvasBasedWindow(canvasElementId = element.id, title = storyboard.title) {
        MaterialTheme(colors = darkColors()) {
            WebStoryEasel(storyboard)
        }
    }
}