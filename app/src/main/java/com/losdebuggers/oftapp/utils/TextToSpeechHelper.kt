package com.losdebuggers.oftapp.utils

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import java.util.Locale

class TextToSpeechHelper(
    context: Context,
    private val onInitialized: ((Boolean) -> Unit)? = null,
    private val onSpeakingChanged: ((Boolean) -> Unit)? = null
) : TextToSpeech.OnInitListener {

    private var tts: TextToSpeech? = TextToSpeech(context.applicationContext, this)
    private var isInitialized = false

    override fun onInit(status: Int) {
        if (status == TextToSpeech.SUCCESS) {
            val result = tts?.setLanguage(Locale.Builder().setLanguage("es").setRegion("ES").build())
            isInitialized = result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED

            tts?.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                    onSpeakingChanged?.invoke(true)
                }

                override fun onDone(utteranceId: String) {
                    onSpeakingChanged?.invoke(false)
                }

                @Deprecated("Deprecated in Java")
                override fun onError(utteranceId: String) {
                    onSpeakingChanged?.invoke(false)
                }
            })

            onInitialized?.invoke(isInitialized)
        } else {
            isInitialized = false
            onInitialized?.invoke(false)
        }
    }

    fun hablar(texto: String) {
        if (isInitialized && texto.isNotBlank()) {
            onSpeakingChanged?.invoke(true)
            tts?.speak(texto, TextToSpeech.QUEUE_FLUSH, null, "OftAppTtsId")
        }
    }

    fun detener() {
        tts?.stop()
        onSpeakingChanged?.invoke(false)
    }

    fun destruir() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
        onSpeakingChanged?.invoke(false)
    }
}
