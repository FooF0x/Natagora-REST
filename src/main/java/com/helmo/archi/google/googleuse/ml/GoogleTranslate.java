package com.helmo.archi.google.googleuse.ml;

import com.google.cloud.translate.Translate;
import com.google.cloud.translate.Translate.TranslateOption;
import com.google.cloud.translate.TranslateOptions;
import com.helmo.archi.google.googleuse.tools.HELMoCredentialsProvider;
import org.springframework.stereotype.Component;

@Component
public class GoogleTranslate {
	
	private Translate translate;
	
	public GoogleTranslate() {
		translate = TranslateOptions
				.newBuilder()
				.setCredentials(HELMoCredentialsProvider.getCredential())
				.build().getService();
	}
	
	public String simpleTranslateInEN(String input) {
		return translate.translate(input).getTranslatedText();
	}
	
	public String simpleTranslateFromENToFR(String input) {
		return translate.translate(input,
				TranslateOption.sourceLanguage("en"),
				TranslateOption.targetLanguage("fr")).getTranslatedText();
	}
	
	public String translateWithOption(String input, String srcLg, String tgtLg) {
		return translate.translate(
				input,
				TranslateOption.sourceLanguage(srcLg),
				TranslateOption.targetLanguage(tgtLg)).getTranslatedText();
	}
}
