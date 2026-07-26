package com.android.server.soundtrigger_middleware;

/* JADX INFO: loaded from: classes3.dex */
public class AidlUtil {
    static android.media.soundtrigger.RecognitionEvent newEmptyRecognitionEvent() {
        android.media.soundtrigger.RecognitionEvent result = new android.media.soundtrigger.RecognitionEvent();
        result.data = new byte[0];
        return result;
    }

    static android.media.soundtrigger.PhraseRecognitionEvent newEmptyPhraseRecognitionEvent() {
        android.media.soundtrigger.PhraseRecognitionEvent result = new android.media.soundtrigger.PhraseRecognitionEvent();
        result.common = newEmptyRecognitionEvent();
        result.phraseExtras = new android.media.soundtrigger.PhraseRecognitionExtra[0];
        return result;
    }

    static android.media.soundtrigger_middleware.RecognitionEventSys newAbortEvent() {
        android.media.soundtrigger.RecognitionEvent recognitionEvent = newEmptyRecognitionEvent();
        recognitionEvent.type = 1;
        recognitionEvent.status = 1;
        android.media.soundtrigger_middleware.RecognitionEventSys recognitionEventSys = new android.media.soundtrigger_middleware.RecognitionEventSys();
        recognitionEventSys.recognitionEvent = recognitionEvent;
        return recognitionEventSys;
    }

    static android.media.soundtrigger_middleware.PhraseRecognitionEventSys newAbortPhraseEvent() {
        android.media.soundtrigger.PhraseRecognitionEvent recognitionEvent = newEmptyPhraseRecognitionEvent();
        recognitionEvent.common.type = 0;
        recognitionEvent.common.status = 1;
        android.media.soundtrigger_middleware.PhraseRecognitionEventSys phraseRecognitionEventSys = new android.media.soundtrigger_middleware.PhraseRecognitionEventSys();
        phraseRecognitionEventSys.phraseRecognitionEvent = recognitionEvent;
        return phraseRecognitionEventSys;
    }
}
