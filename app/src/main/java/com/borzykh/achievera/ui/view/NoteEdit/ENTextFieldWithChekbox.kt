/*
 * Copyright 2025 Ivan Borzykh
 * SPDX-License-Identifier: MIT
 */
package com.borzykh.achievera.ui.view.NoteEdit

import android.content.Context
import android.text.Editable
import android.text.Spannable
import android.text.TextWatcher
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ImageSpan
import android.text.style.StrikethroughSpan
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.widget.EditText
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.core.widget.TextViewCompat
import com.borzykh.achievera.R
import com.borzykh.achievera.ui.theme.BackgroundColor
import com.borzykh.achievera.ui.view.NoteListing.ColorConnect
import com.borzykh.achievera.ui.viewModel.NoteEditViewModel

@Composable
fun InlineCheckboxField_DualLayer(
    BaseColor: String,
    vm:NoteEditViewModel
): () -> Unit {
    var editTextRef by remember { mutableStateOf<EditText?>(null) }
    var currentText=vm.currentText
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = BackgroundColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    top = 16.dp,
                    bottom = 16.dp,
                    start = 16.dp,
                    end = 16.dp
                )
                .background(color = ColorConnect(BaseColor)),
        ) {
            AndroidView(
                modifier = Modifier.fillMaxWidth()
                    .defaultMinSize(minHeight = 700.dp),
                factory = { context ->
                    NoSelectionEditText(context).apply {
                        setText(currentText)
                        movementMethod = LinkMovementMethod.getInstance()
                        gravity = Gravity.TOP
                        background = null
                        addTextChangedListener(object : TextWatcher {
                            var isEditing = false

                            override fun beforeTextChanged(
                                s: CharSequence?,
                                start: Int,
                                count: Int,
                                after: Int
                            ) {
                            }

                            override fun onTextChanged(
                                s: CharSequence?,
                                start: Int,
                                before: Int,
                                count: Int
                            ) {
                            }

                            override fun afterTextChanged(s: Editable?) {
                                if (isEditing) return
                                s ?: return

                                try {
                                    isEditing = true

                                    val oldImageSpans =
                                        s.getSpans(0, s.length, ImageSpan::class.java)
                                    oldImageSpans.forEach { s.removeSpan(it) }

                                    val oldClickSpans =
                                        s.getSpans(0, s.length, ClickableSpan::class.java)
                                    oldClickSpans.forEach { s.removeSpan(it) }

                                    val oldStrikeSpans =
                                        s.getSpans(0, s.length, StrikethroughSpan::class.java)
                                    oldStrikeSpans.forEach { s.removeSpan(it) }

                                    var lineStart = 0
                                    while (lineStart < s.length) {
                                        val lineEnd = s.indexOf('\n', lineStart).let {
                                            if (it == -1) s.length else it
                                        }
                                        if (lineEnd <= lineStart) {
                                            lineStart = lineEnd + 1
                                            continue
                                        }

                                        val firstChar = s.substring(lineStart, lineStart + 1)
                                        if (firstChar == "☐" || firstChar == "☑") {
                                            val isChecked = (firstChar == "☑")

                                            val drawableId =
                                                if (isChecked) R.drawable.chkbxon else R.drawable.chkbxoff
                                            val drawable =
                                                ContextCompat.getDrawable(context, drawableId)

                                            val textSizePx = this@apply.textSize.toInt()
                                            drawable?.setBounds(0, 0, textSizePx, textSizePx)

                                            val imageSpan =
                                                ImageSpan(drawable!!, ImageSpan.ALIGN_BASELINE)
                                            s.setSpan(
                                                imageSpan,
                                                lineStart,
                                                lineStart + 1,
                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                            )

                                            if (isChecked) {
                                                s.setSpan(
                                                    StrikethroughSpan(),
                                                    lineStart + 1,
                                                    lineEnd,
                                                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                                )
                                            }

                                            val toggleSpan = object : ClickableSpan() {
                                                override fun onClick(widget: View) {
                                                    val editText = widget as EditText
                                                    val editable = editText.text

                                                    val spanStart = editable.getSpanStart(this)
                                                    val spanEnd = editable.getSpanEnd(this)
                                                    val currentSymbol =
                                                        editable.substring(spanStart, spanEnd)

                                                    val newSymbol =
                                                        if (currentSymbol == "☐") "☑" else "☐"
                                                    editable.replace(spanStart, spanEnd, newSymbol)

                                                    val lineBegin =
                                                        findLineStart(editable, spanStart)
                                                    val lineStop = findLineEnd(editable, lineBegin)

                                                    val images = editable.getSpans(
                                                        lineBegin,
                                                        lineStop,
                                                        ImageSpan::class.java
                                                    )
                                                    images.forEach { editable.removeSpan(it) }
                                                    val strikes = editable.getSpans(
                                                        lineBegin,
                                                        lineStop,
                                                        StrikethroughSpan::class.java
                                                    )
                                                    strikes.forEach { editable.removeSpan(it) }

                                                    val newDrawableId =
                                                        if (newSymbol == "☑") R.drawable.chkbxon else R.drawable.chkbxoff
                                                    val newDrawable = ContextCompat.getDrawable(
                                                        context,
                                                        newDrawableId
                                                    )
                                                    val textSizePx = editText.textSize.toInt()
                                                    newDrawable?.setBounds(
                                                        0,
                                                        0,
                                                        textSizePx,
                                                        textSizePx
                                                    )
                                                    val newImageSpan = ImageSpan(
                                                        newDrawable!!,
                                                        ImageSpan.ALIGN_BASELINE
                                                    )
                                                    editable.setSpan(
                                                        newImageSpan,
                                                        spanStart,
                                                        spanStart + 1,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                                    )
                                                    editable.setSpan(
                                                        this,
                                                        spanStart,
                                                        spanStart + 1,
                                                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                                    )
                                                    if (newSymbol == "☑") {
                                                        editable.setSpan(
                                                            StrikethroughSpan(),
                                                            spanStart + 1,
                                                            lineStop,
                                                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                                        )
                                                    }
                                                }
                                            }
                                            s.setSpan(
                                                toggleSpan,
                                                lineStart,
                                                lineStart + 1,
                                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                                            )
                                        }

                                        lineStart = lineEnd + 1
                                    }
                                    vm.onTextChange(s.toString())
                                } finally {
                                    isEditing = false
                                }
                            }
                        })
                    }.also {
                        editTextRef = it
                    }
                },

                update = { newEditText ->
                    val updatedText = vm.currentText

                    if (newEditText.text.toString() != updatedText) {
                        newEditText.setText(updatedText)
                        newEditText.setSelection(updatedText.length)
                    }
                    editTextRef = newEditText
                }
            )
        }
    }
    val addCheckBoxReal: () -> Unit = addchekbox@{
        val editText = editTextRef ?: return@addchekbox
        val s = editText.text ?: return@addchekbox
        if(editText.selectionStart!=null){
        val selectionStart = editText.selectionStart

        val lineStart = findLineStart(s, selectionStart)

        if (lineStart >= s.length) {
            s.insert(lineStart, "☐ ")
            return@addchekbox
        }

        val firstChar = s.substring(lineStart, lineStart + 1)

        if (firstChar == "☐" || firstChar == "☑") {

            s.delete(lineStart, lineStart + 1)
            if (lineStart < s.length && s[lineStart] == ' ') {
                s.delete(lineStart, lineStart + 1)
            }
        } else {
            s.insert(lineStart, "☐ ")
        }
    }}
    return addCheckBoxReal
}

private fun findLineStart(editable: Editable, fromIndex: Int): Int {
    var i = fromIndex
    while (i > 0 && editable[i - 1] != '\n') {
        i--
    }
    return i
}

private fun findLineEnd(editable: Editable, fromIndex: Int): Int {
    var i = fromIndex
    while (i < editable.length && editable[i] != '\n') {
        i++
    }
    return i
}
class NoSelectionEditText @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : androidx.appcompat.widget.AppCompatEditText(context, attrs) {
    private var lastValidSelectionStart: Int = 0
    private var lastValidSelectionEnd: Int = 0
    override fun onSelectionChanged(selStart: Int, selEnd: Int) {

        if (isInsideUnselectableRegion(selStart, selEnd)) {
            setSelection(lastValidSelectionStart, lastValidSelectionEnd)
        } else {
            lastValidSelectionStart = selStart
            lastValidSelectionEnd = selEnd
            super.onSelectionChanged(selStart, selEnd)
        }
    }
    private fun isInsideUnselectableRegion(selStart: Int, selEnd: Int): Boolean {
        val editable = text ?: return false

        if (selStart != selEnd) {
            return editable.getSpans(selStart, selEnd, ClickableSpan::class.java).isNotEmpty()
        }

        val spans = editable.getSpans(selStart, selStart, ClickableSpan::class.java)
        for (span in spans) {
            val start = editable.getSpanStart(span)
            val end   = editable.getSpanEnd(span)
            if (selStart > start && selStart < end) return true
        }
        return false
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_UP) {
            val x = (event.x - totalPaddingLeft).toInt()
            val y = (event.y - totalPaddingTop).toInt()
            val layout = layout ?: return super.onTouchEvent(event)
            val line = layout.getLineForVertical(y)
            val offset = layout.getOffsetForHorizontal(line, x.toFloat())

            val clickableSpans = text?.getSpans(offset, offset, ClickableSpan::class.java)
            if (!clickableSpans.isNullOrEmpty()) {
                clickableSpans[0].onClick(this)
                return true
            }
        }
        return super.onTouchEvent(event)
    }
}